package in.bille.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class Cards extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add payment cards");
        FrameLayout cardadd=(FrameLayout)findViewById(R.id.AddCardLayout);
        View.OnClickListener cardlistiner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherIntent = new Intent(Cards.this,AddPaymentCard.class);
                startActivity(otherIntent);
            }
        };

        cardadd.setOnClickListener(cardlistiner);


    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();


        }
        return super.onOptionsItemSelected(item);


    }


}
