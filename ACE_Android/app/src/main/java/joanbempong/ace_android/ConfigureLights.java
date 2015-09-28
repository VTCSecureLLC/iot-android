package joanbempong.ace_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ConfigureLights extends AppCompatActivity {

    TextView bodyText;
    ListView lightsList;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_lights);

        //connects the textview,listview, and button to the widgets created in xml
        bodyText = (TextView)findViewById(R.id.bodyText);
        lightsList = (ListView)findViewById(R.id.lightList);
        nextBtn = (Button)findViewById(R.id.nextBtn);

        //creates an on click listener
        nextBtn.setOnClickListener(nextBtnOnClickListener);

        //sets the text for the textview
        bodyText.setText("Let's configure your lights.");

        //add the lights to the listview
        ArrayList<String> stringArray = new ArrayList<>();
        int lightList = 0;
        while (lightList != HueController.Lights.size()) {
            //add the name of lights to the stringArray array
            System.out.println(HueController.Lights.get(lightList)[3]);
            System.out.println(HueController.Lights.get(lightList)[9]);
            stringArray.add(HueController.Lights.get(lightList)[3]);
            lightList++;
        }

        ConfigureLightsList adapter = new ConfigureLightsList(stringArray, this);
        lightsList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configure_lights, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //action to take when the back button is pressed
    @Override
    public void onBackPressed()
    {
        //navigate to the HueRegister page
        startActivity(new Intent(ConfigureLights.this, HueRegister.class));
    }

    OnClickListener nextBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if (!HueController.allLightsConfigured()){
                //creates a new alert dialog
                final AlertDialog.Builder alert = new AlertDialog.Builder(ConfigureLights.this);
                alert.setTitle("Are you sure?");
                alert.setMessage("All lights that have not been configured will be assumed to be working and do not support color.");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // navigate to the HueDefaultValues page
                        startActivity(new Intent(ConfigureLights.this, HueDefaultValues.class));
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                //create the alert dialog and show it
                alert.create();
                alert.show();
            }
            else {
                // navigate to the HueDefaultValues page
                startActivity(new Intent(ConfigureLights.this, HueDefaultValues.class));
            }
        }
    };
}
