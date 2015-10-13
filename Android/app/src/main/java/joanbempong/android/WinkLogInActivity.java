package joanbempong.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class WinkLogInActivity extends AppCompatActivity {

    private EditText username, password;
    private Button logIn, signUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wink_log_in);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        logIn = (Button)findViewById(R.id.logIn);
        signUp = (Button)findViewById(R.id.signUp);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if authenticated

                //if so, navigate to the HomeActivity page
                startActivity(new Intent(WinkLogInActivity.this, HomeActivity.class));
            }
        });

    }

    public StringBuffer buildHttpAuthenAddress() {
        StringBuffer sb = new StringBuffer();
        sb.append("/winkapi.quirky.com/oauth2/token");
        return sb;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wink_log_in, menu);
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
