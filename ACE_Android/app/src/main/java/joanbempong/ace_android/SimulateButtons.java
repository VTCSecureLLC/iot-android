package joanbempong.ace_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Joan Bempong on 9/28/2015.
 */
public class SimulateButtons extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;

    public SimulateButtons(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int pos) {

        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.simulate_buttons, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView) view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));
        //System.out.println(listItemText.getText());

        //Handle buttons and add onClickListeners
        ImageButton simulateBtn = (ImageButton) view.findViewById(R.id.simulate_btn);

        simulateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creates a new alert dialog
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("An Incoming Call");
                alert.setMessage(listItemText.getText());
                alert.setPositiveButton("Answer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //the pop up window is no longer active
                        HueController.popup = false;

                        //turn off the hue
                        //shouldnt always be 2
                        HueController.putHueOff(2);
                    }
                });

                alert.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //the pop up window is no longer active
                        HueController.popup = false;

                        //turn off the Hue
                        //shouldnt always be 2
                        HueController.putHueOff(2);

                        HueController.simulateAMissedCall(listItemText.getText());
                    }
                });
                //create the alert dialog and show it
                alert.create();
                alert.show();

                //call the simulate function in ACE Controller
                HueController.simulateAnIncomingCall(listItemText.getText());

                notifyDataSetChanged();
            }
        });


        return view;
    }
}
