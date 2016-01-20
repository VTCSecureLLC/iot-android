package joanbempong.android;

import org.linphone.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;

import java.util.List;
import java.util.Map;

public class MyLightsActivity extends Activity {
    private PHHueSDK phHueSDK;
    private PHBridge bridge;
    private EditLightAdapter adapter;
    private Button addBtn;
    private ProgressBar pbar;
    private static final int MAX_TIME=30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lights);

        phHueSDK = PHHueSDK.getInstance();
        bridge = phHueSDK.getSelectedBridge();

        pbar = (ProgressBar) findViewById(R.id.countdownPB);
        pbar.setMax(MAX_TIME);
        pbar.setVisibility(View.GONE);

        addBtn = (Button) findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addOnClick();
            }

        });


        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        adapter = new EditLightAdapter(this, allLights);

        ListView allLightsList = (ListView) findViewById(R.id.listLights);
        allLightsList.setAdapter(adapter);
    }

    public void addOnClick() {
        pbar.setVisibility(View.VISIBLE);
        addBtn.setText(R.string.search_progress);
        bridge.findNewLights(listener);
    }

    public void incrementProgress() {
        pbar.incrementProgressBy(1);
    }

    // If you want to handle the response from the bridge, create a PHLightListener object.
    PHLightListener listener = new PHLightListener() {

        @Override
        public void onSuccess() {
            System.out.println("Success!");
        }

        @Override
        public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
            System.out.println("state update");
        }

        @Override
        public void onError(int code, final String message) {
            System.out.println("error");
            System.out.println(code);
            System.out.println(message);

            System.out.println("search is completed, navigating");

            // navigate to the AddLights page
            startActivity(new Intent(MyLightsActivity.this, MyLightsActivity.class));
        }

        @Override
        public void onReceivingLightDetails(PHLight light) {
            System.out.println("light detail receiving");
            System.out.println(light.getName());
        }

        @Override
        public void onReceivingLights(List<PHBridgeResource> list) {
            System.out.println(list.size());

            PHBridge bridge = phHueSDK.getSelectedBridge();

            if (list.size() != 0) {
                for (PHBridgeResource br : list) {
                    System.out.println(br.getName());
                    System.out.println(br.getIdentifier());
                }

                // Try to disconnect and connect again to update the lights list
                HueSharedPreferences prefs = HueSharedPreferences.getInstance(getApplicationContext());
                PHAccessPoint connection = new PHAccessPoint();
                connection.setIpAddress(prefs.getLastConnectedIPAddress());
                connection.setUsername(prefs.getUsername());
                phHueSDK.disconnect(bridge);
                phHueSDK.connect(connection);
            }

            System.out.println("new light receiving");
            incrementProgress();
        }

        @Override
        public void onSearchComplete() {
            System.out.println("search is completed, navigating");

            // navigate to the AddLights page
            startActivity(new Intent(MyLightsActivity.this, MyLightsActivity.class));
        }
    };

    //action to take when the back button is pressed
    @Override
    public void onBackPressed()
    {
        //navigate to the Home page
        startActivity(new Intent(MyLightsActivity.this, SettingsActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_lights, menu);
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
