package joanbempong.ace_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MyLights extends AppCompatActivity {

    Button addBtn;
    ListView listLights;
    ArrayList<String> stringArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lights);

        //connects the button and listView to the widgets created in xml
        addBtn = (Button)findViewById(R.id.addBtn);
        listLights = (ListView)findViewById(R.id.listLights);

        //creates an on click listener
        addBtn.setOnClickListener(addBtnOnClickListener);

        //sets the text for the button
        addBtn.setText("+ new contact");

        //lists all the stored contacts
        stringArray = new ArrayList<>();

        //add lights to the listview
        stringArray = new ArrayList<>();
        int lightList = 0;
        while (lightList != HueController.Lights.size()) {
            //add the name of lights to the stringArray array
            System.out.println(HueController.Lights.get(lightList)[3]);
            stringArray.add(HueController.Lights.get(lightList)[3]);
            lightList++;
        }

        ListLightsSettings adapter = new ListLightsSettings(stringArray, this);
        listLights.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_lights, menu);
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
        //navigate to the Settings page
        startActivity(new Intent(MyLights.this, Settings.class));
    }

    OnClickListener addBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //navigate to the AddContact page
            //startActivity(new Intent(MyLights.this, AddLight.class));
        }
    };
}
