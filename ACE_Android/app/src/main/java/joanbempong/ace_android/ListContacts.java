package joanbempong.ace_android;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Joan Bempong on 9/29/2015.
 */
public class ListContacts extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;



    public ListContacts(ArrayList<String> list, Context context) {
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
            view = inflater.inflate(R.layout.contacts_list, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView)view.findViewById(R.id.contact_list);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.delete_btn);
        ImageButton editBtn = (ImageButton)view.findViewById(R.id.edit_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String[] nameSplit = listItemText.getText().toString().split("\\s+");
                //go through the Contacts list and delete this contact
                if (HueController.Contacts != null){
                    System.out.println("in loop");
                    for (Iterator<List<String[]>> iter = HueController.Contacts.listIterator(); iter.hasNext();){
                        List<String[]> contact = iter.next();
                        if (nameSplit[0].equals(contact.get(0)[0]) && nameSplit[1].equals(contact.get(0)[1])) {
                            iter.remove();
                            System.out.println("removed successfully");
                        }
                    }
                }

                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String[] nameSplit = listItemText.getText().toString().split("\\s+");

                //save the current first and last name
                HueController.saveCurrentInformation(nameSplit[0], nameSplit[1]);

                notifyDataSetChanged();

                //navigate to the EditContact class
                context.startActivity(new Intent(context, EditContact.class));
            }
        });

        return view;
    }
}
