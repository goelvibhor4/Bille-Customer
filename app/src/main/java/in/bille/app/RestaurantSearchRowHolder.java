package in.bille.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RestaurantSearchRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = RestaurantSearchRowHolder.class.getSimpleName();
    private final Context context;

    protected TextView name;

    protected TextView location;

    private ClickListener clickListener;

    public RestaurantSearchRowHolder(View view) {
        super(view);

        this.name = (TextView) view.findViewById(R.id.rName);
        this.location = (TextView) view.findViewById(R.id.rLocation);



        context = itemView.getContext();
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