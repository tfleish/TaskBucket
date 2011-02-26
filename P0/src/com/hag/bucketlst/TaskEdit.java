package com.hag.bucketlst;

import android.R.layout;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class TaskEdit extends CustomWindow {
	
	private EditText mTaskText;
    private Spinner mCatText;
    private Long mRowId;
    private LDBAdapter lDbHelper;
    private CDBAdapter cDbHelper;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        //lDbHelper = new LDBAdapter(this);
        //lDbHelper.open();
        cDbHelper = new CDBAdapter(this);
        
        setContentView(R.layout.tedit);
		this.title.setText("Task Info");
		
		mTaskText = (EditText)findViewById(R.id.titleGet);
		mCatText = (Spinner)findViewById(R.id.categoryGet);
		
    	//cDbHelper.open();
        //new AlertDialog.Builder(this).setMessage(cDbHelper.toString()).show();
    	//Cursor catLst = cDbHelper.getAllCategory();
    	//cDbHelper.close();
		
    	populateCategories();
		
        
        // Capture our button from layout
        Button buttonDone = (Button)findViewById(R.id.add);
        // Register the onClick listener with the implementation above
        buttonDone.setOnClickListener(mAddListener);

    }
    
    private void populateCategories() {
    	cDbHelper.open();
    	Cursor catLst = cDbHelper.getAllCategory();
    	startManagingCursor(catLst);
    	
        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{CDBAdapter.KEY_CATEGORY};
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text1};
        
        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter adapter = 
        	    new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, catLst, from, to);
        
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		mCatText.setAdapter(adapter);
		cDbHelper.close();
		/**
		mCatText.setOnItemSelectedListener(new OnItemSelectedListener()
        {

			@Override
            public void onItemSelected(AdapterView<?> arg0, 
                    View arg1, int arg2, long arg3) 
                    {
                        int index = mCatText.getSelectedItemPosition();
                        Toast.makeText(getBaseContext(), 
                            "You have selected item : " + "categories", 
                            Toast.LENGTH_SHORT).show();                
                    }
			public void onNothingSelected(AdapterView<?> arg0) {}

        });
        **/
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
