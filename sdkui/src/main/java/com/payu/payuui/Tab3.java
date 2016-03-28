package com.payu.payuui;

/**
 * Created by SHUBHAM on 21-01-2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.payu.india.Model.PaymentDetails;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PayuResponse;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.india.PostParams.PaymentPostParams;

import java.util.ArrayList;

/**
 * Created by hp1 on 21-01-2015.
 */
public class Tab3 extends Fragment implements View.OnClickListener {
    PayuResponse mPayuResponse;
Boolean isViewShown= false;
    private String bankcode;
    private Bundle bundle;
    private ArrayList<PaymentDetails> netBankingList;
    private Spinner spinnerNetbanking;

    private PaymentParams mPaymentParams;
    private PayuHashes payuHashes;


    private Button payNowButton;

    private PayUNetBankingAdapter payUNetBankingAdapter;
    private Toolbar toolbar;
    private PayuConfig payuConfig;

    private TextView amountTextView;
    private TextView transactionIdTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_3,container,false);
        Tab4.isViewShown= false;
        String fontPath = "fonts/Rupee_Foradian.ttf";
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
        (payNowButton = (Button)v. findViewById(R.id.button_pay_now)).setOnClickListener(this);
        spinnerNetbanking = (Spinner)v. findViewById(R.id.spinner_netbanking);
        bundle =getActivity(). getIntent().getExtras();
        mPaymentParams = bundle.getParcelable(PayuConstants.PAYMENT_PARAMS);
        payuHashes = bundle.getParcelable(PayuConstants.PAYU_HASHES);
        payuConfig = bundle.getParcelable(PayuConstants.PAYU_CONFIG);
        payuConfig = null != payuConfig ? payuConfig : new PayuConfig();
        (amountTextView = (TextView)v. findViewById(R.id.text_view_amount)).setText("Amount" + ": " +"` "+ mPaymentParams.getAmount());
        amountTextView.setTypeface(tf);
        (transactionIdTextView = (TextView)v. findViewById(R.id.text_view_transaction_id)).setText("TXN ID" + ":" + mPaymentParams.getTxnId());
     //   Toast.makeText(getActivity(), "TOAST 3", Toast.LENGTH_LONG).show();


        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null) {
            hideKeyboard(getView().getContext());

            isViewShown = true;
            netBankingList = new ArrayList<PaymentDetails>();
            netBankingList=PayUBaseActivity2.al;

            payUNetBankingAdapter = new PayUNetBankingAdapter(getActivity(), netBankingList);
            spinnerNetbanking.setAdapter(payUNetBankingAdapter);
            spinnerNetbanking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                    bankcode = netBankingList.get(index).getBankCode();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


        } else {
            isViewShown = false;
        }
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_pay_now) {
            // okey we need hash fist
            PostData postData = new PostData();
            mPaymentParams.setHash(payuHashes.getPaymentHash());
            mPaymentParams.setBankCode(bankcode);

            postData = new PaymentPostParams(mPaymentParams, PayuConstants.NB).getPaymentPostParams();

//            postData = new NBPostParams(mPaymentParams, mNetBank).getNBPostParams();
            if (postData.getCode() == PayuErrors.NO_ERROR) {
                // launch webview
                payuConfig.setData(postData.getResult());
                Intent intent = new Intent(getView().getContext(), PaymentsActivity.class);
                intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
                startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
            } else {
       //         Toast.makeText(getView().getContext(),"toast from tab3 onclick"+ postData.getResult(), Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            getActivity(). setResult(resultCode, data);
            getActivity(). finish();
        }
    }
    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}
