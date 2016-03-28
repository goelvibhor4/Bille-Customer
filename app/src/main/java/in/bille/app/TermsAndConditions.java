package in.bille.app;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

public class TermsAndConditions extends AppCompatActivity {


    WebView wv;
    Integer flagval;
    String url="",title="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);
        Intent intent = getIntent();
        flagval =Integer.parseInt(intent.getStringExtra("webview"));
        Log.d("flagval", "" + flagval);




        if(flagval==1) {

            url = "http://www.facebook.com/billepay";
            title = "Facebook Page";
            // wv.loadUrl("https://www.facebook.com/billepay");
        }
        else if(flagval==2)
        {
            url = "http://twitter.com/BillePay";
            title = "Twitter";
            // wv.loadUrl("https://twitter.com/BillePay");
        }
        else if(flagval==3)
        {
            url = "http://www.instagram.com/paybille/";
            title = "Instagram";
            //wv.loadUrl("https://www.instagram.com/paybille/");
        }
        else {
            url = "http://bille.in/terms.php";
            title ="Terms and Conditions";
            //wv.loadUrl("http://bille.in/terms.html");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);





        wv = (WebView)findViewById(R.id.webView);

        wv.loadUrl(url);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();
                TermsAndConditions.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




}