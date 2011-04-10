package com.hag.bucketlst.Old;

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
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.hag.bucketlst.R;
import com.hag.bucketlst.activity.NTaskEdit;
import com.hag.bucketlst.db.TbDbAdapter;
import com.hag.bucketlst.views.CheckedTasks;
import com.hag.bucketlst.views.LiveTasks;

public class CopyOfMyTasks extends Activity{
  /** Called when the activity is first created. */
	
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    
    private TbDbAdapter mDbHelper;
    private LiveTasks unCheckedTasks;
    private CheckedTasks checkedTasks;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.test_tasks);
        //this.title.setText("bucketLST");
        mDbHelper = new TbDbAdapter(this);
        mDbHelper.open();
        unCheckedTasks = (LiveTasks) findViewById(R.id.liveList);
        checkedTasks = (CheckedTasks) findViewById(R.id.doneList);
        //fillData();
        //registerForContextMenu(getListView());
    }
    
    private void fillData() {
        Cursor taskCursor = mDbHelper.fetchAllTask();
        startManagingCursor(taskCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{TbDbAdapter.KEY_TASK_TITLE, TbDbAdapter.KEY_TASK_CATID};
        
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text1, R.id.cat};

        // Now create a simple cursor adapter and set it to display
        //SimpleCursorAdapter notes = 
        //	    new SimpleCursorAdapter(this, R.layout.n_task_row, taskCursor, from, to);
        //setListAdapter(notes);
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
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}

    @Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
    	case DELETE_ID:
    		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	        mDbHelper.deleteTask(info.id);
	        fillData();
	        return true;
		}
		return super.onContextItemSelected(item);
	}
    
    private void createNote() {
        Intent i = new Intent(this, NTaskEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, 
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
    
    // Create an anonymous implementation of OnClickListener
    private OnClickListener mAddListener = new OnClickListener()
    {
		public void onClick(View v) {
            createNote();
		}
    };
}
