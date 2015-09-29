package joanbempong.ace_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joan Bempong on 9/29/2015.
 */
public class ListLightsSettings extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;

    int lightNum = 0;
    long sleep = 500;


    public ListLightsSettings(ArrayList<String> list, Context context) {
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
            view = inflater.inflate(R.layout.lights_list_settings, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView)view.findViewById(R.id.light_list);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.delete_btn);
        ImageButton editBtn = (ImageButton)view.findViewById(R.id.edit_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                notifyDataSetChanged();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //save the current light name
                String lightName = listItemText.getText().toString();
                System.out.println("lightName");
                System.out.println(lightName);
                HueController.saveCurrentLightInformation(lightName);

                notifyDataSetChanged();


                int lightVal = 0;
                while (lightVal != HueController.Lights.size()) {
                    System.out.println(lightName);
                    System.out.println(HueController.Lights.get(lightVal)[3]);
                    System.out.println(HueController.Lights.get(lightVal)[0]);
                    if (lightName.equals(HueController.Lights.get(lightVal)[3])) {
                        lightNum = Integer.parseInt(HueController.Lights.get(lightVal)[0]);
                        break;
                    }

                    lightVal++;
                }

                //start flashing
                HueController.testFlash = true;
                HueController.putHueFlash(lightNum, sleep);

                //creates a new alert dialog
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                final EditText rename = new EditText(context);
                rename.setText(lightName);
                alert.setView(rename);
                alert.setTitle("Renaming " + lightName);
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HueController.renameLight(rename.getText().toString(), lightNum);

                        //rename the light in the Lights array
                        for (String[] light: HueController.Lights){
                            System.out.println(rename.getText().toString());
                            System.out.println(light[3]);
                            System.out.println(light[0]);
                            if (HueController.oldLightName.equals(light[3])) {
                                light[3] = rename.getText().toString();
                                break;
                            }
                        }

                        //ensure the contacts' notification lights get renamed as well
                        String[] newLightName = {rename.getText().toString()};
                        if (HueController.Contacts != null){
                            for (List<String[]> contact: HueController.Contacts){
                                if (contact.get(2)[0].equals(HueController.oldLightName)){
                                    contact.set(2, newLightName);
                                }
                                if (contact.get(4)[0].equals(HueController.oldLightName)){
                                    contact.set(4, newLightName);
                                }
                            }
                        }
                        //stop flashing
                        HueController.testFlash = false;


                        //navigate to the MyLights class
                        context.startActivity(new Intent(context, MyLights.class));
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //stop flashing
                        HueController.testFlash = false;
                        dialog.cancel();
                    }
                });
                //create the alert dialog and show it
                alert.create();
                alert.show();
            }
        });

        return view;
    }
}
