package com.hag.bucketlst.customWindows;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.hag.bucketlst.R;

public class CustomWindow extends Activity {
	protected TextView title;
	protected ImageView icon;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.cus_win);        
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);        
        title = (TextView) findViewById(R.id.title);
	}
}