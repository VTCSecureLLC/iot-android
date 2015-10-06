package joanbempong.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;

import java.util.List;

public class ConfigureLightsActivity extends Activity {
    private PHHueSDK phHueSDK;
    private List<PHLight> allLights;
    public static final String TAG = "ACE Notification";
    private HueSharedPreferences prefs;

    private ConfigureLightAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_lights);

        phHueSDK = PHHueSDK.getInstance();

        PHBridge bridge = phHueSDK.getSelectedBridge();
        allLights = bridge.getResourceCache().getAllLights();
        adapter = new ConfigureLightAdapter(this, allLights);
        ListView allLightsList = (ListView) findViewById(R.id.light_list);
        allLightsList.setAdapter(adapter);

        Button nextBtn;

        nextBtn = (Button) findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                nextOnClick();
            }

        });

        prefs = HueSharedPreferences.getInstance(getApplicationContext());
        System.out.println("bridge username: " +  prefs.getUsername());
    }

    public void nextOnClick() {
        // navigate to the HueDefaultValuesActivity page
        startActivity(new Intent(ConfigureLightsActivity.this, HueDefaultValuesActivity.class));
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
}
