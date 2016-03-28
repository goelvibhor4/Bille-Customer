package com.payu.payuui;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PostData;
import com.payu.india.Model.StoredCard;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.india.Payu.PayuUtils;
import com.payu.india.PostParams.PaymentPostParams;

import java.util.ArrayList;

public class Tab4 extends Fragment {

    private PayuHashes mPayuHashes;
    private PaymentParams mPaymentParams;
    private PostData postData;
    private PayuConfig payuConfig;
    private Bundle bundle;
    private PayuUtils payuUtils;
    private TextView amountTextView;
    private TextView transactionIdTextView;
 public static  Boolean isViewShown= false;

    Button PayuButton ;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_4,container,false);
       // Toast.makeText(v.getContext(), "TOAST4", Toast.LENGTH_LONG).show();
        bundle = getActivity().getIntent().getExtras();

        String fontPath = "fonts/Rupee_Foradian.ttf";
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
        // lets get payment default params and hashes
        mPayuHashes = bundle.getParcelable(PayuConstants.PAYU_HASHES);
        mPaymentParams = bundle.getParcelable(PayuConstants.PAYMENT_PARAMS);
        payuConfig = bundle.getParcelable(PayuConstants.PAYU_CONFIG);
        payuConfig = null != payuConfig ? payuConfig : new PayuConfig();
        payuUtils = new PayuUtils();
        PayuButton = (Button)v.findViewById(R.id.button_card_make_payment);
        amountTextView = (TextView)v. findViewById(R.id.text_view_amount);
        transactionIdTextView = (TextView) v.findViewById(R.id.text_view_transaction_id);
        (amountTextView = (TextView)v. findViewById(R.id.text_view_amount)).setText("Amount" + ": " +"` "+ mPaymentParams.getAmount());
        amountTextView.setTypeface(tf);
        (transactionIdTextView = (TextView)v. findViewById(R.id.text_view_transaction_id)).setText("TXN ID" + ":" + mPaymentParams.getTxnId());
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null) {
            isViewShown = true;


            PostData postData;

            // lets try to get the post params
            mPaymentParams.setHash(mPayuHashes.getPaymentHash());
            PayuButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    PostData postData;
                    postData = new PaymentPostParams(mPaymentParams, PayuConstants.PAYU_MONEY).getPaymentPostParams();
                    if(postData.getCode() == PayuErrors.NO_ERROR){
                        // launch webview
                        isViewShown = false;
                        payuConfig.setData(postData.getResult());
                        Intent intent = new Intent(getActivity(), PaymentsActivity.class);
                        intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
                        startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);

                    }else{
                        Toast.makeText(getActivity(), postData.getResult(), Toast.LENGTH_LONG).show();
                    }
                }
            });
//        postData = new PayuWalletPostParams(mPaymentDefaultParams).getPayuWalletPostParams();


            // fetchdata() contains logic to show data when page is selected mostly asynctask to fill the data
            //fetchData();
            //       Toast.makeText(getActivity(), "Toast2", Toast.LENGTH_SHORT).show();
        } else {
            isViewShown = false;
        }
    }


    }

