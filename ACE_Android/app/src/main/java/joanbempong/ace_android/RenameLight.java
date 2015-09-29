package joanbempong.ace_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RenameLight extends AppCompatActivity {

    TextView lightName, bodyText;
    EditText renameLight;
    Button nextBtn;

    int lightNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename_light);

        //connects the textviews and button to the widgets created in xml
        lightName = (TextView)findViewById(R.id.lightName);
        bodyText = (TextView)findViewById(R.id.bodyText);
        renameLight = (EditText)findViewById(R.id.renameLight);
        nextBtn = (Button)findViewById(R.id.nextBtn);

        //creates an on click listener
        nextBtn.setOnClickListener(nextBtnOnClickListener);

        //sets the text for the textviews
        lightName.setText(HueController.currentLightConfigure);
        bodyText.setText("To skip this step, do not modify the name below and hit next.");
        renameLight.setText(HueController.currentLightConfigure);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rename_light, menu);
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
        startActivity(new Intent(RenameLight.this, ColorResult.class));
    }

    OnClickListener nextBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //save the current light name
            String lightName = renameLight.getText().toString();

            System.out.println(lightName);
            System.out.println(HueController.currentLightConfigure);

            if (!lightName.equals(HueController.currentLightConfigure)){
                HueController.saveCurrentLightInformation(HueController.currentLightConfigure);

                int lightVal = 0;
                while (lightVal != HueController.Lights.size()) {
                    System.out.println(lightName);
                    System.out.println(HueController.Lights.get(lightVal)[3]);
                    System.out.println(HueController.Lights.get(lightVal)[0]);
                    if (HueController.currentLightConfigure.equals(HueController.Lights.get(lightVal)[3])) {
                        lightNum = Integer.parseInt(HueController.Lights.get(lightVal)[0]);
                        break;
                    }

                    lightVal++;
                }

                HueController.renameLight(lightName, lightNum);

            }

            if (HueController.allLightsConfigured()){
                // navigate to the HueDefaultValues page
                startActivity(new Intent(RenameLight.this, HueDefaultValues.class));
            }
            else {
                // navigate to the ConfigureLights page
                startActivity(new Intent(RenameLight.this, ConfigureLights.class));
            }
        }
    };
}
