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

public class AddContactActivity extends AppCompatActivity {

    private PHHueSDK phHueSDK;
    private HueController hueController;

    private List<PHLight> allLights;

    private IncomingCallLightChoiceAdapter incomingAdapter;
    private MissedCallLightChoiceAdapter missedAdapter;

    private ListView incomingCallLightChoices, missedCallLightChoices;
    private Spinner incomingCallFlashPatternList, incomingCallFlashRateList, missedCallDurationList;
    private Switch incomingSwitch, missedSwitch, notificationSwitch;
    private ScrollView scrollView;

    private String incomingDefault = "no";
    private String missedDefault = "no";

    EditText firstName, lastName, phoneNumber;
    Button saveBtn;

    ArrayAdapter<CharSequence> adapter;

    List<String> empty = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        phHueSDK = PHHueSDK.getInstance();
        hueController = HueController.getInstance();

        //connects the editTexts, textViews, spinners, and button to the widgets created in xml
        firstName = (EditText)findViewById(R.id.firstName);
        lastName = (EditText)findViewById(R.id.lastName);
        phoneNumber = (EditText)findViewById(R.id.phoneNumber);

        PHBridge bridge = phHueSDK.getSelectedBridge();
        allLights = bridge.getResourceCache().getAllLights();

        incomingDefault = "no";
        missedDefault = "no";

        incomingAdapter = new IncomingCallLightChoiceAdapter(this, allLights, hueController, incomingDefault);
        incomingCallLightChoices = (ListView) findViewById(R.id.incomingCallListView);
        incomingCallLightChoices.setAdapter(incomingAdapter);

        missedAdapter = new MissedCallLightChoiceAdapter(this, allLights, hueController, missedDefault);
        missedCallLightChoices = (ListView) findViewById(R.id.missedCallListView);
        missedCallLightChoices.setAdapter(missedAdapter);

        incomingCallFlashPatternList = (Spinner)findViewById(R.id.incomingCallFlashPatternList);
        incomingCallFlashRateList = (Spinner)findViewById(R.id.incomingCallFlashRateList);
        missedCallDurationList = (Spinner)findViewById(R.id.missedCallDurationList);

        incomingSwitch = (Switch)findViewById(R.id.incomingSwitch);
        missedSwitch = (Switch)findViewById(R.id.missedSwitch);
        notificationSwitch = (Switch)findViewById(R.id.notificationSwitch);

        notificationSwitch.setChecked(true);
        notificationSwitch.setText("ON");

        scrollView = (ScrollView)findViewById(R.id.scrollView);

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

