package joanbempong.ace_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EditContact extends AppCompatActivity {

    EditText firstName, lastName, phoneNumber;
    TextView bodyText;
    TextView incomingCallText, incomingCallLight, incomingCallFlashPattern, incomingCallFlashRate;
    TextView missedCallText, missedCallLight, missedCallFlashPattern, missedCallFlashRate;
    Spinner incomingCallLightList, incomingCallFlashPatternList, incomingCallFlashRateList;
    Spinner missedCallLightList, missedCallFlashPatternList, missedCallFlashRateList;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        //connects the editTexts, textViews, spinners, and button to the widgets created in xml
        firstName = (EditText)findViewById(R.id.firstName);
        lastName = (EditText)findViewById(R.id.lastName);
        phoneNumber = (EditText)findViewById(R.id.phoneNumber);

        bodyText = (TextView)findViewById(R.id.bodyText);

        incomingCallText = (TextView)findViewById(R.id.incomingCallText);
        incomingCallLight = (TextView)findViewById(R.id.incomingCallLight);
        incomingCallFlashPattern = (TextView)findViewById(R.id.incomingCallFlashPattern);
        incomingCallFlashRate = (TextView)findViewById(R.id.incomingCallFlashRate);

        missedCallText = (TextView)findViewById(R.id.missedCallText);
        missedCallLight = (TextView)findViewById(R.id.missedCallLight);
        missedCallFlashPattern = (TextView)findViewById(R.id.missedCallFlashPattern);
        missedCallFlashRate = (TextView)findViewById(R.id.missedCallFlashRate);

        incomingCallLightList = (Spinner)findViewById(R.id.incomingCallLightList);
        incomingCallFlashPatternList = (Spinner)findViewById(R.id.incomingCallFlashPatternList);
        incomingCallFlashRateList = (Spinner)findViewById(R.id.incomingCallFlashRateList);

        missedCallLightList = (Spinner)findViewById(R.id.missedCallLightList);
        missedCallFlashPatternList = (Spinner)findViewById(R.id.missedCallFlashPatternList);
        missedCallFlashRateList = (Spinner)findViewById(R.id.missedCallFlashRateList);

        saveBtn = (Button)findViewById(R.id.saveBtn);

        //sets the text for the textviews
        bodyText.setText("Notifications");
        incomingCallText.setText("Incoming Call");
        incomingCallLight.setText("Light");
        incomingCallFlashPattern.setText("Flash Pattern");
        incomingCallFlashRate.setText("Flash Rate");
        missedCallText.setText("Missed Call");
        missedCallLight.setText("Light");
        missedCallFlashPattern.setText("Flash Pattern");
        missedCallFlashRate.setText("Flash Rate");

        //adds items to the spinners
        addItemsToLightList();
        addItemsToFlashPattern("incoming");
        addItemsToFlashRate("incoming");
        addItemsToFlashPattern("missed");
        addItemsToFlashRate("missed");

        //creates on click listeners
        incomingCallLightList.setOnItemSelectedListener(incomingCallLightListOnItemSelectedListener);
        missedCallLightList.setOnItemSelectedListener(missedCallLightListOnItemSelectedListener);
        saveBtn.setOnClickListener(saveBtnOnClickListener);

        //load the contact's current information
        firstName.setText(HueController.oldFirstName);
        lastName.setText(HueController.oldLastName);
        phoneNumber.setText(HueController.oldPhoneNumber);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_contact, menu);
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

    //action to take when the back button is pressed
    @Override
    public void onBackPressed()
    {
        //navigate to the MyContacts page
        startActivity(new Intent(EditContact.this, MyContacts.class));
    }

    public void addItemsToLightList(){
        List<String> myChoices = new ArrayList<>();
        myChoices.add("--");

        if (!HueController.HUEregistered){
            myChoices.add("(You do not have a registered hub)");
        }
        else{
            int lightList = 0;
            while (lightList != HueController.Lights.size()){
                //add the name of lights to the myChoices array
                myChoices.add(HueController.Lights.get(lightList)[3]);
                lightList++;
            }
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, myChoices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //add the array to the spinner
        incomingCallLightList.setAdapter(adapter);
        missedCallLightList.setAdapter(adapter);

        //set the current selected item
        int selected = adapter.getPosition(HueController.oldIncomingCallLight);
        incomingCallLightList.setSelection(selected);

        selected = adapter.getPosition(HueController.oldMissedCallLight);
        missedCallLightList.setSelection(selected);
    }

    public void addItemsToFlashPatternSimple(String call){
        List<String> myChoices = new ArrayList<>();
        myChoices.add("--");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, myChoices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //add the array to the spinner
        if (call.equals("incoming")) {
            incomingCallFlashPatternList.setAdapter(adapter);
        }
        else if (call.equals("missed")) {
            missedCallFlashPatternList.setAdapter(adapter);
        }
    }

    public void addItemsToFlashPattern(String call){
        List<String> myChoices = new ArrayList<>();
        myChoices.add("--");
        myChoices.add("1");
        myChoices.add("2");
        myChoices.add("3");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, myChoices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //add the array to the spinner
        if (call.equals("incoming")) {
            incomingCallFlashPatternList.setAdapter(adapter);

            //set the current selected item
            int selected = adapter.getPosition(HueController.oldIncomingCallFlashPattern);
            incomingCallFlashPatternList.setSelection(selected);
        }
        else if (call.equals("missed")) {
            missedCallFlashPatternList.setAdapter(adapter);

            //set the current selected item
            int selected = adapter.getPosition(HueController.oldMissedCallFlashPattern);
            missedCallFlashPatternList.setSelection(selected);
        }
    }

    public void addItemsToFlashRateSimple(String call){
        List<String> myChoices = new ArrayList<>();
        myChoices.add("--");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, myChoices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //add the array to the spinner
        if (call.equals("incoming")) {
            incomingCallFlashRateList.setAdapter(adapter);
        }
        else if (call.equals("missed")) {
            missedCallFlashRateList.setAdapter(adapter);
        }
    }

    public void addItemsToFlashRate(String call){
        List<String> myChoices = new ArrayList<>();
        myChoices.add("--");
        myChoices.add("500");
        myChoices.add("1000");
        myChoices.add("1500");
        myChoices.add("2000");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, myChoices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //add the array to the spinner
        if (call.equals("incoming")) {
            incomingCallFlashRateList.setAdapter(adapter);

            //set the current selected item
            int selected = adapter.getPosition(HueController.oldIncomingCallFlashRate);
            incomingCallFlashRateList.setSelection(selected);
        }
        else if (call.equals("missed")) {
            missedCallFlashRateList.setAdapter(adapter);

            //set the current selected item
            int selected = adapter.getPosition(HueController.oldMissedCallFlashRate);
            missedCallFlashRateList.setSelection(selected);
        }
    }

    AdapterView.OnItemSelectedListener incomingCallLightListOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(!parent.getItemAtPosition(position).equals("--")){
                addItemsToFlashPattern("incoming");
                addItemsToFlashRate("incoming");
            }
            else{
                addItemsToFlashPatternSimple("incoming");
                addItemsToFlashRateSimple("incoming");
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener missedCallLightListOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(!parent.getItemAtPosition(position).equals("--")){
                addItemsToFlashPattern("missed");
                addItemsToFlashRate("missed");
            }
            else{
                addItemsToFlashPatternSimple("missed");
                addItemsToFlashRateSimple("missed");
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    OnClickListener saveBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //look for the contact and replace with new information
            HueController.editContact(String.valueOf(firstName.getText()),
                    String.valueOf(lastName.getText()), String.valueOf(phoneNumber.getText()),
                    String.valueOf(incomingCallLightList.getSelectedItem()),
                    String.valueOf(incomingCallFlashPatternList.getSelectedItem()),
                    String.valueOf(incomingCallFlashRateList.getSelectedItem()),
                    String.valueOf(missedCallLightList.getSelectedItem()),
                    String.valueOf(missedCallFlashPatternList.getSelectedItem()),
                    String.valueOf(missedCallFlashRateList.getSelectedItem()));

            //contact has been saved -- show a toast
            Context context = getApplicationContext();
            CharSequence text = "The information for this contact has been updated.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            // navigate to the MyContacts page
            startActivity(new Intent(EditContact.this, MyContacts.class));
        }
    };
}
