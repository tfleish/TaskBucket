package com.hag.bucketlst;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import db.TbDbAdapter;

public class MyTasks extends Activity{
  /** Called when the activity is first created. */
	
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    
    private ListView mLiveTaskList;
    private TbDbAdapter mDbHelper;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.old_main);

        //mDbHelper = new TbDbAdapter(this);
        //mDbHelper.open();
        
        mLiveTaskList = (ListView)findViewById(R.id.liveList);
        fillUnCheckedData();
        mLiveTaskList.setOnItemClickListener(new mLiveTaskClickL());
        mLiveTaskList.setOnCreateContextMenuListener(new mLiveTaskCreateL());
        
        //fillData();
        //registerForContextMenu(getListView());
    	Button b1 = (Button) findViewById(R.id.b1);
    	b1.setOnClickListener(mAddListener);
    }
    
    private void fillUnCheckedData() {
        Cursor taskCursor = mDbHelper.fetchAllTask();
        startManagingCursor(taskCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{TbDbAdapter.KEY_TASK_TITLE, TbDbAdapter.KEY_TASK_CATID};
        
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text1, R.id.cat};

        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter notes = 
        	    new SimpleCursorAdapter(this, R.layout.n_task_row, taskCursor, from, to);
        mLiveTaskList.setAdapter(notes);
    }


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
            createNote();
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
    	case DELETE_ID:
    		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	        mDbHelper.deleteTask(info.id);
	        //fillData();
	        return true;
		}
		return super.onContextItemSelected(item);
	}

    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, 
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //fillData();
    }
    
    private void createNote() {
        Intent i = new Intent(this, NTaskEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }
    
    // Create an anonymous implementation of OnClickListener
    private OnClickListener mAddListener = new OnClickListener()
    {
		public void onClick(View v) {
            createNote();
		}
    };
    
	private class mLiveTaskClickL implements OnItemClickListener 
    {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
	        Intent i = new Intent(getApplicationContext(), NTaskEdit.class);
	        i.putExtra(TbDbAdapter.KEY_TASK_LOCID, id);
	        startActivityForResult(i, ACTIVITY_EDIT);			
		}
    }
	
    private class mLiveTaskCreateL implements OnCreateContextMenuListener
    {

		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) 
		{
				menu.add(0, DELETE_ID, 0, R.string.menu_delete);			
		}
	}
}
