package com.hag.bucketlst.customWindows;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hag.bucketlst.R;
import com.hag.bucketlst.activity.FakeUserAdd;

public class CustomList extends ListActivity {
	protected TextView title;
	protected ImageView icon;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);        
        setContentView(R.layout.cus_win);        
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);        
        title = (TextView) findViewById(R.id.title);
        
        ImageButton profile = (ImageButton) findViewById(R.id.myProfile);
        ImageButton search = (ImageButton) findViewById(R.id.search);
        
        profile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(),"profile", Toast.LENGTH_SHORT).show();
			}
		});
        
        search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        Intent i = new Intent(getApplicationContext(), FakeUserAdd.class);
		        startActivity(i);	
			}
		});
	}
}