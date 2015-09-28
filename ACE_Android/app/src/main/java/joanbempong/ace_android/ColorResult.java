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

public class ColorResult extends AppCompatActivity {

    TextView lightName, bodyText;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_result);

        //connects the textviews and button to the widgets created in xml
        lightName = (TextView)findViewById(R.id.lightName);
        bodyText = (TextView)findViewById(R.id.bodyText);
        nextBtn = (Button)findViewById(R.id.nextBtn);

        //creates an on click listener
        nextBtn.setOnClickListener(nextBtnOnClickListener);

        //sets the text for the textviews
        lightName.setText(HueController.currentLightConfigure);

        if (HueController.testColor){

            bodyText.setText("This light does support color.");
        }
        else{

            bodyText.setText("This light does not support color.");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_color_result, menu);
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
        //navigate to the ColorCheck page
        startActivity(new Intent(ColorResult.this, ColorCheck.class));
    }

    OnClickListener nextBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if (HueController.allLightsConfigured()){
                // navigate to the HueDefaultValues page
                startActivity(new Intent(ColorResult.this, HueDefaultValues.class));
            }
            else {
                // navigate to the ConfigureLights page
                startActivity(new Intent(ColorResult.this, ConfigureLights.class));
            }
        }
    };
}
