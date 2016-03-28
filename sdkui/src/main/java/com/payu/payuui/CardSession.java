package com.payu.payuui;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Vibhor Goel on 2/9/2016.
 */
public class CardSession {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "CardPrefs";

    public static final String KEY_CARDNAME = "cardname";
    public static final String KEY_CARDNO = "cardno";
    public static final String KEY_CARDEXPMONTH = "expirymonth";
    public static final String KEY_CARDEXPYEAR = "expiryyear";
    public static final String KEY_MOBILENO = "mobileno";
    public static final String KEY_BANKS = "banks";



    // Constructor
    public CardSession(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void storeusercard(String cardname, String cardno ,String expirymonth, String expiryyear ,String mobileno){


        // Storing name in pref
        editor.putString(KEY_CARDNAME, cardname);

        // Storing cardno in pref
        editor.putString(KEY_CARDNO, cardno);

        // Storing month in pref
        editor.putString(KEY_CARDEXPMONTH, expirymonth);

        // Storing year in pref
        editor.putString(KEY_CARDEXPYEAR, expiryyear);
        editor.putString(KEY_MOBILENO, mobileno);


        // commit changes
        editor.commit();
    }

    /**
     * Get stored card data
     * */
    public HashMap<String, String> getCardDetails(){
        HashMap<String, String> card = new HashMap<String, String>();
        // card name
        card.put(KEY_CARDNAME, pref.getString(KEY_CARDNAME, null));
// card no.
        card.put(KEY_CARDNO, pref.getString(KEY_CARDNO, null));

        // card month
        card.put(KEY_CARDEXPMONTH, pref.getString(KEY_CARDEXPMONTH, null));
        // card year

        card.put(KEY_CARDEXPYEAR, pref.getString(KEY_CARDEXPYEAR, null));


        card.put(KEY_MOBILENO, pref.getString(KEY_MOBILENO, null));



        // return user
        return card;
    }

    public void StoreMobileNo(String mobileno){
        // Storing mobile no.

        editor.putString(KEY_MOBILENO, mobileno);

        // commit changes
        editor.commit();
    }


    public String GetMobileNo(String mobileno){
        // GETING mobile no.

        mobileno = (pref.getString(KEY_MOBILENO, null));

return mobileno;
    }


}
