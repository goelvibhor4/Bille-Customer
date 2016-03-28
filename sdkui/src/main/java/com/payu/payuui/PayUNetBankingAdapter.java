package com.payu.payuui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.payu.india.Model.PaymentDetails;

import java.util.ArrayList;


class PayUNetBankingAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<PaymentDetails> mNetBankingList;

    public PayUNetBankingAdapter(Context context, ArrayList<PaymentDetails> netBankingList) {
        mContext = context;
        mNetBankingList = netBankingList;
    }

    @Override
    public int getCount() {
        return mNetBankingList.size();
    }

    @Override
    public Object getItem(int i) {
        if(null != mNetBankingList) return mNetBankingList.get(i);
        else return 0;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NetbankingViewHolder netbankingViewHolder = null;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.netbanking_list_item, null);
            netbankingViewHolder = new NetbankingViewHolder(convertView);
            convertView.setTag(netbankingViewHolder);
        } else {
            netbankingViewHolder = (NetbankingViewHolder) convertView.getTag();
        }

        PaymentDetails paymentDetails = mNetBankingList.get(position);

        // set text here
        netbankingViewHolder.netbankingTextView.setText(paymentDetails.getBankName());
        return convertView;
    }


    class NetbankingViewHolder {
        TextView netbankingTextView;

        NetbankingViewHolder(View view) {
            netbankingTextView = (TextView) view.findViewById(R.id.text_view_netbanking);
        }
    }
}




