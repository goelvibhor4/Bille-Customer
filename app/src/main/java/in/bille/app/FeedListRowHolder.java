package in.bille.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedListRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
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
    protected TextView dist;
    protected TextView checkbox;
    protected ImageView logo;
    protected ImageView directionpointer;

    private ClickListener clickListener;

    public FeedListRowHolder(View view) {
        super(view);
        this.id = (TextView) view.findViewById(R.id.id);
        this.name = (TextView) view.findViewById(R.id.name);
        this.phone = (TextView) view.findViewById(R.id.phone);
        this.description = (TextView) view.findViewById(R.id.description);
        this.location = (TextView) view.findViewById(R.id.location);
        this.disc_amt = (TextView) view.findViewById(R.id.disc_amt);
        this.tnc = (TextView) view.findViewById(R.id.tnc);
        this.checkbox = (TextView) view.findViewById(R.id.checkbox);
        this.dist = (TextView) view.findViewById(R.id.distance);
        this.sliderstringholder = (TextView) view.findViewById(R.id.sliderstring);
        this.logo = (ImageView) view.findViewById(R.id.Img_logo);
        this.directionpointer = (ImageView) view.findViewById(R.id.navigatebutton);



        context = itemView.getContext();
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);

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

    @Override
    public boolean onLongClick(View v) {

        clickListener.onClick(v, getPosition(), true);

        return true;
    }




}