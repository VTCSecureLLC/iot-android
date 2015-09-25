package joanbempong.ace_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HueRegister extends AppCompatActivity {

    TextView bodyText, subBodyText;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hue_register);

        //connects the textviews and button to the widgets created in xml
        bodyText = (TextView)findViewById(R.id.bodyText);
        subBodyText = (TextView)findViewById(R.id.subBodyText);
        nextBtn = (Button)findViewById(R.id.nextBtn);

        //creates an on click listener
        nextBtn.setOnClickListener(nextBtnOnClickListener);

        //sets the text for the textviews
        bodyText.setText("Let's register your Hue Bridge");
        subBodyText.setText("press the link button on the bridge and hit next");

        //centers the text
        subBodyText.setGravity(Gravity.CENTER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hue_register, menu);
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
        startActivity(new Intent(HueRegister.this, Products.class));
    }

    OnClickListener nextBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if (!HueController.HUEregistered) {
                //creates a new ACEController object when connected to the Hue Bridge
                HueController.controller = new HueController(new HueConnection() {
                    @Override
                    public void Connected(boolean result) {
                        //if result is true, navigate to the ConnectedActivity class
                        if (result) {
                            if (HueController.postHue()) {
                                HueController.HUEregistered = true;

                                // navigate to the AddLights class
                                startActivity(new Intent(HueRegister.this, MoreLights.class));

                            } else {
                                startActivity(new Intent(HueRegister.this, HueRegisterError.class));
                            }
                        }

                        //if result is false, pop up a window letting the user know there is something wrong
                        //currently does not work
                        else {
                            System.out.println("couldnt connect");
                        }
                    }
                });
                //runs the doInBackground function in HueController
                HueController.controller.execute();
            }
            else{
                // navigate to the AddLights class
                startActivity(new Intent(HueRegister.this, MoreLights.class));
            }
        }
    };
}
