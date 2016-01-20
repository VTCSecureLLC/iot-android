package joanbempong.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MyContactsActivity extends Activity {

    Button addBtn;
    ListView listContacts;
    ArrayList<String> stringArray;
    HueController hueController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contacts);

        hueController = HueController.getInstance();

        //connects the button and listView to the widgets created in xml
        addBtn = (Button)findViewById(R.id.addBtn);
        listContacts = (ListView)findViewById(R.id.listContacts);

        //creates an on click listener
        addBtn.setOnClickListener(addBtnOnClickListener);

        //sets the text for the button
        addBtn.setText("+ new contact");

        //lists all the stored contacts
        stringArray = new ArrayList<String>();

        if (hueController.getContactList().size() != 0){
            for (ACEContact contact: hueController.getContactList()){
                stringArray.add(contact.getFirstName() + " " + contact.getLastName());
            }

            ContactListSettingsAdapter adapter = new ContactListSettingsAdapter(this, stringArray, hueController);
            listContacts.setAdapter(adapter);
        }
    }

    View.OnClickListener addBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //navigate to the AddContact page
            startActivity(new Intent(MyContactsActivity.this, AddContactActivity.class));
        }
    };

    //action to take when the back button is pressed
    @Override
    public void onBackPressed()
    {
        //navigate to the Settings page
        startActivity(new Intent(MyContactsActivity.this, SettingsActivity.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_contacts, menu);
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
