package in.bille.app;

/**
 * Created by Vibhor Goel on 12/30/2015.
 */
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.payu.india.Payu.PayuConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BillDescription extends AppCompatActivity{
    SessionManager session;
    private List<FeedItem> feedsList;
    private RecyclerView mRecyclerView;
    ProgressDialog mProgressDialog;
    RelativeLayout i,j;
    Button Pay;
    public static Activity billdescription;
    Intent intent;
    private BillDescriptionRecyclerAdapter adapter;
    public static String merchid= null;

    String Merchantname="",add="",total="",b="",c="",d="",Discounted="",discount="",date="",Error="",url3="",buttontext="",Text="",Type="",CustomerName,mid="",RealBillid="",RegIdFromPref="", TokenValueFromPref= "",CustomerBillId="";
    TextView name,MerchantAdd,amounttextview,discountedtextview,discounttextview,dateview,BillId,QuickBill;
String UserPhone="", UserName="",UserId="",UserEmail="",refreshurl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bill_description);
        session = new SessionManager(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Your bill");

        /*String fontPath = "fonts/Rupee_Foradian.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);*/
        QuickBill = (TextView)findViewById(R.id.quickbill);
        billdescription =this;
        name = (TextView)findViewById(R.id.textViewRestaurantName);
        MerchantAdd = (TextView)findViewById(R.id.textViewlocation);
        BillId = (TextView)findViewById(R.id.TextViewBillId);
        amounttextview = (TextView)findViewById(R.id.subtotalamount);
        b=amounttextview.getText().toString();
        dateview= (TextView)findViewById(R.id.dateholder);
        discounttextview=(TextView)findViewById(R.id.discountamount);
        c=discounttextview.getText().toString();
        d=discounttextview.getText().toString();

        discountedtextview=(TextView)findViewById(R.id.totalamountdata);
        Pay=(Button)findViewById(R.id.paybutton);


        Intent intent = getIntent();
        RealBillid = intent.getStringExtra("billid");
        merchid = intent.getStringExtra("merchid");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_bill_desc);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        session = new SessionManager(getApplicationContext());

       /* boolean check = session.checkLogin();
        Log.d("this is check", String.valueOf(check));*/
        HashMap<String, String> regdetails = session.getregistrationdetails();
        RegIdFromPref=regdetails.get(SessionManager.KEY_REGEDIT);
        TokenValueFromPref=regdetails.get(SessionManager.KEY_TOKEN);


        HashMap<String, String> user = session.getUserDetails();
         UserPhone = user.get(SessionManager.KEY_PHONE);
         UserName = user.get(SessionManager.KEY_Name);
         UserEmail = user.get(SessionManager.KEY_Email);
          UserId = user.get(SessionManager.KEY_ID);

        if (UserPhone==null){
            Toast.makeText(BillDescription.this, "User not logged in", Toast.LENGTH_SHORT).show();
            AlertDialog alertDialog = new AlertDialog.Builder(
                    BillDescription.this).create();

            // Setting Dialog Title
            alertDialog.setTitle("User not logged in");

            // Setting Dialog Message
            alertDialog.setMessage("Please login to pay this bill");
alertDialog.setCancelable(false);
            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    Intent i = new Intent(BillDescription.this, CustomerLogin.class);

                    startActivity(i);
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }


        if (RealBillid != null && !RealBillid.isEmpty())

        {// called in case of notification
            final String url6 =Config.serverurl+"order_customer.php?bill_id="+RealBillid+"&token="+TokenValueFromPref+"&device_id="+RegIdFromPref;
            Log.d("noti url",url6);
            new AsyncHttpTask().execute(url6);
        } else  if (merchid != null && !merchid.isEmpty())

        {      //called in case when user click on restaurant page
            url3=Config.serverurl+"bill_last.php?m_id="+merchid+"&u_id="+UserId+"&token="+SplashMain.tokenvalue+"&device_id="+SplashMain.regid;
            Log.d("url executing ", url3);

            new AsyncHttpTask().execute(url3);

        }
        i=(RelativeLayout) findViewById(R.id.mainlayout);
        j=(RelativeLayout) findViewById(R.id.nobillfound);

        i.setVisibility(RelativeLayout.INVISIBLE);
        j.setVisibility(RelativeLayout.INVISIBLE);



        Pay = (Button) findViewById(R.id.paybutton);
        Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BillDescription.this, PaymentsMain.class);
               // Double AmountInDouble=  Double.parseDouble(d);
                i.putExtra("amount",Discounted);
                i.putExtra("email",UserEmail);
                i.putExtra("firstname",CustomerName);
                i.putExtra("usercredentials","");
                i.putExtra("productinfo",RealBillid);
                Log.d("onclick id",RealBillid);
                i.putExtra("mobile",UserPhone);
                startActivity(i);
            BillDescription.this.finish();

            }
        });

    }

    public void click(View v) {

        switch(v.getId()) {
            case R.id.getfood: // R.id.textView1
                intent = new Intent(this,Drawer_activity.class);
                break;
        }
        startActivity(intent);
    }



