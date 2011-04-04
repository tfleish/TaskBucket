package com.hag.bucketlst;

import java.util.List;

import android.app.TabActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class LocationCapture extends TabActivity implements LocationListener {
    /** Called when the activity is first created. */
	
	private static final String[] S = { "Out of Service",
			"Temporarily Unavailable", "Available" };

	private TextView output;
	private LocationManager locationManager;
	private String bestProvider;
	
	EditText txted = null;	
	Button btnSimple = null;	
	MapView gMapView = null;	
	MapController mc = null;	
	Drawable defaultMarker	= null;	
	GeoPoint p = null;	
	double latitude	= 18.9599990845; 
	double longitude = 72.819999694;
	
	DBAdapter db;
	Location location;
	CheckBox wifiOn;
	CheckBox gpsOn;
	long minTime = 5*60*1000;
	float minDistance = 500;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // initializes the view tabs
        TabHost mTabHost = getTabHost();
        mTabHost.addTab(mTabHost.newTabSpec("Location Information").setIndicator("Loc Info").setContent(R.id.textView));
        mTabHost.addTab(mTabHost.newTabSpec("Map").setIndicator("Map View").setContent(R.id.mapView));       
        mTabHost.setCurrentTab(0);
        
        db = new DBAdapter(this);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);	    
        
    	//Add find me button.
        Button findMe = (Button)findViewById(R.id.find_me);
        findMe.setOnClickListener(addAddListener);
        wifiOn = (CheckBox)findViewById(R.id.wifi_on);
        gpsOn = (CheckBox)findViewById(R.id.gps_on);

		// List all providers:
		List<String> providers = locationManager.getAllProviders();
		for (String provider : providers) {
			printProvider(provider);
		}

		Criteria criteria = new Criteria();
		bestProvider = locationManager.getBestProvider(criteria, false);
		output.append("\n\nBEST Provider:\n");
		printProvider(bestProvider);

		output.append("\n\nLocations (starting with last known):");
		Location location = locationManager.getLastKnownLocation(bestProvider);
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		printLocation(location);
		
		gMapView = (MapView) findViewById(R.id.myGMap);
		p = new GeoPoint((int) latitude * 1000000, (int) longitude * 1000000);
		gMapView.setSatellite(false);
		mc = gMapView.getController();
		mc.setCenter(p);
		mc.setZoom(14);
		
		// Add a location mark
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay();
		List<Overlay> list = gMapView.getOverlays();
		list.add(myLocationOverlay);
		
		// Adding zoom controls to Map
		//ZoomControls zoomControls = (ZoomControls) gMapView.getZoomControls();
		//zoomControls.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
		//		LayoutParams.WRAP_CONTENT));
		
		gMapView.setBuiltInZoomControls(true);
		gMapView.displayZoomControls(true);
		
		// Getting locationManager and reflecting changes over map if distance travel by
		// user is greater than 500m from current location.
		locationManager.requestLocationUpdates(bestProvider, 20000, 1, this);		
    }
    
    private OnClickListener addAddListener = new OnClickListener() {
    	public void onClick(View v) {
    		long id = 0;
    		try {
    		    //LocationProvider locationProvider = LocationManager.NETWORK_PROVIDER;
    		    //locationManager.requestLocationUpdates(locationProvider, minTime, minDistance, this);
    			id = updateData();
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
    
    public long updateData()
    {
	    latitude = location.getLatitude();
	    longitude = location.getLongitude();
	    double error = location.getAccuracy();
	    boolean wifi = wifiOn.isChecked();
	    boolean gps = gpsOn.isChecked();
	    db.open();        
    	long id = db.insertLocation(Double.toString(latitude), Double.toString(longitude), Double.toString(error),
    								Boolean.toString(wifi), Boolean.toString(gps));
        db.close();
        return id;
    }
    
    public void updateData(Location location)
    {
	    latitude = location.getLatitude();
	    longitude = location.getLongitude();
	    double error = location.getAccuracy();
	    boolean wifi = wifiOn.isChecked();
	    boolean gps = gpsOn.isChecked();
	    db.open();        
    	db.insertLocation(Double.toString(latitude), Double.toString(longitude), Double.toString(error),
				Boolean.toString(wifi), Boolean.toString(gps));
        db.close();
    }
    
	/** Register for the updates when Activity is in foreground */
	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(bestProvider, 20000, 1, this);
	}
	
	/** Stop the updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}
	
	public void onLocationChanged(Location location) {
		printLocation(location);
		if (location != null) {
			updateData(location);
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			p = new GeoPoint((int) latitude * 1000000, (int) longitude * 1000000);
			mc.animateTo(p);
		}
	}
	
	public void onProviderDisabled(String provider) {
		// let okProvider be bestProvider
		// re-register for updates
		output.append("\n\nProvider Disabled: " + provider);
	}
	
	public void onProviderEnabled(String provider) {
		// is provider better than bestProvider?
		// is yes, bestProvider = provider
		output.append("\n\nProvider Enabled: " + provider);
	}
	
	public void onStatusChanged(String provider, int status, Bundle extras) {
		output.append("\n\nProvider Status Changed: " + provider + ", Status="
				+ S[status] + ", Extras=" + extras);
	}
	
	/* User can zoom in/out using keys provided on keypad */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_I) {
			gMapView.getController().setZoom(gMapView.getZoomLevel() + 1);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_O) {
			gMapView.getController().setZoom(gMapView.getZoomLevel() - 1);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_S) {
			gMapView.setSatellite(true);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_T) {
			gMapView.setTraffic(true);
			return true;
		}
		return false;
	}
	
	private void printProvider(String provider) {
		LocationProvider info = locationManager.getProvider(provider);
		output.append(info.toString() + "\n\n");
	}
	
	private void printLocation(Location location) {
		if (location == null)
			output.append("\nLocation[unknown]\n\n");
		else
			output.append("\n\n" + location.toString());
	}
	
	/* Class overload draw method which actually plot a marker,text etc. on Map */
	protected class MyLocationOverlay extends com.google.android.maps.Overlay {
		
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
			Paint paint = new Paint();
			
			super.draw(canvas, mapView, shadow);
			// Converts lat/lng-Point to OUR coordinates on the screen.
			Point myScreenCoords = new Point();
			mapView.getProjection().toPixels(p, myScreenCoords);
			
			paint.setStrokeWidth(1);
			paint.setARGB(255, 255, 255, 255);
			paint.setStyle(Paint.Style.STROKE);
			
			Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
			
			canvas.drawBitmap(bmp, myScreenCoords.x, myScreenCoords.y, paint);
			canvas.drawText("I am here...", myScreenCoords.x, myScreenCoords.y, paint);
			return true;
		}
	}
}