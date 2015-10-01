package joanbempong.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.philips.lighting.model.PHLight;

import java.util.List;

/**
 * Created by Joan Bempong on 9/30/2015.
 */
public class LightListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<PHLight> allLights;

    /**
     * creates instance of {@link LightListAdapter} class.
     *
     * @param context   the Context object.
     * @param allLights an array list of {@link PHLight} object to display.
     */
    public LightListAdapter(Context context, List<PHLight> allLights) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lightlist_name, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView) convertView.findViewById(R.id.lightName);
        PHLight light = allLights.get(position);
        listItemText.setText(light.getName());

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

