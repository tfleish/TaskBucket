package com.hag.bucketlst;

import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LDBAdapter {
	
	int id = 0;
	public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "Title";
    public static final String KEY_CATEGORY = "Category";
    public static final String KEY_CATID = "CatId";
    public static final String KEY_COMPLETED = "Completed";
    private static final String TAG = "LDBAdapter";
    
    private static final String DATABASE_NAME = "BucketLST";
    private static final String DATABASE_TABLE = "tblList";
    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_CREATE =
        "create table " + DATABASE_TABLE + "(" + 
        KEY_ROWID + " integer primary key autoincrement, " + 
        KEY_TITLE + " text not null, " + 
        KEY_CATEGORY + " text not null, " + 
        KEY_CATID + " integer not null, " + 
        KEY_COMPLETED + " integer not null );";
    
    private static final String DATABASE_CAT_CREATE =
        "create table tblCategories(" + 
        KEY_ROWID + " integer primary key autoincrement, " + 
        "Category text not null );";

    private final Context context;  
    
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    
    public LDBAdapter(Context ctx) 
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
            
            db.execSQL(DATABASE_CAT_CREATE);
            
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_CATEGORY, "General");
            db.insert("tblCategories", null, initialValues);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
                              int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                  + " to "
                  + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS tblList");
            onCreate(db);
        }
    }
	
    
    //---opens the database---
    public LDBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
    //---insert a title into the database---
    public long insertTask(String title, String category, long catId, int completed) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_CATEGORY, category);
        initialValues.put(KEY_CATID, (int) catId);
        initialValues.put(KEY_COMPLETED, completed);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the note with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteTask(long rowId) {

        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    /**
     * Return a Cursor over the list of all Tasks in the database
     * 
     * @return Cursor over all tasks
     */
    public Cursor getAllList() {

        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                KEY_CATEGORY, KEY_CATID, KEY_COMPLETED}, null, null, null, null, null);
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

                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE, 
                        KEY_CATEGORY, KEY_CATID, KEY_COMPLETED}, KEY_ROWID + "=" + rowId, null,
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
    public boolean updateTask(long rowId, String title, String category, long catId, int complete) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_CATEGORY, category);
        args.put(KEY_CATID, (int) catId);
        args.put(KEY_COMPLETED, complete);

        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return the count for total amount of entries in the table
     * 
     * @return how many stuff is in the table
     */
    public int getAllCount() 
    {
        Cursor cursor = db.rawQuery(
                    "SELECT COUNT(Title) FROM tblList", null);
                if(cursor.moveToFirst()) {
                    return cursor.getInt(0);
                }
                return cursor.getInt(0);

    }
    
    public String getRandomEntry() 
    {
    	
    	id = getAllCount();
    	Random random = new Random();
    	int rand = random.nextInt(getAllCount());
    	if(rand == 0)
    		++rand;
        Cursor cursor = db.rawQuery(
                    "SELECT * FROM tblCategories WHERE _id = " + rand, null);
                if(cursor.moveToFirst()) {
                    return cursor.getString(0);
                }
                return cursor.getString(0);
    }
}
