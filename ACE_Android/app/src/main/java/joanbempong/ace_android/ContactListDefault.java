package joanbempong.ace_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

public class ContactListDefault extends AppCompatActivity {

    TextView bodyText, subBodyText;
    Button yesBtn, noBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list_default);

        //connects the textviews and buttons to the widgets created in xml
        bodyText = (TextView)findViewById(R.id.bodyText);
        subBodyText = (TextView)findViewById(R.id.subBodyText);
        yesBtn = (Button)findViewById(R.id.yesBtn);
        noBtn = (Button)findViewById(R.id.noBtn);

        //creates on click listeners
        yesBtn.setOnClickListener(yesBtnOnClickListener);
        noBtn.setOnClickListener(noBtnOnClickListener);

        //sets the text for the textviews
        bodyText.setText("Your current contact list has been retrieved. Do you want those people to be assigned the default values?");
        subBodyText.setText("If you choose no, you will not be notified of incoming and/or missed calls by these people. These settings can be modified later.");


        //"retrieve" the contact list by creating new one (for testing purpose)
        if (HueController.Contacts.size() == 0) {
            HueController.newContact("Shareef", "Ali", "1111111111", "", "", "", "", "", "");
            HueController.newContact("Gary", "Behm", "1111111111","", "", "", "", "", "");
            HueController.newContact("Joan", "Bempong", "1111111111", "", "", "", "", "", "");
            HueController.newContact("Brian", "Trager", "1111111111", "", "", "", "", "", "");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_list_default, menu);
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
        //navigate to the HueDefaultValues page
        startActivity(new Intent(ContactListDefault.this, HueDefaultValues.class));
    }

    OnClickListener yesBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //assign the default values to all of the contacts' notification settings
            if (HueController.Contacts != null){
                for (Iterator<List<String[]>> iterate = HueController.Contacts.listIterator(); iterate.hasNext();){
                    List<String[]> contact = iterate.next();
                    HueController.saveCurrentInformation(contact.get(0)[0], contact.get(0)[1]);
                    HueController.editContact(contact.get(0)[0], contact.get(0)[1], contact.get(1)[0],
                            HueController.defaultIncomingLight, HueController.defaultIncomingFlashPattern,
                            HueController.defaultIncomingFlashRate, HueController.defaultMissedLight,
                            HueController.defaultMissedFlashPattern, HueController.defaultMissedFlashRate);
                }
            }
            // navigate to the CompletedSetup page
            startActivity(new Intent(ContactListDefault.this, CompletedSetup.class));
        }
    };

    OnClickListener noBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //leave the contact's notification settings empty and navigate to the CompletedSetup page
            startActivity(new Intent(ContactListDefault.this, CompletedSetup.class));
        }
    };
}
