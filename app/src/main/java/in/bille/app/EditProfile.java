package in.bille.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class EditProfile extends AppCompatActivity  {
    SessionManager session;
EditText Name,Email;
    TextView Phone;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        session = new SessionManager(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");

        HashMap<String, String> user = session.getUserDetails();


        String UserPhone = user.get(SessionManager.KEY_PHONE);
        String UserName = user.get(SessionManager.KEY_Name);
        String UserEmail = user.get(SessionManager.KEY_Email);

        Name = (EditText) findViewById(R.id.namedata);
        Email = (EditText) findViewById(R.id.emaildata);
        Phone = (TextView) findViewById(R.id.telephonedata);

        Name.setText(UserName);
        Email.setText(UserEmail);
        Phone.setText(UserPhone);
        Button savechanges = (Button) findViewById(R.id.saveChanges);

        savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = Name.getText().toString();
                final String useremail = Email.getText().toString();
                if (username.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Enter  a valid name", Toast.LENGTH_SHORT).show();
                }
                if (useremail.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Enter a valid email", Toast.LENGTH_SHORT).show();
                }
                if (username.length() > 0 && Email.length() > 0) {

                    Toast.makeText(getApplicationContext(), "callng Api", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }



    public class Edititems extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(EditProfile.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Loading");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();        }

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
                    parseResult4(response.toString());
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
                Toast.makeText(EditProfile.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void parseResult4(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            if(jsonObj != null){


                String Customername = jsonObj.getString("c_name");
                String Merchantphone = jsonObj.getString("c_phone");
                String tax = jsonObj.getString("tax");
                final String discount1 = jsonObj.getString("discount");
                final String status1 = jsonObj.getString("paid");
                final String total = jsonObj.getString("total");
                final  String Discounted = jsonObj.getString("discounted");
                JSONArray list = jsonObj.getJSONArray("order");




                for ( int k = 0; k < list.length(); k++) {
                    JSONObject post4 = list.optJSONObject(k);
                    FeedItem item = new FeedItem();
                    item.setBillid(post4.optString("menu_id"));
                    final String oid = post4.optString("menu_id");
                    item.setItemname(post4.optString("item"));
                    item.setLocation(post4.optString("m_location"));
                    item.setItemprice(post4.optString("price"));
                    String quantity = post4.optString("qty");
                    item.setItemquantity(quantity + " x ");
                    item.setItemscost(post4.optString("cost"));
                }


            }
        }

        catch (JSONException e) {
            e.printStackTrace();


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

