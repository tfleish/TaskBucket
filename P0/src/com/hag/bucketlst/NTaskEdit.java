package com.hag.bucketlst;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;
import customWindows.CustomTab;
import db.CDBAdapter;
import db.LDBAdapter;
import db.TbDbAdapter;

public class NTaskEdit extends CustomTab {
	
    private static final int ACTIVITY_MAKE_CAT=0;
	
	private EditText mTaskText;
	private Button editCategory;
    private Long mRowId;
    private LDBAdapter lDbHelper;
    private CDBAdapter cDbHelper;
    private TbDbAdapter tbDbHelper;
    private String[] categories;
    private long[] catIdLst;
    private AlertDialog alert;
    private long catItem = 1;
    private String category = "General";
    private int completed = 0;
    
    //private Spinner mCatText;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        this.title.setText("bucketLST");
        setContentView(R.layout.ntedit);
        
        TabHost mTabHost = getTabHost();
        mTabHost.addTab(mTabHost.newTabSpec("Task Information").setIndicator("Task Info").setContent(R.id.infoTab));
        mTabHost.addTab(mTabHost.newTabSpec("Notes").setIndicator("My Notes").setContent(R.id.notesTab));       
        mTabHost.setCurrentTab(0);
        
        
        tbDbHelper = new TbDbAdapter(this);
        tbDbHelper.open();

        /**
        lDbHelper = new LDBAdapter(this);
        cDbHelper = new CDBAdapter(this);
        lDbHelper.open();
    	cDbHelper.open();
        
		
		
		mTaskText = (EditText)findViewById(R.id.titleGet);
		
    	editCategory = (Button) findViewById(R.id.newCategory);

        mRowId = (savedInstanceState != null) ? savedInstanceState.getLong(LDBAdapter.KEY_ROWID) 
											  : null;
		if (mRowId == null) {
		Bundle extras = getIntent().getExtras();            
		mRowId = (extras != null) ? extras.getLong(LDBAdapter.KEY_ROWID) 
								  : null;
		}
		
		populateFields();

    	//Create Category dialog and fill
		genCatLst();
		setupDialogue();
		
		editCategory.setText(category);
        
        // Capture Add Task button from layout
        // Register the onClick listener To add task and return
        Button buttonDone = (Button)findViewById(R.id.add);
        buttonDone.setOnClickListener(mAddListener);
        
        // Capture Edit Category button from layout
        // Register the onClick listener To jump to edit page
    	editCategory.setOnClickListener(mEditListener);
    	**/
    }
    
    private void populateFields() {
        if (mRowId != null) {
            Cursor task = lDbHelper.getTask(mRowId);
            startManagingCursor(task);
            mTaskText.setText(task.getString(
    	            task.getColumnIndexOrThrow(LDBAdapter.KEY_TITLE)));
            
            completed = task.getInt(task.getColumnIndexOrThrow(LDBAdapter.KEY_COMPLETED));
            
            catItem = task.getLong(task.getColumnIndexOrThrow(LDBAdapter.KEY_CATID));
            Cursor cat = cDbHelper.getCat(catItem);
            startManagingCursor(cat);
            category = cat.getString(
                    cat.getColumnIndexOrThrow(CDBAdapter.KEY_CATEGORY));
            editCategory.setText(category);
        }
    }
    
    private void genCatLst()
    {
    	Cursor catLst = cDbHelper.getAllCategory();
    	startManagingCursor(catLst);
    	
    	categories = new String[catLst.getCount()]; 
    	catIdLst = new long[catLst.getCount()];
    	
        if (catLst.moveToFirst())  
        {                         
            for (int i = 0; i < catLst.getCount(); i++)  
            {  
                categories[i] = catLst.getString(1);
                catIdLst[i] = catLst.getLong(0);
                catLst.moveToNext();  
            }             
        }  
    }
    
    private void setupDialogue()
    {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Set Bucket");
    	builder.setPositiveButton("Edit Buckets", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	editCategory();
            }
        });
    	builder.setSingleChoiceItems(categories, 0, new DialogInterface.OnClickListener() {
    	    public void onClick(DialogInterface dialog, int item) {
    	    	catItem = catIdLst[item];
    	    	category = categories[item];
    	    	editCategory.setText(category);
    	        //Toast.makeText(getApplicationContext(), categories[item] + " " + item, Toast.LENGTH_SHORT).show();
    	        dialog.dismiss();
    	    }
    	});
    	alert = builder.create();
    }
    
    private void editCategory() {
        Intent i = new Intent(this, CategoryEdit.class);
        startActivityForResult(i, ACTIVITY_MAKE_CAT);
        //startActivity(i);
    }
    
	private OnClickListener mAddListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{	
    		Context context = getApplicationContext();
			int duration = Toast.LENGTH_LONG;
    		Toast toast;
	    		
    		if (mTaskText.length() != 0)
    		{
        		
    			toast = Toast.makeText(context, "Task: " + mTaskText.getText().toString() + " added", duration);
    			toast.show();
    			
    			//saveState();
    		}
    		else
    		{
    			toast = Toast.makeText(context, "Please Add a Task", duration);
    			toast.show();
    		}
    	}
    };
    
	private OnClickListener mEditListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{
    		alert.show();
    	}
    };
    
    /**
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
		if (mRowId != null) {
	        outState.putLong(LDBAdapter.KEY_ROWID, mRowId);
		}
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        //saveState();
    }
    
    @Override
    protected void onResume() {
       super.onResume();
       populateFields();
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, 
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
		genCatLst();
		setupDialogue();
		alert.show();
    }
    
    private void saveState() {
        String title = mTaskText.getText().toString();

		if (mTaskText.length() != 0)
		{
	        if (mRowId == null) {
	            long id = lDbHelper.insertTask(title, category, catItem, completed);
	            if (id > 0) {
	                mRowId = id;
	            }
	        } else {
	            lDbHelper.updateTask(mRowId, title, category, catItem, completed);
	        }
		}
	    setResult(RESULT_OK);
	    finish();
    }
    

    private void populateCategories() 
    {
		mCatText = (Spinner)findViewById(R.id.categoryGet);
		
    	cDbHelper.open();
    	Cursor catLst = cDbHelper.getAllCategory();
    	startManagingCursor(catLst);

        // OLD COMMENT BEGINS	
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
		// OLD COMMENT ENDS
    	
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
                //int index = mCatText.getSelectedItemPosition();
                Toast.makeText(getBaseContext(), 
                    "You have selected item : " + ((Cursor) mCatText.getSelectedItem()).getString(1),
                    Toast.LENGTH_SHORT).show();  
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}

        });
	}
    **/
    
}
