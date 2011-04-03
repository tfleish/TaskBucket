package views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.hag.bucketlst.NTaskEdit;
import com.hag.bucketlst.R;

import db.TbDbAdapter;

public class LiveTasks extends ListView {

	private static final int DELETE_ID = Menu.FIRST;
	private static final int ACTIVITY_EDIT=1;
	
	private Context mContext;
	private TbDbAdapter tbDbHelper;
	private Cursor mUncheckedTaskCursor;

	
	public LiveTasks(Context context) {
		super(context);
		mContext = context;
		//tbDbHelper = new TbDbAdapter(context);
		//fillData();
	}

	public LiveTasks(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		//tbDbHelper = new TbDbAdapter(context);
		//fillData();
	}

	public LiveTasks(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = (Activity) context;
		//tbDbHelper = new TbDbAdapter(context);
		//fillData();
	}

	
	private void fillData() {
	
		//setOnItemClickListener(this);
		//setOnCreateContextMenuListener(this);
        mUncheckedTaskCursor = tbDbHelper.fetchAllTask();
        ((Activity) mContext).startManagingCursor(mUncheckedTaskCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{TbDbAdapter.KEY_TASK_TITLE, TbDbAdapter.KEY_TASK_CATID};
        
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text1, R.id.cat};

        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter notes = 
        	    new SimpleCursorAdapter(mContext, R.layout.n_task_row, mUncheckedTaskCursor, from, to);
        setAdapter(notes);
		
	}


	/**
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	    long l = ((Long)buttonView.getTag()).longValue();
	    Toast.makeText(mContext, Long.toString(l), Toast.LENGTH_SHORT).show();
	}

    @Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
    {
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}


	@Override
	public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Intent i = new Intent(mContext, NTaskEdit.class);
        i.putExtra(TbDbAdapter.KEY_TASK_LOCID, id);
        ((Activity) mContext).startActivityForResult(i, ACTIVITY_EDIT);
	}
	**/
}
