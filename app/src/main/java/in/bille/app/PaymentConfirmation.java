
package in.bille.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class PaymentConfirmation extends AppCompatActivity {
    SessionManager session;
    Intent intent;
    ImageView ImageType;
    TextView Paymentstatus;
    String Error= "",ProductInfo=" ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentconfirmation);
        session = new SessionManager(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Payment Status");
        HashMap<String, String> user = session.getUserDetails();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ImageType = (ImageView)findViewById(R.id.statusicon);
        Paymentstatus = (TextView)findViewById(R.id.textViewpaystat);

        String UserPhone = user.get(SessionManager.KEY_PHONE);
        String UserName = user.get(SessionManager.KEY_Name);
        String UserEmail= user.get(SessionManager.KEY_Email);

        Log.d("userphone no", UserPhone);
        String unpaid = getIntent().getExtras().getString("status");
        String paid = getIntent().getExtras().getString("Bill id");
        if (unpaid.equals("failed"))

        {
            Toast.makeText(PaymentConfirmation.this, "You did not pay", Toast.LENGTH_SHORT).show();
            ImageType.setImageResource(R.drawable.cancel100);
            Paymentstatus.setText("PAYMENT UNSUCCESSFULL");
        } else  if ((unpaid.equals("success")))

        {
            Toast.makeText(PaymentConfirmation.this, "You paid", Toast.LENGTH_SHORT).show();
            ImageType.setImageResource(R.drawable.ok100);
            Paymentstatus.setText("PAYMENT SUCCESSFULL");
        }



//parseResult(data);
    }



    public void click(View v) {

        switch(v.getId()) {




        }
        startActivity(intent);
    }



    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        //super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), Drawer_activity.class);
        startActivity(i);
        PaymentConfirmation.this.finish();
        // optional depending on your needs
    }
    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();


        }
        return super.onOptionsItemSelected(item);


    }
    private void parseResult(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            if(jsonObj != null){
                Error = jsonObj.optString("status");
                ProductInfo = jsonObj.optString("productinfo");


                Log.d("this is error",ProductInfo);
                //Error = jsonObj.optString("error");




            }

        UpdateUi(Error);
        }

        catch (JSONException e) {
            e.printStackTrace();


        }
    }
    private void UpdateUi(String result2) {
if( result2.matches("success")) {

}    else{

    }

}}


