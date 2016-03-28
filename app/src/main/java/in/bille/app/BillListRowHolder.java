package in.bille.app;

/**
 * Created by Vibhor Goel on 12/30/2015.
 */
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class BillListRowHolder extends RecyclerView.ViewHolder {

    protected TextView itemName;
    protected TextView itemCost;
    protected TextView perItemCost;
    protected TextView itemQty;


    public BillListRowHolder(View view) {
        super(view);
        this.itemName = (TextView) view.findViewById(R.id.textView_menulist);
        this.itemCost = (TextView) view.findViewById(R.id.textView_price);
        this.perItemCost = (TextView) view.findViewById(R.id.textView_itemPrice);
        this.itemQty = (TextView) view.findViewById(R.id.textView_itemQty);

    }
}