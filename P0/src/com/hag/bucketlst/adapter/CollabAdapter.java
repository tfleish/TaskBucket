package com.hag.bucketlst.adapter;

import java.util.HashSet;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.hag.bucketlst.R;
import com.hag.bucketlst.activity.NCollabView;
import com.hag.bucketlst.application.BLApp;
import com.hag.bucketlst.db.TbDbAdapter;

public class CollabAdapter extends ResourceCursorAdapter 
{
	private Long catId;
	private HashSet<Long> checkedCollabs;
	private NCollabView mContext;
	private TbDbAdapter mDbHelper;
	
	public CollabAdapter(Context context, int layout, Cursor c, Long cId) {
		super(context, layout, c);
		mContext = (NCollabView) context;
		mDbHelper = BLApp.getHelper();
		checkedCollabs = new HashSet<Long>();
		catId = cId;
		populateCheckedCollabs();
	}
	
	public CollabAdapter(Context context, int layout, Cursor c,
			boolean autoRequery, Long cId) {
		super(context, layout, c, autoRequery);
		mContext = (NCollabView)context;
		mDbHelper = BLApp.getHelper();
		checkedCollabs = new HashSet<Long>();
		catId = cId;
		populateCheckedCollabs();
	}
	
	private void populateCheckedCollabs(){
		Cursor mCatCursor = mDbHelper.fetchCollabsByCategory(catId);
		mContext.startManagingCursor(mCatCursor);
		
        if (mCatCursor.moveToFirst())  
        {                         
            for (int i = 0; i < mCatCursor.getCount(); i++)  
            {  
                checkedCollabs.add(Long.valueOf(mCatCursor.getLong(0)));
                mCatCursor.moveToNext();  
            }             
        }         
        mCatCursor.close();
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
		
		long uId = cursor.getLong(cursor.getColumnIndexOrThrow(TbDbAdapter.KEY_USER_ID));
		int uuId = (int) uId;
		
		userChooseV.setOnCheckedChangeListener(null);
		userChooseV.setTag(uId);
		userDelV.setTag(uId);	

		if(uuId != 1){
			if (checkedCollabs.contains(uId)){
				userChooseV.setChecked(true);
			}
			userChooseV.setOnCheckedChangeListener(new mCollChooseL());
			userChooseV.setFocusable(false);
			userChooseV.setFocusableInTouchMode(false);
			
			userDelV.setVisibility(View.VISIBLE);
			userDelV.setOnClickListener(new mCatDelL());
			userDelV.setFocusable(false);
			userDelV.setFocusableInTouchMode(false);
		} else {
			userChooseV.setChecked(true);
			userChooseV.setFocusable(true);
			userChooseV.setFocusableInTouchMode(true);
			userChooseV.setClickable(false);
			userDelV.setVisibility(View.INVISIBLE);
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
    
    private class mCollChooseL implements CompoundButton.OnCheckedChangeListener
    {
		@Override
		public void onCheckedChanged(CompoundButton v, boolean isChecked) {
		    long l = ((Long)v.getTag()).longValue();
		    mContext.onCheck(l, isChecked);
		}
	}
}
