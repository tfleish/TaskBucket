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
import com.hag.bucketlst.activity.MyDoneTasks;
import com.hag.bucketlst.application.BLApp;
import com.hag.bucketlst.db.TbDbAdapter;

public class DoneTaskAdapter extends ResourceCursorAdapter 
{
	private MyDoneTasks mContext;
	private TbDbAdapter mDbHelper;
	
	public DoneTaskAdapter(Context context, int layout, Cursor c) {
		super(context, layout, c);
		mContext = (MyDoneTasks) context;
		mDbHelper = BLApp.getHelper();
	}
	
	public DoneTaskAdapter(Context context, int layout, Cursor c,
			boolean autoRequery) {
		super(context, layout, c, autoRequery);
		mContext = (MyDoneTasks)context;
		mDbHelper = BLApp.getHelper();
	}

	@Override
	public void bindView(View view, Context content, Cursor cursor)
	{
		TextView titleV = (TextView)view.findViewById(R.id.text1);
		TextView categoryV = (TextView)view.findViewById(R.id.cat);
		ImageButton checkerV = (ImageButton)view.findViewById(R.id.taskCheck);
		ImageView priorityV = (ImageView)view.findViewById(R.id.priColor);
		ImageButton delV = (ImageButton)view.findViewById(R.id.delButton);	
		
		String titleC = cursor.getString(cursor.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_TITLE));
		titleV.setText(titleC);
		titleV.setPaintFlags(titleV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		long catItem = cursor.getLong(cursor.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_CATID));
		String categoryC = mDbHelper.fetchCategory(catItem).getString(0);
		long mDate = cursor.getLong(cursor.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_DUE));
    	if (mDate != 0){
    		String sDf = new SimpleDateFormat("MMM dd").format(new Date(mDate));
    		categoryC = sDf + " | " + categoryC;
    	}    	
		categoryV.setText(categoryC);
		categoryV.setPaintFlags(categoryV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		int priRC = cursor.getInt(cursor.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_PRIORITY));
		int isChecked = cursor.getInt(cursor.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_ISCHECKED));
		boolean checkState = (isChecked == 1) ? true : false;
		int priRes = mDbHelper.getPriorityImage(priRC, checkState);
		priorityV.setBackgroundResource(priRes);
		long locId = cursor.getLong(cursor.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_LOCID));		
		delV.setTag(locId);
		delV.setOnClickListener(new mDoneTaskDelL());
		checkerV.setTag(locId);
		checkerV.setOnClickListener(new mDoneTaskCheckL());
		checkerV.setFocusable(false);
		checkerV.setFocusableInTouchMode(false);
	}
	
    
    private class mDoneTaskCheckL implements OnClickListener
    {

		@Override
		public void onClick(View buttonView) {
		    long l = ((Long)buttonView.getTag()).longValue();
		    mContext.onCheck(l, false);
		}

	}
    
    private class mDoneTaskDelL implements OnClickListener
    {
		@Override
		public void onClick(View v) {
		    long l = ((Long)v.getTag()).longValue();
		    mContext.onClick(l);
		}
	}
}
