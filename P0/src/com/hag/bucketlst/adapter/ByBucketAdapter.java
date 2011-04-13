package com.hag.bucketlst.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.hag.bucketlst.R;
import com.hag.bucketlst.activity.ByBuckets;
import com.hag.bucketlst.application.BLApp;
import com.hag.bucketlst.db.TbDbAdapter;

public class ByBucketAdapter extends ResourceCursorAdapter 
{
	private ByBuckets mContext;
	private TbDbAdapter mDbHelper;
	
	public ByBucketAdapter(Context context, int layout, Cursor c) {
		super(context, layout, c);
		mContext = (ByBuckets) context;
		mDbHelper = BLApp.getHelper();
	}
	
	public ByBucketAdapter(Context context, int layout, Cursor c,
			boolean autoRequery) {
		super(context, layout, c, autoRequery);
		mContext = (ByBuckets)context;
		mDbHelper = BLApp.getHelper();
	}

	@Override
	public void bindView(View view, Context content, Cursor cursor)
	{
		TextView titleV = (TextView)view.findViewById(R.id.text1);
		TextView categoryV = (TextView)view.findViewById(R.id.cat);
		ImageButton checkerV = (ImageButton)view.findViewById(R.id.taskCheck);
		ImageButton unCheckerV = (ImageButton)view.findViewById(R.id.taskUnCheck);
		ImageView priorityV = (ImageView)view.findViewById(R.id.priColor);
		ImageView collabsV = (ImageView)view.findViewById(R.id.plusOne);	
		ImageButton delV = (ImageButton)view.findViewById(R.id.delButton);
		
		String titleC = cursor.getString(cursor.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_TITLE));
		titleV.setText(titleC);
		long catItem = cursor.getLong(cursor.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_CATID));
		String categoryC = mDbHelper.fetchCategory(catItem).getString(0);
		long mDate = cursor.getLong(cursor.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_DUE));
    	if (mDate != 0){
    		String sDf = new SimpleDateFormat("MMM dd").format(new Date(mDate));
    		categoryC = sDf + " | " + categoryC;
    	}    	
		categoryV.setText(categoryC);
		int priRC = cursor.getInt(cursor.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_PRIORITY));
		int isChecked = cursor.getInt(cursor.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_ISCHECKED));
		boolean checkState = (isChecked == 1) ? true : false;
		int priRes = mDbHelper.getPriorityImage(priRC, checkState);
		priorityV.setBackgroundResource(priRes);
		long locId = cursor.getLong(cursor.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_LOCID));
		long collabCount = mDbHelper.countCollaboratorsByTask(locId);
		if(checkState){
			collabsV.setVisibility(View.GONE);
			unCheckerV.setVisibility(View.GONE);
			titleV.setPaintFlags(titleV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			categoryV.setPaintFlags(categoryV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			delV.setVisibility(View.VISIBLE);
			delV.setTag(locId);
			delV.setOnClickListener(new mTaskButtonL());
			checkerV.setTag(locId);			
			checkerV.setVisibility(View.VISIBLE);
			checkerV.setOnClickListener(new mTaskButtonL());
			checkerV.setFocusable(false);
			checkerV.setFocusableInTouchMode(false);
		} else {		
			delV.setVisibility(View.GONE);
			checkerV.setVisibility(View.GONE);
			if(collabCount > 1){
				collabsV.setVisibility(View.VISIBLE);
			}
			unCheckerV.setTag(locId);
			unCheckerV.setVisibility(View.VISIBLE);
			unCheckerV.setOnClickListener(new mTaskButtonL());
			unCheckerV.setFocusable(false);
			unCheckerV.setFocusableInTouchMode(false);	
		}
	}
	
    
    private class mTaskButtonL implements OnClickListener
    {
		@Override
		public void onClick(View buttonView) {
		    long l = ((Long)buttonView.getTag()).longValue();
		    mContext.onTouch(l);
		}
	}
}
