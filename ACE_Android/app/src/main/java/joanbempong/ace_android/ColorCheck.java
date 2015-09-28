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

public class ColorCheck extends AppCompatActivity {

    TextView lightName, bodyText;
    Button yesBtn, noBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_check);

        //connects the textviews and buttons to the widgets created in xml
        lightName = (TextView)findViewById(R.id.lightName);
        bodyText = (TextView)findViewById(R.id.bodyText);
        yesBtn = (Button)findViewById(R.id.yesBtn);
        noBtn = (Button)findViewById(R.id.noBtn);

        //creates on click listeners
        yesBtn.setOnClickListener(yesBtnOnClickListener);
        noBtn.setOnClickListener(noBtnOnClickListener);

        //sets the text for the textviews
        lightName.setText(HueController.currentLightConfigure);
        bodyText.setText("Did the light turn red?");

        //test and see if the light supports color
        int lightList = 0;
        int lightNum;
        while (lightList != HueController.Lights.size()) {
            if (HueController.currentLightConfigure.equals(HueController.Lights.get(lightList)[3])) {
                lightNum = Integer.parseInt(HueController.Lights.get(lightList)[0]);
                System.out.println("testing color");
                HueController.putHueOff(lightNum);
                HueController.putHueColor(lightNum, "red", 255);
                break;
            }
            lightList++;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_color_check, menu);
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
        //navigate to the FlashCheck page
        startActivity(new Intent(ColorCheck.this, FlashCheck.class));
    }

    OnClickListener yesBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            HueController.testColor = true;

            int lightList = 0;
            int lightNum;
            while (lightList != HueController.Lights.size()) {
                if (HueController.currentLightConfigure.equals(HueController.Lights.get(lightList)[3])) {
                    lightNum = Integer.parseInt(HueController.Lights.get(lightList)[0]);
                    HueController.putHueOff(lightNum);
                    break;
                }
                lightList++;
            }

            //this light has been configured and supports color
            for (String [] light : HueController.Lights){
                if (HueController.currentLightConfigure.equals(light[3])) {
                    light[10] = "color";
                    System.out.println("support color");
                    if (light[9].equals("false")) {
                        light[9] = "true";
                        System.out.println("light has been configured");
                        break;
                    }
                }
            }

            // navigate to the ColorResult class
            startActivity(new Intent(ColorCheck.this, ColorResult.class));
        }
    };

    OnClickListener noBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            HueController.testColor = false;

            int lightList = 0;
            int lightNum;
            while (lightList != HueController.Lights.size()) {
                if (HueController.currentLightConfigure.equals(HueController.Lights.get(lightList)[3])) {
                    lightNum = Integer.parseInt(HueController.Lights.get(lightList)[0]);
                    HueController.putHueOff(lightNum);
                    break;
                }
                lightList++;
            }

            //this light has been configured and does not support color
            for (String [] light : HueController.Lights){
                if (HueController.currentLightConfigure.equals(light[3])) {
                    light[10] = "nocolor";
                    System.out.println("doesnt support color");
                    if (light[9].equals("false")) {
                        light[9] = "true";
                        System.out.println("light has been configured");
                        break;
                    }
                }
            }

            // navigate to the ColorResult page
            startActivity(new Intent(ColorCheck.this, ColorResult.class));
        }
    };
}
