package in.bille.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class PhoneForFacebook extends AppCompatActivity implements View.OnClickListener {
    String url,name,email, mobile,Userid;
    private boolean clicked = false;
    SessionManager session;
    String regid="";
    String password="";
    ProgressDialog mProgressDialog;

    EditText Mobile;
    Button Submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_for_facebook);
        name = getIntent().getStringExtra("name");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Mobile No.");

        email = getIntent().getStringExtra("email");
        Userid = getIntent().getStringExtra("userid");
        Mobile = (EditText)findViewById(R.id.input_phone);
        Submit = (Button)findViewById(R.id.btn_signup);
        Log.d("name for facebook", name);
        Log.d("email for facebook", email);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        regid = user.get(SessionManager.KEY_REGEDIT);
        Log.d("regid", "" + regid);
        Submit.setOnClickListener(this);
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        PhoneForFacebook.this.finish();
    }


    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:

                super.onBackPressed(); // this can go before or after your stuff below
                PhoneForFacebook.this.finish();







                // do your stuff when the back button is pressed
                /*Intent intent = new Intent(getApplicationContext(), CustomerLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
                // super.onBackPressed(); calls finish(); for you
                // clear your SharedPreferences
              //  getSharedPreferences("preferenceName",0).edit().clear().commit();

        }
        return super.onOptionsItemSelected(item);


    }

    @Override
    public void onClick(View v) {

        boolean flag = true;

        mobile = Mobile.getText().toString();

         if (mobile.equals("")||mobile.length()<10) {
            flag = false;
            Toast.makeText(getApplicationContext(),
                    "Enter the correct mobile number.", Toast.LENGTH_SHORT).show();
            Mobile.setFocusable(true);
        }

        if(flag)
        {
            String url;
          //  Toast.makeText(getApplicationContext(),"working",Toast.LENGTH_SHORT).show();
            url = Config.serverurl+"phone_fb.php?phone="+mobile+"&u_id="+Userid+"&token="+SplashMain.tokenvalue+"&device_id="+SplashMain.regid;

                url = url.replaceAll(" ", "%20");
Log.d("phone activty",url);
            new ReadJSONFeedTask().execute(url);

        }


    }

    public String readJSONFeed(String URL) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }
        } catch (Exception e) {

        }
        return stringBuilder.toString();
    }
    public class ReadJSONFeedTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(PhoneForFacebook.this,R.style.AppTheme_Dark_Dialog);
            // Set progressdialog title
            mProgressDialog.setMessage("Verifying No.");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            // Show progressdialog
            mProgressDialog.show();

        }

        @Override
        protected String doInBackground(String... urls)
        {
            return readJSONFeed(urls[0]);
        }
        protected void onPostExecute(String result)
        {   try {
            if ((mProgressDialog != null) &&  mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            mProgressDialog = null;
        }
            try
            {
                JSONObject jsonObject= new JSONObject(result);
                String checkerror = jsonObject.optString("error");

                if (checkerror.equals("true"))
                {
                    Log.d(" INCORRECT","INCORRECT");
                    Toast.makeText(getApplicationContext(),"Phone No already exists",Toast.LENGTH_SHORT).show();
                }


                else if(checkerror.equals("false"))
                {  JSONObject jsonObject1 = jsonObject.optJSONObject("user");
                    Log.d("test", "test");


                    String checkemail = jsonObject1.optString("email");
                    String checkname = jsonObject1.optString("name");
                    String checkphone = jsonObject1.optString("phone");

                    Log.d("phoneno is here",checkphone);
                    Log.d("ERROR",checkerror);

                    Log.d("test", "login");
                    Intent i = new Intent(getApplicationContext(), OtpVerification.class);
                    i.putExtra("useremail", checkemail);
                    i.putExtra("username", checkname);
                    i.putExtra("userphone",mobile);
                    i.putExtra("userid",Userid);
                    i.putExtra("button", clicked);
                    startActivity(i);
                    PhoneForFacebook.this.finish();


                }




            }
            catch(Exception e)
            {

            }
        }
    }


}
