package in.bille.app;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by pulkit-mac on 1/19/16.
 */
public class SearchMainRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //  protected ImageView thumbnail;
    protected TextView rName;
    protected TextView rLocation;

    private ClickListener clickListener;

    //protected Button sendBill;

    public SearchMainRowHolder(View view) {
        super(view);


        this.rName = (TextView) view.findViewById(R.id.rName);
        this.rName = (TextView) view.findViewById(R.id.rLocation);
        view.setOnClickListener(this);

    }


    public interface ClickListener {

        /**
         * Called when the view is clicked.
         *
         * @param v view that is clicked
         * @param position of the clicked item
         * @param isLongClick true if long click, false otherwise
         */
        public void onClick(View v, int position, boolean isLongClick);

    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {

        clickListener.onClick(v, getPosition(), false);

    }

}



