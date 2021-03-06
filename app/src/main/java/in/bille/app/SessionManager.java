package in.bille.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;
	
	// Editor for Shared preferences
	Editor editor;
	
	// Context
	Context _context;
	
	// Shared pref mode
	int PRIVATE_MODE = 0;
	
	// Sharedpref file name
	private static final String PREF_NAME = "MyPrefs";
	
	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";
	
	// User email (make variable public to access from outside)
	public static final String KEY_Email = "email";

	// User id (make variable public to access from outside)
	public static final String KEY_ID = "id";
	
	// User Password (make variable public to access from outside)
	public static final String KEY_Password = "password";

	// User Name (make variable public to access from outside)
	public static final String KEY_Name = "name";


	// User Name (make variable public to access from outside)
	public static final String KEY_PHONE = "phone";
	//Phone registration id
	public static final String KEY_REGEDIT = "regid";

	public static final String KEY_TOKEN = "token";








	// Constructor
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	/**
	 * Create login session
	 * */
	public void createLoginSession(String email, String name ,String phone, String id){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		
		// Storing name in pref
		editor.putString(KEY_Email, email);
		
		// Storing email in pref
		editor.putString(KEY_Name, name);
		// Storing phone in pref

		editor.putString(KEY_PHONE, phone);

		// Storing USERID in pref
		editor.putString(KEY_ID, id);


		// commit changes
		editor.commit();
	}


	public void storeregid(String id, String token){
		// Storing login value as TRUE

		editor.putString(KEY_REGEDIT, id);
		editor.putString(KEY_TOKEN, token);

		// commit changes
		editor.commit();
	}

	public HashMap<String, String> getregistrationdetails(){
		HashMap<String, String> regdetails = new HashMap<String, String>();
		// user registrationid
		regdetails.put(KEY_REGEDIT, pref.getString(KEY_REGEDIT, null));
// user token value
		regdetails.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));




		// return user
		return regdetails;
	}






	/**
	 * Check login method wil check user login status
	 * If false it will redirect user to login page
	 * Else won't do anything
	 * */
	public boolean checkLogin(){
		// Check login status
		boolean flag = false;
		if(!this.isLoggedIn()){
			flag = true;
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, WelcomeScreen.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			// Staring Login Activity
			_context.startActivity(i);
		}
		return flag;
	}
	
	
	
	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		// user email
		user.put(KEY_Email, pref.getString(KEY_Email, null));
// user name
		user.put(KEY_Name, pref.getString(KEY_Name, null));

		// user phone
		user.put(KEY_PHONE, pref.getString(KEY_PHONE, null));
		// user id

		user.put(KEY_ID, pref.getString(KEY_ID, null));
		//regid
		user.put(KEY_REGEDIT, pref.getString(KEY_REGEDIT, null));


		
		// return user
		return user;
	}
	
	/**
	 * Clear session details
	 * */
	public void logoutUser(){
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();
		
		// After logout redirect user to Loing Activity
		Intent i = new Intent(_context, SplashMain.class);
		// Closing all the Activities



		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		// Staring Login Activity
		_context.startActivity(i);
	}
	
	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);
	}
}
