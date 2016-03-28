package in.bille.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import java.util.List;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;



/**
 * Created by Vibhor Goel on 12/30/2015.
 */

public class BillDescriptionRecyclerAdapter extends RecyclerView.Adapter<BillListRowHolder> {

    private List<FeedItem> feedItemList;

    private Context mContext;

    public BillDescriptionRecyclerAdapter(Context context, List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;


    }

    @Override
    public BillListRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bill_description, parent, false);
        BillListRowHolder mh = new BillListRowHolder(v);


        return mh;
    }

    @Override
    public void onBindViewHolder(final BillListRowHolder holder, final int position) {
        final FeedItem feedItem = feedItemList.get(position);

        holder.itemName.setText(feedItem.getItemname());
        holder.itemQty.setText(feedItem.getItemquantity());
        holder.itemCost.setText(feedItem.getItemscost());
        holder.perItemCost.setText(feedItem.getItemprice());

    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }



}