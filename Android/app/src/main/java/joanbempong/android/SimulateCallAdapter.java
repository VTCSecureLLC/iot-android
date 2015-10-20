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
    private MyChoices myChoices;
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

        myChoices = MyChoices.getInstance();

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView) view.findViewById(R.id.list_item_string);
        listItemText.setText(myList.get(position));

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
                        ACEPattern pattern = ACEPattern.getInstance();
                        pattern.setPatternInterrupted(true);
                        controller.setCallAnswered(true);
                        controller.setOnCall(true);
                        /*try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                        // navigate to the MyContacts page
                        context.startActivity(new Intent(context, OnCallActivity.class));
                    }
                });

                alert.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ACEPattern pattern = ACEPattern.getInstance();
                        pattern.setPatternInterrupted(true);
                        controller.setCallAnswered(true);
                        dialog.cancel();
                        //controller.restoreAllLightStates();
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

                final ACEPattern pattern = ACEPattern.getInstance();
                String[] nameSplit = listItemText.getText().toString().split("\\s+");

                if (controller.getContactList().size() != 0) {
                    for (final ACEContact contact : controller.getContactList()) {
                        if (nameSplit[0].equals(contact.getFirstName()) && nameSplit[1].equals(contact.getLastName())) {
                            ACEColors colors = ACEColors.getInstance();
                            Double[] colorXY = {colors.getColorsList().get(contact.getColor())[0], colors.getColorsList().get(contact.getColor())[1]};
                            if (contact.getUseNotification()) {
                                double flashRate = Double.parseDouble(contact.getFlashRate());
                                final String flashPattern = contact.getFlashPattern();
                                MAX_TOTAL_RINGS = (int) ((10 / flashRate) * 2);
                                LONG_PERIOD = (int) (flashRate * 1000);
                                pattern.setIndex(0);

                                for (final PHLight light : allLights) {
                                    for (String lightName : controller.getDefaultLights()) {
                                        if (lightName.equals(light.getName())) {
                                            boolean repeat = true;
                                            pattern.setUseThisPattern(true);
                                            pattern.setPatternInterrupted(false);
                                            switch (flashPattern) {
                                                case "None":
                                                    pattern.nonePattern(light, repeat, Long.valueOf(contact.getFlashRate()), colorXY);
                                                    break;
                                                case "Short On":
                                                    pattern.shortOnPattern(light, repeat, Long.valueOf(contact.getFlashRate()), colorXY);
                                                    break;
                                                case "Long On":
                                                    pattern.longOnPattern(light, repeat, Long.valueOf(contact.getFlashRate()), colorXY);
                                                    break;
                                                case "Color":
                                                    pattern.colorPattern(light, repeat, Long.valueOf(contact.getFlashRate()));
                                                    break;
                                                case "Fire":
                                                    pattern.firePattern(light, repeat, Long.valueOf(contact.getFlashRate()));
                                                    break;
                                                case "RIT":
                                                    pattern.ritPattern(light, repeat, Long.valueOf(contact.getFlashRate()));
                                                    break;
                                                case "Cloudy Sky":
                                                    pattern.cloudySkyPattern(light, repeat, Long.valueOf(contact.getFlashRate()));
                                                    break;
                                                case "Grassy Green":
                                                    pattern.grassyGreenPattern(light, repeat, Long.valueOf(contact.getFlashRate()));
                                                    break;
                                                case "Lavender":
                                                    pattern.lavenderPattern(light, repeat, Long.valueOf(contact.getFlashRate()));
                                                    break;
                                                case "Bloody Red":
                                                    pattern.bloodyRedPattern(light, repeat, Long.valueOf(contact.getFlashRate()));
                                                    break;
                                                case "Spring Mist":
                                                    pattern.springMistPattern(light, repeat, Long.valueOf(contact.getFlashRate()));
                                                    break;
                                            }
                                        }
                                    }
                                }

                                (new Thread() {
                                    public void run() {
                                        Timer timer = new Timer();
                                        timer.schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                System.out.println("ticking");
                                                System.out.println(totalRings);
                                                if (totalRings >= 20) { //lasts for 20 seconds
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
                                                        dialog.cancel();
                                                        pattern.setUseThisPattern(false);
                                                        pattern.setPatternInterrupted(true);
                                                        cancel();
                                                    }
                                                    System.out.println("timer cancelled");
                                                    pattern.setUseThisPattern(false);
                                                    pattern.setPatternInterrupted(true);
                                                    cancel();
                                                }
                                                if (!controller.getCallAnswered()) {
                                                    totalRings++;
                                                } else {
                                                    System.out.println("call answered/declined -- timer cancelled");
                                                    pattern.setUseThisPattern(false);
                                                    pattern.setPatternInterrupted(true);
                                                    cancel();
                                                }
                                            }
                                        }, 0, 1000);
                                    }
                                }).start();
                            } else { //do the same thing but no notification
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
                                                if (totalRings == 20) { //20 rings in total
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
                                        }, 0, 1000);
                                    }
                                }).start();
                            }
                        }
                    }
                }
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
