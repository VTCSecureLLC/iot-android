package joanbempong.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;

import java.util.List;

public class HueDefaultValuesActivity extends Activity {

    private PHHueSDK phHueSDK;
    private HueController hueController;
    public static final String TAG = "ACE Notification";


    private List<PHLight> allLights;

    private DefaultIncomingCallLightChoiceAdapter incomingAdapter;
    private DefaultMissedCallLightChoiceAdapter missedAdapter;

    private ListView incomingCallLightChoices, missedCallLightChoices;
    private Spinner incomingCallFlashPatternList, incomingCallFlashRateList, missedCallDurationList;

    Button nextBtn, skipBtn;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hue_default_values);

        phHueSDK = PHHueSDK.getInstance();
        hueController = HueController.getInstance();

        PHBridge bridge = phHueSDK.getSelectedBridge();
        allLights = bridge.getResourceCache().getAllLights();

        incomingAdapter = new DefaultIncomingCallLightChoiceAdapter(this, allLights, hueController);
        incomingCallLightChoices = (ListView) findViewById(R.id.incomingCallListView);
        incomingCallLightChoices.setAdapter(incomingAdapter);

        missedAdapter = new DefaultMissedCallLightChoiceAdapter(this, allLights, hueController);
        missedCallLightChoices = (ListView) findViewById(R.id.missedCallListView);
        missedCallLightChoices.setAdapter(missedAdapter);

        incomingCallFlashPatternList = (Spinner)findViewById(R.id.incomingCallFlashPatternList);
        incomingCallFlashRateList = (Spinner)findViewById(R.id.incomingCallFlashRateList);
        missedCallDurationList = (Spinner)findViewById(R.id.missedCallDurationList);

        //adds items to the spinners
        addItemsToFlashPattern();
        addItemsToFlashRate();
        addItemsToDuration();

        //connects the button to the widgets created in xml
        nextBtn = (Button)findViewById(R.id.nextBtn);
        skipBtn = (Button)findViewById(R.id.skipBtn);

        //creates an on click listener
        nextBtn.setOnClickListener(nextBtnOnClickListener);
        skipBtn.setOnClickListener(skipBtnOnClickListener);

    }

    public void addItemsToFlashPattern(){
        adapter = ArrayAdapter.createFromResource(this, R.array.flash_pattern_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        incomingCallFlashPatternList.setAdapter(adapter);
    }

    public void addItemsToFlashRate(){
        adapter = ArrayAdapter.createFromResource(this, R.array.flash_rate_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        incomingCallFlashRateList.setAdapter(adapter);
    }

    public void addItemsToDuration(){
        adapter = ArrayAdapter.createFromResource(this, R.array.duration_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        missedCallDurationList.setAdapter(adapter);
    }

    View.OnClickListener nextBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if (hueController.getIncomingCallDefaultLights().size() == 0){
                Toast.makeText(HueDefaultValuesActivity.this, "Please choose a light or more for your default incoming calls.", Toast.LENGTH_SHORT).show();
            }else if (incomingCallFlashPatternList.getSelectedItem().equals("--")){
                Toast.makeText(HueDefaultValuesActivity.this, "Please choose a flash pattern for your default incoming calls.", Toast.LENGTH_SHORT).show();
            }else if (incomingCallFlashRateList.getSelectedItem().equals("--")){
                Toast.makeText(HueDefaultValuesActivity.this, "Please choose a flash rate for your default incoming calls.", Toast.LENGTH_SHORT).show();
            }else if (hueController.getMissedCallDefaultLights().size() == 0){
                Toast.makeText(HueDefaultValuesActivity.this, "Please choose a light or more for your default missed calls.", Toast.LENGTH_SHORT).show();
            }else if (missedCallDurationList.getSelectedItem().equals("--")){
                Toast.makeText(HueDefaultValuesActivity.this, "Please choose a duration for your default missed calls.", Toast.LENGTH_SHORT).show();
            }
            else{
                hueController.saveDefaultIncomingCall(String.valueOf(incomingCallFlashPatternList.getSelectedItem()),
                        String.valueOf(incomingCallFlashRateList.getSelectedItem()));
                hueController.saveDefaultMissedCall(String.valueOf(missedCallDurationList.getSelectedItem()));
                //Toast.makeText(HueDefaultValuesActivity.this, "The default values have been saved", Toast.LENGTH_SHORT).show();
                // navigate to the ContactListDefaultActivity page
                startActivity(new Intent(HueDefaultValuesActivity.this, ContactListDefaultActivity.class));
            }
        }
    };

    View.OnClickListener skipBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            // navigate to the CompletedSetupActivity page
            startActivity(new Intent(HueDefaultValuesActivity.this, CompletedSetupActivity.class));
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hue_default_values, menu);
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
