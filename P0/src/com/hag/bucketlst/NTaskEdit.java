package com.hag.bucketlst;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import customWindows.CustomTab;
import db.CDBAdapter;
import db.LDBAdapter;
import db.TbDbAdapter;

public class NTaskEdit extends CustomTab {
	
	private static final int ACTIVITY_VOICE_RECOGNITION = 1000;
    private static final int ACTIVITY_MAKE_CAT = 2000;
    
	private final int DIALOG_CAT_ID = 0;
	private final int DIALOG_DUE_ID = 1;
	private final int DIALOG_COLLAB_ID = 2;
	
	private EditText mTaskText;
	private EditText mTaskNotes;
	private Button mTaskCat;
	private Button mTaskDue;
	private Button mTaskCollab;
	private Spinner mTaskPri;
	private ImageButton speakNow;
	
	//private DatePickerDialog.OnDateSetListener mDateSetListener;
	private long mDate;
    private long catItem;
    private long[] catIdLst;
    private long priItem;
    private String category;
    private String[] categories;
    
    private long[] collabIdLst;
    private boolean[] chosenCollabLst;

	
    private Long mRowId;
    private LDBAdapter lDbHelper;
    private CDBAdapter cDbHelper;
    private TbDbAdapter tbDbHelper;

    private AlertDialog alert;

    private int completed = 0;
    
    //private Spinner mCatText;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        this.title.setText("task Info");
        setContentView(R.layout.ntedit);
        
        // initializes the view tabs
        TabHost mTabHost = getTabHost();
        mTabHost.addTab(mTabHost.newTabSpec("Task Information").setIndicator("Task Info").setContent(R.id.infoTab));
        mTabHost.addTab(mTabHost.newTabSpec("Notes").setIndicator("My Notes").setContent(R.id.notesTab));       
        mTabHost.setCurrentTab(0);
        
        // initializes the database
        tbDbHelper = new TbDbAdapter(this);
        tbDbHelper.open();

        // gets each input/output fields from view
		mTaskText = (EditText)findViewById(R.id.titleGet);	
		mTaskNotes = (EditText)findViewById(R.id.myNotes);
		mTaskCat = (Button)findViewById(R.id.newCategory);
		mTaskDue = (Button)findViewById(R.id.dueDate); 
		mTaskCollab = (Button)findViewById(R.id.taskCollabs);
		mTaskPri = (Spinner)findViewById(R.id.newPri);
		speakNow = (ImageButton)findViewById(R.id.speakNow);
		
		genCatLst();
		genPriLst();
		genCollabLst();
		
        mTaskDue.setOnClickListener(mTaskDueListener);
        mTaskCat.setOnClickListener(mEditListener);
        mTaskCollab.setOnClickListener(mCollabButtonListener);
        
        
    	
