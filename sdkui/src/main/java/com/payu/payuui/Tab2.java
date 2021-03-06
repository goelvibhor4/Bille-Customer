package com.payu.payuui;

/**
 * Created by SHUBHAM on 21-01-2016.
 */
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.india.Payu.PayuUtils;
import com.payu.india.PostParams.PaymentPostParams;


public class Tab2 extends Fragment  implements View.OnClickListener {

    private Button payNowButton;
    private EditText cardNameEditText;
    private EditText cardNumberEditText;
    private EditText cardCvvEditText;
    private EditText cardExpiryMonthEditText;
    private EditText cardExpiryYearEditText;
    private Bundle bundle;
    private CheckBox saveCardCheckBox;
    private CheckBox enableOneClickPaymentCheckBox;

    private String cardName;
    private String Cardnowithyear;
    private String cardNumber;

    private String cvv;
    private String expiryMonth;
    private String expiryYear;

    private PayuHashes mPayuHashes;
    private PaymentParams mPaymentParams;
    private PostData postData;
    private Toolbar toolbar;

    private TextView amountTextView;
    private TextView transactionIdTextView;
    private PayuConfig payuConfig;

    private PayuUtils payuUtils;
    LinearLayout i;

    int storeOneClickHash;
Boolean isViewShown= false;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2,container,false);
        String fontPath = "fonts/Rupee_Foradian.ttf";
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), fontPath);

        (payNowButton = (Button)v. findViewById(R.id.button_card_make_payment)).setOnClickListener(this);

        cardNameEditText = (EditText)v. findViewById(R.id.edit_text_name_on_card);
        cardNumberEditText = (EditText)v. findViewById(R.id.edit_text_card_number);
        cardCvvEditText = (EditText)v. findViewById(R.id.edit_text_card_cvv);
        cardExpiryMonthEditText = (EditText)v. findViewById(R.id.edit_text_expiry_month);
        cardExpiryYearEditText = (EditText) v.findViewById(R.id.edit_text_expiry_year);
      //  saveCardCheckBox = (CheckBox)v. findViewById(R.id.check_box_save_card);
        enableOneClickPaymentCheckBox = (CheckBox)v. findViewById(R.id.check_box_enable_one_click_payment);
  //      i=(LinearLayout) v.findViewById(R.id.nameholder);
