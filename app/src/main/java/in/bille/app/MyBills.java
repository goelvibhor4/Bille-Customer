package in.bille.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyBills extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    protected static final String TAG = "My Bills";
    private static Context VContext;
    private List<FeedItem> feedItemList5 = new ArrayList<FeedItem>();
    private RecyclerView xRecyclerView;
    private MyBillsAdapter adapter5;
    String date="";
    RelativeLayout nobill;
    View Bill;
    Intent intent;
    ProgressDialog mProgressDialog;
    String Error;
    Boolean isInternetPresent= false;
    Connectiondetector cd;
    String UserID="";

    TextView Drawername,Draweremail,Drawerphone;
    SessionManager session;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bills);
        VContext = getApplicationContext();
        xRecyclerView= (RecyclerView) findViewById(R.id.bills_recycler_view);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Typeface tf= Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Bills");
        session = new SessionManager(getApplicationContext());
        cd = new Connectiondetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        HashMap<String, String> user = session.getUserDetails();
        String UserName = user.get(SessionManager.KEY_Name);
        String UserEmail = user.get(SessionManager.KEY_Email);
        String UserPhone = user.get(SessionManager.KEY_PHONE);
        String UserID = user.get(SessionManager.KEY_ID);

        Log.d("Email", "" + UserEmail);
        Log.d("Name", "" + UserName);
        Log.d("phone", "" + UserPhone);
        Log.d("USERID", "" + UserID);



        final String url5 = Config.serverurl + "billing_customer.php?u_id=" +UserID+"&token="+SplashMain.tokenvalue+"&device_id="+SplashMain.regid;
        new AsyncHttpTask5().execute(url5);
        Log.d("mybills",url5);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_drawer_activity, null);
        navigationView.addHeaderView(header);

        Drawername = (TextView)header.findViewById(R.id.Name);
        Draweremail    = (TextView)header.findViewById(R.id.Email);
        Drawerphone = (TextView)header.findViewById(R.id.Phone);
        session = new SessionManager(getApplicationContext());



        Drawername.setText(UserName);
        Draweremail.setText(UserEmail);

        Drawerphone.setText(UserPhone);

        nobill=(RelativeLayout) findViewById(R.id.nobillfound);
        Bill=(View)findViewById(R.id.bills_recycler_view);

        nobill.setVisibility(RelativeLayout.INVISIBLE);
        Bill.setVisibility(View.INVISIBLE);


        GoogleAnalytics analytics = GoogleAnalytics.getInstance(VContext);


        Tracker tracker = analytics.newTracker("UA-73635301-1");

        // All subsequent hits will be send with screen name = "main screen"
        tracker.setScreenName("My Bills");

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("UX")
                .setAction("click")
                .setLabel("submit")
                .build());



    }

    public void click(View v) {

        switch(v.getId()) {
            case R.id.getfood: // R.id.textView1
                intent = new Intent(this,Drawer_activity.class);
                MyBills.this.finish();
                break;



        }
        startActivity(intent);
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                super.onBackPressed();
                break;
            case  R.id.menu_search:
                Intent int2 = new Intent(MyBills.this,SearchMain.class);
                startActivity(int2);
                break;

        }
        return super.onOptionsItemSelected(item);


    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.ReachHome) {
            Intent int1 = new Intent(MyBills.this,Drawer_activity.class);
            startActivity(int1);
            MyBills.this.finish();
        }/* else if (id == R.id.nav_profile) {
            Intent int2 = new Intent(MyBills.this,EditProfile.class);
            startActivity(int2);

        }*/ else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

            Intent int2 = new Intent(MyBills.this,MyBills.class);
            startActivity(int2);
        }  else if (id == R.id.nav_aboutus) {

            Intent int2 = new Intent(MyBills.this,AboutUs.class);
            startActivity(int2);
            MyBills.this.finish();

        }



        else if (id == R.id.nav_send) {


            if(!isInternetPresent) {

                Toast.makeText(MyBills.this, "Internet not present.", Toast.LENGTH_SHORT).show();


            }


            else if(isInternetPresent) {

                final String logout =Config.serverurl+"logout.php?id="+UserID+"&reg_id="+SplashMain.regid+"&token="+SplashMain.tokenvalue+"&device_id="+SplashMain.regid;
                new LogoutAsync().execute(logout);
                session.logoutUser();
                MyBills.this.finish();
            }




        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class AsyncHttpTask5 extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(MyBills.this);
            // Set progressdialog title
            // Set progressdialog message
            mProgressDialog.setMessage("Loading bills");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setProgressDrawable(getDrawable(R.drawable.footprintyellow));
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

            /* Download complete. Lets update UI */
            if (result == 1) {


                if (Error != null && !Error.isEmpty()) {

                    nobill.setVisibility(RelativeLayout.VISIBLE);
                }else{
                    Bill.setVisibility(RelativeLayout.VISIBLE);

                }


                adapter5 = new MyBillsAdapter(MyBills.this,feedItemList5);
                xRecyclerView.setAdapter(adapter5);
            } else {
                Bill.setVisibility(View.VISIBLE);

                SharedPreferences sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPrefs.getString(TAG, null);
//feedsList = new ArrayList<>();
                Type type = new TypeToken<ArrayList<FeedItem>>() {}.getType();
                List<FeedItem> feedItemList5 = gson.fromJson(json,type);

                adapter5 = new MyBillsAdapter(MyBills.this, feedItemList5);
                xRecyclerView.setAdapter(adapter5);
                Log.e(TAG, "Failed to fetch data!");
            }
        }
    }


    private void parseResult5(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            Error = jsonObj.optString("error");
            if(jsonObj != null){
                JSONArray list = jsonObj.getJSONArray("bill");

                if (null == feedItemList5) {
                    feedItemList5 = new ArrayList<FeedItem>();
                }


                for (int k = 0; k < list.length(); k++) {
                    JSONObject post4 = list.optJSONObject(k);

                    FeedItem item = new FeedItem();
                    item.setId(post4.optString("mer_id"));
                    item.setBillid(post4.optString("bill_id"));
                    item.setCustomerBillId(post4.optString("b_id"));
                    item.setDate(post4.optString("date"));
                    date=post4.getString("date");
                    item.setTime(post4.optString("time"));
                    item.setMerchantname(post4.optString("mer_name"));
                    item.setStatus(post4.optString("status"));
                    item.setLocation(post4.optString("mer_location"));
                    item.setOrder(post4.optString("order"));
                    item.setQuantity(post4.optString("qty"));
                    item.setAmount(post4.optString("amount"));
                    item.setDisc_amt(post4.optString("discount"));
                    item.setTotalamount(post4.optString("total"));
                    item.setStatus(post4.optString("status"));

                    feedItemList5.add(item);
                    SharedPreferences sharedPrefs = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Gson gson = new Gson();

                    String json = gson.toJson(feedItemList5);
                    Log.d("offline", "" + json);
                    editor.putString(TAG, json);
                    editor.commit();

                    Log.d("date", date);
                }

            }
        }

        catch (JSONException e) {
            e.printStackTrace();


        }
    }

    public class LogoutAsync extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(MyBills.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Loading");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
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
                    Logoutfunction(response.toString());
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


                Toast.makeText(MyBills.this, "Succesffully logged out", Toast.LENGTH_SHORT).show();


            }

            else {
                Toast.makeText(MyBills.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void Logoutfunction(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);



            Error = jsonObj.optString("error");






        }

        catch (JSONException e) {
            e.printStackTrace();


        }
    }

}
