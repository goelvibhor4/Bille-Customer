package in.bille.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    EditText Email;
    String email;
    ProgressDialog mProgressDialog;
Button reset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Forgot Password");
        Email= (EditText)findViewById(R.id.input_email);
        reset=(Button)findViewById(R.id.newpassword);
        reset.setOnClickListener(this);

    }




    @Override
    public void onClick(View v) {

        email = Email.getText().toString();

        boolean flag = true;

        if (email.equals("")) {
            flag = false;
            Toast.makeText(getApplicationContext(), "Email is required.",
                    Toast.LENGTH_SHORT).show();
        } else if (email.contains("@") != true) {
            flag = false;
            Toast.makeText(getApplicationContext(), "Invalid email.",
                    Toast.LENGTH_SHORT).show();
        } else if (email.contains(".") != true) {
            flag = false;
            Toast.makeText(getApplicationContext(), "Invalid email.",
                    Toast.LENGTH_SHORT).show();
        }
        if(flag) {
            String url;
            url = Config.serverurl+"reset.php?email="+email+"&token="+SplashMain.tokenvalue+"&device_id="+SplashMain.regid;

            Log.d("url", url);
            new Checkemail().execute(url);
        }

    }


    public class Checkemail extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(ForgotPassword.this);
            // Set progressdialog title
            // Set progressdialog message
            mProgressDialog.setMessage("checking email");
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


                }



            else {
                Toast.makeText(ForgotPassword.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void checkemailfunction(String result) {
        try {
            Log.d("this is result", result);
            JSONObject jsonObject= new JSONObject(result);
            JSONObject jsonObject1 = jsonObject.getJSONObject("user");



            // checkstatus= "1";

        } catch (JSONException e1) {
            e1.printStackTrace();
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

