package joanbempong.android;

import org.linphone.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.ArrayList;
import java.util.List;

public class SetHueDefaultValuesActivity extends Activity {

    private PHHueSDK phHueSDK;
    private HueController hueController;
    private MyChoices myChoices;

    private List<PHLight> allLights;

    private DefaultLightChoiceAdapter defaultAdapter;

    private ListView lightChoices;
    private Spinner durationList, flashPatternList, colorList;
    private EditText flashRateValue;

    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_hue_default_values);

        phHueSDK = PHHueSDK.getInstance();
        hueController = HueController.getInstance();
        myChoices = MyChoices.getInstance();

        PHBridge bridge = phHueSDK.getSelectedBridge();
        allLights = bridge.getResourceCache().getAllLights();

        hueController.saveCurrentDefaultValues();
        hueController.setCleanDefaultLights();

        defaultAdapter = new DefaultLightChoiceAdapter(this, allLights, hueController);
        lightChoices = (ListView) findViewById(R.id.defaultListView);
        lightChoices.setAdapter(defaultAdapter);

        durationList = (Spinner)findViewById(R.id.durationList);
        flashPatternList = (Spinner)findViewById(R.id.flashPatternList);
        //flashRateList = (Spinner)findViewById(R.id.flashRateList);
        colorList = (Spinner)findViewById(R.id.colorList);

        flashRateValue = (EditText)findViewById(R.id.flashRateValue);

        //adds items to the spinners
        addItemsToDurationList();
        addItemsToFlashPatternList();
        //addItemsToFlashRateList();
        addItemsToColorList();

        //connects the button to the widgets created in xml
        saveBtn = (Button)findViewById(R.id.saveBtn);

        //creates an on click listener
        saveBtn.setOnClickListener(saveBtnOnClickListener);
        flashPatternList.setOnItemSelectedListener(flashPatternListOnItemSelectedListener);
        colorList.setOnItemSelectedListener(colorListOnItemSelectedListener);

        hueController.saveAllLightStates();
    }

    public void addItemsToDurationList(){
        ArrayList<String> choices = myChoices.getDurationList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, choices);
        durationList.setAdapter(adapter);

        //set the current selected item
        int selected = adapter.getPosition(hueController.getDefaultDuration());
        durationList.setSelection(selected);
    }

    public void addItemsToFlashPatternList(){
        ArrayList<String> choices = myChoices.getFlashPatternList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, choices);
        flashPatternList.setAdapter(adapter);

        //set the current selected item
        int selected = adapter.getPosition(hueController.getDefaultFlashPattern());
        flashPatternList.setSelection(selected);
    }

    /*public void addItemsToFlashRateList(){
        ArrayList<String> choices = myChoices.getFlashRateList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, choices);
        flashRateList.setAdapter(adapter);

        //set the current selected item
        int selected = adapter.getPosition(hueController.getDefaultFlashRate());
        flashRateList.setSelection(selected);
    }*/

    public void addItemsToColorList(){
        ArrayList<String> choices = myChoices.getColorList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, choices);
        colorList.setAdapter(adapter);

        //set the current selected item
        int selected = adapter.getPosition(hueController.getDefaultColor());
        colorList.setSelection(selected);
    }

    View.OnClickListener saveBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            ACEPattern pattern = ACEPattern.getInstance();
            if (hueController.getDefaultLights().size() == 0){
                Toast.makeText(SetHueDefaultValuesActivity.this, "Please choose a light or more for your default incoming/missed calls.", Toast.LENGTH_SHORT).show();
            }else if (durationList.getSelectedItem().equals("--")){
                Toast.makeText(SetHueDefaultValuesActivity.this, "Please choose a duration for your default missed calls.", Toast.LENGTH_SHORT).show();
            }else if (flashPatternList.getSelectedItem().equals("--")){
                Toast.makeText(SetHueDefaultValuesActivity.this, "Please choose a flash pattern for your default incoming calls.", Toast.LENGTH_SHORT).show();
            }
            /*else if (flashRateList.getSelectedItem().equals("--")){
                Toast.makeText(HueDefaultValuesActivity.this, "Please choose a flash rate for your default incoming calls.", Toast.LENGTH_SHORT).show();
            }*/
            else if (flashRateValue.getText().toString().equals("")) {
                Toast.makeText(SetHueDefaultValuesActivity.this, "Please choose a flash rate value for your default incoming calls.", Toast.LENGTH_SHORT).show();
            }else if (colorList.getSelectedItem().equals("--")){
                Toast.makeText(SetHueDefaultValuesActivity.this, "Please choose a color for your default incoming/missed calls.", Toast.LENGTH_SHORT).show();
            }
            else{
                hueController.saveDefaultValues(String.valueOf(durationList.getSelectedItem()),
                        String.valueOf(flashPatternList.getSelectedItem()), String.valueOf(flashRateValue.getText()),
                        String.valueOf(colorList.getSelectedItem()));

                //pattern has been interrupted, stop it
                pattern.setPatternInterrupted(true);

                Toast.makeText(SetHueDefaultValuesActivity.this, "The default values have been saved", Toast.LENGTH_SHORT).show();
                hueController.setDefaultValues(true);
                hueController.updateContacts();
                hueController.restoreAllLightStates();
                // navigate to the previous page
                finish();
            }
        }
    };

    AdapterView.OnItemSelectedListener flashPatternListOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            System.out.println(flashPatternList.getSelectedItem());
            //show this pattern
            showThisPattern(flashPatternList.getSelectedItem().toString(), false);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener colorListOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            System.out.println(colorList.getSelectedItem());
            //show this pattern
            showThisColor(colorList.getSelectedItem().toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public void showThisPattern(String patternName, Boolean repeat){
        ACEPattern pattern = ACEPattern.getInstance();
        PHHueSDK phHueSDK = PHHueSDK.getInstance();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        ACEColors colors = ACEColors.getInstance();
        Double[] colorXY = {colors.getColorsList().get("white")[0], colors.getColorsList().get("white")[1]};
        if (!patternName.equals("--")) {
            for (String defaultLight : hueController.getDefaultLights()) {
                for (PHLight light : allLights) {
                    if (defaultLight.equals(light.getName())) {
                        if (light.supportsColor()) {
                            pattern.setPatternInterrupted(true);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            pattern.setPatternInterrupted(false);
if (patternName.equals("None")){
pattern.nonePattern(light, repeat, Long.valueOf(String.valueOf(flashRateValue.getText())), colorXY);
}
if (patternName.equals("Short On")){
pattern.shortOnPattern(light, repeat, Long.valueOf(String.valueOf(flashRateValue.getText())), colorXY);
}
if (patternName.equals("Long On")){
pattern.longOnPattern(light, repeat, Long.valueOf(String.valueOf(flashRateValue.getText())), colorXY);
}

if (patternName.equals("Color")){
pattern.colorPattern(light, repeat, Long.valueOf(String.valueOf(flashRateValue.getText())), colorXY);
}
if (patternName.equals("Fire")){
pattern.firePattern(light, repeat, Long.valueOf(String.valueOf(flashRateValue.getText())), colorXY);
}
if (patternName.equals("RIT")){
pattern.ritPattern(light, repeat, Long.valueOf(String.valueOf(flashRateValue.getText())), colorXY);
}
if (patternName.equals("Cloudy Sky")){
pattern.cloudySkyPattern(light, repeat, Long.valueOf(String.valueOf(flashRateValue.getText())), colorXY);
}
if (patternName.equals( "Grassy Green")){
pattern.grassyGreenPattern(light, repeat, Long.valueOf(String.valueOf(flashRateValue.getText())), colorXY);
}
if (patternName.equals("Lavender")){
pattern.lavenderPattern(light, repeat, Long.valueOf(String.valueOf(flashRateValue.getText())), colorXY);
}
if (patternName.equals("Bloody Red")){
pattern.bloodyRedPattern(light, repeat, Long.valueOf(String.valueOf(flashRateValue.getText())), colorXY);
}
if (patternName.equals("Spring Mist")){
pattern.springMistPattern(light, repeat, Long.valueOf(String.valueOf(flashRateValue.getText())), colorXY);
}                        }
                    }
                }
            }
        }
    }

    public void showThisColor(String colorName){
        PHHueSDK phHueSDK = PHHueSDK.getInstance();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        if (!colorName.equals("--")) {
            for (String defaultLight : hueController.getDefaultLights()) {
                for (PHLight light : allLights) {
                    if (defaultLight.equals(light.getName())) {
                        if (light.supportsColor()) {
                            ACEColors colors = ACEColors.getInstance();
                            PHLightState state = new PHLightState();
                            state.setOn(true);
                            bridge.updateLightState(light, state);
                            state.setX(Float.valueOf(String.valueOf(colors.getColorsList().get(colorName)[0])));
                            bridge.updateLightState(light, state);
                            state.setY(Float.valueOf(String.valueOf(colors.getColorsList().get(colorName)[1])));
                            bridge.updateLightState(light, state);
                            break;
                        }
                    }
                }
            }
        }
    }

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
