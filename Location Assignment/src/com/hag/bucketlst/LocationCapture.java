package com.hag.bucketlst;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.nio.CharBuffer;
import java.util.List;

import android.app.TabActivity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class LocationCapture extends TabActivity  implements LocationListener {
    /** Called when the activity is first created. */
	
	private static final String[] S = { "Out of Service",
			"Temporarily Unavailable", "Available" };

	private TextView output;
	private LocationManager locationManager;
	private String bestProvider;
	
	/**
	Button btnSimple = null;	
	MapView gMapView = null;	
	MapController mc = null;	
	Drawable defaultMarker	= null;	
	GeoPoint p = null;	
	**/
	double latitude	= 18.9599990845; 
	double longitude = 72.819999694;
	
	DBAdapter db;
	Location location;
	CheckBox wifiOn;
	CheckBox gpsOn;
	CheckBox enableOn;
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
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);	
        
    	//Add find me button.
        output = (TextView) findViewById(R.id.output);
        Button findMe = (Button)findViewById(R.id.find_me);
        findMe.setOnClickListener(addAddListener);
        Button saveJava = (Button)findViewById(R.id.saveJava);
        saveJava.setOnClickListener(saveJavaListener);
        Button saveAndroid = (Button)findViewById(R.id.saveAndroid);
        saveAndroid.setOnClickListener(saveAndroidListener);
        wifiOn = (CheckBox)findViewById(R.id.wifi_on);
        gpsOn = (CheckBox)findViewById(R.id.gps_on);
        enableOn = (CheckBox)findViewById(R.id.criteria_true);

		// List all providers:
		List<String> providers = locationManager.getAllProviders();

		for (String provider : providers) {
			printProvider(provider);
		}

		Criteria criteria = new Criteria();
		bestProvider = locationManager.getBestProvider(criteria, true);
		output.append("\n\nBEST Provider:\n");
		printProvider(bestProvider);

		output.append("\n\nLocations (starting with last known):");
		location = locationManager.getLastKnownLocation(bestProvider);
		if (location != null){
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}
		printLocation(location);
		

		/**
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
		**/
		
		// Getting locationManager and reflecting changes over map if distance travel by
		// user is greater than 500m from current location.
		locationManager.requestLocationUpdates(bestProvider, 0, 0, this);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 20000, 1, this);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 1, this);
    }
    
    private OnClickListener addAddListener = new OnClickListener() {
    	public void onClick(View v) {
    		long id = 0;
    		try {
    		    //LocationProvider locationProvider = LocationManager.NETWORK_PROVIDER;
    		    //locationManager.requestLocationUpdates(bestProvider, 0, 0, (LocationListener) getApplicationContext());
    			Criteria criteria = new Criteria();
    			bestProvider = locationManager.getBestProvider(criteria, enableOn.isChecked());
    			location = locationManager.getLastKnownLocation(bestProvider); 
    		    printLocation(location);
    			id = updateData();
    			Location locationG = getGpsLoc();
    			printLocation(locationG);
    			updateData(locationG);
    			Location locationN = getNetLoc();
    			printLocation(locationN);
    			updateData(locationN);    		
    		}
    		catch (Exception ex) {
    			CharSequence text = ex.toString() + "ID = " + id;
    			Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    		}
    	}
    };
    
    private OnClickListener saveJavaListener = new OnClickListener()
    {
    	public void onClick(View v) {
    		try {
    			SaveDataToFile(output.getText().toString());
    		}
    		catch (Exception ex) {
    			CharSequence text = ex.toString();
    			Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    		}
    	}
    };
    
    private OnClickListener saveAndroidListener = new OnClickListener()
    {
    	public void onClick(View v) {
    		try {
    			SaveAndroidToFile2(output.getText().toString());
    		}
    		catch (Exception ex) {
    			CharSequence text = ex.toString();
    			Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    		}
    	}
    };
    
    public Location getGpsLoc()
    {
    	boolean gps_enabled = false;
    	 try
    	 {
    		 gps_enabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    	 }
    	 catch(Exception ex)
    	 {
    		 
    	 }
    	 if(gps_enabled)
    		return new Location(LocationManager.GPS_PROVIDER);
		return null;
    }
    
    public Location getNetLoc()
    {
    	boolean network_enabled = false;
         try
         {
        	 network_enabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
         }
         catch(Exception ex)
         {
        	 
         }
         if(network_enabled)
     		return new Location(LocationManager.NETWORK_PROVIDER);
 		return null;
    }
    
    
    public long updateData()
    {
    	double locallatitude;
    	double locallongitude;
    	double error;
    	if (location != null){
    	    locallatitude = location.getLatitude();
    	    latitude = locallatitude;
    	    locallongitude = location.getLongitude();
    	    longitude = locallongitude;
    	    error = location.getAccuracy();	
    	} else {
    		locallatitude = 0;
    		locallongitude = 0;
    		error = 0;
    	}
	    boolean wifi = wifiOn.isChecked();
	    boolean gps = gpsOn.isChecked();
	    db.open();        
    	long id = db.insertLocation(Double.toString(locallatitude), Double.toString(locallongitude), Double.toString(error),
				Boolean.toString(wifi), Boolean.toString(gps));
        db.close();
        return id;
    }
    
    public void updateData(Location location)
    {
    	double locallatitude;
    	double locallongitude;
    	double error;
    	if (location != null){
    	    locallatitude = location.getLatitude();
    	    latitude = locallatitude;
    	    locallongitude = location.getLongitude();
    	    longitude = locallongitude;
    	    error = location.getAccuracy();	
    	} else {
    		locallatitude = 0;
    		locallongitude = 0;
    		error = 0;
    	}
	    boolean wifi = wifiOn.isChecked();
	    boolean gps = gpsOn.isChecked();
	    db.open();        
    	db.insertLocation(Double.toString(locallatitude), Double.toString(locallongitude), Double.toString(error),
				Boolean.toString(wifi), Boolean.toString(gps));
        db.close();
    }

	/** Register for the updates when Activity is in foreground */
	@Override
	protected void onResume() {
		super.onResume();
		Criteria criteria = new Criteria();
		bestProvider = locationManager.getBestProvider(criteria, enableOn.isChecked());
		location = locationManager.getLastKnownLocation(bestProvider);
		locationManager.requestLocationUpdates(bestProvider, 0, 0, this);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 20000, 1, this);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 1, this);
	}
	
	/** Stop the updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}
	
	public void onLocationChanged(Location location) {
		printLocation(location);
		updateData(location);
	}
	
	public void onProviderDisabled(String provider) {
		// let okProvider be bestProvider
		// re-register for updates
		output.append("\n\nProvider Disabled: " + provider);
		updateData();
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
	
	private boolean SaveDataToFile(String data) 
	{ 
		ObjectOutputStream  objectOut = null; 
		try 
		{ 
			File root = new File("/sdcard/download/Loc"); 
			//FileWriter file = new FileWriter("/sdcard/download/LocAdd.txt");
			if(!root.exists()) 
			{ 
				root.mkdir();
			} 
			File file = new File(root, "LocAddJ.txt"); 
			if(!file.exists()) 
				file.createNewFile(); 
			FileOutputStream stream = new FileOutputStream(file); 
			objectOut = new ObjectOutputStream(new BufferedOutputStream(stream)); 
			objectOut.writeObject(data); 
			Toast.makeText(this, "Saved Log file in Loc", Toast.LENGTH_SHORT).show();
			return true; 
		} 
		catch(IOException e) 
		{ 
			e.printStackTrace(); 
			return false; 
		} 
		finally 
		{ 
			try 
			{ 
				if(objectOut != null) 
				objectOut.close(); 
			} 
			catch(IOException ex) 
			{ 
				ex.printStackTrace(); 
			} 
		} 
	}
	
	public void SaveAndroidToFile2(String sBody){
	    try
	    {
	        File root = new File(Environment.getExternalStorageDirectory(), "LocAss");
	        if (!root.exists()) {
	            root.mkdirs();
	        }
	        File gpxfile = new File(root, "LocAssA2.txt");
	        FileWriter writer = new FileWriter(gpxfile);
	        writer.append(sBody);
	        writer.flush();
	        writer.close();
	        Toast.makeText(this, "Saved Log file in LocAss", Toast.LENGTH_SHORT).show();
	    }
	    catch(IOException e)
	    {
	         e.printStackTrace();
	    }
	   }
	
	private boolean SaveAndroidToFile(String data) 
	{ 		
		
        try { // catches IOException below        			
			// ##### Write a file to the disk #####
			/* We have to use the openFileOutput()-method 
			 * the ActivityContext provides, to
			 * protect your file from others and 
			 * This is done for security-reasons. 
			 * We chose MODE_WORLD_READABLE, because
			 *  we have nothing to hide in our file */		
			FileOutputStream fOut = openFileOutput("LocAddA.txt", 
								MODE_WORLD_READABLE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);	

			// Write the string to the file
			osw.write(data);
			/* ensure that everything is 
			 * really written out and close */
			osw.flush();
			osw.close(); 
			return true;

		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}
	} 
	
	private String readAndroidToFile(String data) 
	{ 		
		
        try { // catches IOException below; 
			
			// ##### Read the file back in #####		
			/* We have to use the openFileInput()-method 
			 * the ActivityContext provides. 
			 * Again for security reasons with
			 * openFileInput(...) */
			FileInputStream fIn = openFileInput("LocAdd.txt");
			InputStreamReader isr = new InputStreamReader(fIn);
			/* Prepare a char-Array that will 
			 * hold the chars we read back in. */
			CharBuffer inBuff = null;
			String readString = "";
			// Fill the Buffer with data from the file
			isr.read(inBuff);
			if (inBuff != null)
			{
				char[] inputBuffer = inBuff.array();
				// Transform the chars to a String
				readString = new String(inputBuffer);
			}

			return readString;

		} catch (IOException ioe) {
			ioe.printStackTrace();
			return " ";
		}
	} 
	
	/**
	// User can zoom in/out using keys provided on keypad 
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
	
	// Class overload draw method which actually plot a marker,text etc. on Map 
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
	
	**/
}