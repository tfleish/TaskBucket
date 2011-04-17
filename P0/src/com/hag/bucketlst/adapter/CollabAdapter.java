package com.hag.bucketlst.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.hag.bucketlst.R;
import com.hag.bucketlst.activity.NCollabView;
import com.hag.bucketlst.db.TbDbAdapter;

public class CollabAdapter extends ResourceCursorAdapter 
{
	private NCollabView mContext;
	//private TbDbAdapter mDbHelper;
	
	public CollabAdapter(Context context, int layout, Cursor c) {
		super(context, layout, c);
		mContext = (NCollabView) context;
		//mDbHelper = BLApp.getHelper();
	}
	
	public CollabAdapter(Context context, int layout, Cursor c,
			boolean autoRequery) {
		super(context, layout, c, autoRequery);
		mContext = (NCollabView)context;
		//mDbHelper = BLApp.getHelper();
	}
	
	private void populateCheckedCollabs(){
		
	}

	@Override
	public void bindView(View view, Context content, Cursor cursor)
	{
		TextView dispNameV = (TextView)view.findViewById(R.id.CollabDispName);
		TextView userNameV = (TextView)view.findViewById(R.id.CollabUserName);
		CheckBox userChooseV = (CheckBox)view.findViewById(R.id.collabCheck);
		ImageButton userDelV = (ImageButton)view.findViewById(R.id.collabDelete);	
		
		String dispNameC = cursor.getString(cursor.getColumnIndexOrThrow(TbDbAdapter.KEY_USER_DISPLAYNAME));
		dispNameV.setText(dispNameC);
		
		String userNameC = cursor.getString(cursor.getColumnIndexOrThrow(TbDbAdapter.KEY_USER_NAME));
		userNameV.setText(userNameC);
		
		userChooseV.setOnCheckedChangeListener(null);
		
		
		
		int checkEditable = cursor.getInt(cursor.getColumnIndexOrThrow(TbDbAdapter.KEY_CAT_ISEDITABLE));
		boolean isEditable = (checkEditable == 1) ? true : false;
		
		long catId = cursor.getLong(cursor.getColumnIndexOrThrow(TbDbAdapter.KEY_CAT_ID));
		
		editCat.setTag(catId);
		deleteCat.setTag(catId);

		if(isEditable){
			editCat.setVisibility(View.VISIBLE);
			editCat.setOnClickListener(new mCatEditL());
			editCat.setFocusable(false);
			editCat.setFocusableInTouchMode(false);
			
			deleteCat.setVisibility(View.VISIBLE);
			deleteCat.setOnClickListener(new mCatDelL());
			deleteCat.setFocusable(false);
			deleteCat.setFocusableInTouchMode(false);
		} else {
			editCat.setVisibility(View.INVISIBLE);
			deleteCat.setVisibility(View.INVISIBLE);
		}
	}
	
    private class mCatDelL implements OnClickListener
    {
		@Override
		public void onClick(View v) {
		    long l = ((Long)v.getTag()).longValue();
		    mContext.onDelClick(l);
		}
	}
    
    private class mCatEditL implements OnClickListener
    {
		@Override
		public void onClick(View v) {
		    long l = ((Long)v.getTag()).longValue();
		    mContext.onEditClick(l);
		}
	}
}
