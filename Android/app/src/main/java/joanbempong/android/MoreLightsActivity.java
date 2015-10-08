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

public class MoreLightsActivity extends Activity {
    private PHHueSDK phHueSDK;

    private LightListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_lights);

        phHueSDK = PHHueSDK.getInstance();

        Button yesBtn, noBtn;

        yesBtn = (Button) findViewById(R.id.yesBtn);
        yesBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                yesOnClick();
            }

        });

        noBtn = (Button) findViewById(R.id.noBtn);
        noBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                noOnClick();
            }

        });

        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        adapter = new LightListAdapter(getApplicationContext(), allLights);

        ListView allLightsList = (ListView) findViewById(R.id.light_list);
        allLightsList.setAdapter(adapter);
    }

    public void yesOnClick() {
        // navigate to the AddLights page
        startActivity(new Intent(MoreLightsActivity.this, AddLightSetupActivity.class));
    }

    public void noOnClick() {
        // navigate to the ConfigureLights page
        startActivity(new Intent(MoreLightsActivity.this, ConfigureLightsActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_more_lights, menu);
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
