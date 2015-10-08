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
    private MyChoices myChoices;

    private List<PHLight> allLights;

    private Spinner flashPatternList, flashRateList, colorList;
    private Switch defaultSwitch, notificationSwitch;
    private ScrollView scrollView;
    private EditText firstName, lastName, phoneNumber;
    private Button saveBtn;

    ArrayAdapter<String> adapterFP, adapterFR, adapterC;
    private Boolean useDefault = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        phHueSDK = PHHueSDK.getInstance();
        hueController = HueController.getInstance();
        myChoices = MyChoices.getInstance();

        //connects the editTexts, textViews, spinners, and button to the widgets created in xml
        firstName = (EditText)findViewById(R.id.firstName);
        lastName = (EditText)findViewById(R.id.lastName);
        phoneNumber = (EditText)findViewById(R.id.phoneNumber);

        PHBridge bridge = phHueSDK.getSelectedBridge();
        allLights = bridge.getResourceCache().getAllLights();

        flashPatternList = (Spinner)findViewById(R.id.flashPatternList);
        flashRateList = (Spinner)findViewById(R.id.flashRateList);
        colorList = (Spinner)findViewById(R.id.colorList);

        defaultSwitch = (Switch)findViewById(R.id.defaultSwitch);
        notificationSwitch = (Switch)findViewById(R.id.notificationSwitch);

        notificationSwitch.setChecked(true);
        notificationSwitch.setText("ON");

        scrollView = (ScrollView)findViewById(R.id.scrollView);

        //adds items to the spinners
        addItemsToFlashPatternList();
        addItemsToFlashRateList();
        addItemsToColorList();

        //connects the button to the widgets created in xml
        saveBtn = (Button)findViewById(R.id.saveBtn);

        //creates an on click listener
        saveBtn.setOnClickListener(saveBtnOnClickListener);
        defaultSwitch.setOnCheckedChangeListener(defaultSwitchOnCheckedChangeListener);
        notificationSwitch.setOnCheckedChangeListener(notificationSwitchOnCheckedChangeListener);
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

    View.OnClickListener saveBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if (notificationSwitch.isChecked()) {
                if (!useDefault) {
                    if (flashPatternList.getSelectedItem().equals("--")) {
                        Toast.makeText(AddContactActivity.this, "Please choose a flash pattern.", Toast.LENGTH_SHORT).show();
                    } else if (flashRateList.getSelectedItem().equals("--")) {
                        Toast.makeText(AddContactActivity.this, "Please choose a flash rate.", Toast.LENGTH_SHORT).show();
                    }else if (colorList.getSelectedItem().equals("--")) {
                        Toast.makeText(AddContactActivity.this, "Please choose a color.", Toast.LENGTH_SHORT).show();
                    } else {
                        //look for the contact and replace with new information
                        hueController.createNewContact(String.valueOf(firstName.getText()),
                                String.valueOf(lastName.getText()), String.valueOf(phoneNumber.getText()),
                                String.valueOf(flashPatternList.getSelectedItem()),
                                String.valueOf(flashRateList.getSelectedItem()),
                                String.valueOf(colorList.getSelectedItem()), true, false);
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
                            hueController.getDefaultFlashPattern(),
                            hueController.getDefaultFlashRate(),
                            hueController.getDefaultColor(), true, true);
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
                        "0", "0", "0", false, false);
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

    CompoundButton.OnCheckedChangeListener defaultSwitchOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                useDefault = true;

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
                    //set the current selected item
                    int selected = adapterFP.getPosition(hueController.getDefaultFlashPattern());
                    flashPatternList.setSelection(selected);
                    flashPatternList.setEnabled(false);

                    selected = adapterFR.getPosition(hueController.getDefaultFlashRate());
                    flashRateList.setSelection(selected);
                    flashRateList.setEnabled(false);

                    selected = adapterC.getPosition(hueController.getDefaultColor());
                    colorList.setSelection(selected);
                    colorList.setEnabled(false);
                }
            }
            else{
                useDefault = false;

                flashPatternList.setEnabled(true);
                flashRateList.setEnabled(true);
                colorList.setEnabled(true);
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
