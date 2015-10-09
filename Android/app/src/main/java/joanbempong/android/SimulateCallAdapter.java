package joanbempong.android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Joan Bempong on 10/1/2015.
 */
public class SimulateCallAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<String> myList;
    private Context context;
    private HueController controller;
    private PHHueSDK phHueSDK = PHHueSDK.create();
    private PHBridge bridge = phHueSDK.getSelectedBridge();
    private List<PHLight> allLights = bridge.getResourceCache().getAllLights();
    int totalRings = 0;
    private int MAX_TOTAL_RINGS = 0;
    private int LONG_PERIOD = 0;

    /**
     * creates instance of {@link LightListAdapter} class.
     *
     * @param context   the Context object.
     * @param allContacts an array list of {@link PHLight} object to display.
     */
    public SimulateCallAdapter(Context context, ArrayList<String> allContacts, HueController controller) {
        this.context = context;
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
        this.myList = allContacts;
        this.controller = controller;
    }

    /**
     * Get a View that displays the data at the specified position in the data set.
     *
     * @param position    The row index.
     * @param convertView The row view.
     * @param parent      The view group.
     * @return A View corresponding to the data at the specified position.
     */
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.simulatelist_call, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView) view.findViewById(R.id.list_item_string);
        listItemText.setText(myList.get(position));

        //String[] nameSplit = listItemText.getText)_.toString().split("\\s+");

        //Handle buttons and add onClickListeners
        ImageButton simulateBtn = (ImageButton) view.findViewById(R.id.simulate_btn);

        simulateBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                //controller.saveAllLightStates();

                totalRings = 0;
                controller.setCallAnswered(false);
                //creates a new alert dialog
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("An Incoming Call");
                alert.setMessage(listItemText.getText());
                alert.setPositiveButton("Answer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        controller.setCallAnswered(true);
                        // navigate to the MyContacts page
                        context.startActivity(new Intent(context, OnCallActivity.class));
                    }
                });

                alert.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        controller.setCallAnswered(true);
                        dialog.cancel();
                        controller.restoreAllLightStates();
                    }
                });
                alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        controller.setCallAnswered(true);
                    }
                });
                //create the alert dialog and show it

                alert.create();
                //alert.show();
                final AlertDialog dialog = alert.show();

                //replaces controller.simulateAnIncomingCall()
                String[] nameSplit = listItemText.getText().toString().split("\\s+");

                if (controller.getContactList().size() != 0) {
                    for (final ACEContact contact : controller.getContactList()) {
                        if (nameSplit[0].equals(contact.getFirstName()) && nameSplit[1].equals(contact.getLastName())) {
                            final int val = controller.getHueValue(contact.getColor());
                            if (contact.getUseNotification()) {
                                double flashRate = Double.parseDouble(contact.getFlashRate());
                                MAX_TOTAL_RINGS = (int) ((10 / flashRate) * 2);
                                LONG_PERIOD = (int) (flashRate * 1000);
                                //controller.setTotalCommands(0);
                                (new Thread() {
                                    public void run() {
                                        Timer timer = new Timer();
                                        timer.schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                outerLoop:
                                                for (final PHLight light : allLights) {
                                                    for (String lightName : controller.getDefaultLights()) {
                                                        if (lightName.equals(light.getName())) {
                                                            System.out.println("ticking");
                                                            System.out.println(totalRings);
                                                            if (totalRings == MAX_TOTAL_RINGS) { //lasts for 20 seconds
                                                                if (!controller.getCallAnswered()) {
                                                                    controller.setNewMissedCall(false);
                                                                    //give the previous timer for a missed call (if there is any)
                                                                    //some time to cancel so that a new once can be created.
                                                                    try {
                                                                        Thread.sleep(2000);
                                                                    } catch (InterruptedException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    controller.setNewMissedCall(true);
                                                                    controller.simulateAMissedCall();
                                                                    dialog.cancel();
                                                                    cancel();
                                                                    break outerLoop;
                                                                }
                                                                System.out.println(lightName);
                                                                System.out.println("timer cancelled");
                                                                cancel();
                                                                break outerLoop;
                                                            }
                                                            if (!controller.getCallAnswered()) {
                                                                PHLightState state = new PHLightState();
                                                                state.setOn(controller.getToggle());
                                                                bridge.updateLightState(light, state);
                                                                //controller.incrementAndCheck10();
                                                                state.setBrightness(255);
                                                                bridge.updateLightState(light, state);
                                                                //controller.incrementAndCheck10();
                                                                if (light.supportsColor()){
                                                                    state.setHue(val);
                                                                    bridge.updateLightState(light, state);
                                                                    //controller.incrementAndCheck10();
                                                                }
                                                                System.out.println(light.getName() + " is " + controller.getToggle());
                                                                //controller.setTotalCommands(0);

                                                            } else {
                                                                System.out.println("call answered/declined -- timer cancelled");
                                                                cancel();
                                                                break outerLoop;
                                                            }
                                                        }
                                                    }
                                                }
                                                controller.setToggle();
                                                totalRings++;
                                            }
                                        }, 0, LONG_PERIOD);
                                    }
                                }).start();
                            }
                            else{ //do the same thing but no notification
                                double flashRate = 1;
                                MAX_TOTAL_RINGS = (int) ((10 / flashRate) * 2);
                                LONG_PERIOD = (int) (flashRate * 1000);

                                (new Thread() {
                                    public void run() {
                                        Timer timer = new Timer();
                                        timer.schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                System.out.println("ticking");
                                                System.out.println(totalRings);
                                                if (totalRings == MAX_TOTAL_RINGS) { //20 rings in total
                                                    if (!controller.getCallAnswered()) {
                                                        dialog.cancel();
                                                    }
                                                    System.out.println("timer cancelled");
                                                    cancel();
                                                }
                                                if (controller.getCallAnswered()) {
                                                    System.out.println("call answered/declined -- timer cancelled");
                                                    cancel();
                                                }
                                                totalRings++;
                                            }
                                        }, 0, LONG_PERIOD);
                                    }
                                }).start();
                            }
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });

        return view;
    }


    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The row index.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return myList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return myList.get(position);
    }

    /**
     * Update date of the list view and refresh listview.
     *
     * @param myChoices An array list of {@link PHLight} objects.
     */
    public void updateData(ArrayList<String> myChoices) {
        this.myList = myChoices;
        notifyDataSetChanged();
    }

}
