package in.bille.app;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import com.devmarvel.creditcardentry.library.CardValidCallback;
import com.devmarvel.creditcardentry.library.CreditCard;
import com.devmarvel.creditcardentry.library.CreditCardForm;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.app.DatePickerDialog.OnDateSetListener;
import android.text.InputType;
import android.view.View.OnClickListener;


public class AddPaymentCard extends AppCompatActivity {
    private static final String TAG = "ADD CARD ACTIVITY";
    //UI References
    private EditText fromDateEtxt;
    private EditText toDateEtxt;

    CardValidCallback cardValidCallback = new CardValidCallback() {
        @Override
        public void cardValid(CreditCard card) {
            Log.d(TAG, "valid card: " + card);
            Toast.makeText(AddPaymentCard.this, "Card valid and complete", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add payment card");
        final CreditCardForm justCard   = (CreditCardForm) findViewById(R.id.just_card_form);
        justCard.setOnCardValidCallback(cardValidCallback);
        justCard.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(justCard, InputMethodManager.SHOW_IMPLICIT);

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
