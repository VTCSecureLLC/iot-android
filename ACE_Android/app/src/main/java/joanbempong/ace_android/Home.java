package joanbempong.ace_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    TextView bodyText;
    ListView listContacts;

    ArrayList<String> stringArray;
    int backButtonCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //connects the textview and listview to the widgets created in xml
        bodyText = (TextView)findViewById(R.id.bodyText);
        listContacts = (ListView)findViewById(R.id.listContacts);

        //sets the text for the textview
        bodyText.setText("Simulate an incoming call.");

        //list all the stored contacts
        stringArray = new ArrayList<>();

        if (HueController.Contacts.size() != 0){
            for (List<String[]> contact: HueController.Contacts){
                stringArray.add(contact.get(0)[0] + " " + contact.get(0)[1]);
            }

            SimulateButtons adapter = new SimulateButtons(stringArray, this);
            listContacts.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
            startActivity(new Intent(Home.this, Home.class));
        }
        else if (id == R.id.action_lights) {
            //navigate to the Lights page
            startActivity(new Intent(Home.this, Lights.class));
        }
        else if (id == R.id.action_settings) {
            //navigate to the Settings page
            //startActivity(new Intent(Home.this, Settings.class));
        }

        return super.onOptionsItemSelected(item);
    }

    //action to take when the back button is pressed
    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            //exit the application
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            backButtonCount = 0;
        }
        else
        {
            //have the user press the back button again to exit the application
            Toast.makeText(this, "Press the back button again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
}
