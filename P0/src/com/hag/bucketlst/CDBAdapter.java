package com.hag.bucketlst;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CDBAdapter {

	int id = 0;
	public static final String KEY_ROWID = "_id";
    public static final String KEY_CATEGORY = "Category";
    private static final String TAG = "CDBAdapter";
    
    private static final String DATABASE_NAME = "BucketLST";
    private static final String DATABASE_TABLE = "tblCategories";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE =
        "create table " + DATABASE_TABLE + "(" + 
        KEY_ROWID + " integer primary key autoincrement, " + 
        KEY_CATEGORY + " text not null );";

    private final Context context;  
    
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    
    public CDBAdapter(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
    
	private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
                              int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                  + " to "
                  + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS tblCategories");
            onCreate(db);
        }
    }
	
    
    //---opens the database---
    public CDBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
    //---insert a category into the database---
    public long insertCategory(String Category) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CATEGORY, Category);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    
    /**
     * Return a Cursor over the list of all Tasks in the database
     * 
     * @return Cursor over all tasks
     */
    public Cursor getAllCategory() {

        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_CATEGORY}
        				, null, null, null, null, null);
    }
    
    /**
     * Return a Cursor positioned at the task that matches the given rowId
     * 
     * @param rowId id of tasks to retrieve
     * @return Cursor positioned to matching task, if found
     * @throws SQLException if task could not be found/retrieved
     */
    public Cursor getTask(long rowId) throws SQLException {

        Cursor mCursor =

                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                        KEY_CATEGORY}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    /**
     * Update the tasks using the details provided. The task to be updated is
     * specified using the rowId, and it is altered to use the title, category,
     * and completeness values passed in
     * 
     * @param rowId id of tasks to update
     * @param title value to set task title to
     * @param category value to set task category to
     * @param completed value to set task completeness to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateNote(long rowId, String title, int catId, int complete) {
        ContentValues args = new ContentValues();
        args.put(KEY_CATEGORY, title);

        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return the count for total amount of entries in the table
     * 
     * @return how many stuff is in the table
     */
    public int getAllEntriesCount() 
    {
        Cursor cursor = db.rawQuery(
                    "SELECT COUNT(Category) FROM tblCategories", null);
                if(cursor.moveToFirst()) {
                    return cursor.getInt(0);
                }
                return cursor.getInt(0);

    }
}
