package in.bille.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;


import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nineoldandroids.view.ViewHelper;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Restaurant extends AppCompatActivity  implements ObservableScrollViewCallbacks  {
    private static Context VContext;
   private ImageView mImageView;
    private View mToolbarView;
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;
    ProgressDialog mProgressDialog;
    String Error= "", Message= "";
    Connectiondetector cd;

    ImageButton notificationwhite,notificationyellow;
TextView getnotified;
    Boolean isInternetPresent= false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        // getting intent data
        Intent in = getIntent();
        cd = new Connectiondetector(getApplicationContext());

        isInternetPresent = cd.isConnectingToInternet();

        // Get JSON values from previous intent
        String Restaurantname = in.getStringExtra("rname");
        final String rphone = in.getStringExtra("rphone");
        String Sdescription = in.getStringExtra("rdescription");
        String Soffer = in.getStringExtra("rdisc_amt");
        FrameLayout layout = (FrameLayout)findViewById(R.id.DiscountFrame);
        Log.d("offers",Soffer);

        if(Soffer.equals("null"))
        {
            layout.setVisibility(View.GONE);

        }
        String Rlocation = in.getStringExtra("rlocation");
        final String Stnc = in.getStringExtra("rtnc");
        final String Sid = in.getStringExtra("rid");
        String Rcheckbox = in.getStringExtra("rcheckbox");
        String Rsliderimage = in.getStringExtra("rslider");
        mImageView = (ImageView)findViewById(R.id.image);
        Picasso.with(VContext).load(Rsliderimage)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(mImageView);

        Log.d("this is rslider",Rsliderimage);
        String foodtype = in.getStringExtra("foodtype");
        String foodcost = in.getStringExtra("foodcost");
        String timings = in.getStringExtra("timings");
        mToolbarView = findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(null);


        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.primary)));


        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);

        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);

        VContext = getApplicationContext();
        TextView id = (TextView) findViewById(R.id.id);
        TextView name = (TextView) findViewById(R.id.body);
        final TextView phone = (TextView) findViewById(R.id.phone);
        TextView Offer = (TextView) findViewById(R.id.offerdata);
        final TextView tnc = (TextView) findViewById(R.id.tnc);
        TextView checkbox = (TextView) findViewById(R.id.checkbox);

        TextView timing = (TextView)findViewById(R.id.TimeBulletPointsdata);
        TextView price = (TextView)findViewById(R.id.rupeeBulletPointsdata);
        TextView pos = (TextView)findViewById(R.id.posBulletPointsdata);
        TextView waiter = (TextView)findViewById(R.id.WaiterBulletPointsdata);

        getnotified = (TextView) findViewById(R.id.GetNotifiedText1);
        notificationwhite= (ImageButton) findViewById(R.id.GetNotified2);
        notificationyellow=(ImageButton) findViewById(R.id.GetNotified3);
        Log.d("Rcheck",""+Rcheckbox);

        if(Rcheckbox.matches("Payments Accepted"))
        {
            String a = "Payments Accepted";
            pos.setText(a);
        }
        else
        {
            String a = "Payments Not Accepted";
            pos.setText(a);
        }

        TextView description = (TextView) findViewById(R.id.descriptiondata);
        TextView tandc = (TextView) findViewById(R.id.TncHolder);
        final TextView locationT = (TextView) findViewById(R.id.Addressdata);
        ImageButton Calling = (ImageButton) findViewById(R.id.callbutton2);
        ImageButton Map = (ImageButton)findViewById(R.id.MapButton);
        ImageButton Bill = (ImageButton)findViewById(R.id.BillButton2);


     //   Picasso.with(VContext).load(Rsliderimage).into((ImageView) mImageView);

        View overlay = (View) findViewById(R.id.overlay);
        int opacity = 75; // from 0 to 255
        overlay.setBackgroundColor(opacity * 0x1000000); // black with a variable alpha
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, 580);
        overlay.setLayoutParams(params);
        overlay.invalidate(); // update the view


        name.setText(Restaurantname);
        phone.setText(rphone);
        description.setText(Sdescription);
        locationT.setText(Rlocation);
        Offer.setText(Soffer);

        waiter.setText(foodtype);
        timing.setText(timings);
        price.setText(foodcost);



