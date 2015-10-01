package joanbempong.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.philips.lighting.model.PHLight;

import java.util.ArrayList;

/**
 * Created by Joan Bempong on 10/1/2015.
 */
public class SimulateCallAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<String> myList;
    private Context context;
    private HueController controller;

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
        //System.out.println(listItemText.getText());



        //Handle buttons and add onClickListeners
        ImageButton simulateBtn = (ImageButton) view.findViewById(R.id.simulate_btn);

        simulateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.setCallAnswered(false);
                //creates a new alert dialog
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("An Incoming Call");
                alert.setMessage(listItemText.getText());
                alert.setPositiveButton("Answer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        controller.setCallAnswered(true);
                    }
                });

                alert.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        controller.setCallAnswered(true);
                    }
                });
                //create the alert dialog and show it
                alert.create();
                alert.show();

                //call the simulate function in ACE Controller
                controller.simulateAnIncomingCall(listItemText.getText());

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
