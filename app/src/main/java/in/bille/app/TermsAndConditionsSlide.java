
package in.bille.app;

import android.os.Bundle;
import android.widget.TextView;

import com.klinker.android.sliding.SlidingActivity;

public class TermsAndConditionsSlide extends SlidingActivity {

    @Override
    public void init(Bundle savedInstanceState) {
        setTitle("Terms And Conditions");
        setContent(R.layout.termsandconditions_content);
        TextView tandcdata=(TextView)findViewById(R.id.termsandconditionsdata);
        String data= getIntent().getStringExtra("TERMS AND CONDITIONS");
        tandcdata.setText(data);


    }




}
