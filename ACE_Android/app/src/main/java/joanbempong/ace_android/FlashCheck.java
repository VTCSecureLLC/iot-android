package joanbempong.ace_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class FlashCheck extends AppCompatActivity {

    TextView lightName, bodyText, subBodyText;
    Button yesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_check);

        //connects the textviews and button to the widgets created in xml
        lightName = (TextView)findViewById(R.id.lightName);
        bodyText = (TextView)findViewById(R.id.bodyText);
        subBodyText = (TextView)findViewById(R.id.subBodyText);
        yesBtn = (Button)findViewById(R.id.yesBtn);

        //creates an on click listener
        yesBtn.setOnClickListener(yesBtnOnClickListener);

        //sets the text for the textviews
        lightName.setText(HueController.currentLightConfigure);
        bodyText.setText("Is this light flashing?");
        subBodyText.setText("If not, make sure it is turned on");

        //centers the text
        bodyText.setGravity(Gravity.CENTER);
        subBodyText.setGravity(Gravity.CENTER);

        HueController.testFlash = true;

        int lightList = 0;
        int lightNum;
        while (lightList != HueController.Lights.size()) {
            if (HueController.currentLightConfigure.equals(HueController.Lights.get(lightList)[3])) {
                lightNum = Integer.parseInt(HueController.Lights.get(lightList)[0]);
                HueController.putHueColorOn(lightNum);
                HueController.putTestHueFlash(lightNum);
                break;
            }
            lightList++;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flash_check, menu);
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
        //navigate to the Products page
        startActivity(new Intent(FlashCheck.this, ConfigureLights.class));
    }

    OnClickListener yesBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            HueController.testFlash = false;
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // navigate to the ColorCheck class
            startActivity(new Intent(FlashCheck.this, ColorCheck.class));
        }
    };
}
