package joanbempong.ace_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Joan Bempong on 9/28/2015.
 */
public class ListLights extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    int lightNum = 0;


    public ListLights(ArrayList<String> list, Context context) {
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
            view = inflater.inflate(R.layout.lights_list, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView) view.findViewById(R.id.light_list);
        listItemText.setText(list.get(position));
        //System.out.println(listItemText.getText());

        final SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);

        final Button redBtn = (Button)view.findViewById(R.id.redBtn);
        final Button orangeBtn = (Button)view.findViewById(R.id.orangeBtn);
        final Button yellowBtn = (Button)view.findViewById(R.id.yellowBtn);
        final Button greenBtn = (Button)view.findViewById(R.id.greenBtn);
        final Button blueBtn = (Button)view.findViewById(R.id.blueBtn);
        final Button purpleBtn = (Button)view.findViewById(R.id.purpleBtn);
        final Button pinkBtn = (Button)view.findViewById(R.id.pinkBtn);

        //display the color buttons only if this light supports color
        String lightString = listItemText.getText().toString();

        int lightval = 0;
        while (lightval != HueController.Lights.size()) {
            System.out.println(lightString);
            System.out.println(HueController.Lights.get(lightval)[3]);
            System.out.println(HueController.Lights.get(lightval)[0]);
            if (lightString.equals(HueController.Lights.get(lightval)[3])) {
                lightNum = Integer.parseInt(HueController.Lights.get(lightval)[0]);
                System.out.println(HueController.Lights.get(lightval)[10]);
                if(HueController.Lights.get(lightval)[10].equals("nocolor")){
                    redBtn.setVisibility(View.GONE);
                    orangeBtn.setVisibility(View.GONE);
                    yellowBtn.setVisibility(View.GONE);
                    greenBtn.setVisibility(View.GONE);
                    blueBtn.setVisibility(View.GONE);
                    purpleBtn.setVisibility(View.GONE);
                    pinkBtn.setVisibility(View.GONE);
                }
                break;
            }

            lightval++;
        }

        redBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the number of current light
                String lightString = listItemText.getText().toString();

                int lightval = 0;
                while (lightval != HueController.Lights.size()) {
                    System.out.println(lightString);
                    System.out.println(HueController.Lights.get(lightval)[3]);
                    System.out.println(HueController.Lights.get(lightval)[0]);
                    if (lightString.equals(HueController.Lights.get(lightval)[3])) {
                        lightNum = Integer.parseInt(HueController.Lights.get(lightval)[0]);
                        break;
                    }

                    lightval++;
                }

                int lightBright = seekBar.getProgress();

                //show the color on the bulb
                HueController.putHueColor(lightNum, "red", lightBright);
            }
        });

        orangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the number of current light
                String lightString = listItemText.getText().toString();

                int lightval = 0;
                while (lightval != HueController.Lights.size()) {
                    System.out.println(lightString);
                    System.out.println(HueController.Lights.get(lightval)[3]);
                    System.out.println(HueController.Lights.get(lightval)[0]);
                    if (lightString.equals(HueController.Lights.get(lightval)[3])) {
                        lightNum = Integer.parseInt(HueController.Lights.get(lightval)[0]);
                        break;
                    }

                    lightval++;
                }

                int lightBright = seekBar.getProgress();

                //show the color on the bulb
                HueController.putHueColor(lightNum, "orange", lightBright);
            }
        });

        yellowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the number of current light
                String lightString = listItemText.getText().toString();

                int lightval = 0;
                while (lightval != HueController.Lights.size()) {
                    System.out.println(lightString);
                    System.out.println(HueController.Lights.get(lightval)[3]);
                    System.out.println(HueController.Lights.get(lightval)[0]);
                    if (lightString.equals(HueController.Lights.get(lightval)[3])) {
                        lightNum = Integer.parseInt(HueController.Lights.get(lightval)[0]);
                        break;
                    }

                    lightval++;
                }

                int lightBright = seekBar.getProgress();

                //show the color on the bulb
                HueController.putHueColor(lightNum, "yellow", lightBright);
            }
        });

        greenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the number of current light
                String lightString = listItemText.getText().toString();

                int lightval = 0;
                while (lightval != HueController.Lights.size()) {
                    System.out.println(lightString);
                    System.out.println(HueController.Lights.get(lightval)[3]);
                    System.out.println(HueController.Lights.get(lightval)[0]);
                    if (lightString.equals(HueController.Lights.get(lightval)[3])) {
                        lightNum = Integer.parseInt(HueController.Lights.get(lightval)[0]);
                        break;
                    }

                    lightval++;
                }

                int lightBright = seekBar.getProgress();

                //show the color on the bulb
                HueController.putHueColor(lightNum, "green", lightBright);
            }
        });

        blueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the number of current light
                String lightString = listItemText.getText().toString();

                int lightval = 0;
                while (lightval != HueController.Lights.size()) {
                    System.out.println(lightString);
                    System.out.println(HueController.Lights.get(lightval)[3]);
                    System.out.println(HueController.Lights.get(lightval)[0]);
                    if (lightString.equals(HueController.Lights.get(lightval)[3])) {
                        lightNum = Integer.parseInt(HueController.Lights.get(lightval)[0]);
                        break;
                    }

                    lightval++;
                }

                int lightBright = seekBar.getProgress();

                //show the color on the bulb
                HueController.putHueColor(lightNum, "blue", lightBright);
            }
        });

        purpleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the number of current light
                String lightString = listItemText.getText().toString();

                int lightval = 0;
                while (lightval != HueController.Lights.size()) {
                    System.out.println(lightString);
                    System.out.println(HueController.Lights.get(lightval)[3]);
                    System.out.println(HueController.Lights.get(lightval)[0]);
                    if (lightString.equals(HueController.Lights.get(lightval)[3])) {
                        lightNum = Integer.parseInt(HueController.Lights.get(lightval)[0]);
                        break;
                    }

                    lightval++;
                }

                int lightBright = seekBar.getProgress();

                //show the color on the bulb
                HueController.putHueColor(lightNum, "purple", lightBright);
            }
        });

        pinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the number of current light
                String lightString = listItemText.getText().toString();

                int lightval = 0;
                while (lightval != HueController.Lights.size()) {
                    System.out.println(lightString);
                    System.out.println(HueController.Lights.get(lightval)[3]);
                    System.out.println(HueController.Lights.get(lightval)[0]);
                    if (lightString.equals(HueController.Lights.get(lightval)[3])) {
                        lightNum = Integer.parseInt(HueController.Lights.get(lightval)[0]);
                        break;
                    }

                    lightval++;
                }

                int lightBright = seekBar.getProgress();

                //show the color on the bulb
                HueController.putHueColor(lightNum, "pink", lightBright);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int lightBright = seekBar.getProgress();

                //display the color buttons only if this light supports color
                String lightString = listItemText.getText().toString();

                int lightval = 0;
                while (lightval != HueController.Lights.size()) {
                    System.out.println(lightString);
                    System.out.println(HueController.Lights.get(lightval)[3]);
                    System.out.println(HueController.Lights.get(lightval)[0]);
                    if (lightString.equals(HueController.Lights.get(lightval)[3])) {
                        lightNum = Integer.parseInt(HueController.Lights.get(lightval)[0]);
                        break;
                    }

                    lightval++;
                }
                if (lightBright == 0) {
                    HueController.putHueOff(lightNum);
                }
                else {
                    HueController.putHueOn(lightNum, lightBright);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
    }
}
