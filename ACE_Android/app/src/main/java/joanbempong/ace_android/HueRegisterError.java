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

public class HueRegisterError extends AppCompatActivity {

    TextView bodyText;
    Button tryAgainBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hue_register_error);

        //connects the textview and button to the widgets created in xml
        bodyText = (TextView)findViewById(R.id.bodyText);
        tryAgainBtn = (Button)findViewById(R.id.tryAgainBtn);

        //creates an on click listener
        tryAgainBtn.setOnClickListener(tryAgainBtnOnClickListener);

        //sets the text for the textview
        bodyText.setText("Please make sure you pressed the link button on the hue bridge.");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hue_register_error, menu);
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
        startActivity(new Intent(HueRegisterError.this, HueRegister.class));
    }

    OnClickListener tryAgainBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //keep trying until the link button has been pressed by the user
            if (HueController.postHue()) {
                //the Hue has been registered
                HueController.HUEregistered = true;

                //navigate to the MoreLights page
                startActivity(new Intent(HueRegisterError.this, MoreLights.class));
            }
        }
    };
}
