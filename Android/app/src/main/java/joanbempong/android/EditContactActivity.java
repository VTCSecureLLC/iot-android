package joanbempong.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;

import java.util.List;

public class EditContactActivity extends AppCompatActivity {

    private PHHueSDK phHueSDK;
    private HueController hueController;
    public static final String TAG = "ACE Notification";

    private List<PHLight> allLights;

    private IncomingCallLightChoiceAdapter incomingAdapter;
    private MissedCallLightChoiceAdapter missedAdapter;

    private ListView incomingCallLightChoices, missedCallLightChoices;
    private Spinner incomingCallFlashPatternList, incomingCallFlashRateList, missedCallDurationList;

    EditText firstName, lastName, phoneNumber;
    Button saveBtn;

    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        phHueSDK = PHHueSDK.getInstance();
        hueController = HueController.getInstance();

        //connects the editTexts, textViews, spinners, and button to the widgets created in xml
        firstName = (EditText)findViewById(R.id.firstName);
        lastName = (EditText)findViewById(R.id.lastName);
        phoneNumber = (EditText)findViewById(R.id.phoneNumber);

        PHBridge bridge = phHueSDK.getSelectedBridge();
        allLights = bridge.getResourceCache().getAllLights();

        incomingAdapter = new IncomingCallLightChoiceAdapter(this, allLights, hueController);
        incomingCallLightChoices = (ListView) findViewById(R.id.incomingCallListView);
        incomingCallLightChoices.setAdapter(incomingAdapter);

        missedAdapter = new MissedCallLightChoiceAdapter(this, allLights, hueController);
        missedCallLightChoices = (ListView) findViewById(R.id.missedCallListView);
        missedCallLightChoices.setAdapter(missedAdapter);

        incomingCallFlashPatternList = (Spinner)findViewById(R.id.incomingCallFlashPatternList);
        incomingCallFlashRateList = (Spinner)findViewById(R.id.incomingCallFlashRateList);
        missedCallDurationList = (Spinner)findViewById(R.id.missedCallDurationList);

        //adds items to the spinners
        addItemsToFlashPattern();
        addItemsToFlashRate();
        addItemsToDuration();

        hueController.setCleanIncomingCallLights();
        hueController.setCleanMissedCallLights();

        //connects the button to the widgets created in xml
        saveBtn = (Button)findViewById(R.id.saveBtn);

        //creates an on click listener
        saveBtn.setOnClickListener(saveBtnOnClickListener);

        //load the contact's current information
        firstName.setText(hueController.getOldContactName()[0]);
        lastName.setText(hueController.getOldContactName()[1]);
        phoneNumber.setText(hueController.getOldContactPhoneNumber()[0]);
    }

    public void addItemsToFlashPattern(){
        adapter = ArrayAdapter.createFromResource(this, R.array.flash_pattern_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        incomingCallFlashPatternList.setAdapter(adapter);

        //set the current selected item
        int selected = adapter.getPosition(hueController.getOldContactIncomingCallFlash()[0]);
        incomingCallFlashPatternList.setSelection(selected);
    }

    public void addItemsToFlashRate(){
        adapter = ArrayAdapter.createFromResource(this, R.array.flash_rate_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        incomingCallFlashRateList.setAdapter(adapter);

        //set the current selected item
        int selected = adapter.getPosition(hueController.getOldContactIncomingCallFlash()[1]);
        incomingCallFlashRateList.setSelection(selected);
    }

    public void addItemsToDuration(){
        adapter = ArrayAdapter.createFromResource(this, R.array.duration_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        missedCallDurationList.setAdapter(adapter);

        //set the current selected item
        int selected = adapter.getPosition(hueController.getOldContactMissedDuration()[0]);
        missedCallDurationList.setSelection(selected);
    }

    View.OnClickListener saveBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if (hueController.getIncomingCallDefaultLights().size() == 0){
                Toast.makeText(EditContactActivity.this, "Please choose a light or more for your default incoming calls.", Toast.LENGTH_SHORT).show();
            }else if (incomingCallFlashPatternList.getSelectedItem().equals("--")){
                Toast.makeText(EditContactActivity.this, "Please choose a flash pattern for your default incoming calls.", Toast.LENGTH_SHORT).show();
            }else if (incomingCallFlashRateList.getSelectedItem().equals("--")){
                Toast.makeText(EditContactActivity.this, "Please choose a flash rate for your default incoming calls.", Toast.LENGTH_SHORT).show();
            }else if (hueController.getMissedCallDefaultLights().size() == 0){
                Toast.makeText(EditContactActivity.this, "Please choose a light or more for your default missed calls.", Toast.LENGTH_SHORT).show();
            }else if (missedCallDurationList.getSelectedItem().equals("--")){
                Toast.makeText(EditContactActivity.this, "Please choose a duration for your default missed calls.", Toast.LENGTH_SHORT).show();
            }
            else {
                //look for the contact and replace with new information
                hueController.editContact(String.valueOf(firstName.getText()),
                        String.valueOf(lastName.getText()), String.valueOf(phoneNumber.getText()),
                        hueController.getIncomingCallLights(),
                        String.valueOf(incomingCallFlashPatternList.getSelectedItem()),
                        String.valueOf(incomingCallFlashRateList.getSelectedItem()),
                        hueController.getMissedCallLights(),
                        String.valueOf(missedCallDurationList.getSelectedItem()));

                //contact has been saved -- show a toast
                Context context = getApplicationContext();
                CharSequence text = "The information for this contact has been updated.";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                // navigate to the MyContacts page
                startActivity(new Intent(EditContactActivity.this, MyContactsActivity.class));
            }
        }
    };

    //action to take when the back button is pressed
    @Override
    public void onBackPressed()
    {
        //navigate to the MyContacts page
        startActivity(new Intent(EditContactActivity.this, MyContactsActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_contact, menu);
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
