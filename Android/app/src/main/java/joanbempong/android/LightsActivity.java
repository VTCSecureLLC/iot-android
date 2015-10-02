package joanbempong.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;

import java.util.ArrayList;
import java.util.List;

public class LightsActivity extends AppCompatActivity {

    ListView listLights;
    ArrayList<String> stringArray;
    PHHueSDK phHueSDK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lights);

        phHueSDK = PHHueSDK.create();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        //connects the listview to the widget created in xml
        listLights = (ListView)findViewById(R.id.listLights);

        LightListControlAdapter adapter = new LightListControlAdapter(this, allLights);
        listLights.setAdapter(adapter);
    }

    //action to take when the back button is pressed
    @Override
    public void onBackPressed()
    {
        //navigate to the Home page
        startActivity(new Intent(LightsActivity.this, HomeActivity.class));
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
            startActivity(new Intent(LightsActivity.this, HomeActivity.class));
        }
        else if (id == R.id.action_lights) {
            //navigate to the Lights page
            startActivity(new Intent(LightsActivity.this, LightsActivity.class));
        }
        else if (id == R.id.action_settings) {
            //navigate to the Settings page
            startActivity(new Intent(LightsActivity.this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
