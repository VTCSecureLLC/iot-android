package joanbempong.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.List;

/**
 * Created by Joan Bempong on 10/1/2015.
 */
public class LightListControlAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private List<PHLight> allLights;
    int red = 0;
    int orange = 6375;
    int yellow = 12750;
    int green = 25500;
    int blue = 46920;
    int purple = 50100;
    int pink = 61100;
    private HueController controller;


    private PHHueSDK phHueSDK = PHHueSDK.create();
    private PHBridge bridge = phHueSDK.getSelectedBridge();

    /**
     * creates instance of {@link LightListAdapter} class.
     *
     * @param context   the Context object.
     * @param allLights an array list of {@link PHLight} object to display.
     */
    public LightListControlAdapter(Context context, List<PHLight> allLights, HueController controller) {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
        this.allLights = allLights;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lightlist_control, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView) convertView.findViewById(R.id.lightName);
        final PHLight light = allLights.get(position);
        listItemText.setText(light.getName());

        PHHueSDK phHueSDK;
        phHueSDK = PHHueSDK.create();
        final PHBridge bridge = phHueSDK.getSelectedBridge();
        final PHLightState state = new PHLightState();

        final SeekBar seekBar = (SeekBar) convertView.findViewById(R.id.seekBar);
        int currentBright;

        final Button redBtn = (Button)convertView.findViewById(R.id.redBtn);
        final Button orangeBtn = (Button)convertView.findViewById(R.id.orangeBtn);
        final Button yellowBtn = (Button)convertView.findViewById(R.id.yellowBtn);
        final Button greenBtn = (Button)convertView.findViewById(R.id.greenBtn);
        final Button blueBtn = (Button)convertView.findViewById(R.id.blueBtn);
        final Button purpleBtn = (Button)convertView.findViewById(R.id.purpleBtn);
        final Button pinkBtn = (Button)convertView.findViewById(R.id.pinkBtn);

        if (!light.supportsColor()){
            redBtn.setVisibility(View.GONE);
            orangeBtn.setVisibility(View.GONE);
            yellowBtn.setVisibility(View.GONE);
            greenBtn.setVisibility(View.GONE);
            blueBtn.setVisibility(View.GONE);
            purpleBtn.setVisibility(View.GONE);
            pinkBtn.setVisibility(View.GONE);
        }

        redBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.setHue(red);
                bridge.updateLightState(light, state);
            }
        });

        orangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.setHue(orange);
                bridge.updateLightState(light, state);
            }
        });

        yellowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.setHue(yellow);
                bridge.updateLightState(light, state);
            }
        });

        greenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.setHue(green);
                bridge.updateLightState(light, state);
            }
        });

        blueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.setHue(blue);
                bridge.updateLightState(light, state);

            }
        });

        purpleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.setHue(purple);
                bridge.updateLightState(light, state);
            }
        });

        pinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.setHue(pink);
                bridge.updateLightState(light, state);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int lightBright = seekBar.getProgress();
                state.setBrightness(lightBright);
                bridge.updateLightState(light, state);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                int lightBright = seekBar.getProgress();
                state.setBrightness(lightBright);
                bridge.updateLightState(light, state);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int lightBright = seekBar.getProgress();
                state.setBrightness(lightBright);
                bridge.updateLightState(light, state);
            }
        });

        for (PHLight l : allLights){
            if (listItemText.getText().equals(l.getName())){
                currentBright = l.getLastKnownLightState().getBrightness();
                seekBar.setProgress(currentBright);
                if (!l.getLastKnownLightState().isOn()){
                    seekBar.setEnabled(false);
                }
                break;
            }
        }

        listItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(listItemText.getText() + " was clicked");
                for (PHLight l : allLights) {
                    if (l.getName().equals(listItemText.getText())) {
                        if (l.getLastKnownLightState().isOn()) {
                            PHLightState state = new PHLightState();
                            state.setOn(false);
                            bridge.updateLightState(l, state);
                            seekBar.setEnabled(false);
                        } else {
                            PHLightState state = new PHLightState();
                            state.setOn(true);
                            state.setBrightness(l.getLastKnownLightState().getBrightness());
                            bridge.updateLightState(l, state);
                            seekBar.setEnabled(true);
                        }
                    }
                }
            }
        });

        return convertView;
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

