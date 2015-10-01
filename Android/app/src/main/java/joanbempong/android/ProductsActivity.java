package joanbempong.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ProductsActivity extends AppCompatActivity {

    Button hueBtn, winkBtn, linkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        //connects the buttons to the widgets created in xml
        hueBtn = (Button)findViewById(R.id.hueBtn);
        winkBtn = (Button)findViewById(R.id.winkBtn);
        linkBtn = (Button)findViewById(R.id.linkBtn);

        //creates on click listeners
        hueBtn.setOnClickListener(hueBtnOnClickListener);
        winkBtn.setOnClickListener(winkBtnOnClickListener);
        linkBtn.setOnClickListener(linkBtnOnClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_products, menu);
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
        //navigate to the Welcome page
        startActivity(new Intent(ProductsActivity.this, WelcomeActivity.class));
    }

    View.OnClickListener hueBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            // navigate to the HueRegister page
            startActivity(new Intent(ProductsActivity.this, HueBridgeSearchActivity.class));
        }
    };

    View.OnClickListener winkBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //this option is not supported yet
            final AlertDialog.Builder alert = new AlertDialog.Builder(ProductsActivity.this);
            alert.setTitle("Wink Hub");
            alert.setMessage("Not supported...yet");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.create();
            alert.show();
        }
    };

    View.OnClickListener linkBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //this option is not supported yet
            final AlertDialog.Builder alert = new AlertDialog.Builder(ProductsActivity.this);
            alert.setTitle("Link Hub");
            alert.setMessage("Not supported...yet");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert.create();
            alert.show();
        }
    };
}
