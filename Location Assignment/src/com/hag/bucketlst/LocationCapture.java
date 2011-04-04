package com.hag.bucketlst;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class LocationCapture extends Activity {
    /** Called when the activity is first created. */
	
	DBAdapter db;
	LocationManager locationManager;
	Location location;
	CheckBox wifiOn;
	CheckBox gpsOn;
	long minTime = 5*60*1000;
	float minDistance = 500;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        db = new DBAdapter(this);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);	    


    	//Add find me button.
        Button findMe = (Button)findViewById(R.id.find_me);
        findMe.setOnClickListener(addAddListener);
        wifiOn = (CheckBox)findViewById(R.id.wifi_on);
        if (wifiOn.isChecked()) {
            wifiOn.setChecked(false);
        }
        gpsOn = (CheckBox)findViewById(R.id.gps_on);
        if (gpsOn.isChecked()) {
            gpsOn.setChecked(false);
        }

    }
    
    private OnClickListener addAddListener = new OnClickListener() {
    	public void onClick(View v) {
    		long id = 0;
    		try {
    		    //LocationProvider locationProvider = LocationManager.NETWORK_PROVIDER;
    		    //locationManager.requestLocationUpdates(locationProvider, minTime, minDistance, this);

    		    double lat = location.getLatitude();
    		    double longi = location.getLongitude();
    		    double error = location.getAccuracy();
    		    boolean wifi = wifiOn.isChecked();
    		    boolean gps = gpsOn.isChecked();
    		    db.open();        
            	id = db.insertLocation(lat, longi, error, wifi, gps);
                db.close();
    		}
    		catch (Exception ex) {
    			Context context = getApplicationContext();
    			CharSequence text = ex.toString() + "ID = " + id;
    			int duration = Toast.LENGTH_LONG;
    			Toast toast = Toast.makeText(context, text, duration);
    			toast.show();
    		}
    	}
    };
}