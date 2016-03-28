package com.payu.payuui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.payu.india.Interfaces.PaymentRelatedDetailsListener;
import com.payu.india.Model.MerchantWebService;
import com.payu.india.Model.PaymentDetails;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PayuResponse;
import com.payu.india.Model.PostData;
import com.payu.india.Model.StoredCard;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.india.Payu.PayuUtils;
import com.payu.india.PostParams.MerchantWebServicePostParams;
import com.payu.india.Tasks.GetPaymentRelatedDetailsTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PayUBaseActivity2 extends ActionBarActivity implements  PaymentRelatedDetailsListener {
    CardSession session;
    PayuResponse mPayuResponse;
    Intent mIntent;
    PayuConfig payuConfig;
   public static ArrayList<PaymentDetails> al;
    public static   ArrayList<PaymentDetails> list;
    public ArrayList<StoredCard> storedCards;
    ArrayList<StoredCard> oneClickCards;
    HashMap<String, String> oneClickCardTokens;
String BillId= "";
    //    PaymentDefaultParams mPaymentDefaultParams;
    PaymentParams mPaymentParams;
    PayuHashes mPayUHashes;

    int storeOneClickHash;
    Bundle bundle;
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Credit/Debit","Netbanking","PayU Wallet"};
    int Numboftabs =3;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base2);
        context = getApplicationContext();

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select your payment method");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        storedCards = new ArrayList<>();
        oneClickCards = new ArrayList<>();
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.container);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        storedCards = new ArrayList<>();
        oneClickCards = new ArrayList<>();


        // lets collect the details from bundle to fetch the payment related details for a merchant
        bundle = getIntent().getExtras();
        for (String key: bundle.keySet())
        {
            Log.d("myApplication", key + " is a key in the bundle");
        }

        payuConfig = bundle.getParcelable(PayuConstants.PAYU_CONFIG);
        Log.d("myApplication2", String.valueOf(payuConfig));

        payuConfig = null != payuConfig ? payuConfig : new PayuConfig();

        // TODO add null pointer check here
//        mPaymentDefaultParams = bundle.getParcelable(PayuConstants.PAYMENT_DEFAULT_PARAMS);
        mPaymentParams = bundle.getParcelable(PayuConstants.PAYMENT_PARAMS);
        Log.d("myApplication2", String.valueOf(mPaymentParams));
        BillId= mPaymentParams.getUdf2().toString();
        Log.d("this is bill",BillId);

        // Todo change the name to PAYMENT_PARAMS
        mPayUHashes = bundle.getParcelable(PayuConstants.PAYU_HASHES);

        Log.d("myApplication2", String.valueOf(mPayUHashes));

        storeOneClickHash = bundle.getInt(PayuConstants.STORE_ONE_CLICK_HASH);

        oneClickCardTokens = (HashMap<String, String>) bundle.getSerializable(PayuConstants.ONE_CLICK_CARD_TOKENS);

        MerchantWebService merchantWebService = new MerchantWebService();
        merchantWebService.setKey(mPaymentParams.getKey());
        merchantWebService.setCommand(PayuConstants.PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK);
        merchantWebService.setVar1(mPaymentParams.getUserCredentials() == null ? "default" : mPaymentParams.getUserCredentials());

        // hash we have to generate


        merchantWebService.setHash(mPayUHashes.getPaymentRelatedDetailsForMobileSdkHash());

