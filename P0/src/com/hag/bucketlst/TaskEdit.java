package com.hag.bucketlst;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    private String[] categories;
    private AlertDialog alert;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        lDbHelper = new LDBAdapter(this);
        cDbHelper = new CDBAdapter(this);
        
        setContentView(R.layout.tedit);
		this.title.setText("Task Info");
		
    	//populateCategories();
		genCatLst();
		setupDialogue();
        
        // Capture Add Task button from layout
        // Register the onClick listener To add task and return
        Button buttonDone = (Button)findViewById(R.id.add);
        buttonDone.setOnClickListener(mAddListener);
        
        // Capture Edit Category button from layout
        // Register the onClick listener To jump to edit page
    	Button editCategory = (Button) findViewById(R.id.newCategory);
    	editCategory.setOnClickListener(mEditListener);

    }
    
    private void genCatLst()
    {
    	cDbHelper.open();
    	Cursor catLst = cDbHelper.getAllCategory();
    	startManagingCursor(catLst);
    	
    	categories = new String[catLst.getCount()]; 
    	
        if (catLst.moveToFirst())  
        {                         
            for (int i = 0; i < catLst.getCount(); i++)  
            {  
                categories[i] = catLst.getString(1);
                catLst.moveToNext();  
            }             
        }  
        
        catLst.close();
        cDbHelper.close();
    }
    
    private void setupDialogue()
    {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Set Category");
    	builder.setSingleChoiceItems(categories, -1, new DialogInterface.OnClickListener() {
    	    public void onClick(DialogInterface dialog, int item) {
    	        Toast.makeText(getApplicationContext(), categories[item], Toast.LENGTH_SHORT).show();
    	    }
    	});
    	alert = builder.create();
    }
    
    private void populateCategories() 
    {
		mCatText = (Spinner)findViewById(R.id.categoryGet);
		
    	cDbHelper.open();
    	Cursor catLst = cDbHelper.getAllCategory();
    	startManagingCursor(catLst);

        /**     	
    	String[] categories = new String[catLst.getCount()];  

        if (catLst.moveToFirst())  
        {                         
            for (int i = 0; i < catLst.getCount(); i++)  
            {  
                categories[i] = catLst.getString(1);
                catLst.moveToNext();  
            }             
        }  
    	
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, categories);
        **/
    	
        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{CDBAdapter.KEY_CATEGORY};
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{android.R.id.text1};
        
        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter adapter = 
        	    new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, catLst, from, to);

        
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		mCatText.setAdapter(adapter);
		cDbHelper.close();
		mCatText.setOnItemSelectedListener(new OnItemSelectedListener()
        {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
                //int index = mCatText.getSelectedItemPosition();
                Toast.makeText(getBaseContext(), 
                    "You have selected item : " + ((Cursor) mCatText.getSelectedItem()).getString(1),
                    Toast.LENGTH_SHORT).show();  
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}

        });
	}

	private OnClickListener mAddListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{	
    		Context context = getApplicationContext();
			int duration = Toast.LENGTH_LONG;
    		Toast toast;
    		
    		try
    		{
	    		mTaskText = (EditText)findViewById(R.id.titleGet);
	    		
	    		if (mTaskText.length() != 0)
	    		{
	    			String Title = mTaskText.getText().toString();
	    			
	        		lDbHelper.open();
	    			lDbHelper.insertList(Title,0,0);
	        		lDbHelper.close();
	        		
	    			toast = Toast.makeText(context, "Task: " + Title + " added", duration);
	    			toast.show();

		        	Intent intent = new Intent(TaskEdit.this, BucketMain.class);
		        	startActivity(intent);
	    		}
	    		else
	    		{
	    			toast = Toast.makeText(context, "Please Add a Task", duration);
	    			toast.show();
	    		}
    		}
    		catch (Exception ex)
    		{
    			context = getApplicationContext();
    			CharSequence text = ex.toString();
    			toast = Toast.makeText(context, text, duration);
    			toast.show();
    		}
    	}
    };
    
	private OnClickListener mEditListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{
    		// do something when button is clicked
    		try
    		{
            	//Intent intent = new Intent(TaskEdit.this, CategoryEdit.class);
            	//startActivity(intent);
    			alert.show();
    		}
    		catch (Exception ex)
    		{

    		}
    	}
    };
    
}
