package com.hag.bucketlst.customWindows;

import android.app.TabActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.hag.bucketlst.R;

public class CustomTab extends TabActivity {
	protected TextView title;
	protected ImageView icon;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);        
        setContentView(R.layout.cus_tab);        
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        //View view = getLayoutInflater().inflate(R.layout.window_title, null);
        title = (TextView) findViewById(R.id.title);
	}
}