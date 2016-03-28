package in.bille.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class CustomerLogin extends AppCompatActivity implements View.OnClickListener {
    CallbackManager callbackManager;
    LoginButton loginfb;
    TextView fblogin;
    EditText username,password;
    String facebookname= "", facebookemail="",Userid="",email="",phone= "",name= "";
    TextView signuplink;
    TextView forgotpassword;
    private Button login;
    Connectiondetector cd;
    Boolean isInternetPresent= false;
    public static Activity cl;

    public static String BillId = null;
    Intent intent;

    ProgressDialog mProgressDialog;
     View b;
    SessionManager session;
    String useremail,pass;
    private boolean clicked = false;
    String regid="",checkstatus= "";

    private static final int REQUEST_SIGNUP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();
        setContentView(R.layout.activity_customerlogin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Login");
        session = new SessionManager(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        loginfb = (LoginButton)findViewById(R.id.fb_login);
        loginfb.setText("CONNECT WITH FACEBOOK");
        cd = new Connectiondetector(getApplicationContext());
        cl=this;
        isInternetPresent = cd.isConnectingToInternet();
        //BillId = intent.getStringExtra("billid");


        loginfb .setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        loginfb.setReadPermissions("public_profile email");

        loginfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isInternetPresent) {
                    Toast.makeText(getApplicationContext(),
                            "Internet not present", Toast.LENGTH_SHORT).show();}
                else if(isInternetPresent) {


                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();

                    Toast.makeText(getApplicationContext(),
                            "Access token present", Toast.LENGTH_SHORT).show();
                }
            }}
        });

        loginfb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                if (AccessToken.getCurrentAccessToken() != null) {
                    RequestData();
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
            }
        });


        HashMap<String, String> user = session.getUserDetails();
        regid = user.get(SessionManager.KEY_REGEDIT);
        username = (EditText)findViewById(R.id.input_email);
        password = (EditText)findViewById(R.id.input_password);
        login = (Button)findViewById(R.id.btn_login);
        signuplink=(TextView)findViewById(R.id.link_signup);

        signuplink.setText(Html.fromHtml("<u>Don't have an account yet? Signup</u>"));
       forgotpassword=(TextView)findViewById(R.id.forgot);
       forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Forgot activity
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        signuplink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
        login.setOnClickListener(this);

    }


    public void RequestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object,GraphResponse response) {

                JSONObject json = response.getJSONObject();
                try {
                    if(json != null){
                        facebookname=json.getString("name");
                        facebookemail=json.getString("email");

                        Log.d("facebook name",facebookname);
                        Log.d("facebook email",facebookemail);
                        String urlemailcheck="";


                        Log.d("i came here 2","i came here 2");
                        mProgressDialog = new ProgressDialog(CustomerLogin.this,R.style.AppTheme_Dark_Dialog);
                        // Set progressdialog title
                        mProgressDialog.setMessage("Verifying Email");
                        mProgressDialog.setIndeterminate(false);
                        mProgressDialog.setCancelable(false);
                        // Show progressdialog
                        mProgressDialog.show();
                        urlemailcheck = Config.serverurl+"login.php?tag=fblogin&email="+facebookemail+"&name="+facebookname+"&reg_id="+regid+"&fb_active=1"+"&token="+SplashMain.tokenvalue+"&device_id="+SplashMain.regid;;
                        urlemailcheck = urlemailcheck.replaceAll(" ", "%20");
                        Log.d("url", urlemailcheck);
                        new Checkemail().execute(urlemailcheck);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();



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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


Log.d("i came here", "i came here");

    }

    @Override
    public void onClick(View v) {

        if(!isInternetPresent) {
            Toast.makeText(getApplicationContext(),
                        "Internet not present", Toast.LENGTH_SHORT).show();}

            else if(isInternetPresent) {

        useremail = username.getText().toString();
        pass = password.getText().toString();


        boolean flag = true;

        if (useremail.equals("")) {
            flag = false;
            Toast.makeText(getApplicationContext(), "Email is required.",
                    Toast.LENGTH_SHORT).show();
        } else if (useremail.contains("@") != true) {
            flag = false;
            Toast.makeText(getApplicationContext(), "Invalid email.",
                    Toast.LENGTH_SHORT).show();
        } else if (useremail.contains(".") != true) {
            flag = false;
            Toast.makeText(getApplicationContext(), "Invalid email.",
                    Toast.LENGTH_SHORT).show();
        } else if (pass.equals("")) {
            flag = false;
            Toast.makeText(getApplicationContext(), "Password is required.",
                    Toast.LENGTH_SHORT).show();
        }

        if(flag) {
            String url;
            url = Config.serverurl+"login.php?tag=login&email="+useremail+"&password="+pass+"&reg_id="+regid+"&token="+SplashMain.tokenvalue+"&device_id="+SplashMain.regid;;

            Log.d("url",url);
            new ReadJSONFeedTask().execute(url);
        }
        clicked = true;

    }}

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
            mProgressDialog = new ProgressDialog(CustomerLogin.this,R.style.AppTheme_Dark_Dialog);
            // Set progressdialog title
            mProgressDialog.setMessage("Logging");
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
        {

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


            try
            {
                JSONObject jsonObject= new JSONObject(result);
                String checkerror = jsonObject.optString("error");

                if (checkerror.equals("true"))
                {
                    Log.d(" INCORRECT", "INCORRECT");
                    Toast.makeText(getApplicationContext(),"Incorrect username or password",Toast.LENGTH_SHORT).show();
                }

                else if(checkerror.equals("false"))
                {

                    JSONObject jsonObject1 = jsonObject.optJSONObject("user");
                    Log.d("test", "test");
                    String checkemail = jsonObject1.optString("email");
                    String checkname = jsonObject1.optString("name");
                    String checkphone = jsonObject1.optString("phone");
                    String checkID = jsonObject1.optString("id");
                    String checkstatus = jsonObject1.optString("authentic");

                    if(checkstatus.equals("1"))

                    {
                        Log.d("test", "login");
                        Toast.makeText(getApplicationContext(), "WELCOME", Toast.LENGTH_LONG).show();
                        session.createLoginSession(checkemail, checkname, checkphone, checkID);
                        Intent i = new Intent(getApplicationContext(), Drawer_activity.class);
                        i.putExtra("email", useremail);
                        i.putExtra("button", clicked);
                        startActivity(i);
                        CustomerLogin.this.finish();
                        WelcomeScreen.wa.finish();
                    }
                    else if(checkstatus.equals("0")){
                        Log.d("test2", "login2");
                        Toast.makeText(getApplicationContext(),"Please complete your registration first",Toast.LENGTH_LONG).show();
                        //session.createLoginSession(checkemail, checkname,checkphone,checkID);
                        Intent i = new Intent(getApplicationContext(), OtpVerification.class);
                        i.putExtra("useremail", checkemail);
                        i.putExtra("username", checkname);
                        i.putExtra("userphone", checkphone);
                        i.putExtra("userid",checkID);
                        i.putExtra("button", clicked);
                        startActivity(i);
                        CustomerLogin.this.finish();
                        WelcomeScreen.wa.finish();


                    }

                }


            }
            catch(Exception e)
            {

            }
        }
    }



    public class Checkemail extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {

            mProgressDialog = new ProgressDialog(CustomerLogin.this,R.style.AppTheme_Dark_Dialog);
            // Set progressdialog title
            mProgressDialog.setTitle("Verifying Email");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            // Show progressdialog
            mProgressDialog.show();

               }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    Log.d("str", response.toString());
                    checkemailfunction(response.toString());

                    result = 1; // Successful

                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                //Log.d(TAG, e.getLocalizedMessage());
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

      if(checkstatus.matches("0")){

    Intent i= new Intent(CustomerLogin.this,PhoneForFacebook.class);
                i.putExtra("name", facebookname);
                i.putExtra("email", facebookemail);
          i.putExtra("userid", Userid);

          startActivity(i);
          CustomerLogin.this.finish();
          WelcomeScreen.wa.finish();

      }

                else {
          session.createLoginSession(email,name,phone,Userid);
          Intent i= new Intent(CustomerLogin.this,Drawer_activity.class);
          i.putExtra("name", facebookname);
          i.putExtra("email", facebookemail);
          startActivity(i);
          CustomerLogin.this.finish();
          WelcomeScreen.wa.finish();



      }

            }

            else {
                Toast.makeText(CustomerLogin.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void checkemailfunction(String result) {
        try {
Log.d("this is result", result);
            JSONObject jsonObject= new JSONObject(result);
            JSONObject jsonObject1 = jsonObject.getJSONObject("user");

           checkstatus = jsonObject1.getString("authentic");
            Userid = jsonObject1.getString("id");
            phone = jsonObject1.getString("phone");
            email = jsonObject1.getString("email");
            name = jsonObject1.getString("name");




            // checkstatus= "1";
            Log.d("hello", checkstatus);

            } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }


    }




