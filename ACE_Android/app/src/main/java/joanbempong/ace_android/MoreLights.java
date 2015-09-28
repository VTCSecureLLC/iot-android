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

public class MoreLights extends AppCompatActivity {

    TextView bodyText;
    Button yesBtn, noBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_lights);

        //connects the textview and buttons to the widgets created in xml
        bodyText = (TextView)findViewById(R.id.bodyText);
        yesBtn = (Button)findViewById(R.id.yesBtn);
        noBtn = (Button)findViewById(R.id.noBtn);

        //creates on click listeners
        yesBtn.setOnClickListener(yesBtnOnClickListener);
        noBtn.setOnClickListener(noBtnOnClickListener);


        //counts the total number of lights for the Hue system
        int lightCount = 0;
        int lightList = 0;
        while (lightList != HueController.Lights.size()) {
            lightCount++;
            lightList++;
        }

        //sets the text for the textview
        bodyText.setText(String.format("You have registered your hue bridge. Now, do you have more " +
                "lights to add to the system?" + " (You currently have %s lights)", lightCount));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_lights, menu);
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
        //navigate to the HueRegister page
        startActivity(new Intent(MoreLights.this, HueRegister.class));
    }

    OnClickListener yesBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            // navigate to the AddLights page
            //startActivity(new Intent(MoreLights.this, AddLights.class));
        }
    };

    OnClickListener noBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            // navigate to the ConfigureLights page
            startActivity(new Intent(MoreLights.this, ConfigureLights.class));
        }
    };
}
