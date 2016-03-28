package com.payu.payuui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.india.PostParams.PaymentPostParams;

import java.util.HashMap;

/**
 * Created by Vibhor Goel on 2/9/2016.
 */
public class SavedCards extends AppCompatActivity  implements View.OnClickListener {
    TextView Name,Number,Month,Year;
    EditText Cvv;
    CardSession session;
    Button Pay;
    String CVVNO="", Cardnowithyear="";
    private PaymentParams mPaymentParams;
    private PayuHashes payuHashes;
    private PayuConfig payuConfig;
    private Bundle bundle;
    int storeOneClickHash;
    String CardName= "",CardNo="",CardMonth="",CardYear="",MobileNo= "", MobileNofromPayu="";
    private PostData postData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storedcards);

        session = new CardSession(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Stored cards");


        bundle = getIntent().getExtras();
        storeOneClickHash = bundle.getInt(PayuConstants.STORE_ONE_CLICK_HASH);
        payuHashes = bundle.getParcelable(PayuConstants.PAYU_HASHES);
        mPaymentParams = bundle.getParcelable(PayuConstants.PAYMENT_PARAMS);
        payuConfig = bundle.getParcelable(PayuConstants.PAYU_CONFIG);
        payuConfig = null != payuConfig ? payuConfig : new PayuConfig();


        MobileNofromPayu = mPaymentParams.getUdf1().toString();
        Log.d("payu mobile",MobileNofromPayu);
        HashMap<String, String> card = session.getCardDetails();
        MobileNo= card.get(CardSession.KEY_MOBILENO);
        Log.d("prefrence mobile",MobileNo);
 if(MobileNofromPayu.matches(MobileNo)){


Log.d("Mobile is same", " same");
     CardName = card.get(CardSession.KEY_CARDNAME);
     CardNo = card.get(CardSession.KEY_CARDNO);
     CardMonth = card.get(CardSession.KEY_CARDEXPMONTH);
     CardYear = card.get(CardSession.KEY_CARDEXPYEAR);

     Cardnowithyear="20"+CardYear;
     Log.d("card name", "" + CardName);
     Log.d("card no", "" + CardNo);
     Log.d("card month", "" + CardMonth);
     Log.d("card year", "" + Cardnowithyear);
     Log.d("mOBILE NO", "" +MobileNo );
        }



        Name=(TextView)findViewById(R.id.name);
        Number=(TextView)findViewById(R.id.number);
        Month=(TextView)findViewById(R.id.month);
        Year=(TextView)findViewById(R.id.year);
        Cvv=(EditText)findViewById(R.id.cvv);
        Pay=(Button)findViewById(R.id.pay);
        Pay.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        postData = null;
        CVVNO = Cvv.getText().toString();
        /*if (CVVNO.equals("")) {
            Toast.makeText(getApplicationContext(), "cvv is required.",
                    Toast.LENGTH_SHORT).show();
        } */

        mPaymentParams.setHash(payuHashes.getPaymentHash()); // make sure that you set payment hash
 //  mPaymentParams.setCardToken(storedCard.getCardToken());

        mPaymentParams.setNameOnCard(CardName);
        mPaymentParams.setCardName(CardName);
        mPaymentParams.setCardNumber(CardNo);

        mPaymentParams.setExpiryMonth(CardMonth);
        mPaymentParams.setExpiryYear(Cardnowithyear);
        mPaymentParams.setCvv(CVVNO);

        postData = new PaymentPostParams(mPaymentParams, PayuConstants.CC).getPaymentPostParams();
        if (postData.getCode() == PayuErrors.NO_ERROR) {
            // okay good to go.. lets make a transaction
            // launch webview
            payuConfig.setData(postData.getResult());
            Intent intent = new Intent(this, PaymentsActivity.class);
            intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
            intent.putExtra(PayuConstants.STORE_ONE_CLICK_HASH,PayuConstants.STORE_ONE_CLICK_HASH_NONE);
            startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
        } else {
        //    Toast.makeText(this, postData.getResult(), Toast.LENGTH_LONG).show();
        }
    }

}