//        i.setVisibility(LinearLayout.GONE);
        bundle = getActivity().getIntent().getExtras();

        storeOneClickHash = bundle.getInt(PayuConstants.STORE_ONE_CLICK_HASH);
        if(storeOneClickHash == PayuConstants.STORE_ONE_CLICK_HASH_NONE)
            enableOneClickPaymentCheckBox.setVisibility(View.GONE);

        // lets get payment default params and hashes
        mPayuHashes = bundle.getParcelable(PayuConstants.PAYU_HASHES);
        mPaymentParams = bundle.getParcelable(PayuConstants.PAYMENT_PARAMS);
        payuConfig = bundle.getParcelable(PayuConstants.PAYU_CONFIG);
        payuConfig = null != payuConfig ? payuConfig : new PayuConfig();

        (amountTextView = (TextView)v. findViewById(R.id.text_view_amount)).setText("Amount" + ": " +"` "+ mPaymentParams.getAmount());
         amountTextView.setTypeface(tf);
        (transactionIdTextView = (TextView)v. findViewById(R.id.text_view_transaction_id)).setText("TXN ID" + ":" + mPaymentParams.getTxnId());

        payuUtils = new PayuUtils();


        cardNumberEditText.addTextChangedListener(new TextWatcher() {
            String issuer;
            Drawable issuerDrawable;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 5) { // to confirm rupay card we need min 6 digit.
                    if (null == issuer) issuer = payuUtils.getIssuer(charSequence.toString());
                    if (issuer != null && issuer.length() > 1 && issuerDrawable == null) {
                        issuerDrawable = getIssuerDrawable(issuer);
                        if (issuer.contentEquals(PayuConstants.SMAE)) { // hide cvv and expiry
                            cardExpiryMonthEditText.setVisibility(View.GONE);
                            cardExpiryYearEditText.setVisibility(View.GONE);
                            cardCvvEditText.setVisibility(View.GONE);
                        } else { //show cvv and expiry
                            cardExpiryMonthEditText.setVisibility(View.VISIBLE);
                            cardExpiryYearEditText.setVisibility(View.VISIBLE);
                            cardCvvEditText.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    issuer = null;
                    issuerDrawable = null;
                }
                cardNumberEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, issuerDrawable, null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

   /*     saveCardCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (saveCardCheckBox.isChecked()) {
                    i.setVisibility(LinearLayout.VISIBLE);
                } else {
                    i.setVisibility(LinearLayout.GONE);

                }
            }
        });*/
     //   Toast.makeText(v.getContext(), "TOAST 2", Toast.LENGTH_LONG).show();

        return v;


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null) {
            isViewShown = true;
            // fetchdata() contains logic to show data when page is selected mostly asynctask to fill the data
            //fetchData();
          //  Toast.makeText(getActivity(), "Toast2", Toast.LENGTH_SHORT).show();
        } else {
            isViewShown = false;
        }
    }


    @Override
    public void onClick(View v) {
        // Oh crap! Resource IDs cannot be used in a switch statement in Android library modules less... (Ctrl+F1)
        // Validates using resource IDs in a switch statement in Android library module
        // we cant not use switch and gotta use simple if else
        if (v.getId() == R.id.button_card_make_payment) {

            // do i have to store the card
        /*    if (saveCardCheckBox.isChecked()) {
                mPaymentParams.setStoreCard(1);
            }else{
                mPaymentParams.setStoreCard(0);
            }*/

            // do i have to store the cvv
          /*  if (enableOneClickPaymentCheckBox.isChecked()) {
                mPaymentParams.setEnableOneClickPayment(1);
            }else{
                mPaymentParams.setEnableOneClickPayment(0);
            }
*/

            // setup the hash
            mPaymentParams.setHash(mPayuHashes.getPaymentHash());

            // lets try to get the post params

            postData = null;
            // lets get the current card number;
            cardNumber = String.valueOf(cardNumberEditText.getText());
//            cardName = cardNameEditText.getText().toString();
            expiryMonth = cardExpiryMonthEditText.getText().toString();
            expiryYear = cardExpiryYearEditText.getText().toString();
            Cardnowithyear="20"+expiryYear;
            cvv = cardCvvEditText.getText().toString();

            // lets not worry about ui validations.
            mPaymentParams.setCardNumber(cardNumber);
           // mPaymentParams.setCardName(cardName);
        //    mPaymentParams.setNameOnCard(cardName);
            mPaymentParams.setExpiryMonth(expiryMonth);
            mPaymentParams.setExpiryYear(Cardnowithyear);
            mPaymentParams.setCvv(cvv);
            postData = new PaymentPostParams(mPaymentParams, PayuConstants.CC).getPaymentPostParams();
            if (postData.getCode() == PayuErrors.NO_ERROR) {
                // okay good to go.. lets make a transaction
                // launch webview
                payuConfig.setData(postData.getResult());
                Intent intent = new Intent(v.getContext(), PaymentsActivity.class);
                intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
                intent.putExtra(PayuConstants.STORE_ONE_CLICK_HASH, storeOneClickHash);
                startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
            } else {
             //   Toast.makeText(v.getContext(),"hello"+ postData.getResult(), Toast.LENGTH_LONG).show();
            }
        } else {
          //  Toast.makeText(v.getContext(),"hello2"+ postData.getResult(), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            Log.d("hello", String.valueOf(data));
            getActivity().  setResult(resultCode, data);
            getActivity().   finish();
        }
    }
    private Drawable getIssuerDrawable(String issuer){

        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            switch (issuer) {
                case PayuConstants.VISA:
                    return getResources().getDrawable(R.drawable.visa);
                case PayuConstants.LASER:
                    return getResources().getDrawable(R.drawable.laser);
                case PayuConstants.DISCOVER:
                    return getResources().getDrawable(R.drawable.discover);
                case PayuConstants.MAES:
                    return getResources().getDrawable(R.drawable.maestro);
                case PayuConstants.MAST:
                    return getResources().getDrawable(R.drawable.master);
                case PayuConstants.AMEX:
                    return getResources().getDrawable(R.drawable.amex);
                case PayuConstants.DINR:
                    return getResources().getDrawable(R.drawable.diner);
                case PayuConstants.JCB:
                    return getResources().getDrawable(R.drawable.jcb);
                case PayuConstants.SMAE:
                    return getResources().getDrawable(R.drawable.maestro);
                case PayuConstants.RUPAY:
                    return getResources().getDrawable(R.drawable.rupay);
            }
            return null;
        }else {

            switch (issuer) {
                case PayuConstants.VISA:
                    return getResources().getDrawable(R.drawable.visa, null);
                case PayuConstants.LASER:
                    return getResources().getDrawable(R.drawable.laser, null);
                case PayuConstants.DISCOVER:
                    return getResources().getDrawable(R.drawable.discover, null);
                case PayuConstants.MAES:
                    return getResources().getDrawable(R.drawable.maestro, null);
                case PayuConstants.MAST:
                    return getResources().getDrawable(R.drawable.master, null);
                case PayuConstants.AMEX:
                    return getResources().getDrawable(R.drawable.amex, null);
                case PayuConstants.DINR:
                    return getResources().getDrawable(R.drawable.diner, null);
                case PayuConstants.JCB:
                    return getResources().getDrawable(R.drawable.jcb, null);
                case PayuConstants.SMAE:
                    return getResources().getDrawable(R.drawable.maestro, null);
                case PayuConstants.RUPAY:
                    return getResources().getDrawable(R.drawable.rupay, null);
            }
            return null;
        }
    }
}
