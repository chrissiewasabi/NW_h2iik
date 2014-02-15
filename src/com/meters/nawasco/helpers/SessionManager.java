package com.meters.nawasco.helpers;

import java.util.HashMap;

import com.meters.nawasco.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

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
		private static final String PREF_NAME = "nawascopref";
		
		// All Shared Preferences Keys
		private static final String IS_LOGIN = "IsLoggedIn";
		private static final String IS_Saved = "saved";
		public static final String KEY_NAME = "name";		
		public static final String KEY_PWD = "psd";
		public static final String KEY_FAILS = "fails";
		public static final String KEY_METERS = "meters";
		// Constructor
		public SessionManager(Context context){
			this._context = context;
			pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
			editor = pref.edit();
		}
		/**
		 * Create login session
		 * */
		public void createLoginSession(String uname, String pwd){
			// Storing login value as TRUE
			editor.putBoolean(IS_LOGIN, true);
			editor.putString(KEY_NAME, uname);			
			editor.putString(KEY_PWD, pwd);			
			editor.commit();
		}
		public void createErrorList(String arr_fails ){
			editor.putBoolean(IS_LOGIN, true);
			editor.putString(KEY_FAILS, arr_fails);
			editor.commit();
		}
		public void createMeterList(String arr_meters){
			editor.putBoolean(IS_Saved, true);
			editor.putString(KEY_METERS, arr_meters);
			editor.commit();
		}
		public void checkLogin(){
			// Check login status
			if(!this.isLoggedIn()){
				// user is not logged in redirect him to Login Activity
				Intent i = new Intent(_context,MainActivity.class);
				// Closing all the Activities
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				
				// Add new Flag to start new Activity
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				_context.startActivity(i);
			}
			
		}
		public void CheckStuff(){
			if(!this.isSaved()){
				// user is not logged in redirect him to Login Activity
				Log.d("bad", "not saved");
			}
		}
		public HashMap<String, String> getUserDetails(){
			HashMap<String, String> user = new HashMap<String, String>();
			// user name
			user.put(KEY_NAME, pref.getString(KEY_NAME, null));
			
			// user password
			user.put(KEY_PWD, pref.getString(KEY_PWD, null));
			
			// return user
			return user;
		}
		public void logoutUser(){
			// Clearing all data from Shared Preferences
			editor.clear();
			editor.commit();
			
			Intent i = new Intent(_context, MainActivity.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			// Staring Login Activity
			_context.startActivity(i);
		}
		
		// Get Login State
		public boolean isLoggedIn(){
			return pref.getBoolean(IS_LOGIN, false);
		}
		public boolean isSaved(){
			return pref.getBoolean(IS_Saved, false);
		}
		
		
}
