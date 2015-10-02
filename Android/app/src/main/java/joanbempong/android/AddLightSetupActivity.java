package joanbempong.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;

import java.util.List;
import java.util.Map;

public class AddLightSetupActivity extends Activity {
    private PHHueSDK phHueSDK;
    private ProgressBar pbar;
    private static final int MAX_TIME=60;
    private boolean isDialogShowing;
    private PHBridge bridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_light_setup);


        isDialogShowing=false;

        phHueSDK = PHHueSDK.getInstance();

        pbar = (ProgressBar) findViewById(R.id.countdownPB);
        pbar.setMax(MAX_TIME);


        Button searchBtn;

        searchBtn = (Button) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchOnClick();
            }

        });


    }

    public void searchOnClick() {
        System.out.println("current light: ");

        bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        for (PHLight light : allLights){
            System.out.println(light.getName());
        }

        PHWizardAlertDialog.getInstance().showProgressDialog(R.string.search_progress, AddLightSetupActivity.this);
        bridge.findNewLights(listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_light, menu);
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
            System.out.println(message);
        }

        @Override
        public void onReceivingLightDetails(PHLight light) {
            System.out.println("light detail receiving");
            System.out.println(light.getName());
        }

        @Override
        public void onReceivingLights(List<PHBridgeResource> list) {
            System.out.println(list.size());
            if (list.size() != 0) {
                for (PHBridgeResource br : list) {
                    System.out.println(br.getName());
                    System.out.println(br.getIdentifier());

                    //works but an error is received:
                    //Attempt to invoke virtual method 'com.philips.lighting.model.PHLightState com.philips.lighting.model.PHLight.getLastKnownLightState()' on a null object reference
                    /*PHLightState state = new PHLightState();
                    state.setOn(true);
                    bridge.updateLightState(br.getIdentifier(), state, listener);*/

                    System.out.println("light should be on");
                }
            }
            System.out.println("new light receiving");
            incrementProgress();
        }

        @Override
        public void onSearchComplete() {
            System.out.println("updated light: ");

            List<PHLight> allLights = bridge.getResourceCache().getAllLights();
            for (PHLight light : allLights){
                System.out.println(light.getName());
            }

            System.out.println("search is completed, navigating");

            // navigate to the AddLights page
            startActivity(new Intent(AddLightSetupActivity.this, ConfigureLightsActivity.class));
        }
    };
}
