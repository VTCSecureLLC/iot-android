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

public class AddLightActivity extends Activity {
    private PHHueSDK phHueSDK;
    private ProgressBar pbar;
    private static final int MAX_TIME=60;
    private boolean isDialogShowing;
    private PHBridge bridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_light);


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
        bridge = phHueSDK.getSelectedBridge();
        PHWizardAlertDialog.getInstance().showProgressDialog(R.string.search_progress, AddLightActivity.this);
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
            /*if (code == PHMessageType.PUSHLINK_BUTTON_NOT_PRESSED) {
                incrementProgress();
            }
            else if (code == PHMessageType.)(code == PHMessageType.PUSHLINK_AUTHENTICATION_FAILED) {
                incrementProgress();

                if (!isDialogShowing) {
                    isDialogShowing=true;
                    AddLightActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddLightActivity.this);
                            builder.setMessage(message).setNeutralButton(R.string.btn_ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                        }
                                    });

                            builder.create();
                            builder.show();
                        }
                    });
                }

            }*/

        }

        @Override
        public void onReceivingLightDetails(PHLight light) {
            System.out.println("light detail receiving");
            System.out.println(light.getName());
        }

        @Override
        public void onReceivingLights(List<PHBridgeResource> arg0) {
            System.out.println("new light receiving");
            incrementProgress();
        }

        @Override
        public void onSearchComplete() {
            System.out.println("search is completed, navigating");
            // navigate to the AddLights page
            startActivity(new Intent(AddLightActivity.this, ConfigureLightsActivity.class));
        }
    };
}