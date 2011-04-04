package com.hag.bucketlst;

import adapter.DoneTaskAdapter;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import db.TbDbAdapter;

public class MyDoneTasks extends Activity{
  /** Called when the activity is first created. */
    
    private ListView mDoneTaskList;
    private TbDbAdapter mDbHelper;
    private Cursor mDoneCursor;
    private DoneTaskAdapter mDoneAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.done_tasks);

        mDbHelper = new TbDbAdapter(this);
        mDbHelper.open();
        
        mDoneTaskList = (ListView)findViewById(R.id.checkedList);
        initCheckedData();
    }
    
    private void initCheckedData() 
    {    	
        mDoneCursor = mDbHelper.fetchAllCheckedTasks();
        startManagingCursor(mDoneCursor);
        
        mDoneAdapter = new DoneTaskAdapter(this, R.layout.d_task_row, mDoneCursor);
        mDoneTaskList.setAdapter(mDoneAdapter);
    }
    
    private void updateDoneList()
    {
    	mDoneCursor.requery();
    }

    public TbDbAdapter getDbHelper()
    {
    	return mDbHelper;
    }

	public void onCheck(long l, boolean isChecked)
	{
		int checkedInt = (isChecked) ? 1 : 0;
	    mDbHelper.updateIsChecked(l, checkedInt);
	    updateDoneList();
	}
	
	public void onClick(long l)
	{
		mDbHelper.deleteTask(l);
		updateDoneList();
	}
        
    @Override
    protected void onResume() {
       super.onResume();
	   updateDoneList();
    }
}
