package in.bille.app;

/**
 * Created by Vibhor Goel on 11/14/2015.
 */


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MyBillsRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = FeedListRowHolder.class.getSimpleName();

    private final Context context;
    protected TextView id;
    protected TextView name;
    protected TextView phone;
    protected TextView description;
    protected TextView location;
    protected TextView disc_amt;
    protected TextView tnc;
    protected TextView sliderstringholder;
    protected TextView billid;
    protected TextView order;
    protected TextView quantity;
    protected TextView amount;
    protected TextView total2;
    protected TextView total;
    protected TextView status;
    protected TextView date;
    protected ImageView statusimage;
    protected TextView Customerbillid;
    protected TextView checkbox;

    Connectiondetector cd;
    Boolean isInternetPresent= false;

    public MyBillsRowHolder(View view) {
        super(view);
        this.id = (TextView) view.findViewById(R.id.id);
        this.name = (TextView) view.findViewById(R.id.name);
        this.phone = (TextView) view.findViewById(R.id.phone);
        this.description = (TextView) view.findViewById(R.id.description);
        this.location = (TextView) view.findViewById(R.id.adressplaceholder);
        this.disc_amt = (TextView) view.findViewById(R.id.discountdata);
        this.tnc = (TextView) view.findViewById(R.id.tnc);
        this.checkbox = (TextView) view.findViewById(R.id.checkbox);
        this.sliderstringholder = (TextView) view.findViewById(R.id.sliderstring);
        this.billid = (TextView) view.findViewById(R.id.orderid);
        this.order = (TextView) view.findViewById(R.id.orderitems);
        this.quantity = (TextView) view.findViewById(R.id.itemsquantity);
        this.amount = (TextView) view.findViewById(R.id.subtotaldata);
        this.total2 = (TextView) view.findViewById(R.id.totalamount);
        this.total = (TextView) view.findViewById(R.id.totalamountdata);
        this.status = (TextView) view.findViewById(R.id.paidstatus);
        this.date = (TextView) view.findViewById(R.id.Date);
        this.statusimage = (ImageView) view.findViewById(R.id.statusimage);
        this.Customerbillid = (TextView) view.findViewById(R.id.customerorderid);

        context = itemView.getContext();
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        cd = new Connectiondetector(context.getApplicationContext());
        //isInternetPresent = cd.isConnectingToInternet();
    }
    @Override
    public void onClick(View v) {
        isInternetPresent = cd.isConnectingToInternet();
if (isInternetPresent){
        final Intent intent;
        Log.d(TAG, "Item clicked at position " + getPosition());
    /*    String Rname=name.getText().toString();
        String Rphone=phone.getText().toString();
        String Rdescription=description.getText().toString();
        String Rlocation=location.getText().toString();
        String Rdisc_amt=disc_amt.getText().toString();
        String Rtnc=tnc.getText().toString();
        String Rcheckbox=checkbox.getText().toString();
        String Rslider=sliderstringholder.getText().toString();*/
        String BillId=billid.getText().toString();
        String CustomerBillId=Customerbillid.getText().toString();
        String PaidStatus=status.getText().toString();

        intent =  new Intent(context, BillSummary.class);
        intent.putExtra("Bill id",BillId);
        intent.putExtra("Customer_Bill_id",CustomerBillId);
        intent.putExtra("paymentstatus",PaidStatus);

        context.startActivity(intent);}
        else{

    Toast.makeText(context.getApplicationContext(),"Internet not present",Toast.LENGTH_SHORT).show();

}

    }
    @Override
    public boolean onLongClick(View v) {
        Log.d(TAG, "Item long-clicked at position " + getPosition());
        return true;
    }




}
