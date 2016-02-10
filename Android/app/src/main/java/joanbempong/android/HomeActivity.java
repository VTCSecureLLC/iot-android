package joanbempong.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.linphone.R;

import java.util.ArrayList;

public class HomeActivity extends Activity {

    SetupController setupController;
    ListView listContacts;
    HueController hueController;

    ArrayList<String> stringArray;
    int backButtonCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupController = SetupController.getInstance();
        if (!setupController.getSetupCompleted()){
            //navigate to the Welcome (initial set up) page
            startActivity(new Intent(HomeActivity.this, WelcomeActivity.class));
        }

        else {
            hueController = HueController.getInstance();

            //connects the listview to the widgets created in xml
            listContacts = (ListView) findViewById(R.id.listContacts);


            //list all the stored contacts
            stringArray = new ArrayList<String>();

            if (hueController.getContactList().size() != 0) {
                for (ACEContact contact : hueController.getContactList()) {
                    stringArray.add(contact.getFirstName() + " " + contact.getLastName());
                }

                SimulateCallAdapter adapter = new SimulateCallAdapter(this, stringArray, hueController);
                listContacts.setAdapter(adapter);
            }
        }
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
            startActivity(new Intent(HomeActivity.this, HomeActivity.class));
        }
        else if (id == R.id.action_lights) {
            //navigate to the Lights page
            startActivity(new Intent(HomeActivity.this, LightsActivity.class));
        }
        else if (id == R.id.action_settings) {
            //navigate to the Settings page
            startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
