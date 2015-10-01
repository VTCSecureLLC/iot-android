package joanbempong.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MyContactsActivity extends AppCompatActivity {

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
        stringArray = new ArrayList<>();

        if (hueController.getContactList().size() != 0){
            for (List<String[]> contact: hueController.getContactList()){
                stringArray.add(contact.get(0)[0] + " " + contact.get(0)[1]);
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
