package in.bille.app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.HashMap;


public class AboutUs extends AppCompatActivity implements View.OnClickListener {
    SessionManager session;
    Intent intent;
    ImageButton fb,tweet,insta;
    Integer flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        session = new SessionManager(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("About us");

        HashMap<String, String> user = session.getUserDetails();

        fb = (ImageButton)findViewById(R.id.imageButtonFacebook);
        tweet = (ImageButton)findViewById(R.id.imageButtonTwitter);
        insta = (ImageButton)findViewById(R.id.imageButtonInstagram);


        fb.setOnClickListener(this);
        tweet.setOnClickListener(this);
        insta.setOnClickListener(this);

        String UserPhone = user.get(SessionManager.KEY_PHONE);
        String UserName = user.get(SessionManager.KEY_Name);
        String UserEmail= user.get(SessionManager.KEY_Email);

        Log.d("userphone no",UserPhone);

    }

    public void click(View v) {

        switch(v.getId()) {
            case R.id.termsandcondtions: // R.id.textView1
                flag=4;
                intent = new Intent(this,TermsAndConditions.class);
                intent.putExtra("webview",flag.toString());
                startActivity(intent);

                break;

            case R.id.contactus:
                Intent intent = new Intent(Intent.ACTION_SEND);


                Intent email_intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "contact@bille.in", null));

                startActivity(Intent.createChooser(email_intent, "Send Query"));
                break;




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

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.imageButtonFacebook)
        {
            flag=1;
            intent = new Intent(this,TermsAndConditions.class);
            intent.putExtra("webview",flag.toString());
            Log.d("flag",""+flag);
            startActivity(intent);
        }
        else if(v.getId()==R.id.imageButtonTwitter)
        {
            flag = 2;
            intent = new Intent(this,TermsAndConditions.class);
            intent.putExtra("webview",flag.toString());
            Log.d("flag", "" + flag);
            startActivity(intent);
        }
        else if(v.getId()==R.id.imageButtonInstagram)
        {
            flag = 3;
            intent = new Intent(this,TermsAndConditions.class);
            intent.putExtra("webview",flag.toString());
            startActivity(intent);
        }
    }
}


