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

import java.util.ArrayList;
import java.util.List;

public class HueDefaultValuesActivity extends Activity {

    private PHHueSDK phHueSDK;
    private HueController hueController;
    private MyChoices myChoices;
    private List<PHLight> allLights;

    private DefaultLightChoiceAdapter defaultAdapter;

    private ListView lightChoices;
    private Spinner durationList, flashPatternList, flashRateList, colorList;

    private Button nextBtn, skipBtn;
    private ArrayAdapter<String> adapterFP, adapterFR, adapterC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hue_default_values);

        phHueSDK = PHHueSDK.getInstance();
        hueController = HueController.getInstance();
        myChoices = MyChoices.getInstance();

        PHBridge bridge = phHueSDK.getSelectedBridge();
        allLights = bridge.getResourceCache().getAllLights();

        defaultAdapter = new DefaultLightChoiceAdapter(this, allLights, hueController);
        lightChoices = (ListView) findViewById(R.id.defaultListView);
        lightChoices.setAdapter(defaultAdapter);

        durationList = (Spinner)findViewById(R.id.durationList);
        flashPatternList = (Spinner)findViewById(R.id.flashPatternList);
        flashRateList = (Spinner)findViewById(R.id.flashRateList);
        colorList = (Spinner)findViewById(R.id.colorList);

        //adds items to the spinners
        addItemsToDurationList();
        addItemsToFlashPatternList();
        addItemsToFlashRateList();
        addItemsToColorList();

        //connects the button to the widgets created in xml
        nextBtn = (Button)findViewById(R.id.nextBtn);
        skipBtn = (Button)findViewById(R.id.skipBtn);

        //creates an on click listener
        nextBtn.setOnClickListener(nextBtnOnClickListener);
        skipBtn.setOnClickListener(skipBtnOnClickListener);

    }

    public void addItemsToDurationList(){
        ArrayList<String> choices = myChoices.getDurationList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choices);
        durationList.setAdapter(adapter);
    }

    public void addItemsToFlashPatternList(){
        ArrayList<String> choices = myChoices.getFlashPatternList();
        adapterFP = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choices);
        flashPatternList.setAdapter(adapterFP);
    }

    public void addItemsToFlashRateList(){
        ArrayList<String> choices = myChoices.getFlashRateList();
        adapterFR = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choices);
        flashRateList.setAdapter(adapterFR);
    }

    public void addItemsToColorList(){
        ArrayList<String> choices = myChoices.getColorList();
        adapterC = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choices);
        colorList.setAdapter(adapterC);
    }


    View.OnClickListener nextBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if (hueController.getDefaultLights().size() == 0){
                Toast.makeText(HueDefaultValuesActivity.this, "Please choose a light or more for your default incoming/missed calls.", Toast.LENGTH_SHORT).show();
            }else if (durationList.getSelectedItem().equals("--")){
                Toast.makeText(HueDefaultValuesActivity.this, "Please choose a duration for your default missed calls.", Toast.LENGTH_SHORT).show();
            }else if (flashPatternList.getSelectedItem().equals("--")){
                Toast.makeText(HueDefaultValuesActivity.this, "Please choose a flash pattern for your default incoming calls.", Toast.LENGTH_SHORT).show();
            }else if (flashRateList.getSelectedItem().equals("--")){
                Toast.makeText(HueDefaultValuesActivity.this, "Please choose a flash rate for your default incoming calls.", Toast.LENGTH_SHORT).show();
            }else if (colorList.getSelectedItem().equals("--")){
                Toast.makeText(HueDefaultValuesActivity.this, "Please choose a color for your default incoming/missed calls.", Toast.LENGTH_SHORT).show();
            }
            else{
                hueController.saveDefaultValues(String.valueOf(durationList.getSelectedItem()),
                        String.valueOf(flashPatternList.getSelectedItem()), String.valueOf(flashRateList.getSelectedItem()),
                        String.valueOf(colorList.getSelectedItem()));
                hueController.setDefaultValues(true);
                //"retrieve" the contact list by creating new one (for testing purpose)
                if (hueController.getContactList().size() == 0) {
                    hueController.createNewContact("Shareef", "Ali", "1111111111", "0", "0", "0", false, false);
                    hueController.createNewContact("Gary", "Behm", "1111111111", "0", "0", "0", false, false);
                    hueController.createNewContact("Joan", "Bempong", "1111111111", "0", "0", "0", false, false);
                    hueController.createNewContact("Brian", "Trager", "1111111111", "0", "0", "0", false, false);
                }
                // navigate to the ContactListDefaultActivity page
                startActivity(new Intent(HueDefaultValuesActivity.this, ContactListDefaultActivity.class));
            }
        }
    };

    View.OnClickListener skipBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //"retrieve" the contact list by creating new one (for testing purpose)
            if (hueController.getContactList().size() == 0) {
                hueController.createNewContact("Shareef", "Ali", "1111111111", "0", "0", "0", false, false);
                hueController.createNewContact("Gary", "Behm", "1111111111", "0", "0", "0", false, false);
                hueController.createNewContact("Joan", "Bempong", "1111111111", "0", "0", "0", false, false);
                hueController.createNewContact("Brian", "Trager", "1111111111", "0", "0", "0", false, false);
            }
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