//        tnc.setText(Stnc);

        OnClickListener calllistner = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone.getText()));
                startActivity(intent);
            }
        };

        Calling.setOnClickListener(calllistner);

        OnClickListener maplistner = new OnClickListener() {
            @Override
            public void onClick(View v) {


                String url = "http://maps.google.com/maps?f=d&daddr=" + locationT.getText();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        };

        Map.setOnClickListener(maplistner);


        OnClickListener billlistner = new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isInternetPresent) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                                    Restaurant.this);

                            // set title
                            alertDialogBuilder.setTitle("Ahh, No Internet?");

                            // set dialog message
                            alertDialogBuilder
                                    .setMessage("Looks like you have lost it")
                                    .setCancelable(false)
                                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                            startActivity(getIntent());
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // if this button is clicked, just close
                                            // the dialog box and do nothing
                                            dialog.cancel();

                                        }
                                    });

                            // create alert dialog
                            android.app.AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();
                        }
                    }, 1000);}

                else if(isInternetPresent) {

                Intent otherIntent = new Intent(Restaurant.this,BillDescription.class);
                otherIntent.putExtra("merchid", Sid);

                startActivity(otherIntent);}
            }
        };

        Bill.setOnClickListener(billlistner);


        tandc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int2 = new Intent(Restaurant.this, TermsAndConditionsSlide.class);
                int2.putExtra("TERMS AND CONDITIONS", Stnc);
                startActivity(int2);
            }
        });



        notificationwhite.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Get Notified");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Do you want to subscribe to notification from this restaurant")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                final String url6 =Config.serverurl+"get_notified.php?m_id=56519907b180752d210041a8&u_id=566fa64eb1807535050041a7"+"&token="+SplashMain.tokenvalue+"&device_id="+SplashMain.regid;;
                                new callfornoti().execute(url6);


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }

        });

        GoogleAnalytics analytics = GoogleAnalytics.getInstance(VContext);


        Tracker tracker = analytics.newTracker("UA-73635301-1");

        // All subsequent hits will be send with screen name = "main screen"
        tracker.setScreenName("Restaurant "+Restaurantname);

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("UX")
                .setAction("click")
                .setLabel("submit")
                .build());






    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_activity, menu);
        return true;
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.primary);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        ViewHelper.setTranslationY(mImageView, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                super.onBackPressed();
                break;
            case  R.id.menu_search:
                Intent int2 = new Intent(Restaurant.this,SearchMain.class);
                startActivity(int2);
                break;

        }
        return super.onOptionsItemSelected(item);


    }
    public class callfornoti extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {

            mProgressDialog = new ProgressDialog(Restaurant.this);
            // Set progressdialog title

            // Set progressdialog message
            mProgressDialog.setMessage("Subscribing...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            Integer result = 0;
            HttpURLConnection urlConnection = null;

            try {
                /* forming th java.net.URL object */
                URL url6 = new URL(params[0]);

                urlConnection = (HttpURLConnection) url6.openConnection();

                /* for Get request */
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
                if (statusCode ==  200) {
                    System.out.println("Status code is:" + statusCode);
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println("this is response" + response.toString());

                    callfornotifunction(response.toString());


                    result = 1; // Successful

                }else{
                    result = 0; //"Failed to fetch data!"// ;

                }

            } catch (Exception e) {


                Log.d("message", e.getLocalizedMessage());
            }

            return result; //"Failed to fetch data!";
        }
        @Override
        protected void onPostExecute(Integer result) {
            try {
                if ((mProgressDialog != null) &&  mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            } catch (final IllegalArgumentException e) {
                // Handle or log or ignore
            } catch (final Exception e) {
                // Handle or log or ignore
            } finally {
                mProgressDialog = null;
            }
            if (result == 1) {

                if(Error.matches("false")&&(Message.matches("Notified"))) {
                    Toast.makeText(Restaurant.this, "Notification Subscribed !", Toast.LENGTH_SHORT).show();
                    getnotified.setText("Notified");
                    notificationwhite.setVisibility(View.GONE);
                    notificationyellow.setVisibility(View.VISIBLE);

                }  else {


                }


            }

            else {
                Toast.makeText(Restaurant.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void callfornotifunction(String result) {
        try {
            Log.d("this is result",result);
            JSONObject jsonObject= new JSONObject(result);

            Error = jsonObject.getString("error");
            Message = jsonObject.getString("msg");




        }

        catch (JSONException e) {
            e.printStackTrace();


        }
    }


}
