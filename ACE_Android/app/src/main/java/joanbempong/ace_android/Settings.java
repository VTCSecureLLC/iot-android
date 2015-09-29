package joanbempong.ace_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    TextView bodyText1, bodyText2, bodyText3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //connects the textviews to the widgets created in xml
        bodyText1 = (TextView)findViewById(R.id.bodyText1);
        bodyText2 = (TextView)findViewById(R.id.bodyText2);
        bodyText3 = (TextView)findViewById(R.id.bodyText3);

        //creates on click listeners
        bodyText1.setOnClickListener(bodyText1OnClickListener);
        bodyText2.setOnClickListener(bodyText2OnClickListener);
        bodyText3.setOnClickListener(bodyText3OnClickListener);

        //sets the text for the textviews
        bodyText1.setText("My Contacts");
        bodyText2.setText("My Lights");
        bodyText3.setText("My Devices");
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
            startActivity(new Intent(Settings.this, Home.class));
        }
        else if (id == R.id.action_lights) {
            //navigate to the Lights page
            startActivity(new Intent(Settings.this, Lights.class));
        }
        else if (id == R.id.action_settings) {
            //navigate to the Settings page
            startActivity(new Intent(Settings.this, Settings.class));
        }

        return super.onOptionsItemSelected(item);
    }

    //action to take when the back button is pressed
    @Override
    public void onBackPressed()
    {
        //navigate to the Home page
        startActivity(new Intent(Settings.this, Home.class));
    }

    OnClickListener bodyText1OnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //navigate to the MyContacts page
            startActivity(new Intent(Settings.this, MyContacts.class));
        }
    };

    OnClickListener bodyText2OnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //navigate to the MyLights page
            startActivity(new Intent(Settings.this, MyLights.class));
        }
    };

    OnClickListener bodyText3OnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //navigate to the MyDevices page
            //startActivity(new Intent(Settings.this, MyDevices.class));
        }
    };

}
