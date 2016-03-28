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

public class SearchRestaurantsRecylerAdapter extends RecyclerView.Adapter<RestaurantSearchRowHolder> {


    private List<FeedItem> feedItemList;

    private Context mContext;

    public SearchRestaurantsRecylerAdapter(Context context, List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public RestaurantSearchRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_search_restaurants, viewGroup,false);
        RestaurantSearchRowHolder mh = new RestaurantSearchRowHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(RestaurantSearchRowHolder holder, int i) {
        final FeedItem feedItem = feedItemList.get(i);

        Log.d("feedName", feedItem.getName());

        //RestaurantSearchRowHolder.id.setText(Html.fromHtml(feedItem.getId()));

        holder.name.setText(feedItem.getName());
        holder.location.setText(feedItem.getLocation());



        holder.setClickListener(new RestaurantSearchRowHolder.ClickListener() {
            @Override
            public void onClick(View v, int pos, boolean isLongClick) {
                if (isLongClick) {
                    // View v at position pos is long-clicked.
                } else {
                    final Intent intent;
                    String Rid = feedItem.getId();
                    String Rname = feedItem.getName();
                    String Rphone = feedItem.getPhone();
                    String Rdescription = feedItem.getDescription();
                    String Rdisc_amt = feedItem.getDisc_amt();
                    String Rlocation = feedItem.getLocation();
                    String Rtnc = feedItem.getTnc();
                    String Rcheckbox = feedItem.getCheckbox();
                    String Rslider = feedItem.getSliderstring();
                    final String foodtype=feedItem.getFoodType();
                    final String foodcost=feedItem.getFoodCost();
                    final String timings=feedItem.getTimings();

                    intent =  new Intent(mContext, Restaurant.class);
                    intent.putExtra("rid",Rid);
                    intent.putExtra("rname",Rname);
                    intent.putExtra("rphone",Rphone);
                    intent.putExtra("rdescription",Rdescription);
                    intent.putExtra("rdisc_amt",Rdisc_amt);
                    intent.putExtra("rlocation", Rlocation);
                    intent.putExtra("rtnc", Rtnc);
                    intent.putExtra("rcheckbox", Rcheckbox);
                    intent.putExtra("rslider", Rslider);
                    intent.putExtra("foodtype", foodtype);
                    intent.putExtra("foodcost", foodcost);
                    intent.putExtra("timings", timings);
                    Bundle extras = new Bundle();
                    mContext.startActivity(intent);
                    //Log.d(TAG, " slider image link " + Rslider);

                    // View v at position pos is clicked.
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }
}
