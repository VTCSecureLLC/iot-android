package joanbempong.android;

import org.linphone.R;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.philips.lighting.model.PHLight;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Joan Bempong on 10/1/2015.
 */
public class ContactListSettingsAdapter extends BaseAdapter {
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
    public ContactListSettingsAdapter(Context context, ArrayList<String> allContacts, HueController controller) {
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
            view = inflater.inflate(R.layout.contactlist_settings, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView) view.findViewById(R.id.contactName);
        listItemText.setText(myList.get(position));
        //System.out.println(listItemText.getText());

        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.delete_btn);
        ImageButton editBtn = (ImageButton)view.findViewById(R.id.edit_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String[] nameSplit = listItemText.getText().toString().split("\\s+");
                //go through the Contacts list and delete this contact
                if (controller.getContactList().size() != 0){
                    System.out.println("in loop");
                    for (Iterator<ACEContact> iter = controller.getContactList().listIterator(); iter.hasNext();){
                        ACEContact contact = iter.next();
                        if (nameSplit[0].equals(contact.getFirstName()) && nameSplit[1].equals(contact.getLastName())) {
                            iter.remove();
                            System.out.println("removed successfully");
                        }
                    }
                }

                myList.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String[] nameSplit = listItemText.getText().toString().split("\\s+");

                //save the current first and last name
                controller.saveCurrentInformation(nameSplit[0], nameSplit[1]);

                notifyDataSetChanged();

                //navigate to the EditContactActivity class
                context.startActivity(new Intent(context, EditContactActivity.class));
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
