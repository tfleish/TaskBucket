package com.hag.bucketlst;

// test push from eclipse in testBranch?
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BucketMain extends CustomWindow {
    /** Called when the activity is first created. */
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	Button b1 = (Button) findViewById(R.id.b1);
    	b1.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			Intent intent = new Intent(BucketMain.this, TaskEdit.class);
    			
    			startActivity(intent);
    		}
    	});
    }
}
