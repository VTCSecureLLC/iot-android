package joanbempong.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ContactListDefaultActivity extends AppCompatActivity {

    Button yesBtn, noBtn;
    HueController hueController;
    List<String> empty = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list_default);

        hueController = HueController.getInstance();

        //connects the textviews and buttons to the widgets created in xml
        yesBtn = (Button)findViewById(R.id.yesBtn);
        noBtn = (Button)findViewById(R.id.noBtn);

        //creates on click listeners
        yesBtn.setOnClickListener(yesBtnOnClickListener);
        noBtn.setOnClickListener(noBtnOnClickListener);

        //"retrieve" the contact list by creating new one (for testing purpose)
        if (hueController.getContactList().size() == 0) {
            hueController.createNewContact("Shareef", "Ali", "1111111111", empty, "0", "0", empty, "0", "no", "no", "no");
            hueController.createNewContact("Gary", "Behm", "1111111111", empty, "0", "0", empty, "0", "no", "no", "no");
            hueController.createNewContact("Joan", "Bempong", "1111111111", empty, "0", "0", empty, "0", "no", "no", "no");
            hueController.createNewContact("Brian", "Trager", "1111111111", empty, "0", "0", empty, "0", "no", "no", "no");
        }
    }


    View.OnClickListener yesBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //assign the default values to all of the contacts' notification settings
            if (hueController.getContactList() != null){
                for (Iterator<List<String[]>> iterate = hueController.getContactList().listIterator(); iterate.hasNext();){
                    List<String[]> contact = iterate.next();
                    hueController.saveCurrentInformation(contact.get(0)[0], contact.get(0)[1]);
                    hueController.editContact(contact.get(0)[0], contact.get(0)[1], contact.get(1)[0],
                            hueController.getDefaultIncomingLight(), hueController.getDefaultIncomingFlashPattern(),
                            hueController.getDefaultIncomingFlashRate(), hueController.getDefaultMissedLight(),
                            hueController.getDefaultMissedDuration(), "yes", "yes", "yes");
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
