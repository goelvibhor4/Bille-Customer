package in.bille.app;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import in.bille.app.Database.DatabaseHandler;


public class Drawer_activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener,
        ResultCallback<LocationSettingsResult> {
    protected static final String TAG = "Drawer activty";
    private static Context VContext;
    private List<FeedItem> feedItemList4 = new ArrayList<FeedItem>();
    private RecyclerView xRecyclerView;
    private MyRecyclerAdapter4 adapter4;
    TextView Drawername,Draweremail,Drawerphone;
    Intent intent;
    Drawer_activity ma;
    public static Activity da;

    ProgressDialog mProgressDialog;

    SessionManager session;
    TextView updatelocationholder;
    Geocoder geocoder;
    List<Address> addresses;
    Connectiondetector cd;
    Boolean isInternetPresent= false;

String Error= "";
    /**
     * Constant used in the location settings dialog.
     */
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 100000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    protected final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    protected final static String KEY_LOCATION = "location";
    protected final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    protected LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;

    // UI Widgets.
    protected ImageButton mStartUpdatesButton;
    //   protected Button mStopUpdatesButton;
    protected TextView mLastUpdateTimeTextView;
    protected TextView mLatitudeTextView;
    protected TextView mLongitudeTextView;
    ImageView img;
    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;

Boolean x=false;
    Cursor y;
String UserID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_activity);
        session = new SessionManager(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Home");
        da=this;
        updatelocationholder =   (TextView) findViewById( R.id.location );
        ma=Drawer_activity.this;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_drawer_activity, null);
        navigationView.addHeaderView(header);
        cd = new Connectiondetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        Drawername = (TextView)header.findViewById(R.id.Name);
        Draweremail    = (TextView)header.findViewById(R.id.Email);
        Drawerphone = (TextView)header.findViewById(R.id.Phone);

     /*   DatabaseHandler db = new DatabaseHandler(this);

        // Inserting Cards
        Log.d("Insert: ", "Inserting ..");
        db.addCard(new CardFunctions("mogambo","9100000000","5123456789012346","12","2018"));
      *//*  db.addCard(new CardFunctions("Srinivas", "9199999999"));
        db.addCard(new CardFunctions("Tommy", "9522222222"));
        db.addCard(new CardFunctions("Karthik", "9533333333"));
*//*
        // Reading all cards
        Log.d("Reading: ", "Reading all cards..");
        List<CardFunctions> cards = db.getAllCards();*/




            boolean check = session.checkLogin();
        if(check)
        {
            Log.d("Came here", "Came here");

            this.finish();
        }
        else
        {
            Log.d("Came here 2", "Came here 2");

            // session.checkLogin();
        }

        HashMap<String, String> user = session.getUserDetails();

        String UserName = user.get(SessionManager.KEY_Name);
        String UserEmail = user.get(SessionManager.KEY_Email);
        String UserPhone = user.get(SessionManager.KEY_PHONE);
        UserID = user.get(SessionManager.KEY_ID);

        Log.d("Email",""+UserEmail);
        Log.d("Name", "" + UserName);
        Log.d("phone", "" + UserPhone);
        Log.d("USERID", "" + UserID);

        Drawername.setText(UserName);
        Draweremail.setText(UserEmail);
        Drawerphone.setText(UserPhone);

        Intent intent = getIntent();
        String activity = intent.getStringExtra("activity");
        String location = intent.getStringExtra("location");
        String latitute = intent.getStringExtra("lati");
        String longitude = intent.getStringExtra("longi");
        String address = intent.getStringExtra("address");
        String nolocation = intent.getStringExtra("nolocation");
       // String billdescription = intent.getStringExtra("Billdescription");



