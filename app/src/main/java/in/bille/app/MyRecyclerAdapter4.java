package in.bille.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyRecyclerAdapter4 extends RecyclerView.Adapter<FeedListRowHolder> {


    private List<FeedItem> feedItemList4;
    Connectiondetector cd;
     Boolean isInternetPresent= false;
    private Context mContext;

    public MyRecyclerAdapter4(Context context, List<FeedItem> feedItemList) {
        this.feedItemList4 = feedItemList;
        this.mContext = context;

    }

    @Override
    public FeedListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);
        FeedListRowHolder mh = new FeedListRowHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(FeedListRowHolder feedListRowHolder, int i) {
        FeedItem feedItem = feedItemList4.get(i);
        cd = new Connectiondetector(mContext.getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        if(!isInternetPresent) {
            feedListRowHolder.directionpointer.setVisibility(View.GONE);
            feedListRowHolder.dist.setVisibility(View.GONE);

        }


        Picasso.with(mContext).load(feedItem.getLogo())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(feedListRowHolder.logo);

        feedListRowHolder.id.setText(Html.fromHtml(feedItem.getId()));
        feedListRowHolder.name.setText(Html.fromHtml(feedItem.getName()));
        feedListRowHolder.phone.setText(Html.fromHtml(feedItem.getPhone()));
        feedListRowHolder.description.setText(Html.fromHtml(feedItem.getDescription()));
        feedListRowHolder.location.setText(Html.fromHtml(feedItem.getLocation()));
        feedListRowHolder.tnc.setText(Html.fromHtml(feedItem.getTnc()));
        feedListRowHolder.dist.setText(Html.fromHtml(feedItem.getDistance()));
        feedListRowHolder.sliderstringholder.setText(Html.fromHtml(feedItem.getSliderstring()));
        String SliderImage=feedItem.getSliderstring();
        Log.d("Slider string",SliderImage);


        String distance=feedItem.getDistance();
        final String hello= feedItem.getDisc_amt();
        if (hello.equals("null")){
            feedListRowHolder.disc_amt.setVisibility(TextView.INVISIBLE);}

        else{
                feedListRowHolder.disc_amt.setText(hello);
                feedListRowHolder.disc_amt.setVisibility(TextView.VISIBLE);}

        if (distance.equals("")){
            feedListRowHolder.directionpointer.setVisibility(View.GONE);
            feedListRowHolder.dist.setVisibility(View.GONE);
            }







        String canpay = feedItem.getCheckbox();
        if(canpay.matches("1"))
        {
            String a = "Payments Accepted";
            feedListRowHolder.checkbox.setText(a);
        }
        else
        {

            feedListRowHolder.checkbox.setVisibility(TextView.INVISIBLE);
        }





        final String Rid=feedItem.getId();
        final String Rname=feedItem.getName();
        final String Rphone=feedItem.getPhone();
        final String Rdescription=feedItem.getDescription();
        final String Rlocation=feedItem.getLocation();
        //final String Rdisc_amt=feedListRowHolder.disc_amt.getText().toString();
        final String Rtnc=feedListRowHolder.tnc.getText().toString();
        final String Rcheckbox=feedListRowHolder.checkbox.getText().toString();
        final String Rslider=feedListRowHolder.sliderstringholder.getText().toString();
        final String foodtype=feedItem.getFoodType();
        final String foodcost=feedItem.getFoodCost();
        final String timings=feedItem.getTimings();

        feedListRowHolder.setClickListener(new FeedListRowHolder.ClickListener()

        {
            @Override
            public void onClick(View v, int pos, boolean isLongClick) {
                if (isLongClick) {
                    // View v at position pos is long-clicked.
                } else {
                    Intent intent =  new Intent(mContext, Restaurant.class);
                    intent.putExtra("rid",Rid);
                    intent.putExtra("rname", Rname);
                    intent.putExtra("rphone",Rphone);
                    intent.putExtra("rdescription",Rdescription);
                    intent.putExtra("rdisc_amt",hello);
                    intent.putExtra("rlocation",Rlocation);
                    intent.putExtra("rtnc", Rtnc);
                    intent.putExtra("rcheckbox", Rcheckbox);
                    intent.putExtra("rslider", Rslider);
                    intent.putExtra("foodtype", foodtype);
                    intent.putExtra("foodcost", foodcost);
                    intent.putExtra("timings", timings);

                    Bundle extras = new Bundle();
                    mContext.startActivity(intent);

                    // View v at position pos is clicked.
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != feedItemList4 ? feedItemList4.size() : 0);
    }
}
