package com.hag.bucketlst.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.hag.bucketlst.R;
import com.hag.bucketlst.activity.NCategoryView;
import com.hag.bucketlst.db.TbDbAdapter;

public class CatAdapter extends ResourceCursorAdapter 
{
	private NCategoryView mContext;
	//private TbDbAdapter mDbHelper;
	
	public CatAdapter(Context context, int layout, Cursor c) {
		super(context, layout, c);
		mContext = (NCategoryView) context;
		//mDbHelper = BLApp.getHelper();
	}
	
	public CatAdapter(Context context, int layout, Cursor c,
			boolean autoRequery) {
		super(context, layout, c, autoRequery);
		mContext = (NCategoryView)context;
		//mDbHelper = BLApp.getHelper();
	}

	@Override
	public void bindView(View view, Context content, Cursor cursor)
	{
		TextView titleC = (TextView)view.findViewById(R.id.text1);
		ImageButton editCat = (ImageButton)view.findViewById(R.id.catColEdit);
		ImageButton deleteCat = (ImageButton)view.findViewById(R.id.catColDelete);	
		
		String categoryC = cursor.getString(cursor.getColumnIndexOrThrow(TbDbAdapter.KEY_CAT_NAME));
		titleC.setText(categoryC);
		
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
