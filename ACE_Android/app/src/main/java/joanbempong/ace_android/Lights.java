package joanbempong.ace_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class Lights extends AppCompatActivity {


    ListView listLights;
    ArrayList<String> stringArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lights);

        //connects the listview to the widget created in xml
        listLights = (ListView)findViewById(R.id.listLights);

        //add lights to the listview
        stringArray = new ArrayList<>();
        int lightList = 0;
        while (lightList != HueController.Lights.size()) {
            //add the name of lights to the stringArray array
            System.out.println(HueController.Lights.get(lightList)[3]);
            stringArray.add(HueController.Lights.get(lightList)[3]);
            lightList++;
        }

        ListLights adapter = new ListLights(stringArray, this);
        listLights.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lights, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            //navigate to the Home page
            startActivity(new Intent(Lights.this, Home.class));
        }
        else if (id == R.id.action_lights) {
            //navigate to the Lights page
            startActivity(new Intent(Lights.this, Lights.class));
        }
        else if (id == R.id.action_settings) {
            //navigate to the Settings page
            startActivity(new Intent(Lights.this, Settings.class));
        }

        return super.onOptionsItemSelected(item);
    }

    //action to take when the back button is pressed
    @Override
    public void onBackPressed()
    {
        //navigate to the Home page
        startActivity(new Intent(Lights.this, Home.class));
    }
}
