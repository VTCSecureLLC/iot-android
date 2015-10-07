package joanbempong.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;

import java.util.ArrayList;
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
    private Switch incomingSwitch, missedSwitch, notificationSwitch;
    private ScrollView scrollView;

    EditText firstName, lastName, phoneNumber;
    Button saveBtn;

    ArrayAdapter<CharSequence> adapter;

    List<String> empty = new ArrayList<>();

    private String incomingDefault, missedDefault, notificationYN;

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

        incomingCallFlashPatternList = (Spinner)findViewById(R.id.incomingCallFlashPatternList);
        incomingCallFlashRateList = (Spinner)findViewById(R.id.incomingCallFlashRateList);
        missedCallDurationList = (Spinner)findViewById(R.id.missedCallDurationList);

        incomingSwitch = (Switch)findViewById(R.id.incomingSwitch);
        missedSwitch = (Switch)findViewById(R.id.missedSwitch);
        notificationSwitch = (Switch)findViewById(R.id.notificationSwitch);

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
        incomingSwitch.setOnCheckedChangeListener(incomingSwitchOnCheckedChangeListener);
        missedSwitch.setOnCheckedChangeListener(missedSwitchOnCheckedChangeListener);
        notificationSwitch.setOnCheckedChangeListener(notificationSwitchOnCheckedChangeListener);

        scrollView = (ScrollView)findViewById(R.id.scrollView);

        incomingDefault = hueController.getOldContactIncomingCallUseDefault()[0];
        missedDefault = hueController.getOldContactMissedCallUseDefault()[0];
        notificationYN = hueController.getOldContactNotificationYN()[0];

        //load the contact's current information
        firstName.setText(hueController.getOldContactName()[0]);
        lastName.setText(hueController.getOldContactName()[1]);
        phoneNumber.setText(hueController.getOldContactPhoneNumber()[0]);
        if (incomingDefault.equals("yes")){
            incomingSwitch.setChecked(true);
        }
        if (missedDefault.equals("yes")){
            missedSwitch.setChecked(true);
        }
        if (notificationYN.equals("yes")){
            notificationSwitch.setText("ON");
            notificationSwitch.setChecked(true);
            scrollView.setVisibility(View.VISIBLE);
        }
        else{
            notificationSwitch.setText("OFF");
            notificationSwitch.setChecked(false);
            scrollView.setVisibility(View.GONE);
        }

        incomingAdapter = new IncomingCallLightChoiceAdapter(this, allLights, hueController, incomingDefault);
        incomingCallLightChoices = (ListView) findViewById(R.id.incomingCallListView);
        incomingCallLightChoices.setAdapter(incomingAdapter);

        missedAdapter = new MissedCallLightChoiceAdapter(this, allLights, hueController, missedDefault);
        missedCallLightChoices = (ListView) findViewById(R.id.missedCallListView);
        missedCallLightChoices.setAdapter(missedAdapter);
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
            if (notificationSwitch.isChecked()) {
                hueController.saveCurrentInformation(String.valueOf(firstName.getText()), String.valueOf(lastName.getText()));
                if ((incomingDefault.equals("no")) && (missedDefault.equals("no"))) {
                    if (hueController.getIncomingCallLights().size() == 0) {
                        Toast.makeText(EditContactActivity.this, "Please choose a light or more for your incoming calls.", Toast.LENGTH_SHORT).show();
                    } else if (incomingCallFlashPatternList.getSelectedItem().equals("--")) {
                        Toast.makeText(EditContactActivity.this, "Please choose a flash pattern for your incoming calls.", Toast.LENGTH_SHORT).show();
                    } else if (incomingCallFlashRateList.getSelectedItem().equals("--")) {
                        Toast.makeText(EditContactActivity.this, "Please choose a flash rate for your incoming calls.", Toast.LENGTH_SHORT).show();
                    } else if (hueController.getMissedCallLights().size() == 0) {
                        Toast.makeText(EditContactActivity.this, "Please choose a light or more for your missed calls.", Toast.LENGTH_SHORT).show();
                    } else if (missedCallDurationList.getSelectedItem().equals("--")) {
                        Toast.makeText(EditContactActivity.this, "Please choose a duration for your missed calls.", Toast.LENGTH_SHORT).show();
                    } else {
                        //look for the contact and replace with new information
                        hueController.editContact(String.valueOf(firstName.getText()),
                                String.valueOf(lastName.getText()), String.valueOf(phoneNumber.getText()),
                                hueController.getIncomingCallLights(),
                                String.valueOf(incomingCallFlashPatternList.getSelectedItem()),
                                String.valueOf(incomingCallFlashRateList.getSelectedItem()),
                                hueController.getMissedCallLights(),
                                String.valueOf(missedCallDurationList.getSelectedItem()), incomingDefault, missedDefault, "yes");

                        //contact has been saved -- show a toast
                        Context context = getApplicationContext();
                        CharSequence text = "The information for this contact has been updated.";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        // navigate to the MyContacts page
                        startActivity(new Intent(EditContactActivity.this, MyContactsActivity.class));
                    }
                } else if ((incomingDefault.equals("no")) && (missedDefault.equals("yes"))) {
                    if (hueController.getIncomingCallLights().size() == 0) {
                        Toast.makeText(EditContactActivity.this, "Please choose a light or more for your incoming calls.", Toast.LENGTH_SHORT).show();
                    } else if (incomingCallFlashPatternList.getSelectedItem().equals("--")) {
                        Toast.makeText(EditContactActivity.this, "Please choose a flash pattern for your incoming calls.", Toast.LENGTH_SHORT).show();
                    } else if (incomingCallFlashRateList.getSelectedItem().equals("--")) {
                        Toast.makeText(EditContactActivity.this, "Please choose a flash rate for your incoming calls.", Toast.LENGTH_SHORT).show();
                    } else {
                        //look for the contact and replace with new information
                        hueController.editContact(String.valueOf(firstName.getText()),
                                String.valueOf(lastName.getText()), String.valueOf(phoneNumber.getText()),
                                hueController.getIncomingCallLights(),
                                String.valueOf(incomingCallFlashPatternList.getSelectedItem()),
                                String.valueOf(incomingCallFlashRateList.getSelectedItem()),
                                hueController.getDefaultMissedLight(),
                                hueController.getDefaultMissedDuration(), incomingDefault, missedDefault, "yes");

                        //contact has been saved -- show a toast
                        Context context = getApplicationContext();
                        CharSequence text = "The information for this contact has been updated.";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        // navigate to the MyContacts page
                        startActivity(new Intent(EditContactActivity.this, MyContactsActivity.class));
                    }
                } else if ((incomingDefault.equals("yes")) && (missedDefault.equals("no"))) {
                    if (hueController.getMissedCallLights().size() == 0) {
                        Toast.makeText(EditContactActivity.this, "Please choose a light or more for your missed calls.", Toast.LENGTH_SHORT).show();
                    } else if (missedCallDurationList.getSelectedItem().equals("--")) {
                        Toast.makeText(EditContactActivity.this, "Please choose a duration for your missed calls.", Toast.LENGTH_SHORT).show();
                    } else {
                        //look for the contact and replace with new information
                        hueController.editContact(String.valueOf(firstName.getText()),
                                String.valueOf(lastName.getText()), String.valueOf(phoneNumber.getText()),
                                hueController.getDefaultIncomingLight(), hueController.getDefaultIncomingFlashPattern(),
                                hueController.getDefaultIncomingFlashRate(),
                                hueController.getMissedCallLights(),
                                String.valueOf(missedCallDurationList.getSelectedItem()), incomingDefault, missedDefault, "yes");

                        //contact has been saved -- show a toast
                        Context context = getApplicationContext();
                        CharSequence text = "The information for this contact has been updated.";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        // navigate to the MyContacts page
                        startActivity(new Intent(EditContactActivity.this, MyContactsActivity.class));
                    }
                } else {
                    hueController.editContact(String.valueOf(firstName.getText()),
                            String.valueOf(lastName.getText()), String.valueOf(phoneNumber.getText()),
                            hueController.getDefaultIncomingLight(), hueController.getDefaultIncomingFlashPattern(),
                            hueController.getDefaultIncomingFlashRate(), hueController.getDefaultMissedLight(),
                            hueController.getDefaultMissedDuration(), incomingDefault, missedDefault, "yes");
                    //contact has been saved -- show a toast
                    Context context = getApplicationContext();
                    CharSequence text = "The information for this contact has been updated.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    // navigate to the MyContacts page
                    startActivity(new Intent(EditContactActivity.this, MyContactsActivity.class));
                }
            } else {
                hueController.editContact(String.valueOf(firstName.getText()),
                        String.valueOf(lastName.getText()), String.valueOf(phoneNumber.getText()),
                        empty, "0", "0", empty, "0", "no", "no", "no");

                //contact has been saved -- show a toast
                Context context = getApplicationContext();
                CharSequence text = "The information for this contact has been saved.";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                // navigate to the MyContacts page
                startActivity(new Intent(EditContactActivity.this, MyContactsActivity.class));
            }
        }
    };

    CompoundButton.OnCheckedChangeListener incomingSwitchOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                if (!hueController.getDefaultValues()){
                    //creates a new alert dialog
                    final AlertDialog.Builder alert = new AlertDialog.Builder(EditContactActivity.this);
                    alert.setTitle("There is no default values yet");
                    alert.setMessage("Do you want to set them now?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            buttonView.toggle();
                            // navigate to the SetHueDefaultValues page
                            startActivity(new Intent(EditContactActivity.this, SetHueDefaultValuesActivity.class));
                        }
                    });

                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            buttonView.toggle();
                        }
                    });
                    //create the alert dialog and show it
                    alert.create();
                    alert.show();
                }
                else {
                    incomingDefault = "yes";

                    hueController.setCleanIncomingCallLights();
                    incomingAdapter = new IncomingCallLightChoiceAdapter(EditContactActivity.this, allLights, hueController, incomingDefault);
                    incomingCallLightChoices = (ListView) findViewById(R.id.incomingCallListView);
                    incomingCallLightChoices.setAdapter(incomingAdapter);

                    //set the current selected item
                    adapter = ArrayAdapter.createFromResource(EditContactActivity.this, R.array.flash_pattern_list, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    int selected = adapter.getPosition(hueController.getDefaultIncomingFlashPattern());
                    incomingCallFlashPatternList.setSelection(selected);
                    incomingCallFlashPatternList.setEnabled(false);


                    adapter = ArrayAdapter.createFromResource(EditContactActivity.this, R.array.flash_rate_list, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    selected = adapter.getPosition(hueController.getDefaultIncomingFlashRate());
                    incomingCallFlashRateList.setSelection(selected);
                    incomingCallFlashRateList.setEnabled(false);
                }
            }
            else{
                incomingDefault = "no";

                hueController.setCleanIncomingCallLights();
                incomingAdapter = new IncomingCallLightChoiceAdapter(EditContactActivity.this, allLights, hueController, incomingDefault);
                incomingCallLightChoices = (ListView) findViewById(R.id.incomingCallListView);
                incomingCallLightChoices.setAdapter(incomingAdapter);

                incomingCallFlashPatternList.setEnabled(true);
                addItemsToFlashPattern();
                incomingCallFlashRateList.setEnabled(true);
                addItemsToFlashRate();
            }
        }
    };

    CompoundButton.OnCheckedChangeListener missedSwitchOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                if (!hueController.getDefaultValues()){
                    //creates a new alert dialog
                    final AlertDialog.Builder alert = new AlertDialog.Builder(EditContactActivity.this);
                    alert.setTitle("There is no default values yet");
                    alert.setMessage("Do you want to set them now?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            buttonView.toggle();
                            // navigate to the SetHueDefaultValues page
                            startActivity(new Intent(EditContactActivity.this, SetHueDefaultValuesActivity.class));
                        }
                    });

                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            buttonView.toggle();
                        }
                    });
                    //create the alert dialog and show it
                    alert.create();
                    alert.show();
                }
                else {
                    missedDefault = "yes";

                    hueController.setCleanMissedCallLights();
                    missedAdapter = new MissedCallLightChoiceAdapter(EditContactActivity.this, allLights, hueController, missedDefault);
                    missedCallLightChoices = (ListView) findViewById(R.id.missedCallListView);
                    missedCallLightChoices.setAdapter(missedAdapter);

                    //set the current selected item
                    adapter = ArrayAdapter.createFromResource(EditContactActivity.this, R.array.duration_list, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    int selected = adapter.getPosition(hueController.getDefaultMissedDuration());
                    missedCallDurationList.setSelection(selected);
                    missedCallDurationList.setEnabled(false);
                }
            }
            else{
                missedDefault = "no";

                hueController.setCleanMissedCallLights();
                missedAdapter = new MissedCallLightChoiceAdapter(EditContactActivity.this, allLights, hueController, missedDefault);
                missedCallLightChoices = (ListView) findViewById(R.id.missedCallListView);
                missedCallLightChoices.setAdapter(missedAdapter);

                missedCallDurationList.setEnabled(true);
                addItemsToDuration();
            }
        }
    };

    CompoundButton.OnCheckedChangeListener notificationSwitchOnCheckedChangeListener= new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                notificationSwitch.setText("ON");
                scrollView.setVisibility(View.VISIBLE);
            }
            else{
                notificationSwitch.setText("OFF");
                scrollView.setVisibility(View.GONE);
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
