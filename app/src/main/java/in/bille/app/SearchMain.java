package in.bille.app;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchMain extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ProgressDialog mProgressDialog;
    private static Context VContext;

    final Context context = this;
    private List<FeedItem> feedItemList = new ArrayList<FeedItem>();
    private RecyclerView xRecyclerView;
    private SearchRestaurantsRecylerAdapter adapter;
    private static final String TAG = "menu";
    private List<FeedItem> mModels;
    private ProgressBar progressBar;
    SessionManager session;

    private SearchView mSearchView;

    private String stringId = null;
    private String stringQty = null;
    private List<FeedItem> feedsList;
    String url ="";
    private List<FeedItem> filteredModelList;

    private Integer flag;

    Button checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Search Restaurant");

        session = new SessionManager(getApplicationContext());

        url = Config.serverurl+"search_merchant.php?limit=10&offset=0"+"&token="+SplashMain.tokenvalue+"&device_id="+SplashMain.regid;

        mSearchView = (SearchView)findViewById(R.id.searchView);

        setupSearchView();

        getIntent();

        VContext = getApplicationContext();
        xRecyclerView= (RecyclerView) findViewById(R.id.recycler_view_bill);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        HashMap<String, String> user = session.getUserDetails();


        new AsyncHttpTask().execute(url);




    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }







    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            Integer result = 0;
            HttpURLConnection urlConnection = null;

            try {
                /* forming th java.net.URL object */
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

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
                    System.out.println("this is search response" + response.toString());

                    parseResult(response.toString());


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

            setProgressBarIndeterminateVisibility(false);

            /* Download complete. Lets update UI */
            if (result == 1) {

                flag = 1;

                Log.d("feedlist adapter",feedItemList.toString());

                //adapter.notifyDataSetChanged();
                adapter = new SearchRestaurantsRecylerAdapter(SearchMain.this,feedItemList);

                xRecyclerView.setAdapter(adapter);
                //feedItemList.clear();

            } else {

                Log.e(TAG, "Failed to fetch data!");
            }
        }
    }

    private void parseResult(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);

            JSONArray list = jsonObj.getJSONArray("featured");

            if (null == feedItemList) {
                feedItemList = new ArrayList<FeedItem>();
            }


            for (int k = 0; k < list.length(); k++) {
                JSONObject post = list.optJSONObject(k);

                FeedItem item = new FeedItem();

                item.setId(post.optString("id"));
                item.setName(post.optString("name"));
                item.setPhone(post.optString("phone"));
                item.setDescription(post.optString("description"));
                item.setLocation(post.optString("location"));
                item.setDisc_amt(post.optString("discount"));
                item.setTnc(post.optString("tnc"));
                item.setLogo(post.optString("slider"));
                item.setSlider(post.optString("logo"));
                item.setCheckbox(post.optString("checkbox"));
                item.setSliderstring(post.optString("slider"));
                item.setDistance(post.optString("dist"));
                item.setFoodType(post.optString("food_type"));
                item.setFoodCost(post.optString("food_cost"));
                item.setTimings(post.optString("timings"));
                Log.d("nameAsync",post.optString("name"));

                feedItemList.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();


        }
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
        mSearchView.setSubmitButtonEnabled(false);

    }



    @Override
    public boolean onQueryTextChange(String query) {
        SharedPreferences sharedPrefs = getSharedPreferences("MyPrefsCreateBill", Context.MODE_PRIVATE);

/*        mSearchView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Put your call to the server here (with mQueryString)
            }
        }, 300);*/

        feedItemList.clear();

        Log.d("feedList",feedItemList.toString());
        if(flag == 1) {
            Log.d("filter", query);
            url = Config.serverurl + "search_merchant.php?limit=10&offset=0&query=" + query;

            new AsyncHttpTask().execute(url);

            adapter.notifyDataSetChanged();
            //filteredModelList = filter(feedsList, query);


        }

        flag = 0;

        // filteredModelList.clear();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }


}