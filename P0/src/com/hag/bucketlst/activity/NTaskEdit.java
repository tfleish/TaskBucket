package com.hag.bucketlst.activity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import com.hag.bucketlst.R;
import com.hag.bucketlst.application.BLApp;
import com.hag.bucketlst.customWindows.CustomTab;
import com.hag.bucketlst.db.TbDbAdapter;

public class NTaskEdit extends CustomTab {
	
	private static final int ACTIVITY_VOICE_RECOGNITION_TITLE = 1000;
	private static final int ACTIVITY_VOICE_RECOGNITION_NOTES = 2000;
    private static final int ACTIVITY_MAKE_CAT = 3000;
    
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    
	private final int DIALOG_CAT_ID = 0;
	private final int DIALOG_DUE_ID = 1;
	
	private EditText mTaskText;
	private EditText mTaskNotes;
	private Button mTaskCat;
	private Button mTaskDue;
	private Button mTaskCollab;
	private Button mTaskDone;
	private Button mTaskDone1;
	private Spinner mTaskPri;
	private ImageButton speakNowTitle;
	private ImageButton speakNowNote;
	
	//private DatePickerDialog.OnDateSetListener mDateSetListener;
	private long mDate;
    private long catItem;
    private long[] catIdLst;
    private long priItem;
    private String category;
    private String[] categories;
    private String[] collabNameLst;
    
    private long[] collabIdLst;
    private boolean[] chosenCollabLst;
    private HashSet<Long> checkedCollabs;
    private HashSet<Long> newCollabs;

    private Long mTaskId;	
    private long webId = 0;
    private int isChecked = 0;
    private int isMine = 1;
    private int isDeleted = 0;
    private int isSynced = 0;
    private int isUptodate = 0;
    private int isVersion = 0;
    
    private String voiceTitleResult;
    private String voiceNoteResult;
    private String initTitle;

    private TbDbAdapter tbDbHelper;

    //private Spinner mCatText;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        //this.title.setText("task Info");
        setContentView(R.layout.ntedit);
        this.buttonView.setVisibility(View.INVISIBLE);
        
        // initializes the view tabs
        TabHost mTabHost = getTabHost();
        mTabHost.addTab(mTabHost.newTabSpec("Task Information").setIndicator("Task Info").setContent(R.id.infoTab));
        mTabHost.addTab(mTabHost.newTabSpec("Notes").setIndicator("My Notes").setContent(R.id.notesTab));       
        mTabHost.setCurrentTab(0);
        
        // initializes the database
        //tbDbHelper = new TbDbAdapter(this);
        tbDbHelper = BLApp.getHelper();
        tbDbHelper.open();

        // gets each input/output fields from view
		mTaskText = (EditText)findViewById(R.id.titleGet);	
		mTaskNotes = (EditText)findViewById(R.id.myNotes);
		mTaskCat = (Button)findViewById(R.id.newCategory);
		mTaskDue = (Button)findViewById(R.id.dueDate); 
		mTaskCollab = (Button)findViewById(R.id.taskCollabs);
		mTaskDone = (Button)findViewById(R.id.taskDone);
		mTaskDone1 = (Button)findViewById(R.id.taskDone1);
		mTaskPri = (Spinner)findViewById(R.id.newPri);
		speakNowTitle = (ImageButton)findViewById(R.id.speakNowTitle);
		speakNowNote = (ImageButton)findViewById(R.id.speakNowNote);
		
		genCatLst();
		genPriLst();
		checkedCollabs = new HashSet<Long>();
		newCollabs = new HashSet<Long>();
		
        mTaskId = (savedInstanceState != null) ? savedInstanceState.getLong(TbDbAdapter.KEY_TASK_LOCID) 
				  : null;
		if (mTaskId == null) 
		{
			Bundle extras = getIntent().getExtras();  
			int ReqCode = extras.getInt("ReqCodeFromPrevIntent");
			switch(ReqCode)
			{
	    	case ACTIVITY_EDIT:
				mTaskId = (extras != null) ? extras.getLong(TbDbAdapter.KEY_TASK_LOCID) 
						  : null;
	    	case ACTIVITY_CREATE:
				initTitle = (savedInstanceState != null) ? savedInstanceState.getString("TitleFromPrevIntent")
						: extras.getString("TitleFromPrevIntent");
			}
		}		
		
		populateFields();
		
        mTaskDue.setOnClickListener(mTaskDueListener);
        mTaskCat.setOnClickListener(mEditListener);
        mTaskCollab.setOnClickListener(mCollabButtonListener);
        mTaskDone.setOnClickListener(mAddListener);
        mTaskDone1.setOnClickListener(mAddListener);
        
