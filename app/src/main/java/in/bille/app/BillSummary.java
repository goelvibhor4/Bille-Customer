package in.bille.app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BillSummary extends AppCompatActivity {
    private List<FeedItem>  Billdata  = new ArrayList<FeedItem>();
    ProgressDialog mProgressDialog;
    SessionManager session;
    private List<FeedItem> feedsList;
    private RecyclerView mRecyclerView;
    protected static final String TAG = "Bill Summary";
    private BillDescriptionRecyclerAdapter adapter;
    public static String billid = null;
    public static String merchid= null;
    RelativeLayout billlayout;
    String Merchantname="",add="",total="",b="",c="",z="",Discounted="",discount1="",date="",Error="",phone="",text= "",Type="",BillText="";
    TextView name,orderid,MerchantAdd,Subtotal,Discount,Total,dateview,Status,QuickBill;
ImageView Statusimage;
    CardView card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Bill details");
        session = new SessionManager(getApplicationContext());
        Intent in = getIntent();
        String BillId = in.getStringExtra("Bill id");
        String CustomerBillId=in.getStringExtra("Customer_Bill_id");
        String PaymentStatus=in.getStringExtra("paymentstatus");
        Log.d("paid status",PaymentStatus);
        QuickBill = (TextView)findViewById(R.id.quickbill);

        billlayout=(RelativeLayout) findViewById(R.id.mainlayout);
        billlayout.setVisibility(RelativeLayout.INVISIBLE);

        name = (TextView)findViewById(R.id.textViewRestaurantName);
        MerchantAdd = (TextView)findViewById(R.id.BStextViewlocation);
        orderid = (TextView)findViewById(R.id.BSTextViewBillId);
        dateview= (TextView)findViewById(R.id.dateholder);
        Status=(TextView)findViewById(R.id.paidstatus);
        Statusimage=(ImageView)findViewById(R.id.statusimage);
        Subtotal = (TextView)findViewById(R.id.subtotalamount);
        b = Subtotal.getText().toString();

        Discount = (TextView)findViewById(R.id.discountValue);
        c = Discount.getText().toString();

        Total = (TextView)findViewById(R.id.totalamountdata);
        z = Total.getText().toString();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_bill_desc);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        String UserPhone = user.get(SessionManager.KEY_PHONE);
        String UserName = user.get(SessionManager.KEY_Name);
        String UserEmail = user.get(SessionManager.KEY_Email);
        String UserId = user.get(SessionManager.KEY_ID);
        orderid.setText(CustomerBillId);
        if(PaymentStatus.equals("PAID")){

            Status.setText("PAID");
            Statusimage.setImageResource(R.drawable.ok24);
        }
        else if (PaymentStatus.equals("PENDING")){
            Status.setText("PENDING");
            Statusimage.setImageResource(R.drawable.cancel24);

        }


        final String url6 =Config.serverurl+"order_customer.php?bill_id="+BillId+"&token="+SplashMain.tokenvalue+"&device_id="+SplashMain.regid;
        new AsyncHttpTask4().execute(url6);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();


        }
        return super.onOptionsItemSelected(item);



    }
    public class AsyncHttpTask4 extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {

            mProgressDialog = new ProgressDialog(BillSummary.this);
            // Set progressdialog title
            // Set progressdialog message
            mProgressDialog.setMessage("Getting the bill...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            Integer result = 0;
            HttpURLConnection urlConnection = null;

            try {
                /* forming th java.net.URL object */
                URL url6 = new URL(params[0]);

                urlConnection = (HttpURLConnection) url6.openConnection();

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

                    parseResult4(response.toString());


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
                billlayout.setVisibility(RelativeLayout.VISIBLE);



                if (Error != null && !Error.isEmpty()) {


                }else{

                }
                name.setText(Merchantname);
                dateview.setText(date);
                MerchantAdd.setText(add);
                b+=total;
                Subtotal.setText(b);
                c+=discount1;
                Discount.setText(c);
                z+=Discounted;
                Total.setText(z);

                if (Type.equals("detail")){
                    QuickBill.setVisibility(TextView.INVISIBLE);
                    adapter = new BillDescriptionRecyclerAdapter(BillSummary.this,feedsList);
                    mRecyclerView.setAdapter(adapter);}
                else if (Type.equals("custom")){
                    QuickBill.setText(BillText);
                }


            }

            else {
                Toast.makeText(BillSummary.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void parseResult4(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            if(jsonObj != null){

                feedsList = new ArrayList<>();
                Error = jsonObj.optString("error");
                String Customername = jsonObj.getString("c_name");
                Merchantname = jsonObj.getString("m_name");
                add= jsonObj.getString("m_location");
                String Merchantphone = jsonObj.getString("c_phone");
                String tax = jsonObj.getString("tax");
                text=jsonObj.getString("text");
                Type= jsonObj.getString("type");
                BillText= jsonObj.optString("text");

                discount1 = jsonObj.getString("discount");
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
        }

        catch (JSONException e) {
            e.printStackTrace();


        }
    }



}