    View.OnClickListener saveBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if (notificationSwitch.isChecked()) {
                if ((incomingDefault.equals("no")) && (missedDefault.equals("no"))) {
                    if (hueController.getIncomingCallLights().size() == 0) {
                        Toast.makeText(AddContactActivity.this, "Please choose a light or more for your incoming calls.", Toast.LENGTH_SHORT).show();
                    } else if (incomingCallFlashPatternList.getSelectedItem().equals("--")) {
                        Toast.makeText(AddContactActivity.this, "Please choose a flash pattern for your incoming calls.", Toast.LENGTH_SHORT).show();
                    } else if (incomingCallFlashRateList.getSelectedItem().equals("--")) {
                        Toast.makeText(AddContactActivity.this, "Please choose a flash rate for your incoming calls.", Toast.LENGTH_SHORT).show();
                    } else if (hueController.getMissedCallLights().size() == 0) {
                        Toast.makeText(AddContactActivity.this, "Please choose a light or more for your missed calls.", Toast.LENGTH_SHORT).show();
                    } else if (missedCallDurationList.getSelectedItem().equals("--")) {
                        Toast.makeText(AddContactActivity.this, "Please choose a duration for your missed calls.", Toast.LENGTH_SHORT).show();
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
                        CharSequence text = "The information for this contact has been saved.";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        // navigate to the MyContacts page
                        startActivity(new Intent(AddContactActivity.this, MyContactsActivity.class));
                    }
                } else if ((incomingDefault.equals("no")) && (missedDefault.equals("yes"))) {
                    if (hueController.getIncomingCallLights().size() == 0) {
                        Toast.makeText(AddContactActivity.this, "Please choose a light or more for your incoming calls.", Toast.LENGTH_SHORT).show();
                    } else if (incomingCallFlashPatternList.getSelectedItem().equals("--")) {
                        Toast.makeText(AddContactActivity.this, "Please choose a flash pattern for your incoming calls.", Toast.LENGTH_SHORT).show();
                    } else if (incomingCallFlashRateList.getSelectedItem().equals("--")) {
                        Toast.makeText(AddContactActivity.this, "Please choose a flash rate for your incoming calls.", Toast.LENGTH_SHORT).show();
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
                        CharSequence text = "The information for this contact has been saved.";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        // navigate to the MyContacts page
                        startActivity(new Intent(AddContactActivity.this, MyContactsActivity.class));
                    }
                } else if ((incomingDefault.equals("yes")) && (missedDefault.equals("no"))) {
                    if (hueController.getMissedCallLights().size() == 0) {
                        Toast.makeText(AddContactActivity.this, "Please choose a light or more for your missed calls.", Toast.LENGTH_SHORT).show();
                    } else if (missedCallDurationList.getSelectedItem().equals("--")) {
                        Toast.makeText(AddContactActivity.this, "Please choose a duration for your missed calls.", Toast.LENGTH_SHORT).show();
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
                        CharSequence text = "The information for this contact has been saved.";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        // navigate to the MyContacts page
                        startActivity(new Intent(AddContactActivity.this, MyContactsActivity.class));
                    }
                } else {
                    hueController.createNewContact(String.valueOf(firstName.getText()),
                            String.valueOf(lastName.getText()), String.valueOf(phoneNumber.getText()),
                            hueController.getDefaultIncomingLight(), hueController.getDefaultIncomingFlashPattern(),
                            hueController.getDefaultIncomingFlashRate(), hueController.getDefaultMissedLight(),
                            hueController.getDefaultMissedDuration(), incomingDefault, missedDefault, "yes");
                    //contact has been saved -- show a toast
                    Context context = getApplicationContext();
                    CharSequence text = "The information for this contact has been saved.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    // navigate to the MyContacts page
                    startActivity(new Intent(AddContactActivity.this, MyContactsActivity.class));
                }
            }
            else{
                hueController.createNewContact(String.valueOf(firstName.getText()),
                        String.valueOf(lastName.getText()), String.valueOf(phoneNumber.getText()),
                        empty, "0", "0", empty, "0", "no", "no", "no");

                //contact has been saved -- show a toast
                Context context = getApplicationContext();
                CharSequence text = "The information for this contact has been saved.";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                // navigate to the MyContacts page
                startActivity(new Intent(AddContactActivity.this, MyContactsActivity.class));
            }
        }
    };

    CompoundButton.OnCheckedChangeListener incomingSwitchOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                if (!hueController.getDefaultValues()){
                    //creates a new alert dialog
                    final AlertDialog.Builder alert = new AlertDialog.Builder(AddContactActivity.this);
                    alert.setTitle("There is no default values yet");
                    alert.setMessage("Do you want to set them now?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            buttonView.toggle();
                            // navigate to the SetHueDefaultValues page
                            startActivity(new Intent(AddContactActivity.this, SetHueDefaultValuesActivity.class));
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
                    incomingAdapter = new IncomingCallLightChoiceAdapter(AddContactActivity.this, allLights, hueController, incomingDefault);
                    incomingCallLightChoices = (ListView) findViewById(R.id.incomingCallListView);
                    incomingCallLightChoices.setAdapter(incomingAdapter);


                    //set the current selected item
                    adapter = ArrayAdapter.createFromResource(AddContactActivity.this, R.array.flash_pattern_list, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    int selected = adapter.getPosition(hueController.getDefaultIncomingFlashPattern());
                    incomingCallFlashPatternList.setSelection(selected);
                    incomingCallFlashPatternList.setEnabled(false);
                    System.out.println(hueController.getDefaultIncomingFlashPattern());
                    System.out.println(selected);


                    adapter = ArrayAdapter.createFromResource(AddContactActivity.this, R.array.flash_rate_list, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    selected = adapter.getPosition(hueController.getDefaultIncomingFlashRate());
                    incomingCallFlashRateList.setSelection(selected);
                    incomingCallFlashRateList.setEnabled(false);
                    System.out.println(hueController.getDefaultIncomingFlashRate());
                    System.out.println(selected);
                }
            }
            else{
                incomingDefault = "no";

                hueController.setCleanIncomingCallLights();
                incomingAdapter = new IncomingCallLightChoiceAdapter(AddContactActivity.this, allLights, hueController, incomingDefault);
                incomingCallLightChoices = (ListView) findViewById(R.id.incomingCallListView);
                incomingCallLightChoices.setAdapter(incomingAdapter);

                incomingCallFlashPatternList.setEnabled(true);
                incomingCallFlashRateList.setEnabled(true);
            }
        }
    };

    CompoundButton.OnCheckedChangeListener missedSwitchOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                if (!hueController.getDefaultValues()){
                    //creates a new alert dialog
                    final AlertDialog.Builder alert = new AlertDialog.Builder(AddContactActivity.this);
                    alert.setTitle("There is no default values yet");
                    alert.setMessage("Do you want to set them now?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            buttonView.toggle();
                            // navigate to the SetHueDefaultValues page
                            startActivity(new Intent(AddContactActivity.this, SetHueDefaultValuesActivity.class));
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
                    missedAdapter = new MissedCallLightChoiceAdapter(AddContactActivity.this, allLights, hueController, missedDefault);
                    missedCallLightChoices = (ListView) findViewById(R.id.missedCallListView);
                    missedCallLightChoices.setAdapter(missedAdapter);

                    //set the current selected item
                    adapter = ArrayAdapter.createFromResource(AddContactActivity.this, R.array.duration_list, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    int selected = adapter.getPosition(hueController.getDefaultMissedDuration());
                    missedCallDurationList.setSelection(selected);
                    missedCallDurationList.setEnabled(false);
                    System.out.println(hueController.getDefaultMissedDuration());
                    System.out.println(selected);
                }
            }
            else{
                missedDefault = "no";

                hueController.setCleanMissedCallLights();
                missedAdapter = new MissedCallLightChoiceAdapter(AddContactActivity.this, allLights, hueController, missedDefault);
                missedCallLightChoices = (ListView) findViewById(R.id.missedCallListView);
                missedCallLightChoices.setAdapter(missedAdapter);

                missedCallLightChoices.setEnabled(true);
                missedCallDurationList.setEnabled(true);
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
        startActivity(new Intent(AddContactActivity.this, MyContactsActivity.class));
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
