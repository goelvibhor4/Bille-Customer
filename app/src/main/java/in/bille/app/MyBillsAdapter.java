package in.bille.app;

/**
 * Created by Vibhor Goel on 11/14/2015.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MyBillsAdapter extends RecyclerView.Adapter<MyBillsRowHolder> {


    private List<FeedItem> myBillsRowHolder5;

    private Context mContext;

    public MyBillsAdapter(Context context, List<FeedItem> billItemList) {
        this.myBillsRowHolder5 = billItemList;
        this.mContext = context;
    }

    @Override
    public MyBillsRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mybills_list, viewGroup,false);
        MyBillsRowHolder mh = new MyBillsRowHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(MyBillsRowHolder myBillsRowHolder, int i) {
        FeedItem billItem = myBillsRowHolder5.get(i);




        myBillsRowHolder.billid.setText(Html.fromHtml(billItem.getBillid()));
        myBillsRowHolder.location.setText(Html.fromHtml(billItem.getLocation()));
        myBillsRowHolder.order.setText(Html.fromHtml(billItem.getOrder()));
        myBillsRowHolder.quantity.setText(Html.fromHtml(billItem.getQuantity()));
        myBillsRowHolder.amount.setText(Html.fromHtml(billItem.getAmount()));
        myBillsRowHolder.total.setText(Html.fromHtml(billItem.getTotalamount()));
        myBillsRowHolder.total2.setText(Html.fromHtml(billItem.getTotalamount()));
        myBillsRowHolder.disc_amt.setText(Html.fromHtml(billItem.getDisc_amt()));
        myBillsRowHolder.date.setText(Html.fromHtml(billItem.getDate()));
        myBillsRowHolder.name.setText(Html.fromHtml(billItem.getMerchantname()));
        myBillsRowHolder.Customerbillid.setText(Html.fromHtml(billItem.getCustomerBillId()));

        //     myBillsRowHolder.status.setText(Html.fromHtml(billItem.getStatus()));

        final String BillStatus= billItem.getStatus();

        if (BillStatus=="1"){

            myBillsRowHolder.status.setText("PAID");
            myBillsRowHolder.statusimage.setImageResource(R.drawable.ok24);

        }
        else    if (BillStatus=="0"){

            myBillsRowHolder.status.setText("PENDING");
            myBillsRowHolder.statusimage.setImageResource(R.drawable.cancel24);

        }

    }

    @Override
    public int getItemCount() {
        return (null != myBillsRowHolder5 ? myBillsRowHolder5.size() : 0);
    }
}