        // Check to see if a recognition activity is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            speakNowTitle.setOnClickListener(mAutoTitle);
            speakNowNote.setOnClickListener(mAutoNote);
        } else {
            speakNowTitle.setEnabled(false);
            speakNowNote.setEnabled(false);
        }
    }
    
    // ********************************************************
    // * HELPER METHODS
    // * 
    // * 
    // * 
    // ********************************************************
    
    private void populateFields() {    	
        if (mTaskId != null) {
            
            Cursor task = tbDbHelper.fetchTask(mTaskId);
            startManagingCursor(task);
                
            String localTitle = task.getString(task.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_TITLE));            
            String localNotes = task.getString(task.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_NOTES));
            webId = task.getLong(task.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_WEBID));
            catItem = task.getLong(task.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_CATID));
            mDate = task.getLong(task.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_DUE));
            priItem = task.getLong(task.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_PRIORITY));
            isChecked = task.getInt(task.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_ISCHECKED));
            isMine = task.getInt(task.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_ISMINE));
            isDeleted = task.getInt(task.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_ISDELETED));
            isSynced = task.getInt(task.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_ISSYNCED));
            isUptodate = task.getInt(task.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_ISUPTODATE));
            isVersion = task.getInt(task.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_VERSION));
                        
            if (voiceTitleResult == null){
            	mTaskText.setText(localTitle);
            	this.title.setText(localTitle);
            } else {
            	mTaskText.setText(voiceTitleResult);
            	this.title.setText(voiceTitleResult);
            	voiceTitleResult = null;
            }
            if (voiceNoteResult == null){
                mTaskNotes.setText(localNotes);
            } else {
            	mTaskNotes.setText(voiceNoteResult);
            	voiceNoteResult = null;
            }

            long collabCount = tbDbHelper.countCollaboratorsByCategory(catItem);
            if (collabCount > 1){
                mTaskCat.setClickable(false);                
                mTaskCat.setEnabled(false);	
            }

            category = tbDbHelper.fetchCategory(catItem).getString(0);
            mTaskCat.setText(category);
            updateDueButton();
            mTaskPri.setSelection(((int) priItem) - 1);
            task.close();
        } else {
            if (voiceTitleResult != null){
            	mTaskText.setText(voiceTitleResult);
            	this.title.setText(voiceTitleResult);
            	voiceTitleResult = null;
            }
            if (voiceNoteResult != null){
            	mTaskNotes.setText(voiceNoteResult);
            	voiceNoteResult = null;
            }
            if (initTitle != null){
            	mTaskText.setText(initTitle);
            	this.title.setText(initTitle);
            	initTitle = null;
            }        
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
    	// sets default value for the priority
    	priItem = tbDbHelper.getDefaultPriorityId();
    	
    	//sets up the priority spinner
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
		mTaskPri.setSelection(adapter.getCount() - 1);
		mTaskPri.setOnItemSelectedListener(mPriListener);
	}
    
    private void populateCheckedCollabs(){
    	if (mTaskId != null){
        	Cursor checkedCollabLst = tbDbHelper.fetchCollabsByTask(mTaskId);
        	startManagingCursor(checkedCollabLst);
        	
            if (checkedCollabLst.moveToFirst())  
            {                         
                for (int i = 0; i < checkedCollabLst.getCount(); i++)  
                {  
                	checkedCollabs.add(checkedCollabLst.getLong(0));
                	checkedCollabLst.moveToNext();  
                }             
            }  
            
            checkedCollabLst.close();		
    	}
    }
    
    private void genCollabLst()
    {
    	populateCheckedCollabs();
    	if (catItem > 0)
    	{
        	// Creates an Array of category and their id for picker dialog
        	Cursor collabLst = tbDbHelper.fetchCollabsByCategory(catItem);
        	startManagingCursor(collabLst);
        	
        	collabIdLst = new long[collabLst.getCount()];
        	collabNameLst = new String[collabLst.getCount()];
        	chosenCollabLst = new boolean[collabLst.getCount()];
        	
            if (collabLst.moveToFirst())  
            {                         
                for (int i = 0; i < collabLst.getCount(); i++)  
                {  
                	Long theCollab = collabLst.getLong(0);
                    collabIdLst[i] = theCollab;
                    chosenCollabLst[i] = (checkedCollabs.contains(theCollab))? true : false;
                    collabNameLst[i] = tbDbHelper.fetchUserInfoById(theCollab).getString(1);
                    collabLst.moveToNext();  
                }             
            }  
            
            collabLst.close();	
    	}
    }
    
    protected void updateNewCollabs()
    {
    	for (Long ncollabs: newCollabs){
    		tbDbHelper.addUser2Task(mTaskId, ncollabs);
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
        	/**
        	builder.setPositiveButton("Edit Buckets", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	editCategory();
                }
            });
            **/
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
        }
    }
    
    protected void createCollabDialog(){
    	genCollabLst();
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Pick Task Collaborators");
		builder.setMultiChoiceItems(collabNameLst, chosenCollabLst, mCollabListner);
		builder.create().show();
    }
    
    private void startVoiceRecognitionTitle()
    {
		Intent localIntent1 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		localIntent1.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		localIntent1.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your Task");
		startActivityForResult(localIntent1, ACTIVITY_VOICE_RECOGNITION_TITLE);
    }
    
    private void startVoiceRecognitionNotes()
    {
		Intent localIntent1 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		localIntent1.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		localIntent1.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your Notes");
		startActivityForResult(localIntent1, ACTIVITY_VOICE_RECOGNITION_NOTES);
    }

    // updates the date in the TextView
    private void updateDueButton() 
    {
    	if (mDate != 0){
    		String dateF = DateFormat.getDateInstance(DateFormat.FULL).format(new Date(mDate));
        	mTaskDue.setText(dateF);
    	}
    }    
    
    /**
    private void editCategory() {
        Intent i = new Intent(this, NCategoryView.class);
        startActivityForResult(i, ACTIVITY_MAKE_CAT);
    }
    **/
    
    private void saveState() {
    	long localWebId = webId;
        String localTitle = mTaskText.getText().toString();
        String localNotes = mTaskNotes.getText().toString();
        long localCatId = catItem;
        long localDueDate = mDate;
        long localPriority = priItem;
        int localChecked = isChecked;
        int localMine = isMine;
        int localDeleted = isDeleted;
        int localSynced = isSynced;
        int localUptodate = isUptodate;
        int localVersion = isVersion;

		if (mTaskText.length() != 0)
		{
	        if (mTaskId == null) {
	            long id = tbDbHelper.makeTask(localWebId, localTitle, localCatId, localNotes,
	            		localDueDate, localPriority, localChecked, localMine, localDeleted, 
	            		localSynced, localUptodate, localVersion);
	            if (id > 0) {
	                mTaskId = id;
	                updateNewCollabs();
	            }
	        } else {
	        	tbDbHelper.updateTaskImp(mTaskId, localTitle, localNotes,
	            	localDueDate, localPriority, localUptodate, localVersion);
	        }
		}
	    setResult(RESULT_OK);
	    finish();
    }
    
    // ********************************************************
    // * LISTENERS
    // * 
    // * 
    // * 
    // ********************************************************
    
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
			public void onClick(DialogInterface dialog, int i,
					boolean isChecked) {
				Long userId = collabIdLst[i];
				if (mTaskId != null){					
					if (isChecked){
						tbDbHelper.addUser2Task(mTaskId, userId);	
					} else {
						tbDbHelper.removeUserTask(mTaskId, userId);
					}
				} else {
					if (isChecked){
						newCollabs.add(userId);	
					} else {
						newCollabs.remove(userId);
					}
				}
			}
	};

	private OnClickListener mAddListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{		    
    		if (mTaskText.length() != 0)
    		{
        		/**
    			toast = Toast.makeText(context, "Task: " + mTaskText.getText().toString() + " added", duration);
    			toast.show();
    			**/
    			saveState();
    		}
    		else
    		{
    			Toast.makeText(getApplicationContext(), "Please Add a Task", Toast.LENGTH_LONG).show();
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
    		createCollabDialog();
    	}
    };
    
	private OnClickListener mAutoTitle = new OnClickListener()
    {
    	public void onClick(View v)
    	{
    		startVoiceRecognitionTitle();
    	}
    };
    
	private OnClickListener mAutoNote = new OnClickListener()
    {
    	public void onClick(View v)
    	{
    		startVoiceRecognitionNotes();
    	}
    };
    
    // ********************************************************
    // * ACTIVITY LIFE CYCLE
    // * 
    // * 
    // * 
    // ********************************************************
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
		if (mTaskId != null) {
	        outState.putLong(TbDbAdapter.KEY_TASK_LOCID, mTaskId);
		} 
		else {
			outState.putString("TitleFromPrevIntent", mTaskText.getText().toString());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
    	super.onActivityResult(requestCode, resultCode, intent);  
        if ((requestCode == ACTIVITY_VOICE_RECOGNITION_TITLE) && (resultCode == RESULT_OK))
        {
          String str = (String)intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
          voiceTitleResult = mTaskText.getText() + str;
        }
        
        if ((requestCode == ACTIVITY_VOICE_RECOGNITION_NOTES) && (resultCode == RESULT_OK))
        {
          String str = (String)intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
          voiceNoteResult = mTaskNotes.getText() + str;
        }
        
        if ((requestCode == ACTIVITY_MAKE_CAT) && (resultCode == RESULT_OK))
        {
    		//genCatLst();
    		//setupDialogue();
    		//alert.show();
        }
    
    }
}
