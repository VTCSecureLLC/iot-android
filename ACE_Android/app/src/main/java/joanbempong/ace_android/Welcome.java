package joanbempong.ace_android;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Welcome extends AppCompatActivity {

    TextView welcomeTitle, bodyText;
    Button nextBtn;
    int backButtonCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //connects the textviews and button to the widgets created in xml
        welcomeTitle = (TextView)findViewById(R.id.welcomeTitle);
        bodyText = (TextView)findViewById(R.id.bodyText);
        nextBtn = (Button)findViewById(R.id.nextBtn);

        //creates an on click listener
        nextBtn.setOnClickListener(nextBtnOnClickListener);

        //sets the text for the textviews
        welcomeTitle.setText("Welcome!");
        bodyText.setText("Let's configure your notification system.");

        //used to resolve the NetworkOnMainThreadException Exceptions
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
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
        if(backButtonCount >= 1)
        {
            //exit the application
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            backButtonCount = 0;
        }
        else
        {
            //have the user press the back button again to exit the application
            Toast.makeText(this, "Press the back button again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    OnClickListener nextBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            // navigate to the Products page
            startActivity(new Intent(Welcome.this, Products.class));
        }
    };
}
