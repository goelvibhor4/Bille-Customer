package in.bille.app;

/**
 * Created by Vibhor Goel on 2/1/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import java.util.HashMap;


public class Offers extends AppCompatActivity {
    SessionManager session;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        session = new SessionManager(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Offers");

        HashMap<String, String> user = session.getUserDetails();


        String UserPhone = user.get(SessionManager.KEY_PHONE);
        String UserName = user.get(SessionManager.KEY_Name);
        String UserEmail= user.get(SessionManager.KEY_Email);

        Log.d("userphone no", UserPhone);

    }

    public void click(View v) {

        switch(v.getId()) {




        }
    }




    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();


        }
        return super.onOptionsItemSelected(item);


    }
}

