package com.hag.bucketlst.activity;

import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import com.hag.bucketlst.R;
import com.hag.bucketlst.adapter.ByBucketAdapter;
import com.hag.bucketlst.application.BLApp;
import com.hag.bucketlst.customWindows.CustomWindow;
import com.hag.bucketlst.db.TbDbAdapter;

public class ByBuckets extends CustomWindow{
  /** Called when the activity is first created. */
	
	private static final int ACTIVITY_VOICE_RECOGNITION_TASK = 1001;
    private static final int ACTIVITY_BUCKET_CREATE = 2;
    private static final int ACTIVITY_BUCKET_EDIT = 3;
    
    private static final int INSERT_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;
    
    private Long mCatId;
    private EditText mTaskTitle;
    private ListView mLiveTaskList;
    private TbDbAdapter mDbHelper;
    private Cursor mTaskCursor;
    private ByBucketAdapter mTasksAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.by_buckets);

        //mDbHelper = new TbDbAdapter(this);
        mDbHelper = BLApp.getHelper();
        mDbHelper.open();
        
        mTaskTitle = (EditText)findViewById(R.id.taskGet);
        mLiveTaskList = (ListView)findViewById(R.id.liveList);
        
        mCatId = (savedInstanceState != null) ? savedInstanceState.getLong(TbDbAdapter.KEY_CAT_ID) 
				  : null;
		if (mCatId == null) 
		{
			Bundle extras = getIntent().getExtras();  
    		mCatId = (extras != null) ? extras.getLong(TbDbAdapter.KEY_CAT_ID) 
    				: null;    		
		}	
        
        //registerForContextMenu(getListView());
        ImageButton mTaskAdd = (ImageButton) findViewById(R.id.mainTaskAdd);
        mTaskAdd.setOnClickListener(mAddListener);
        ImageButton mTaskSpeak = (ImageButton)findViewById(R.id.speakNowTitle);
        // Check to see if a recognition activity is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
        	mTaskSpeak.setOnClickListener(mAutoTitle);
        } else {
        	mTaskSpeak.setEnabled(false);
        }
        initUnCheckedData();
    }
        
    // ********************************************************
    // * HELPERS
    // * 
    // * 
    // * 
    // ********************************************************  
    
    private void initUnCheckedData() 
    {    	
    	String catName = mDbHelper.fetchCategory(mCatId).getString(0);
    	this.title.setText(catName);
    	
        mLiveTaskList.setOnItemClickListener(new mLiveTaskClickL());
        mLiveTaskList.setOnCreateContextMenuListener(new mLiveTaskCreateL());
        
        mTaskCursor = mDbHelper.fetchTaskByCategory(mCatId);
        startManagingCursor(mTaskCursor);
        
        mTasksAdapter = new ByBucketAdapter(this, R.layout.n_bucket_row, mTaskCursor);
        mLiveTaskList.setAdapter(mTasksAdapter);
    }
    
    private void createTask() {
        String taskNameFin = BLApp.wordfix(mTaskTitle.getText());
        
        Intent i = new Intent(this, NTaskEdit.class);
        i.putExtra("TitleFromPrevIntent", taskNameFin);
        i.putExtra("DefaultCategory", mCatId);
        i.putExtra("ReqCodeFromPrevIntent", ACTIVITY_BUCKET_CREATE);
        startActivityForResult(i, ACTIVITY_BUCKET_CREATE);
    }
 	
    private void startVoiceRecognitionTitle()
    {
		Intent localIntent1 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		localIntent1.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		localIntent1.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your Task");
		startActivityForResult(localIntent1, ACTIVITY_VOICE_RECOGNITION_TASK);
    }
    
    private void updateLiveList()
    {
    	//mTaskCursor.requery();
	    initUnCheckedData();
    }
    
    // ********************************************************
    // * LISTENERS
    // * 
    // * 
    // * 
    // ********************************************************
    
    // Create an anonymous implementation of OnClickListener
    private OnClickListener mAddListener = new OnClickListener()
    {
		public void onClick(View v) 
		{            
    		if (mTaskTitle.length() != 0)
    		{
    			createTask();
    		}
    		else
    		{
    			Toast.makeText(getApplicationContext(), "Please Add a Task", Toast.LENGTH_SHORT).show();
    		}
		}
    };
    
	private class mLiveTaskClickL implements OnItemClickListener 
    {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
	        Intent i = new Intent(getApplicationContext(), NTaskEdit.class);
	        i.putExtra(TbDbAdapter.KEY_TASK_LOCID, id);
	        i.putExtra("ReqCodeFromPrevIntent", ACTIVITY_BUCKET_EDIT);	        
	        startActivityForResult(i, ACTIVITY_BUCKET_EDIT);			
		}
    }
	
    private class mLiveTaskCreateL implements OnCreateContextMenuListener
    {

		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) 
		{
				menu.add(0, EDIT_ID, 0, R.string.menu_edit);							
		}
	}

	public void onTouch(long l)
	{
		Toast.makeText(getApplicationContext(), "Tasks can only be modified from your view", Toast.LENGTH_LONG).show();
	}
	
	private OnClickListener mAutoTitle = new OnClickListener()
    {
    	public void onClick(View v)
    	{
    		startVoiceRecognitionTitle();
    	}
    };
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
        case INSERT_ID:
            createTask();
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch(item.getItemId())
		{
    	case EDIT_ID:
    		info = (AdapterContextMenuInfo) item.getMenuInfo();
	        Intent i = new Intent(getApplicationContext(), NTaskEdit.class);
	        i.putExtra(TbDbAdapter.KEY_TASK_LOCID, info.id);
	        startActivityForResult(i, ACTIVITY_BUCKET_EDIT);	
	        return true;        	        
		}
		return super.onContextItemSelected(item);
	}

    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, 
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if ((requestCode == ACTIVITY_VOICE_RECOGNITION_TASK) && (resultCode == RESULT_OK))
        {
          String str = (String)intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
          String strCon = mTaskTitle.getText() + str;
          mTaskTitle.setText(strCon);
        }
        
        if ((requestCode == ACTIVITY_BUCKET_CREATE) && (resultCode == RESULT_OK))
        {
            updateLiveList();
            mTaskTitle.setText("");
        }
        
        if ((requestCode == ACTIVITY_BUCKET_EDIT) && (resultCode == RESULT_OK))
        {
            updateLiveList();
        }
    }
        
    // ********************************************************
    // * ACTIVITY LIFE CYCLE
    // * 
    // * 
    // * 
    // ********************************************************
    
    @Override
    protected void onResume() {
       super.onResume();
	   updateLiveList();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
		if (mCatId != null) {
	        outState.putLong(TbDbAdapter.KEY_CAT_ID, mCatId);
		} 
    }
}
