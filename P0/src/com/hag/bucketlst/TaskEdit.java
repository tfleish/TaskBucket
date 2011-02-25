package com.hag.bucketlst;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TaskEdit extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tedit);
        
        // Capture our button from layout
        Button buttonDone = (Button)findViewById(R.id.add);
        // Register the onClick listener with the implementation above
        buttonDone.setOnClickListener(mAddListener);

    }
    
    private OnClickListener mAddListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{
    		// do something when button is clicked
    		try
    		{
            	Intent intent = new Intent(TaskEdit.this, BucketMain.class);
            	startActivity(intent);
    		}
    		catch (Exception ex)
    		{

    		}
    	}
    };
    
}
