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

public class CompletedSetup extends AppCompatActivity {

    TextView bodyText;
    Button takeMeHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_setup);

        //connects the textview and button to the widgets created in xml
        bodyText = (TextView)findViewById(R.id.bodyText);
        takeMeHomeBtn = (Button)findViewById(R.id.takeMeHomeBtn);

        //creates an on click listeners
        takeMeHomeBtn.setOnClickListener(takeMeHomeBtnOnClickListener);

        //sets the text for the textview
        bodyText.setText("Congratulations! You have set up your Hue system.");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_completed_setup, menu);
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
        //navigate to the ContactListDefault page
        startActivity(new Intent(CompletedSetup.this, ContactListDefault.class));
    }

    OnClickListener takeMeHomeBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //navigate to the ACEHome page
            startActivity(new Intent(CompletedSetup.this, Home.class));
        }
    };
}
