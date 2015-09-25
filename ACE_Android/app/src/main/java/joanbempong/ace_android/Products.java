package joanbempong.ace_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Products extends AppCompatActivity {

    TextView bodyText;
    Button hueBtn, winkBtn, linkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        //connects the textview and buttons to the widgets created in xml
        bodyText = (TextView)findViewById(R.id.bodyText);
        hueBtn = (Button)findViewById(R.id.hueBtn);
        winkBtn = (Button)findViewById(R.id.winkBtn);
        linkBtn = (Button)findViewById(R.id.linkBtn);

        //creates on click listeners
        hueBtn.setOnClickListener(hueBtnOnClickListener);
        winkBtn.setOnClickListener(winkBtnOnClickListener);
        linkBtn.setOnClickListener(linkBtnOnClickListener);

        //sets the text for the textview
        bodyText.setText("Which product do you have?");
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
        startActivity(new Intent(Products.this, Welcome.class));
    }

    OnClickListener hueBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            // navigate to the HueBridge class
            startActivity(new Intent(Products.this, HueRegister.class));
        }
    };

    OnClickListener winkBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //this option is not supported yet
            final AlertDialog.Builder alert = new AlertDialog.Builder(Products.this);
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

    OnClickListener linkBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //this option is not supported yet
            final AlertDialog.Builder alert = new AlertDialog.Builder(Products.this);
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