        /**
        lDbHelper = new LDBAdapter(this);
        cDbHelper = new CDBAdapter(this);
        lDbHelper.open();
    	cDbHelper.open();
        
		
		

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
            mTaskCat.setText(category);
        }
    }
    
    private void genCatLst()
    {
    	// Sets defaults values for category id and name
    	catItem = tbDbHelper.getDefaultCategoryId();
    	category = tbDbHelper.fetchCategory(catItem).getString(0);
    	
    	// Sets category picker to default value
    	mTaskCat.setText(category);
    	
    	// Creates an Array of category and their id for picker dialog
    	Cursor catLst = tbDbHelper.fetchAllCategories();
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
        
        catLst.close();
    }
    
    private void genPriLst() 
    {
    	Cursor priLst = tbDbHelper.fetchAllPriorities();
    	startManagingCursor(priLst);
    	
        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{TbDbAdapter.KEY_PRI_NAME};
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{android.R.id.text1};
        
        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter adapter = 
        	    new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, priLst, from, to);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		mTaskPri.setAdapter(adapter);
		mTaskPri.setSelection((adapter.getCount() != 0) ? adapter.getCount() - 1 : 0);
		mTaskPri.setOnItemSelectedListener(mPriListener);
	}
    
    private void genCollabLst()
    {
    	if (catItem > 1)
    	{
        	// Creates an Array of category and their id for picker dialog
        	Cursor collabLst = tbDbHelper.fetchCollabsByCategory(catItem);
        	startManagingCursor(collabLst);
        	
        	collabIdLst = new long[collabLst.getCount()];
        	chosenCollabLst = new boolean[collabLst.getCount()];
        	
            if (collabLst.moveToFirst())  
            {                         
                for (int i = 0; i < collabLst.getCount(); i++)  
                {  
                    collabIdLst[i] = collabLst.getLong(0);
                    collabLst.moveToNext();  
                }             
            }  
            
            collabLst.close();	
    	}
    }
    
    protected Dialog onCreateDialog(int id)
    {
        Dialog dialog = null;
        AlertDialog.Builder builder;
        switch(id) {
        case DIALOG_CAT_ID:
        	builder = new AlertDialog.Builder(this);
        	builder.setTitle("Set Bucket");
        	builder.setPositiveButton("Edit Buckets", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	editCategory();
                }
            });
        	builder.setSingleChoiceItems(categories, 0, mCategoryListner);
        	//builder.setSingleChoiceItems(catLst, 0, TbDbAdapter.KEY_CAT_NAME, mCategoryListner2);
        	dialog = builder.create();
            break;
        case DIALOG_DUE_ID:
            // get the current date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
        	dialog =  new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
            break;
        case DIALOG_COLLAB_ID:
        	Cursor collbLst = tbDbHelper.fetchAllCategories();
        	startManagingCursor(collbLst);
        	builder = new AlertDialog.Builder(this);
        	builder.setTitle("Pick Task Collaborators");
        	builder.setMultiChoiceItems(collbLst, null, TbDbAdapter.KEY_CAT_NAME, mCollabListner);
        	//builder.setSingleChoiceItems(collbLst, 0, TbDbAdapter.KEY_CAT_NAME, mCollabListner2);
        	dialog = builder.create();
            break;
        default:
            dialog = null;
        }
        return dialog;
    }
    
    protected void onPrepareDialog(int id, Dialog dialog){
        switch(id) {
        case DIALOG_CAT_ID:
            break;
        case DIALOG_DUE_ID:
            // get the current date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
        	((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
            break;
        case DIALOG_COLLAB_ID:
            break;
        }
    }

    // updates the date in the TextView
    private void updateDueButton() 
    {
        String dateF = DateFormat.getDateInstance(DateFormat.FULL).format(new Date(mDate));
        mTaskDue.setText(dateF);
    }    
    
    private void editCategory() {
        Intent i = new Intent(this, CategoryEdit.class);
        startActivityForResult(i, ACTIVITY_MAKE_CAT);
        //startActivity(i);
    }
    
    
    // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
        new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
			{               
                mDate = (new Date(year - 1900, monthOfYear, dayOfMonth)).getTime();                
                updateDueButton();
			}
    };
    
    private DialogInterface.OnClickListener mCategoryListner = 
    	new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int item) {
	    	catItem = catIdLst[item];
	    	category = categories[item];
	    	mTaskCat.setText(category);
	        //Toast.makeText(getApplicationContext(), categories[item] + " " + item, Toast.LENGTH_SHORT).show();
	        dialog.dismiss();
	    }
	};
	
	private OnItemSelectedListener mPriListener = new OnItemSelectedListener()
    {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
            priItem = ((Cursor) mTaskPri.getSelectedItem()).getLong(0);
            //Toast.makeText(getBaseContext(),
            //    "You have selected item : " + ((Cursor) mTaskPri.getSelectedItem()).getLong(0),
            //    Toast.LENGTH_SHORT).show();  
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) 
		{
			
		}
    };
    
    private DialogInterface.OnMultiChoiceClickListener mCollabListner = 
    	new DialogInterface.OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which,
					boolean isChecked) {
				// TODO Auto-generated method stub
				//chosenCollabLst[which] = isChecked;
				Toast.makeText(getApplicationContext(), Integer.toString(which), Toast.LENGTH_SHORT).show();
			}
	};
	
    private DialogInterface.OnClickListener mCollabListner2 = 
    	new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int item) {
	    	Toast.makeText(getApplicationContext(), Integer.toString(item), Toast.LENGTH_SHORT).show();
	    }
	};
    
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
    		showDialog(DIALOG_CAT_ID);
    	}
    };
    
	private OnClickListener mTaskDueListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{
    		showDialog(DIALOG_DUE_ID);
    	}
    };
    
	private OnClickListener mCollabButtonListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{
    		showDialog(DIALOG_COLLAB_ID);
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
    	    	mTaskCat.setText(category);
    	        //Toast.makeText(getApplicationContext(), categories[item] + " " + item, Toast.LENGTH_SHORT).show();
    	        dialog.dismiss();
    	    }
    	});
    	alert = builder.create();
    }
    
    	
    private DialogInterface.OnClickListener mCategoryListner2 = 
    	new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), Integer.toString(which), Toast.LENGTH_SHORT).show();
			}
	};
    **/
    
}
