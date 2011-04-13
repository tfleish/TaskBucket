package com.hag.bucketlst.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.hag.bucketlst.db.TbDbAdapter;

public class BLApp extends Application{
	
	private static TbDbAdapter mDbHelper;
	private static SQLiteDatabase db;

	
	public static SQLiteDatabase getDB()
	{
		return db;
	}

	public static TbDbAdapter getHelper()
	{
		return mDbHelper;
	}
	
	public static String wordfix(Editable word)
	{
    	String nomWord = word.toString().trim();
        String nomCap = String.valueOf(nomWord.substring(0, 1).toUpperCase());
        String nomFin = nomCap + nomWord.substring(1);
        return nomFin;
	}

	public void onCreate()
	{
		super.onCreate();
		mDbHelper = new TbDbAdapter(this);
		db = mDbHelper.getDB();
	}
}