//        PostData postData = new PostParams(merchantWebService).getPostParams();

        // Dont fetch the data if calling activity is PaymentActivity

        // fetching for the first time.
        if(null == savedInstanceState){ // dont fetch the data if its been called from payment activity.
            PostData postData = new MerchantWebServicePostParams(merchantWebService).getMerchantWebServicePostParams();
            if(postData.getCode() == PayuErrors.NO_ERROR){
                // ok we got the post params, let make an api call to payu to fetch the payment related details
                payuConfig.setData(postData.getResult());

                // lets set the visibility of progress bar
                GetPaymentRelatedDetailsTask paymentRelatedDetailsForMobileSdkTask = new GetPaymentRelatedDetailsTask(this);
                    paymentRelatedDetailsForMobileSdkTask.execute(payuConfig);
            } else {
                Toast.makeText(this, postData.getResult(), Toast.LENGTH_LONG).show();
                // close the progress bar
//                findViewById(R.id.progress_bar).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        //super.onBackPressed();
        Intent intent = new Intent("sdkui.intent.action.Launch");
        intent.putExtra("billid", BillId);
        startActivity(intent);

        PayUBaseActivity2.this.finish();
          // optional depending on your needs
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();
                Intent intent = new Intent("sdkui.intent.action.Launch");
                intent.putExtra("billid", BillId);
                startActivity(intent);
                PayUBaseActivity2.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onPaymentRelatedDetailsResponse(PayuResponse payuResponse) {
        mPayuResponse = payuResponse;
//        findViewById(R.id.progress_bar).setVisibility(View.GONE);
        HashMap<String, ArrayList<StoredCard>> storedCardMap = new HashMap<>();
        switch (storeOneClickHash){
            case PayuConstants.STORE_ONE_CLICK_HASH_MOBILE:
                storedCardMap = new PayuUtils().getStoredCard(this, mPayuResponse.getStoredCards());
                storedCards = storedCardMap.get(PayuConstants.STORED_CARD);
                oneClickCards = storedCardMap.get(PayuConstants.ONE_CLICK_CHECKOUT);
                break;
            case PayuConstants.STORE_ONE_CLICK_HASH_SERVER:
                storedCardMap = new PayuUtils().getStoredCard(mPayuResponse.getStoredCards(), oneClickCardTokens);
                storedCards = storedCardMap.get(PayuConstants.STORED_CARD);
                oneClickCards = storedCardMap.get(PayuConstants.ONE_CLICK_CHECKOUT);
                break;
            case PayuConstants.STORE_ONE_CLICK_HASH_NONE: // all are stored cards.
            default:
                storeOneClickHash = 0;
                storedCards = payuResponse.getStoredCards();
                break;
        }

//        HashMap<String, ArrayList<StoredCard>> storedCardMap = new PayuUtils().getStoredCard(this, mPayuResponse.getStoredCards());
//        HashMap<String, ArrayList<StoredCard>> storedCardMap = new PayuUtils().getStoredCard(mPayuResponse.getStoredCards(), oneClickCardTokens);

        if(payuResponse.isResponseAvailable() && payuResponse.getResponseStatus().getCode() == PayuErrors.NO_ERROR){ // ok we are good to go
            Toast.makeText(this, payuResponse.getResponseStatus().getResult(), Toast.LENGTH_LONG).show();

            if(payuResponse.isStoredCardsAvailable() && null != storedCards && storedCards.size() > 0){
            }
            if(payuResponse.isStoredCardsAvailable() && oneClickCards.size() > 0){
            }
            if(payuResponse.isNetBanksAvailable()){
                Log.d("this is hello", String.valueOf(mPayuResponse.getNetBanks()));
                 al = new ArrayList<PaymentDetails>();
                al=mPayuResponse.getNetBanks();



              /*  CardSession  CardPrefs=this.getSharedPreferences("yourPrefsKey",Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=prefs.edit();

                Set<String> set = new HashSet<String>();
                set.addAll(your Arraylist Name);
                edit.putStringSet("yourKey", set);
                edit.commit();*/


               /* Bundle bundle = new Bundle();

                mIntent = new Intent(this, Tab3.class);
                mIntent.putParcelableArrayListExtra(PayuConstants.NETBANKING, mPayuResponse.getNetBanks());
*/
                /* list = new ArrayList<>();
                list.addAll(mPayuResponse.getNetBanks());*/

            }

            if(payuResponse.isCreditCardAvailable() || payuResponse.isDebitCardAvailable()){
            }
        }else{
            Toast.makeText(this, "Something went wrong4 : " + payuResponse.getResponseStatus().getResult(), Toast.LENGTH_LONG).show();
        }

        // no mater what response i get just show this button, so that we can go further.
       // findViewById(R.id.linear_layout_verify_api).setVisibility(View.VISIBLE);
    }


}
