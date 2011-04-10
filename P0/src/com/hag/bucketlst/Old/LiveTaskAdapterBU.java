package com.hag.bucketlst.Old;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.hag.bucketlst.R;
import com.hag.bucketlst.activity.MyTasks;
import com.hag.bucketlst.application.BLApp;
import com.hag.bucketlst.db.TbDbAdapter;

public class LiveTaskAdapterBU extends ResourceCursorAdapter 
{
	private MyTasks mContext;
	private TbDbAdapter mDbHelper;
	
	public LiveTaskAdapterBU(Context context, int layout, Cursor c) {
		super(context, layout, c);
		mContext = (MyTasks) context;
		mDbHelper = BLApp.getHelper();
	}
	
	public LiveTaskAdapterBU(Context context, int layout, Cursor c,
			boolean autoRequery) {
		super(context, layout, c, autoRequery);
		mContext = (MyTasks)context;
		mDbHelper = BLApp.getHelper();
	}

	@Override
	public void bindView(View view, Context content, Cursor cursor)
	{
		TextView titleV = (TextView)view.findViewById(R.id.text1);
		TextView categoryV = (TextView)view.findViewById(R.id.cat);
		CheckBox checkerV = (CheckBox)view.findViewById(R.id.taskCheck);
		ImageView priorityV = (ImageView)view.findViewById(R.id.priColor);
		ImageView collabsV = (ImageView)view.findViewById(R.id.plusOne);	
		
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
		checkerV.setChecked(checkState);
			//Toast.makeText(mContext, "Checked", Toast.LENGTH_SHORT).show();
		long locId = cursor.getLong(cursor.getColumnIndexOrThrow(TbDbAdapter.KEY_TASK_LOCID));
		long collabCount = mDbHelper.countCollaboratorsByTask(locId);
		if(collabCount > 1){
			collabsV.setVisibility(View.VISIBLE);
		}
		checkerV.setTag(locId);
		checkerV.setOnCheckedChangeListener(new mLiveTaskCheckL());
	}
	
    
    private class mLiveTaskCheckL implements OnCheckedChangeListener
    {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		    long l = ((Long)buttonView.getTag()).longValue();
		    mContext.onCheck(l, isChecked);
		}

	}
}
