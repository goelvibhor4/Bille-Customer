package in.bille.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OtpVerification extends AppCompatActivity implements View.OnClickListener {
    String otpnumber;
View b;
    int color1= 0xffa500;

    protected static final String TAG = "otp verfication";
    int length;
    static EditText Otp;
    Button Submit,otp;
    String uname,uemail,uphone,uid;
    SessionManager session;
    private boolean clicked = false;
    ProgressDialog mProgressDialog;
    String url= "";
    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    public TextView text;
    private final long startTime = 30 * 1000;
    private final long interval = 1 * 1000;

    String jsonObject1="",jsonObject2="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        session = new SessionManager(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("OTP verification");
        Intent in = getIntent();
        otp=(Button)findViewById(R.id.resend);
        text = (TextView) this.findViewById(R.id.timer);
        otp.setEnabled(false);
        countDownTimer = new MyCountDownTimer(startTime, interval);
        text.setText(text.getText() + String.valueOf(startTime / 1000));

        if (!timerHasStarted) {
            countDownTimer.start();
            timerHasStarted = true;}

       uname = in.getStringExtra("username");
        uemail = in.getStringExtra("useremail");

        uphone = in.getStringExtra("userphone");
        Log.d("phone no", uphone);
        uid = in.getStringExtra("userid");
     Log.d("username",""+uname);
        Log.d("username",""+uemail);
        Log.d("username",""+uphone);
        Log.d("username",""+uid);
        Otp = (EditText)findViewById(R.id.input_otp);
        Submit = (Button)findViewById(R.id.btn_inside);
        Submit.setOnClickListener(this);




        Otp.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 6) {


                }


            }
        });


    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            text.setText("0");
            otp.setEnabled(true);


            otp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method
                    String urlotp = Config.serverurl + "otp_resend.php?u_id=" + uid+"&token="+SplashMain.tokenvalue+"&device_id="+SplashMain.regid;
                    new ResendOtp().execute(urlotp);

                }
            });

        }

        @Override
        public void onTick(long millisUntilFinished) {
            text.setText("" + millisUntilFinished / 1000);
        }
    }

    public void recivedSms(String message)
    { Otp.setText(message);
        otpnumber=message;
        Submit.performClick();

b.callOnClick();
          try
        {
            length= message.length();
            Log.d("log here o", length + "");
            url = Config.serverurl+"otp_verify.php?u_id="+uid+"&otp="+otpnumber;
            Log.d("url", url);
            new AsyncHttpTask5().execute(url);
        }
        catch (Exception e)
        {
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

        boolean flag = true;
        otpnumber = Otp.getText().toString();

        if (otpnumber.equals("") || otpnumber == "") {
            flag = false;
            Toast.makeText(getApplicationContext(),
                    "OTP is required.", Toast.LENGTH_SHORT).show();
            Otp.setFocusable(true);

        }


        if(flag)
        {
       //     Toast.makeText(getApplicationContext(),"working",Toast.LENGTH_SHORT).show();
            url = Config.serverurl+"otp_verify.php?u_id="+uid+"&otp="+otpnumber;
            Log.d("url", url);
            new AsyncHttpTask5().execute(url);

        }


    }


    public class ResendOtp extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {

            mProgressDialog = new ProgressDialog(OtpVerification.this);
            // Set progressdialog title

            // Set progressdialog message
            mProgressDialog.setMessage("Requesting...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            Integer result = 0;
            HttpURLConnection urlConnection = null;

            try {
                /* forming th java.net.URL object */
                URL url4 = new URL(params[0]);

                urlConnection = (HttpURLConnection) url4.openConnection();

                /* for Get request */
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
                if (statusCode ==  200) {
                    System.out.println("Status code is:" + statusCode);
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println("this is response" + response.toString());

                    otpresult(response.toString());


                    result = 1; // Successful

                }else{
                    result = 0; //"Failed to fetch data!"// ;

                }

            } catch (Exception e) {


                Log.d(TAG, e.getLocalizedMessage());
            }

            return result; //"Failed to fetch data!";
        }
        @Override
        protected void onPostExecute(Integer result) {

            try {
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
            if (result == 1) {
                if(jsonObject1.equals("false"))
                {
/*
                    Intent i = new Intent(getApplicationContext(),Drawer_activity.class);

                    i.putExtra("email", uemail);
                    i.putExtra("button", clicked);
                    session.createLoginSession(uemail, uname, uphone, uid);
                    startActivity(i);
                    OtpVerification.this.finish();
                    WelcomeScreen.wa.finish();*/

                } else {

                    Log.e(TAG, "Failed to fetch data!");
                }
            }
        }
        private void otpresult(String result) {
            try {
                JSONObject jsonObject= new JSONObject(result);
                jsonObject1 = jsonObject.optString("error");
                Log.d("ERROR",jsonObject1);
            }

            catch (JSONException e) {
                e.printStackTrace();


            }
        }

    }
    public class AsyncHttpTask5 extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            Integer result = 0;
            HttpURLConnection urlConnection = null;

            try {
                /* forming th java.net.URL object */
                URL url4 = new URL(params[0]);

                urlConnection = (HttpURLConnection) url4.openConnection();

                /* for Get request */
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
                if (statusCode ==  200) {
                    System.out.println("Status code is:" + statusCode);
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println("this is response" + response.toString());

                    parseResult5(response.toString());


                    result = 1; // Successful

                }else{
                    result = 0; //"Failed to fetch data!"// ;

                }

            } catch (Exception e) {


                Log.d(TAG, e.getLocalizedMessage());
            }

            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPreExecute() {

            mProgressDialog = new ProgressDialog(OtpVerification.this);
            // Set progressdialog title

            // Set progressdialog message
            mProgressDialog.setMessage("Verifying OTP");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

        }
        @Override
        protected void onPostExecute(Integer result) {

            try {
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

            if (result == 1) {

                if(jsonObject1.equals("true")){

                    Toast.makeText(getApplicationContext(),
                            "Incorrect code entered. Please try again.", Toast.LENGTH_SHORT).show();
                }

                else if(jsonObject1.equals("false"))
                {

                    Intent i = new Intent(getApplicationContext(),Drawer_activity.class);

                    i.putExtra("email", uemail);
                    i.putExtra("button", clicked);
                    session.createLoginSession(uemail, uname, uphone, uid);
                    startActivity(i);
                   // CustomerLogin.cl.finish();
                    OtpVerification.this.finish();
                    WelcomeScreen.wa.finish();

                } else {

                    Log.e(TAG, "Failed to fetch data!");
                }
            }
        }
        private void parseResult5(String result) {
            try {
                Log.d("this is response", result);
                JSONObject jsonObject= new JSONObject(result);
                jsonObject1 = jsonObject.optString("error");
                jsonObject2 = jsonObject.optString("msg");


                Log.d("ERROR",jsonObject1);
            }

            catch (JSONException e) {
                e.printStackTrace();


            }
        }

    }


}