/*    @Override
    public void onClick(View v) {

        new AsyncHttpTask().execute(url3);

    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        CreateMenu(menu);
        return true;
    }

    @SuppressLint("NewApi")
    private void CreateMenu(Menu menu) {


        MenuItem menu1 = menu.add(0, 0, 0, "Refresh");
        {
            menu1.setIcon(R.drawable.refreshwhite);
            menu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        //super.onBackPressed();

        if(PaymentsMain.active){
            Intent int2 = new Intent(BillDescription.this,Drawer_activity.class);
            startActivity(int2);
            BillDescription.this.finish();
            PaymentsMain.object.finish();
        }
else{

            Intent int3 = new Intent(BillDescription.this,Drawer_activity.class);
            startActivity(int3);
            BillDescription.this.finish();

        }
        // optional depending on your needs
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                if(PaymentsMain.active){
                    Intent int2 = new Intent(BillDescription.this,Drawer_activity.class);
                    startActivity(int2);
                    BillDescription.this.finish();
                    PaymentsMain.object.finish();
                    return true;

                }
                else{

                    Intent int3 = new Intent(BillDescription.this,Drawer_activity.class);
                    startActivity(int3);
                    BillDescription.this.finish();
                    return true;


                }

            case 0:

                if (i.getVisibility() == View.VISIBLE) {
                    Toast.makeText(BillDescription.this, "Updated", Toast.LENGTH_SHORT).show();

                } else if(i.getVisibility() == View.INVISIBLE) {
                    refreshurl=Config.serverurl+"bill_last.php?m_id="+merchid+"&u_id="+UserId+"&token="+TokenValueFromPref+"&device_id="+RegIdFromPref;
                    Log.d("url is here :",refreshurl);
                    new AsyncHttpTask().execute(refreshurl);

                }



                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(BillDescription.this);
            // Set progressdialog title
            // Set progressdialog message
            mProgressDialog.setMessage("Getting the bill...");
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


                if (Error != null && !Error.isEmpty()) {

                    i.setVisibility(RelativeLayout.INVISIBLE);
                    j.setVisibility(RelativeLayout.VISIBLE);
                }else{
                    i.setVisibility(RelativeLayout.VISIBLE);
                    j.setVisibility(RelativeLayout.INVISIBLE);
                }

                name.setText(Merchantname);
                MerchantAdd.setText(add);
                dateview.setText(date);
                BillId.setText(CustomerBillId);
                b+=total;
                amounttextview.setText(b);
                c+=discount;
                discounttextview.setText(c);
                d+=Discounted;
                discountedtextview.setText(d);
                buttontext = Pay.getText().toString();
                buttontext+="|"+d;

                if (Type.equals("detail")){
                    QuickBill.setVisibility(TextView.INVISIBLE);
                    adapter = new BillDescriptionRecyclerAdapter(BillDescription.this,feedsList);
                    mRecyclerView.setAdapter(adapter);}
                else if (Type.equals("custom")){

                    QuickBill.setText(Text);
                }

            }

            else {
                Toast.makeText(BillDescription.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void parseResult4(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);


            feedsList = new ArrayList<>();
            Error = jsonObj.optString("error");
            CustomerName = jsonObj.getString("c_name");
            Text= jsonObj.optString("text");
            Type= jsonObj.getString("type");
            Merchantname = jsonObj.getString("m_name");
            add= jsonObj.getString("m_location");
            mid = jsonObj.getString("m_id");
            CustomerBillId = jsonObj.optString("bill_id");
            RealBillid=jsonObj.optString("b_id");
            String tax = jsonObj.getString("tax");
            discount = jsonObj.getString("discount");
            date = jsonObj.getString("datetime");
            final String status1 = jsonObj.getString("paid");
            total = jsonObj.getString("total");
            Discounted = jsonObj.getString("discounted");
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
                feedsList.add(item);
            }

        }

        catch (JSONException e) {
            e.printStackTrace();


        }
    }


}