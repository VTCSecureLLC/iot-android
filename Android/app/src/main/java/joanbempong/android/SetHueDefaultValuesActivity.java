package joanbempong.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class SetHueDefaultValuesActivity extends AppCompatActivity {

    private PHHueSDK phHueSDK;
    private HueController hueController;


    private List<PHLight> allLights;

    private DefaultIncomingCallLightChoiceAdapter incomingAdapter;
    private DefaultMissedCallLightChoiceAdapter missedAdapter;

    private ListView incomingCallLightChoices, missedCallLightChoices;
    private Spinner incomingCallFlashPatternList, incomingCallFlashRateList, missedCallDurationList;

    Button saveBtn;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_hue_default_values);

        phHueSDK = PHHueSDK.getInstance();
        hueController = HueController.getInstance();

        PHBridge bridge = phHueSDK.getSelectedBridge();
        allLights = bridge.getResourceCache().getAllLights();

        hueController.saveCurrentDefaultValues();
        hueController.setCleanDefaultValues();

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
        saveBtn = (Button)findViewById(R.id.saveBtn);

        //creates an on click listener
        saveBtn.setOnClickListener(saveBtnOnClickListener);
    }

    public void addItemsToFlashPattern(){
        adapter = ArrayAdapter.createFromResource(this, R.array.flash_pattern_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        incomingCallFlashPatternList.setAdapter(adapter);

        //set the current selected item
        int selected = adapter.getPosition(hueController.getDefaultIncomingFlashPattern());
        incomingCallFlashPatternList.setSelection(selected);
        System.out.println(hueController.getDefaultIncomingFlashPattern());
        System.out.println(selected);
    }

    public void addItemsToFlashRate(){
        adapter = ArrayAdapter.createFromResource(this, R.array.flash_rate_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        incomingCallFlashRateList.setAdapter(adapter);

        //set the current selected item
        int selected = adapter.getPosition(hueController.getDefaultIncomingFlashRate());
        incomingCallFlashRateList.setSelection(selected);

        System.out.println(hueController.getDefaultIncomingFlashRate());
        System.out.println(selected);
    }

    public void addItemsToDuration(){
        adapter = ArrayAdapter.createFromResource(this, R.array.duration_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        missedCallDurationList.setAdapter(adapter);

        //set the current selected item
        int selected = adapter.getPosition(hueController.getDefaultMissedDuration());
        missedCallDurationList.setSelection(selected);


        System.out.println(hueController.getDefaultMissedDuration());
        System.out.println(selected);
    }

    View.OnClickListener saveBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if (hueController.getIncomingCallDefaultLights().size() == 0){
                Toast.makeText(SetHueDefaultValuesActivity.this, "Please choose a light or more for your default incoming calls.", Toast.LENGTH_SHORT).show();
            }else if (incomingCallFlashPatternList.getSelectedItem().equals("--")){
                Toast.makeText(SetHueDefaultValuesActivity.this, "Please choose a flash pattern for your default incoming calls.", Toast.LENGTH_SHORT).show();
            }else if (incomingCallFlashRateList.getSelectedItem().equals("--")){
                Toast.makeText(SetHueDefaultValuesActivity.this, "Please choose a flash rate for your default incoming calls.", Toast.LENGTH_SHORT).show();
            }else if (hueController.getMissedCallDefaultLights().size() == 0){
                Toast.makeText(SetHueDefaultValuesActivity.this, "Please choose a light or more for your default missed calls.", Toast.LENGTH_SHORT).show();
            }else if (missedCallDurationList.getSelectedItem().equals("--")){
                Toast.makeText(SetHueDefaultValuesActivity.this, "Please choose a duration for your default missed calls.", Toast.LENGTH_SHORT).show();
            }
            else{
                hueController.saveDefaultValues(String.valueOf(incomingCallFlashPatternList.getSelectedItem()),
                        String.valueOf(incomingCallFlashRateList.getSelectedItem()), String.valueOf(missedCallDurationList.getSelectedItem()));
                Toast.makeText(SetHueDefaultValuesActivity.this, "The default values have been saved", Toast.LENGTH_SHORT).show();
                hueController.setDefaultValues(true);
                hueController.updateContacts();
                // navigate to the previous page
                finish();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_hue_default_values, menu);
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
