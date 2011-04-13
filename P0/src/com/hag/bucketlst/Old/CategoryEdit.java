package com.hag.bucketlst.Old;

// test push from eclipse in testBranch?
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.hag.bucketlst.R;
import com.hag.bucketlst.customWindows.CustomList;
import com.hag.bucketlst.db.CDBAdapter;

public class CategoryEdit extends CustomList {
    /** Called when the activity is first created. */
	
    private CDBAdapter db;
    private EditText Category;
    private static final int DELETE_ID = Menu.FIRST;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cedit);
		this.title.setText("Category Info");
		
        db = new CDBAdapter(this);
        db.open();
		
        fillData();
        registerForContextMenu(getListView());
        
    	// Create an anonymous implementation of OnClickListener
    	Button b2 = (Button) findViewById(R.id.b2);
    	b2.setOnClickListener(mAddListener);
    }

    private void fillData() {
        Cursor catCursor = db.getAllCategory();
        startManagingCursor(catCursor);
        catCursor.moveToNext();
        
        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{CDBAdapter.KEY_CATEGORY};
        
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text1};

        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter notes = 
        	    new SimpleCursorAdapter(this, R.layout.cat_rows, catCursor, from, to);
        setListAdapter(notes);
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
	        db.deleteCat(info.id);
	        fillData();
	        return true;
		}
		return super.onContextItemSelected(item);
	}
    
    @Override
    protected void onPause() {
        super.onPause();
	    setResult(RESULT_OK);
	    finish();
    }
    
    @Override
    protected void onResume() {
       super.onResume();
       //fillData();
    }
    
    // Create an anonymous implementation of OnClickListener
    private OnClickListener mAddListener = new OnClickListener()
    {
		public void onClick(View v)
		{
	    	//db.open();
	    	long id = 0;
	    	// do something when the button is clicked
	    	try
	    	{
	    		Category = (EditText)findViewById(R.id.catGet);
	    		db.insertCategory(Category.getText().toString());
	    		
	    		fillData();
	
	    		/**
	    		id = db.getAllEntriesCount();
	
	    		Context context = getApplicationContext();
	    		CharSequence text = "The quote '" + Category.getText() + "' was added successfully!\nQuotes Total = " + id;
	    		int duration = Toast.LENGTH_LONG;
	
	    		Toast toast = Toast.makeText(context, text, duration);
	    		toast.show();
	    		 **/
	    		Category.setText("");

	    	}
	    	catch (Exception ex)
	    	{
	    		Context context = getApplicationContext();
	    		CharSequence text = ex.toString() + "ID = " + id;
	    		int duration = Toast.LENGTH_LONG;
	
	    		Toast toast = Toast.makeText(context, text, duration);
	    		toast.show();
	    	}
		}
	};

}
