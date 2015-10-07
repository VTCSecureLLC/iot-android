package joanbempong.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.philips.lighting.model.PHLight;

import java.util.List;

/**
 * Created by Joan Bempong on 10/1/2015.
 */
public class DefaultIncomingCallLightChoiceAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<PHLight> myChoices;
    private Context context;
    private HueController controller;

    /**
     * creates instance of {@link LightListAdapter} class.
     *
     * @param context   the Context object.
     * @param allLights an array list of {@link PHLight} object to display.
     */
    public DefaultIncomingCallLightChoiceAdapter(Context context, List<PHLight> allLights, HueController controller) {
        this.context = context;
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
        this.myChoices = allLights;
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
            view = inflater.inflate(R.layout.light_choice, null);
        }

        //Handle Checkbox and display string from your list
        final CheckBox lightName = (CheckBox) view.findViewById(R.id.lightName);
        final PHLight light = myChoices.get(position);
        lightName.setText(light.getName());

        lightName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    controller.addToIncomingCallDefaultLights(light.getName());
                }
                else{
                    controller.removeFromIncomingCallDefaultLights(light.getName());
                }
            }
        });

        if (controller.getOldDefaultIncomingLight() != null) {
            for (String l : controller.getOldDefaultIncomingLight()){
                System.out.println(l);
                if (lightName.getText().equals(l)){
                    System.out.println("set checked");
                    lightName.setChecked(true);
                }
            }
        }
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
        return myChoices.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return myChoices.get(position);
    }

    /**
     * Update date of the list view and refresh listview.
     *
     * @param myChoices An array list of {@link PHLight} objects.
     */
    public void updateData(List<PHLight> myChoices) {
        this.myChoices = myChoices;
        notifyDataSetChanged();
    }

}