// Locate the UI widgets.
        mStartUpdatesButton = (ImageButton) findViewById(R.id.mStartUpdatesButton);
        //   mStopUpdatesButton = (Button) findViewById(R.id.stop_updates_button);
        mLatitudeTextView = (TextView) findViewById(R.id.latitude_text);
        mLongitudeTextView = (TextView) findViewById(R.id.longitude_text);
        mLastUpdateTimeTextView = (TextView) findViewById(R.id.last_update_time_text);

        Resources r = getResources();



        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle.


        if (location != null && !location.isEmpty())

        {
            String url6 =Config.serverurl+"search_list_offers.php?place="+location+"&token="+SplashMain.tokenvalue+"&device_id="+SplashMain.regid;
            Log.d("url is here",url6);

          //  Toast.makeText(ma, location, Toast.LENGTH_SHORT).show();

            url6 = url6.replaceAll(" ", "%20");
            Log.d("this is url", url6);
            new AsyncHttpTask4().execute(url6);
            updatelocationholder.setText(location);
        } else  if (latitute != null && !latitute.isEmpty())

        {    //  Toast.makeText(ma, latitute, Toast.LENGTH_SHORT).show();
            final String url4 = Config.serverurl+"location_list_offers.php?latitude="+latitute+"&longitude="+longitude+"&token="+SplashMain.tokenvalue+"&device_id="+SplashMain.regid;
            //Toast.makeText(ma, url4, Toast.LENGTH_SHORT).show();
            Log.d("this is url", url4);

            new AsyncHttpTask4().execute(url4);

            updatelocationholder.setText(address);
        }
        else  if (nolocation != null && !nolocation.isEmpty())

        {     // Toast.makeText(ma, nolocation, Toast.LENGTH_SHORT).show();
            final String url3 = Config.serverurl+"featured_offers.php"+"&token="+SplashMain.tokenvalue+"&device_id="+SplashMain.regid;
          //  Toast.makeText(ma, url3, Toast.LENGTH_SHORT).show();
            new AsyncHttpTask4().execute(url3);
            Log.d("this is url" , url3);
            updatelocationholder.setText("Select location");
        }


        else{
            Log.d("i am here", "i am here");
            updateValuesFromBundle(savedInstanceState);
            buildGoogleApiClient();
            createLocationRequest();
            buildLocationSettingsRequest();
            mGoogleApiClient.connect();

        }


        VContext = getApplicationContext();
        xRecyclerView= (RecyclerView) findViewById(R.id.fourth_recycler_view);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        GoogleAnalytics analytics = GoogleAnalytics.getInstance(VContext);


        Tracker tracker = analytics.newTracker("UA-73635301-1");

        // All subsequent hits will be send with screen name = "main screen"
        tracker.setScreenName("Home Screen");

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("UX")
                .setAction("click")
                .setLabel("submit")
                .build());


        // final String url5 ="http://54.68.65.111/mozipper/mongo_api/featured_offers.php";

    }

    public void click(View v) {

        switch(v.getId()) {
            case R.id.TitleBar:

                if(!isInternetPresent) {
                     new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            Drawer_activity.this);

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
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
            }, 1000);}

                else if(isInternetPresent) {

                    intent = new Intent(this,Search.class);
                    startActivity(intent);

                }
                break;



        }

    }

    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
            updateUI();
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the f+++++++++++++++++++++++7-
        // astest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);



    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        //**************************
        //this is the key ingredient
        //**************************
    }

    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
     */
    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }

    /**
     * The callback invoked when
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} is called. Examines the
     * {@link com.google.android.gms.location.LocationSettingsResult} object and determines if
     * location settings are adequate. If they are not, begins the process of presenting a location
     * settings dialog to the user.
     */
    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    if(!isInternetPresent) {
                        Log.d("Internet np", "np");
                    }else if(isInternetPresent) {
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                                "upgrade location settings ");
                    status.startResolutionForResult(Drawer_activity.this, REQUEST_CHECK_SETTINGS);}
                }
                catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }

    /**
     * Handles the Start Updates button and requests start of location updates. Does nothing if
     * updates have already been requested.
     */
    public void startUpdatesButtonHandler(View view) {
        checkLocationSettings();
    }

    /**
     * Handles the Stop Updates button, and requests removal of location updates.
     */


    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = true;
                startLocationUpdates();


            }
        });

    }

    /**
     * Updates all UI fields.
     */
    private void updateUI() {
        updateLocationUI();
    }



    /**
     * Sets the value of the UI fields for the location latitude, longitude and last update time.
     */
    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            mLatitudeTextView.setText(String.valueOf(mCurrentLocation.getLatitude()));
            mLongitudeTextView.setText(String.valueOf(mCurrentLocation.getLongitude()));
            mLastUpdateTimeTextView.setText(mLastUpdateTime);
            Log.i(TAG, "we are here now.");

            String lati=mLatitudeTextView.getText().toString();
            String longi =mLongitudeTextView.getText().toString();

            double lat = Double.parseDouble(mLatitudeTextView.getText().toString());
            double lon= Double.parseDouble(mLongitudeTextView.getText().toString());


            Log.d("latitute", "Value: " +lati);
            Log.d("longitude", "Value: " +longi );


            geocoder = new Geocoder(Drawer_activity.this, Locale.ENGLISH);
            try {
                addresses = geocoder.getFromLocation(lat,lon, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuilder str = new StringBuilder();
            if (geocoder.isPresent()) {
                try{
                   /* Toast.makeText(getApplicationContext(),
                            "geocoder present", Toast.LENGTH_SHORT).show();*/
                    String address = addresses.get(0).getAddressLine(1);
                    updatelocationholder.setText(address);
                }
                catch (Exception ignored){
                    /*Toast.makeText(getApplicationContext(),
                            "geocoder absent", Toast.LENGTH_SHORT).show();*/
                    updatelocationholder.setText("Select location");
                    // after a while, Geocoder start to throw "Service not available" exception. really weird since it was working before (same device, same Android version etc..
                }
            }


            final String url3 = Config.serverurl+"location_list_offers.php?latitude="+lati+"&longitude="+longi+"&token="+SplashMain.tokenvalue+"&device_id="+SplashMain.regid;
            feedItemList4.clear();

            new AsyncHttpTask4().execute(url3);

        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,
                this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = false;
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            System.out.print("location"+mCurrentLocation);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateLocationUI();
        }
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateLocationUI();
        Toast.makeText(this, getResources().getString(R.string.location_updated_message),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    /**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }


    public class AsyncHttpTask4 extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(Drawer_activity.this,R.style.AppTheme_Dark_Dialog);
            // Set progressdialog title
            mProgressDialog.setMessage("Fetching Restaurants");
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
                URL url4 = new URL(params[0]);

                urlConnection = (HttpURLConnection) url4.openConnection();

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

                    parseResult4(response.toString());


                    result = 1; // Successful

                }else{
                    result = 0; //"Failed to fetch data!"// ;

                }

            } catch (Exception e) {


                Log.d(TAG, e.getLocalizedMessage());
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

            /* Download complete. Lets update UI */
            if (result == 1) {

                adapter4 = new MyRecyclerAdapter4(Drawer_activity.this,feedItemList4);
                xRecyclerView.setAdapter(adapter4);
            } else {

                SharedPreferences sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPrefs.getString(TAG, null);
                //feedsList = new ArrayList<>();
                Type type = new TypeToken<ArrayList<FeedItem>>() {}.getType();
                List<FeedItem> feedItemList4 = gson.fromJson(json,type);

                adapter4 = new MyRecyclerAdapter4(Drawer_activity.this, feedItemList4);
                xRecyclerView.setAdapter(adapter4);

                Toast.makeText(Drawer_activity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();

                Log.e(TAG, "Failed to fetch data!");
            }
        }
    }

    private void parseResult4(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            if(jsonObj != null){
                JSONArray list = jsonObj.getJSONArray("featured");

                if (null == feedItemList4) {
                    feedItemList4 = new ArrayList<FeedItem>();
                }


                for (int k = 0; k < list.length(); k++) {
                    JSONObject post4 = list.optJSONObject(k);

                    FeedItem item = new FeedItem();
                    item.setId(post4.optString("id"));
                    item.setName(post4.optString("name"));
                    item.setPhone(post4.optString("phone"));
                    item.setDescription(post4.optString("description"));
                    item.setLocation(post4.optString("location"));
                    item.setDisc_amt(post4.optString("discount"));
                    item.setTnc(post4.optString("tnc"));
                    item.setLogo(post4.optString("slider"));
                    item.setSlider(post4.optString("logo"));
                    item.setCheckbox(post4.optString("payment_mode"));
                    item.setSliderstring(post4.optString("slider"));
                    item.setDistance(post4.optString("dist"));
                    item.setFoodType(post4.optString("food_type"));
                    item.setFoodCost(post4.optString("food_cost"));
                    item.setTimings(post4.optString("timings"));

                    feedItemList4.add(item);


                    SharedPreferences sharedPrefs = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Gson gson = new Gson();

                    String json = gson.toJson(feedItemList4);
                    Log.d("offline", "" + json);
                    editor.putString(TAG, json);
                    editor.commit();


                }
            }
        }

        catch (JSONException e) {
            e.printStackTrace();


        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_search) {
            Intent int2 = new Intent(Drawer_activity.this,SearchMain.class);
            startActivity(int2);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.ReachHome) {

            // Handle the camera action
        }/* else if (id == R.id.nav_profile) {
            Intent int2 = new Intent(Drawer_activity.this,EditProfile.class);
            startActivity(int2);

        } */else if (id == R.id.nav_slideshow) {
            Intent int2 = new Intent(Drawer_activity.this,Offers.class);
            startActivity(int2);

        } else if (id == R.id.nav_manage) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Here is the share content body";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        } else if (id == R.id.nav_share) {

            Intent int2 = new Intent(Drawer_activity.this,MyBills.class);
            startActivity(int2);
            Drawer_activity.this.finish();
        }
  /*      else if (id == R.id.add_card) {

            Intent int3 = new Intent(Drawer_activity.this,Cards.class);
            startActivity(int3);

        }*/

        else if (id == R.id.nav_aboutus) {
            Intent int2 = new Intent(Drawer_activity.this,AboutUs.class);
            startActivity(int2);
        }
        else if (id == R.id.nav_send) {
            if(!isInternetPresent) {

                Toast.makeText(Drawer_activity.this, "Internet not present.", Toast.LENGTH_SHORT).show();


            }


            else if(isInternetPresent) {

                final String logout =Config.serverurl+"logout.php?id="+UserID+"&reg_id="+SplashMain.regid+"&token="+SplashMain.tokenvalue+"&device_id="+SplashMain.regid;
                new LogoutAsync().execute(logout);
                session.logoutUser();
            Drawer_activity.this.finish();
            }


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class LogoutAsync extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(Drawer_activity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Loading");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            // Show progressdialog
            mProgressDialog.show();        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    Log.d("str", response.toString());
                    Logoutfunction(response.toString());
                    result = 1; // Successful

                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                //Log.d(TAG, e.getLocalizedMessage());
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


                Toast.makeText(Drawer_activity.this, "Succesffully logged out", Toast.LENGTH_SHORT).show();


            }

            else {
                Toast.makeText(Drawer_activity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void Logoutfunction(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);



            Error = jsonObj.optString("error");






        }

        catch (JSONException e) {
            e.printStackTrace();


        }
    }




}
