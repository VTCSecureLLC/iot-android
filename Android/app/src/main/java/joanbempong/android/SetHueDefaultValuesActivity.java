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

import java.util.ArrayList;
import java.util.List;

public class SetHueDefaultValuesActivity extends AppCompatActivity {

    private PHHueSDK phHueSDK;
    private HueController hueController;
    private MyChoices myChoices;

    private List<PHLight> allLights;

    private DefaultLightChoiceAdapter defaultAdapter;

    private ListView lightChoices;
    private Spinner durationList, flashPatternList, flashRateList, colorList;

    private Button saveBtn;
    private ArrayAdapter<CharSequence> adapter;

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
        flashRateList = (Spinner)findViewById(R.id.flashRateList);
        colorList = (Spinner)findViewById(R.id.colorList);

        //adds items to the spinners
        addItemsToDurationList();
        addItemsToFlashPatternList();
        addItemsToFlashRateList();
        addItemsToColorList();

        //connects the button to the widgets created in xml
        saveBtn = (Button)findViewById(R.id.saveBtn);

        //creates an on click listener
        saveBtn.setOnClickListener(saveBtnOnClickListener);
    }

    public void addItemsToDurationList(){
        ArrayList<String> choices = myChoices.getDurationList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choices);
        durationList.setAdapter(adapter);

        //set the current selected item
        int selected = adapter.getPosition(hueController.getDefaultDuration());
        durationList.setSelection(selected);
    }

    public void addItemsToFlashPatternList(){
        ArrayList<String> choices = myChoices.getFlashPatternList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choices);
        flashPatternList.setAdapter(adapter);

        //set the current selected item
        int selected = adapter.getPosition(hueController.getDefaultFlashPattern());
        flashPatternList.setSelection(selected);
    }

    public void addItemsToFlashRateList(){
        ArrayList<String> choices = myChoices.getFlashRateList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choices);
        flashRateList.setAdapter(adapter);

        //set the current selected item
        int selected = adapter.getPosition(hueController.getDefaultFlashRate());
        flashRateList.setSelection(selected);
    }

    public void addItemsToColorList(){
        ArrayList<String> choices = myChoices.getColorList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choices);
        colorList.setAdapter(adapter);

        //set the current selected item
        int selected = adapter.getPosition(hueController.getDefaultColor());
        colorList.setSelection(selected);
    }

    View.OnClickListener saveBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if (hueController.getDefaultLights().size() == 0){
                Toast.makeText(SetHueDefaultValuesActivity.this, "Please choose a light or more for your default incoming/missed calls.", Toast.LENGTH_SHORT).show();
            }else if (durationList.getSelectedItem().equals("--")){
                Toast.makeText(SetHueDefaultValuesActivity.this, "Please choose a duration for your default missed calls.", Toast.LENGTH_SHORT).show();
            }else if (flashPatternList.getSelectedItem().equals("--")){
                Toast.makeText(SetHueDefaultValuesActivity.this, "Please choose a flash pattern for your default incoming calls.", Toast.LENGTH_SHORT).show();
            }else if (flashRateList.getSelectedItem().equals("--")){
                Toast.makeText(SetHueDefaultValuesActivity.this, "Please choose a flash rate for your default incoming calls.", Toast.LENGTH_SHORT).show();
            }else if (colorList.getSelectedItem().equals("--")){
                Toast.makeText(SetHueDefaultValuesActivity.this, "Please choose a color for your default incoming/missed calls.", Toast.LENGTH_SHORT).show();
            }
            else{
                hueController.saveDefaultValues(String.valueOf(durationList.getSelectedItem()),
                        String.valueOf(flashPatternList.getSelectedItem()), String.valueOf(flashRateList.getSelectedItem()),
                        String.valueOf(colorList.getSelectedItem()));
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
