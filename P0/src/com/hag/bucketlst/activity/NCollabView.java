package com.hag.bucketlst.activity;

// test push from eclipse in testBranch?
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.hag.bucketlst.R;
import com.hag.bucketlst.adapter.CollabAdapter;
import com.hag.bucketlst.application.BLApp;
import com.hag.bucketlst.db.TbDbAdapter;

public class NCollabView extends Activity {
    /** Called when the activity is first created. */
	
    private static final int ACTIVITY_VOICE_RECOGNITION_TASK = 1002;
    //private static final int ACTIVITY_BUCK_VIEW = 10;
    
    private EditText mCollabName;
    private ListView mCollabList;
    private TbDbAdapter mDbHelper;
    private Cursor mCollabCursor;
    private CollabAdapter mCollabAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collab_edit);
        
        mDbHelper = BLApp.getHelper();
        mDbHelper.open();
        
        mCollabName = (EditText)findViewById(R.id.collabUnameGet);
        mCollabList = (ListView)findViewById(R.id.collabList);
        
        ImageButton mCollabAdd = (ImageButton) findViewById(R.id.collabAdd);
        mCollabAdd.setOnClickListener(mAddListener);
        
        initCollabData();
    }

    // ********************************************************
    // * HELPER METHODS
    // * 
    // * 
    // * 
    // ********************************************************
    
    private void initCollabData() {
    	//mCollabList.setOnItemClickListener(new mCollabClickL());
        
    	mCollabCursor = mDbHelper.fetchAllCategories();
        startManagingCursor(mCollabCursor);
        
        mCollabAdapter = new CollabAdapter(this, R.layout.n_collab_row, mCollabCursor);
        mCollabList.setAdapter(mCollabAdapter);
    }    
    
    
    private void createCollaborator() {
    	/**
    	String catNameFin = BLApp.wordfix(mCatName.getText());
    	mDbHelper.makeCategory(catNameFin);
    	mCatName.setText("");
        InputMethodManager mgr = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(mCatName.getApplicationWindowToken(), 0);
    	updateCatView();
    	**/
    	Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
    }
    
    private void updateCollabView()
    {
    	mCollabCursor.requery();
	    //initCatData();
    }
    
    private void deleteBucket(long rowId)
    {
    	mDbHelper.deleteCategory(rowId);
    	updateCollabView();
    }

    // ********************************************************
    // * LIFE CYCLE
    // * 
    // * 
    // * 
    // ********************************************************
    
    @Override
    protected void onResume() {
       super.onResume();
    }
    
    // ********************************************************
    // * LISTENERS
    // * 
    // * 
    // * 
    // ********************************************************
    
	private class mCollabClickL implements OnItemClickListener 
    {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
	        Intent i = new Intent(getApplicationContext(), ByBuckets.class);
	        i.putExtra(TbDbAdapter.KEY_CAT_ID, id);
	        startActivity(i);
	        //startActivityForResult(i, ACTIVITY_BUCK_VIEW);
		}
    }

    // Create an anonymous implementation of OnClickListener
    private OnClickListener mAddListener = new OnClickListener()
    {
		public void onClick(View v)
		{
	    	try
	    	{
	    		createCollaborator();
	    	}
	    	catch (Exception ex)
	    	{
	    		Toast.makeText(getApplicationContext(), "Please Add a Bucket", Toast.LENGTH_SHORT).show();
	    	}
		}
	};

	public void onDelClick(final long l) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Delete Bucket?");
    	builder.setMessage("All tasks associated with this bucket will also be deleted.");
    	builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	deleteBucket(l);
            }
        });
    	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	dialog.cancel();
            }
        });

    	Dialog dialog = builder.create();
    	dialog.show();
	}

	public void onEditClick(long l) {
		Toast.makeText(getApplicationContext(), "Edit", Toast.LENGTH_SHORT).show();	
	}

	public void onCheck(long l, boolean isChecked) {
		// TODO Auto-generated method stub
		
	}

}
