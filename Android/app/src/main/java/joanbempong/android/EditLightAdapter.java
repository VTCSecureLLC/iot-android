package joanbempong.android;

import org.linphone.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Joan Bempong on 10/2/2015.
 */
public class EditLightAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<PHLight> allLights;
    private Context context;
    private long sleep = 500;
    private HueController controller = HueController.getInstance();

    /**
     * creates instance of {@link LightListAdapter} class.
     *
     * @param context   the Context object.
     * @param allLights an array list of {@link PHLight} object to display.
     */
    public EditLightAdapter(Context context, List<PHLight> allLights) {
        this.context = context;
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
        this.allLights = allLights;
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
            view = inflater.inflate(R.layout.lightlist_configure, null);
        }

        //Handle TextView and display string from your list
        final TextView lightName = (TextView) view.findViewById(R.id.lightName);
        final PHLight light = allLights.get(position);
        lightName.setText(light.getName());

        lightName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println(lightName.getText() + " is listening......");
                //creates a new alert dialog
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                final EditText rename = new EditText(context);
                rename.setText(light.getName());
                alert.setView(rename);
                alert.setTitle("Renaming " + light.getName());
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        renameLight(light, rename);
                        controller.stopFlashing();

                    }
                });
                alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        controller.stopFlashing();
                    }
                });
                //create the alert dialog and show it
                alert.create();
                alert.show();
                controller.startFlashing(light);
            }

        });

        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mainHandler = new Handler(context.getMainLooper());
                Runnable deleteRunner = new Runnable() {

                    @SuppressLint("NewApi")
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(light.getName());
                        builder.setMessage("Are you sure you want to delete this light?");
                        builder.setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                controller.stopFlashing();
                                deleteOnClick(light);
                                try {
                                    TimeUnit.MILLISECONDS.sleep(sleep);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                //navigate to the ConfigureLightsActivity class
                                context.startActivity(new Intent(context, MyLightsActivity.class));
                            }
                        });
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                controller.stopFlashing();
                            }
                        });
                        builder.setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                controller.stopFlashing();
                                dialog.cancel();
                            }
                        });

                        builder.create();
                        builder.show();
                        controller.startFlashing(light);
                    }
                };
                mainHandler.post(deleteRunner);
            }
        });

        return view;
    }

    public void deleteOnClick(PHLight light) {
        //update the selected default lights
        for (Iterator<String> iter = controller.getDefaultLights().listIterator(); iter.hasNext();){
            String currentLight = iter.next();
            if (currentLight.equals(light.getName())){
                iter.remove();
                System.out.println(currentLight + " removed successfully from preferred default lights");
            }
        }

        // If you want to handle the response from the bridge, create a PHLightListener object.
        PHLightListener listener = new PHLightListener() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {}

            @Override
            public void onError(int arg0, String arg1) {}

            @Override
            public void onReceivingLightDetails(PHLight arg0) {}

            @Override
            public void onReceivingLights(List<PHBridgeResource> arg0) {}

            @Override
            public void onSearchComplete() {}
        };
        PHHueSDK phHueSDK = PHHueSDK.create();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        bridge.deleteLight(light.getIdentifier(), listener);


    }

    public void renameLight(final PHLight light, EditText newName) {
        light.setName(newName.getText().toString());
        try {
            TimeUnit.MILLISECONDS.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // If you want to handle the response from the bridge, create a PHLightListener object.
        PHLightListener listener = new PHLightListener() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {}

            @Override
            public void onError(int arg0, String arg1) {}

            @Override
            public void onReceivingLightDetails(PHLight arg0) {}

            @Override
            public void onReceivingLights(List<PHBridgeResource> arg0) {}

            @Override
            public void onSearchComplete() {}
        };
        PHHueSDK phHueSDK = PHHueSDK.create();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        bridge.updateLight(light, listener);

        //navigate to the ConfigureLightsActivity class
        context.startActivity(new Intent(context, MyLightsActivity.class));
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
        return allLights.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return allLights.get(position);
    }

    /**
     * Update date of the list view and refresh listview.
     *
     * @param allLights An array list of {@link PHLight} objects.
     */
    public void updateData(List<PHLight> allLights) {
        this.allLights = allLights;
        notifyDataSetChanged();
    }

}


