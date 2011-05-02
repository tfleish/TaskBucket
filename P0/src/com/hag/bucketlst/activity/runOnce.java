package com.hag.bucketlst.activity;

// test push from eclipse in testBranch?
import java.util.LinkedList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
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

public class runOnce extends CustomWindow {
    /** Called when the activity is first created. */
	
    private TbDbAdapter mDbHelper;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.run_once);
		this.title.setText("Welcome");
		
		mDbHelper = BLApp.getHelper();
		mDbHelper.open();

    	// Create an anonymous implementation of OnClickListener
    	Button b2 = (Button) findViewById(R.id.addUser);
    	b2.setOnClickListener(mAddListener);
    }
    
    public void addMe2MadeCat(){
    	mDbHelper.addUser2Cat(1, 1);
    	mDbHelper.addUser2Cat(2, 1);
    	mDbHelper.addUser2Cat(3, 1);
    }

    public String getUsername(){
        AccountManager manager = AccountManager.get(this); 
        Account[] accounts = manager.getAccountsByType("com.google"); 
        List<String> possibleEmails = new LinkedList<String>();

        for (Account account : accounts) {
          possibleEmails.add(account.name);
        }

        if(!possibleEmails.isEmpty() && possibleEmails.get(0) != null){
            String email = possibleEmails.get(0);
            String[] parts = email.split("@");
            if(parts.length > 0 && parts[0] != null)
                return parts[0];
            else
                return null;
        }else
            return null;
    }
    
    // Create an anonymous implementation of OnClickListener
    private OnClickListener mAddListener = new OnClickListener()
    {
		public void onClick(View v)
		{
	    	try
	    	{
	    		EditText dName = (EditText)findViewById(R.id.dnameGet);
	    		String userName = getUsername();
	    			    			    	
	    		if (dName.length() != 0){
	    			String dispName = BLApp.wordfix(dName.getText());
	    			if (userName == null){
	    				userName = dispName;
	    			}
		    		mDbHelper.makeCollaborators(userName, dispName);
		    		addMe2MadeCat();
		    		Toast.makeText(getApplicationContext(), userName + " " + dispName + " has been added", Toast.LENGTH_SHORT).show();
		    		SharedPreferences.Editor preferences = getSharedPreferences("GetUserName", 0).edit();
		    		preferences.putBoolean("First Run", false);
		    		preferences.commit();
		            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		            mgr.hideSoftInputFromWindow(dName.getApplicationWindowToken(), 0);
		            setResult(RESULT_OK);
		            finish();
	    		} else {
	    			Toast.makeText(getApplicationContext(), "Please pick a display name", Toast.LENGTH_SHORT).show();	
	    		}
	    	}
	    	catch (Exception ex)
	    	{
	    		Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
	    	}
		}
	};	

}
