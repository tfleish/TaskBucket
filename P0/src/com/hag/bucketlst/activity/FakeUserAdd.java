package com.hag.bucketlst.activity;

// test push from eclipse in testBranch?
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hag.bucketlst.R;
import com.hag.bucketlst.application.BLApp;
import com.hag.bucketlst.customWindows.CustomWindow;
import com.hag.bucketlst.db.TbDbAdapter;

public class FakeUserAdd extends CustomWindow {
    /** Called when the activity is first created. */
	
    private TbDbAdapter mDbHelper;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fake_user);
		this.title.setText("New Users");
		this.buttonView.setVisibility(View.INVISIBLE);
		
		mDbHelper = BLApp.getHelper();
		mDbHelper.open();

    	// Create an anonymous implementation of OnClickListener
    	Button b2 = (Button) findViewById(R.id.addUser);
    	b2.setOnClickListener(mAddListener);
    }

    // Create an anonymous implementation of OnClickListener
    private OnClickListener mAddListener = new OnClickListener()
    {
		public void onClick(View v)
		{
	    	try
	    	{
	    		EditText userName = (EditText)findViewById(R.id.unameGet);
	    		EditText dispName = (EditText)findViewById(R.id.dnameGet);
	    		
	    		if ((userName.length() != 0) && (dispName.length() != 0)){
		    		mDbHelper.makeCollaborators(userName.getText().toString(), dispName.getText().toString());
		    		Toast.makeText(getApplicationContext(), dispName.getText().toString() + " has been added", Toast.LENGTH_SHORT).show();		    		
		    		dispName.setText("");
		    		userName.setText("");
		            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		            mgr.hideSoftInputFromWindow(userName.getApplicationWindowToken(), 0);		           	
	    		} else {
	    			Toast.makeText(getApplicationContext(), "Add the proper fields", Toast.LENGTH_SHORT).show();	
	    		}
	    	}
	    	catch (Exception ex)
	    	{
	    		Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
	    	}
		}
	};

}
