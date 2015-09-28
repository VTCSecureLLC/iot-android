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


/**
 * Created by Joan Bempong on 9/25/2015.
 */
public class ConfigureLightsList extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<>();
    private Context context;

    public ConfigureLightsList(ArrayList<String> list, Context context) {
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
            view = inflater.inflate(R.layout.configure_lights, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView) view.findViewById(R.id.list_lights);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        ImageButton configuredBtn = (ImageButton) view.findViewById(R.id.configured);

        int lightList = 0;
        while (lightList != HueController.Lights.size()) {
            if (list.get(position).equals(HueController.Lights.get(lightList)[3])) {
                if (HueController.Lights.get(lightList)[9].equals("true")) {
                    configuredBtn.setBackgroundResource(android.R.drawable.presence_online);
                    break;
                }
                else{
                    configuredBtn.setBackgroundResource(android.R.drawable.presence_invisible);
                    break;
                }
            }
            lightList++;
        }

        configuredBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //store the name of the current light being configured
                HueController.currentLightConfigure = list.get(position);

                //navigate to the FlashCheck page
                context.startActivity(new Intent(context, FlashCheck.class));

                notifyDataSetChanged();
            }
        });
        return view;
    }
}
