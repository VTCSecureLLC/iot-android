package joanbempong.android;

import org.linphone.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class CompletedSetupActivity extends Activity {

    Button takeMeHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_setup);

        //connects the button to the widgets created in xml
        takeMeHomeBtn = (Button)findViewById(R.id.takeMeHomeBtn);

        //creates an on click listeners
        takeMeHomeBtn.setOnClickListener(takeMeHomeBtnOnClickListener);
    }

    View.OnClickListener takeMeHomeBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            SetupController setupController = SetupController.getInstance();
            setupController.setSetupCompleted(true);
            //navigate to the HomeActivity page
            startActivity(new Intent(CompletedSetupActivity.this, HomeActivity.class));
        }
    };

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
}
