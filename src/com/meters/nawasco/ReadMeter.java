package com.meters.nawasco;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;
import com.meters.nawasco.helpers.LocationService;

public class ReadMeter extends SherlockActivity {
	LocationService locator;
	String lat,lng,location;
	Config config;
	EditText et_read;
	Button btn_submit,btn_cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_meter);
		locator=new LocationService(ReadMeter.this);
		 if(locator.canGetLocation()){
		    	
		    	double latitude = locator.getLatitude();
		    	double longitude = locator.getLongitude();
		    	lat=Double.toString(latitude);
		    	lng=Double.toString(longitude);
		    	location=lat+","+lng;
		    	setTitle("Current Location:"+location);
		    	Log.d("loc","loc"+location);
					
		    }
		  else{
		    	// can't get location
		    	// GPS or Network is not enabled
		    	// Ask usen't get location
		    	// GPS or Network is not enabled
		    	// Ask user to enable GPS/network in settings
		    	locator.showSettingsAlert();
		    } 
		 et_read=(EditText)findViewById(R.id.et_read);
		 btn_cancel=(Button)findViewById(R.id.Button02);
		 btn_submit=(Button)findViewById(R.id.Button01);
	}

	

}
