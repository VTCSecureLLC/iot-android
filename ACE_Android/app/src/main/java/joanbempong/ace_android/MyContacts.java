package joanbempong.ace_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MyContacts extends AppCompatActivity {

    Button addBtn;
    ListView listContacts;
    ArrayList<String> stringArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contacts);

        //connects the button and listView to the widgets created in xml
        addBtn = (Button)findViewById(R.id.addBtn);
        listContacts = (ListView)findViewById(R.id.listContacts);

        //creates an on click listener
        addBtn.setOnClickListener(addBtnOnClickListener);

        //sets the text for the button
        addBtn.setText("+ new contact");

        //lists all the stored contacts
        stringArray = new ArrayList<>();

        if (HueController.Contacts != null){
            for (List<String[]> contact: HueController.Contacts){
                stringArray.add(contact.get(0)[0] + " " + contact.get(0)[1]);
            }

            ListContacts adapter = new ListContacts(stringArray, this);
            listContacts.setAdapter(adapter);
        }
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
        if (id == R.id.action_home) {
            //navigate to the Home page
            startActivity(new Intent(MyContacts.this, Home.class));
        }
        else if (id == R.id.action_lights) {
            //navigate to the Lights page
            startActivity(new Intent(MyContacts.this, Lights.class));
        }
        else if (id == R.id.action_settings) {
            //navigate to the Settings page
            startActivity(new Intent(MyContacts.this, Settings.class));
        }

        return super.onOptionsItemSelected(item);
    }

    //action to take when the back button is pressed
    @Override
    public void onBackPressed()
    {
        //navigate to the Settings page
        startActivity(new Intent(MyContacts.this, Settings.class));
    }

    OnClickListener addBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //navigate to the AddContact page
            startActivity(new Intent(MyContacts.this, AddContact.class));
        }
    };
}
