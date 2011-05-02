package com.hag.bucketlst.activity;

// test push from eclipse in testBranch?
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.hag.bucketlst.R;
import com.hag.bucketlst.adapter.CollabAdapter;
import com.hag.bucketlst.application.BLApp;
import com.hag.bucketlst.customWindows.CustomWindow;
import com.hag.bucketlst.db.TbDbAdapter;

public class NCollabView extends CustomWindow {
    /** Called when the activity is first created. */
    
    private Long mCatId;
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
        
        mCatId = (savedInstanceState != null) ? savedInstanceState.getLong(TbDbAdapter.KEY_CAT_ID) 
				  : null;
		if (mCatId == null) 
		{
			Bundle extras = getIntent().getExtras();  
			mCatId = (extras != null) ? extras.getLong(TbDbAdapter.KEY_CAT_ID) 
						  : null;
		}
        
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
        String catTitle = mDbHelper.fetchCategory(mCatId).getString(0);
        this.title.setText(catTitle);
    	
    	mCollabCursor = mDbHelper.fetchAllCollaborators();
        startManagingCursor(mCollabCursor);
        
        mCollabAdapter = new CollabAdapter(this, R.layout.n_collab_row, mCollabCursor, mCatId);
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
    	String nameToAdd = mCollabName.getText().toString();
    	Toast.makeText(getApplicationContext(), nameToAdd + " isn't in the database. Feature coming soon", Toast.LENGTH_SHORT).show();
    }
    
    private void updateCollabView()
    {
    	mCollabCursor.requery();
	    //initCatData();
    }
    
    private void deleteCollaborator(long rowId)
    {
    	mDbHelper.deleteCollaborator(rowId);
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
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
		if (mCatId != null) {
	        outState.putLong(TbDbAdapter.KEY_CAT_ID, mCatId);
		}
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
	    	try
	    	{
	    		createCollaborator();
	    	}
	    	catch (Exception ex)
	    	{
	    		Toast.makeText(getApplicationContext(), "Please type a username", Toast.LENGTH_SHORT).show();
	    	}
		}
	};

	public void onDelClick(final long l) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Delete Collaborator?");
    	builder.setMessage("This Collaborator will be removed from all tasks.");
    	builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	deleteCollaborator(l);
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

	public void onCheck(long l, boolean isChecked) {
		if (isChecked){
			mDbHelper.addUser2Cat(mCatId, l);
		} else {
			mDbHelper.removeUserCat(mCatId, l);
		}
	}

}
