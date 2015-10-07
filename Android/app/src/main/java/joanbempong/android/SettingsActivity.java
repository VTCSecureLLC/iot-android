package joanbempong.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    TextView contactClick, lightClick, deviceClick, notificationClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //connects the textviews to the widgets created in xml
        contactClick = (TextView)findViewById(R.id.contactClick);
        lightClick = (TextView)findViewById(R.id.lightClick);
        deviceClick = (TextView)findViewById(R.id.deviceClick);
        notificationClick = (TextView)findViewById(R.id.notificationClick);

        //creates on click listeners
        contactClick.setOnClickListener(contactClickOnClickListener);
        lightClick.setOnClickListener(lightClickOnClickListener);
        deviceClick.setOnClickListener(deviceClickOnClickListener);
        notificationClick.setOnClickListener(notificationClickOnClickListener);
    }

    View.OnClickListener contactClickOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //navigate to the MyContacts page
            startActivity(new Intent(SettingsActivity.this, MyContactsActivity.class));
        }
    };

    View.OnClickListener lightClickOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //navigate to the MyLights page
            startActivity(new Intent(SettingsActivity.this, MyLightsActivity.class));
        }
    };

    View.OnClickListener deviceClickOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //navigate to the MyDevices page
            //startActivity(new Intent(SettingsActivity.this, MyDevices.class));
        }
    };

    View.OnClickListener notificationClickOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //navigate to the SetHueDefaultValuesActivity page
            startActivity(new Intent(SettingsActivity.this, SetHueDefaultValuesActivity.class));
        }
    };

    //action to take when the back button is pressed
    @Override
    public void onBackPressed()
    {
        //navigate to the Home page
        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
            startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        }
        else if (id == R.id.action_lights) {
            //navigate to the Lights page
            startActivity(new Intent(SettingsActivity.this, LightsActivity.class));
        }
        else if (id == R.id.action_settings) {
            //navigate to the Settings page
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
