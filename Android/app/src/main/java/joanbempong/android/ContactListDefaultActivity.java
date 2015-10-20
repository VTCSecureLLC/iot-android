package joanbempong.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Iterator;

public class ContactListDefaultActivity extends Activity {

    Button yesBtn, noBtn;
    HueController hueController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list_default);

        hueController = HueController.getInstance();

        //connects the textViews and buttons to the widgets created in xml
        yesBtn = (Button)findViewById(R.id.yesBtn);
        noBtn = (Button)findViewById(R.id.noBtn);

        //creates on click listeners
        yesBtn.setOnClickListener(yesBtnOnClickListener);
        noBtn.setOnClickListener(noBtnOnClickListener);
    }

    View.OnClickListener yesBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //assign the default values to all of the contacts' notification settings
            if (hueController.getContactList() != null){
                for (Iterator<ACEContact> iterate = hueController.getContactList().listIterator(); iterate.hasNext();){
                    ACEContact contact = iterate.next();
                    hueController.saveCurrentInformation(contact.getFirstName(), contact.getLastName());
                    hueController.editContact(contact.getFirstName(), contact.getLastName(), contact.getPhoneNumber(),
                            hueController.getDefaultFlashPattern(), hueController.getDefaultFlashRate(),
                            hueController.getDefaultColor(), true,
                            true);
                }
            }
            // navigate to the CompletedSetup page
            startActivity(new Intent(ContactListDefaultActivity.this, CompletedSetupActivity.class));
        }
    };

    View.OnClickListener noBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //leave the contact's notification settings empty and navigate to the CompletedSetup page
            startActivity(new Intent(ContactListDefaultActivity.this, CompletedSetupActivity.class));
        }
    };

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
}
