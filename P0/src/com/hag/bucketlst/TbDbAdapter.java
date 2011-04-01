package com.hag.bucketlst;

import java.util.Date;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class TbDbAdapter {
	
	int id = 0;
	public static final String KEY_TASK_LOCID = "_id";
	public static final String KEY_TASK_WEBID = "web_id";
    public static final String KEY_TASK_TITLE = "title";
    public static final String KEY_TASK_CATID = "category_id";
    public static final String KEY_TASK_NOTES = "notes";
    public static final String KEY_TASK_DUE = "due_date";
    public static final String KEY_TASK_PRIORITY = "priority";
    public static final String KEY_TASK_ISCHECKED = "is_checked";
    public static final String KEY_TASK_ISMINE = "is_mine";
    public static final String KEY_TASK_ISDELETED = "is_deleted";
    public static final String KEY_TASK_ISSYNCED = "is_synced";
    public static final String KEY_TASK_ISUPTODATE = "is_uptodate";
    public static final String KEY_TASK_VERSION = "version";
    
	public static final String KEY_CAT_ID = "_id";
    public static final String KEY_CAT_NAME = "name";
    public static final String KEY_CAT_ISEDITABLE = "is_editable";

	public static final String KEY_PRI_ID = "_id";
    public static final String KEY_PRI_NAME = "priority_name";
    public static final String KEY_PRI_COLOR = "priority_color";
    
	public static final String KEY_USER_ID = "_id";
    public static final String KEY_USER_NAME = "username";
    public static final String KEY_USER_DISPLAYNAME = "display_name";
    
	public static final String KEY_TASKUSER_USERID = "user_id";
    public static final String KEY_TASKUSER_TASKID = "task_id";
    
	public static final String KEY_CATUSER_USERID = "user_id";
    public static final String KEY_CATUSER_CATID = "category_id";
    
    private static final String TAG = "Task Bucket DB Adapter";
    
    private static final String DATABASE_NAME = "BucketLst";
    
    private static final String DATABASE_TABLE_TASKS = "tblTasks";
    private static final String DATABASE_TABLE_CATEGORIES = "tblCategories";
    private static final String DATABASE_TABLE_PRIORITY = "tblPriorities";
    private static final String DATABASE_TABLE_USERS = "tblUsers";
    private static final String DATABASE_TABLE_TASK_USER = "tblTasks2Users";
    private static final String DATABASE_TABLE_CAT_USER = "tblCategories2Users";
    
    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_CREATE =
        "create table " + DATABASE_TABLE_TASKS + "(" + 
        KEY_TASK_LOCID + " integer primary key autoincrement, " + 
        KEY_TASK_WEBID + " integer not null, " + 
        KEY_TASK_TITLE + " text not null, " + 
        KEY_TASK_CATID + " integer not null, " + 
        KEY_TASK_NOTES + " text not null, " + 
        KEY_TASK_DUE + " integer not null, " + 
        KEY_TASK_PRIORITY + " integer not null, " + 
        KEY_TASK_ISCHECKED + " integer not null, " + 
        KEY_TASK_ISMINE + " integer not null, " +
        KEY_TASK_ISDELETED + " integer not null, " +
        KEY_TASK_ISSYNCED + " integer not null, " +
        KEY_TASK_ISUPTODATE + " integer not null, " +
        KEY_TASK_VERSION + " integer not null );";
    
    private static final String DATABASE_CAT_CREATE =
        "create table " + DATABASE_TABLE_CATEGORIES + "(" + 
        KEY_CAT_ID + " integer primary key autoincrement, " +
        KEY_CAT_NAME + " text not null, " +
        KEY_CAT_ISEDITABLE + " integer not null );";
    
    private static final String DATABASE_PRI_CREATE =
        "create table " + DATABASE_TABLE_PRIORITY + "(" + 
        KEY_PRI_ID + " integer primary key autoincrement, " +
        KEY_PRI_NAME + " text not null, " +
        KEY_PRI_COLOR + " text not null );";
    
    private static final String DATABASE_USERS_CREATE =
        "create table " + DATABASE_TABLE_USERS + "(" + 
        KEY_USER_ID + " integer primary key autoincrement, " +
        KEY_USER_NAME + " text not null, " +
        KEY_USER_DISPLAYNAME + " text not null );";
    
    private static final String DATABASE_CAT_TASKUSER =
        "create table " + DATABASE_TABLE_TASK_USER + "(" + 
        KEY_TASKUSER_USERID + " integer not null, " +
        KEY_TASKUSER_TASKID + " integer not null );";
    
    private static final String DATABASE_CAT_CATUSER =
        "create table " + DATABASE_TABLE_CAT_USER + "(" + 
        KEY_CATUSER_USERID + " integer not null, " +
        KEY_CATUSER_CATID + " integer not null );";

    private final Context context;  
    
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    
    public TbDbAdapter(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
       
    public SQLiteDatabase getDB()
    {
    	return db;
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
            db.execSQL(DATABASE_PRI_CREATE);
            db.execSQL(DATABASE_USERS_CREATE);
            db.execSQL(DATABASE_CAT_TASKUSER);
            db.execSQL(DATABASE_CAT_CATUSER);
            
            addPriority(db, "High", "red_grad_bg");
            addPriority(db, "Normal", "yellow_grad_bg");
            addPriority(db, "Low", "green_grad_bg");
            addPriority(db, "None", "gray_grad_bg");
            addCategory(db, "General", 0);
            addCategory(db, "Work", 1);
            addCategory(db, "Home", 1);
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
        
        
        private void addCategory(SQLiteDatabase db, String paramString, int paramBoolean)
        {
          ContentValues localContentValues = new ContentValues();
          localContentValues.put(KEY_CAT_NAME, paramString);
          localContentValues.put(KEY_CAT_ISEDITABLE, paramBoolean);
          db.insert(DATABASE_TABLE_CATEGORIES, null, localContentValues);
        }

        private void addPriority(SQLiteDatabase db, String paramString1, String paramString2)
        {
          ContentValues localContentValues = new ContentValues();
          localContentValues.put(KEY_PRI_NAME, paramString1);
          localContentValues.put(KEY_PRI_COLOR, paramString2);
          db.insert(DATABASE_TABLE_PRIORITY, null, localContentValues);
        }
    }
	
    
    //---opens the database---
    public TbDbAdapter open() throws SQLException 
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
    public long makeTask(long webid, String title, long catId, String notes, long duedate, long priority, int checked, int mine, int deleted, int synced, int uptodate, int version) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TASK_TITLE, title);
        initialValues.put(KEY_TASK_NOTES, notes);
        
        Long localLong1 = Long.valueOf(catId);
        initialValues.put(KEY_TASK_CATID, localLong1);
        Long localLong2 = Long.valueOf(duedate);
        initialValues.put(KEY_TASK_DUE, localLong2);
        Long localLong3 = Long.valueOf(priority);
        initialValues.put(KEY_TASK_PRIORITY, localLong3);
        Long localLong4 = Long.valueOf(webid);
        initialValues.put(KEY_TASK_WEBID, localLong4);
        
        initialValues.put(KEY_TASK_ISCHECKED, checked);
        initialValues.put(KEY_TASK_ISMINE, mine);
        initialValues.put(KEY_TASK_ISDELETED, deleted);
        initialValues.put(KEY_TASK_ISSYNCED, synced);
        initialValues.put(KEY_TASK_ISUPTODATE, uptodate);
        initialValues.put(KEY_TASK_VERSION, version);
        
        return db.insert(DATABASE_TABLE_TASKS, null, initialValues);
    }

    //---insert a title into the database---
    public long makeCategor(String name, int editable) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CAT_NAME, name);
        initialValues.put(KEY_CAT_ISEDITABLE, editable);
        
        return db.insert(DATABASE_TABLE_CATEGORIES, null, initialValues);
    }
    
    /**
     * Delete the note with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteTask(long rowId) 
    {
    	String str = KEY_TASK_LOCID + "=" + rowId;
        return db.delete(DATABASE_TABLE_TASKS, str, null) > 0;
    }
    
    public boolean deleteCategory(long rowId)
    {
    	SQLiteDatabase localSQLiteDatabase1 = getDB();
		String[] arrayOfString = new String[2];
		arrayOfString[0] = KEY_CAT_ISEDITABLE;
		arrayOfString[1] = KEY_CAT_ID;
		String str = KEY_CAT_ID + "=" + rowId;
		Cursor localCursor = localSQLiteDatabase1.query(DATABASE_TABLE_CATEGORIES, arrayOfString, str, null, null, null, null);
		if (localCursor.moveToFirst())
		{
			int i = localCursor.getColumnIndexOrThrow(KEY_CAT_ISEDITABLE);
			if (i == 1)
			{
				long l = getDefaultCategoryId();
				ContentValues localContentValues = new ContentValues();
				Long localLong = Long.valueOf(l);
				localContentValues.put(KEY_TASK_CATID, localLong);
				SQLiteDatabase localSQLiteDatabase2 = getDB();
				String str2 = KEY_TASK_CATID + "=" + rowId;
				localSQLiteDatabase2.update(DATABASE_TABLE_TASKS, localContentValues, str2, null);
				SQLiteDatabase localSQLiteDatabase3 = getDB();
				String str3 = KEY_TASK_CATID + "=" + rowId;
				localSQLiteDatabase3.delete(DATABASE_TABLE_CATEGORIES, str3, null);
			}
		}
		localCursor.close();
		return true;
    }
    
	public boolean unTrashTask(long rowId)
	{
		ContentValues localContentValues = new ContentValues();
		localContentValues.put(KEY_TASK_ISDELETED, 0);
		localContentValues.put(KEY_TASK_ISCHECKED, 0);
		
		SQLiteDatabase localSQLiteDatabase = getDB();
    	String str = KEY_TASK_LOCID + "=" + rowId;
		return localSQLiteDatabase.update(DATABASE_TABLE_TASKS, localContentValues, str, null) > 0;
	}
    
	public boolean updateIsDeleted(long rowId, int paramBoolean)
	{
		ContentValues localContentValues = new ContentValues();
		localContentValues.put(KEY_TASK_ISDELETED, paramBoolean);
		
		SQLiteDatabase localSQLiteDatabase = getDB();
		String str = KEY_TASK_LOCID + "=" + rowId;
		return localSQLiteDatabase.update(DATABASE_TABLE_TASKS, localContentValues, str, null) > 0;
	}
	
	public boolean updateIsChecked(long rowId, int paramBoolean)
	{
		ContentValues localContentValues = new ContentValues();
		localContentValues.put(KEY_TASK_ISCHECKED, paramBoolean);
		
		SQLiteDatabase localSQLiteDatabase = getDB();
		String str = KEY_TASK_LOCID + "=" + rowId;
		return localSQLiteDatabase.update(DATABASE_TABLE_TASKS, localContentValues, str, null) > 0;
	}
	
	public boolean updateIsSynced(long rowId, int paramBoolean)
	{
		ContentValues localContentValues = new ContentValues();
		localContentValues.put(KEY_TASK_ISSYNCED, paramBoolean);
		
		SQLiteDatabase localSQLiteDatabase = getDB();
		String str = KEY_TASK_LOCID + "=" + rowId;
		return localSQLiteDatabase.update(DATABASE_TABLE_TASKS, localContentValues, str, null) > 0;
	}
	
	public boolean updateIsUpToDate(long rowId, int paramBoolean)
	{
		ContentValues localContentValues = new ContentValues();
		localContentValues.put(KEY_TASK_ISUPTODATE, paramBoolean);
		
		SQLiteDatabase localSQLiteDatabase = getDB();
		String str = KEY_TASK_LOCID + "=" + rowId;
		return localSQLiteDatabase.update(DATABASE_TABLE_TASKS, localContentValues, str, null) > 0;
	}
	
	public boolean updateVersion(long rowId, int paramBoolean)
	{
		ContentValues localContentValues = new ContentValues();
		localContentValues.put(KEY_TASK_VERSION, paramBoolean);
		
		SQLiteDatabase localSQLiteDatabase = getDB();
		String str = KEY_TASK_LOCID + "=" + rowId;
		return localSQLiteDatabase.update(DATABASE_TABLE_TASKS, localContentValues, str, null) > 0;
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
    public boolean updateTask(long rowId, long webid, String title, long catId, String notes, long duedate, long priority, int checked, int mine, int deleted, int synced, int uptodate, int version) 
    {
        ContentValues args = new ContentValues();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TASK_TITLE, title);
        initialValues.put(KEY_TASK_NOTES, notes);
        
        Long localLong1 = Long.valueOf(catId);
        initialValues.put(KEY_TASK_CATID, localLong1);
        Long localLong2 = Long.valueOf(duedate);
        initialValues.put(KEY_TASK_DUE, localLong2);
        Long localLong3 = Long.valueOf(priority);
        initialValues.put(KEY_TASK_PRIORITY, localLong3);
        Long localLong4 = Long.valueOf(webid);
        initialValues.put(KEY_TASK_WEBID, localLong4);
        
        initialValues.put(KEY_TASK_ISCHECKED, checked);
        initialValues.put(KEY_TASK_ISMINE, mine);
        initialValues.put(KEY_TASK_ISDELETED, deleted);
        initialValues.put(KEY_TASK_ISSYNCED, synced);
        initialValues.put(KEY_TASK_ISUPTODATE, uptodate);
        initialValues.put(KEY_TASK_VERSION, version);
        
        String str = KEY_TASK_LOCID + "=" + rowId;

        return db.update(DATABASE_TABLE_TASKS, args, str, null) > 0;
    }
	  
    /**
     * Return a Cursor over the list of all Tasks in the database
     * 
     * @return Cursor over all tasks
     */
    public Cursor fetchAllTask() 
    {
        SQLiteDatabase localSQLiteDatabase = getDB();
        String[] arrayOfStrings = new String[5];
        arrayOfStrings[0] = KEY_TASK_LOCID;
        arrayOfStrings[1] = KEY_TASK_TITLE;
        arrayOfStrings[2] = KEY_TASK_NOTES;
        arrayOfStrings[3] = KEY_TASK_ISCHECKED;
        arrayOfStrings[4] = KEY_TASK_CATID;
        return localSQLiteDatabase.query(DATABASE_TABLE_TASKS, arrayOfStrings, null, null, null, null, null);
    }
    
    /**
     * Return a Cursor over the list of all Tasks in the database
     * 
     * @return Cursor over all tasks
     */
    public Cursor fetchAllCategories() 
    {
        SQLiteDatabase localSQLiteDatabase = getDB();
        String[] arrayOfStrings = new String[3];
        arrayOfStrings[0] = KEY_CAT_ID;
        arrayOfStrings[1] = KEY_CAT_NAME;
        arrayOfStrings[2] = KEY_CAT_ISEDITABLE;
        return localSQLiteDatabase.query(DATABASE_TABLE_CATEGORIES, arrayOfStrings, null, null, null, null, null);
    }
    
    /**
     * Return a Cursor over the list of all Tasks in the database
     * 
     * @return Cursor over all tasks
     */
    public Cursor fetchAllPriorities() 
    {
        SQLiteDatabase localSQLiteDatabase = getDB();
        String[] arrayOfStrings = new String[2];
        arrayOfStrings[0] = KEY_PRI_ID;
        arrayOfStrings[1] = KEY_PRI_NAME;
        return localSQLiteDatabase.query(DATABASE_TABLE_PRIORITY, arrayOfStrings, null, null, null, null, null);
    }
    
	public Cursor fetchAllCheckedTasks()
	{
		SQLiteDatabase localSQLiteDatabase = getDB();
		String[] arrayOfStrings = new String[12];
		
		arrayOfStrings[0] = KEY_TASK_LOCID;
		arrayOfStrings[1] = KEY_TASK_TITLE;
		arrayOfStrings[2] = KEY_TASK_NOTES;
		arrayOfStrings[3] = KEY_TASK_ISCHECKED;
		arrayOfStrings[4] = KEY_TASK_CATID;
		arrayOfStrings[5] = KEY_TASK_DUE;
		arrayOfStrings[6] = KEY_TASK_PRIORITY;
		arrayOfStrings[7] = KEY_TASK_WEBID;
		arrayOfStrings[8] = KEY_TASK_ISMINE;
		arrayOfStrings[9] = KEY_TASK_ISSYNCED;
		arrayOfStrings[10] = KEY_TASK_ISUPTODATE;
		arrayOfStrings[11] = KEY_TASK_VERSION;
		
		String str = KEY_TASK_ISCHECKED + "='1' AND " + KEY_TASK_ISDELETED + "='0'";
		//String str = "is_checked='1' AND is_deleted='0'"
		return localSQLiteDatabase.query(DATABASE_TABLE_TASKS, arrayOfStrings, str, null, null, null, null, null);
	}
	
	public Cursor fetchAllUnCheckedTasks()
	{
		SQLiteDatabase localSQLiteDatabase = getDB();
		String[] arrayOfStrings = new String[13];
		
		arrayOfStrings[0] = KEY_TASK_LOCID;
		arrayOfStrings[1] = KEY_TASK_TITLE;
		arrayOfStrings[2] = KEY_TASK_NOTES;
		arrayOfStrings[3] = KEY_TASK_ISCHECKED;
		arrayOfStrings[4] = KEY_TASK_CATID;
		arrayOfStrings[5] = KEY_TASK_DUE;
		arrayOfStrings[6] = KEY_TASK_PRIORITY;
		arrayOfStrings[7] = KEY_TASK_WEBID;
		arrayOfStrings[8] = KEY_TASK_ISMINE;
		arrayOfStrings[9] = KEY_TASK_ISDELETED;
		arrayOfStrings[10] = KEY_TASK_ISSYNCED;
		arrayOfStrings[11] = KEY_TASK_ISUPTODATE;
		arrayOfStrings[12] = KEY_TASK_VERSION;
		
		String str = KEY_TASK_ISCHECKED + "='0'";
		return localSQLiteDatabase.query(DATABASE_TABLE_TASKS, arrayOfStrings, str, null, null, null, null, null);
	}
	
	public Cursor fetchAllDeletedTasks()
	{
		SQLiteDatabase localSQLiteDatabase = getDB();
		String[] arrayOfStrings = new String[13];
		
		arrayOfStrings[0] = KEY_TASK_LOCID;
		arrayOfStrings[1] = KEY_TASK_TITLE;
		arrayOfStrings[2] = KEY_TASK_NOTES;
		arrayOfStrings[3] = KEY_TASK_ISCHECKED;
		arrayOfStrings[4] = KEY_TASK_CATID;
		arrayOfStrings[5] = KEY_TASK_DUE;
		arrayOfStrings[6] = KEY_TASK_PRIORITY;
		arrayOfStrings[7] = KEY_TASK_WEBID;
		arrayOfStrings[8] = KEY_TASK_ISMINE;
		arrayOfStrings[9] = KEY_TASK_ISDELETED;
		arrayOfStrings[10] = KEY_TASK_ISSYNCED;
		arrayOfStrings[11] = KEY_TASK_ISUPTODATE;
		arrayOfStrings[12] = KEY_TASK_VERSION;
		
		String str = KEY_TASK_ISDELETED + "='1'";
		return localSQLiteDatabase.query(DATABASE_TABLE_TASKS, arrayOfStrings, str, null, null, null, null, null);
	}
	
	public Cursor fetchAllUnDeletedTasks()
	{
		SQLiteDatabase localSQLiteDatabase = getDB();
		String[] arrayOfStrings = new String[13];
		
		arrayOfStrings[0] = KEY_TASK_LOCID;
		arrayOfStrings[1] = KEY_TASK_TITLE;
		arrayOfStrings[2] = KEY_TASK_NOTES;
		arrayOfStrings[3] = KEY_TASK_ISCHECKED;
		arrayOfStrings[4] = KEY_TASK_CATID;
		arrayOfStrings[5] = KEY_TASK_DUE;
		arrayOfStrings[6] = KEY_TASK_PRIORITY;
		arrayOfStrings[7] = KEY_TASK_WEBID;
		arrayOfStrings[8] = KEY_TASK_ISMINE;
		arrayOfStrings[9] = KEY_TASK_ISDELETED;
		arrayOfStrings[10] = KEY_TASK_ISSYNCED;
		arrayOfStrings[11] = KEY_TASK_ISUPTODATE;
		arrayOfStrings[12] = KEY_TASK_VERSION;
		
		String str = KEY_TASK_ISDELETED + "='0'";
		return localSQLiteDatabase.query(DATABASE_TABLE_TASKS, arrayOfStrings, str, null, null, null, null, null);
	}
    
	public Cursor fetchAllUnSyncedTasks()
	{
		SQLiteDatabase localSQLiteDatabase = getDB();
		String[] arrayOfStrings = new String[13];
		
		arrayOfStrings[0] = KEY_TASK_LOCID;
		arrayOfStrings[1] = KEY_TASK_TITLE;
		arrayOfStrings[2] = KEY_TASK_NOTES;
		arrayOfStrings[3] = KEY_TASK_ISCHECKED;
		arrayOfStrings[4] = KEY_TASK_CATID;
		arrayOfStrings[5] = KEY_TASK_DUE;
		arrayOfStrings[6] = KEY_TASK_PRIORITY;
		arrayOfStrings[7] = KEY_TASK_WEBID;
		arrayOfStrings[8] = KEY_TASK_ISMINE;
		arrayOfStrings[9] = KEY_TASK_ISDELETED;
		arrayOfStrings[10] = KEY_TASK_ISSYNCED;
		arrayOfStrings[11] = KEY_TASK_ISUPTODATE;
		arrayOfStrings[12] = KEY_TASK_VERSION;
		
		String str = KEY_TASK_ISSYNCED + "='0'";
		return localSQLiteDatabase.query(DATABASE_TABLE_TASKS, arrayOfStrings, str, null, null, null, null, null);
	}
		
	public Cursor fetchAllUnUpdatedTasks()
	{
		SQLiteDatabase localSQLiteDatabase = getDB();
		String[] arrayOfStrings = new String[13];
		
		arrayOfStrings[0] = KEY_TASK_LOCID;
		arrayOfStrings[1] = KEY_TASK_TITLE;
		arrayOfStrings[2] = KEY_TASK_NOTES;
		arrayOfStrings[3] = KEY_TASK_ISCHECKED;
		arrayOfStrings[4] = KEY_TASK_CATID;
		arrayOfStrings[5] = KEY_TASK_DUE;
		arrayOfStrings[6] = KEY_TASK_PRIORITY;
		arrayOfStrings[7] = KEY_TASK_WEBID;
		arrayOfStrings[8] = KEY_TASK_ISMINE;
		arrayOfStrings[9] = KEY_TASK_ISDELETED;
		arrayOfStrings[10] = KEY_TASK_ISSYNCED;
		arrayOfStrings[11] = KEY_TASK_ISUPTODATE;
		arrayOfStrings[12] = KEY_TASK_VERSION;
		
		String str = KEY_TASK_ISUPTODATE + "='0'";
		return localSQLiteDatabase.query(DATABASE_TABLE_TASKS, arrayOfStrings, str, null, null, null, null, null);
	}
	
	public Cursor fetchAllMyTasks()
	{
		SQLiteDatabase localSQLiteDatabase = getDB();
		String[] arrayOfStrings = new String[13];
		
		arrayOfStrings[0] = KEY_TASK_LOCID;
		arrayOfStrings[1] = KEY_TASK_TITLE;
		arrayOfStrings[2] = KEY_TASK_NOTES;
		arrayOfStrings[3] = KEY_TASK_ISCHECKED;
		arrayOfStrings[4] = KEY_TASK_CATID;
		arrayOfStrings[5] = KEY_TASK_DUE;
		arrayOfStrings[6] = KEY_TASK_PRIORITY;
		arrayOfStrings[7] = KEY_TASK_WEBID;
		arrayOfStrings[8] = KEY_TASK_ISMINE;
		arrayOfStrings[9] = KEY_TASK_ISDELETED;
		arrayOfStrings[10] = KEY_TASK_ISSYNCED;
		arrayOfStrings[11] = KEY_TASK_ISUPTODATE;
		arrayOfStrings[12] = KEY_TASK_VERSION;
		
		String str = KEY_TASK_ISMINE + "='1'";
		return localSQLiteDatabase.query(DATABASE_TABLE_TASKS, arrayOfStrings, str, null, null, null, null, null);
	}
	
    /**
     * Return a Cursor positioned at the task that matches the given rowId
     * 
     * @param rowId id of tasks to retrieve
     * @return Cursor positioned to matching task, if found
     * @throws SQLException if task could not be found/retrieved
     */
	public Cursor fetchTask(long rowId)	throws SQLException
	{
		SQLiteDatabase localSQLiteDatabase = getDB();
		String[] arrayOfStrings = new String[13];
        arrayOfStrings[0] = KEY_TASK_LOCID;
        arrayOfStrings[1] = KEY_TASK_TITLE;
        arrayOfStrings[2] = KEY_TASK_NOTES;
        arrayOfStrings[3] = KEY_TASK_ISCHECKED;
        arrayOfStrings[4] = KEY_TASK_CATID;
        arrayOfStrings[5] = KEY_TASK_DUE;
        arrayOfStrings[6] = KEY_TASK_PRIORITY;
        arrayOfStrings[7] = KEY_TASK_WEBID;
        arrayOfStrings[8] = KEY_TASK_ISMINE;
        arrayOfStrings[9] = KEY_TASK_ISDELETED;
        arrayOfStrings[10] = KEY_TASK_ISSYNCED;
        arrayOfStrings[11] = KEY_TASK_ISUPTODATE;
        arrayOfStrings[12] = KEY_TASK_VERSION;
        
        String str = KEY_TASK_LOCID + "=" + rowId;

		Cursor localCursor = localSQLiteDatabase.query(DATABASE_TABLE_TASKS, arrayOfStrings, str, null, null, null, null, null);
		if (localCursor != null){
			localCursor.moveToFirst();
		}
		return localCursor;
	}
    
    /**
     * Return a Cursor positioned at the task that matches the given rowId
     * 
     * @param rowId id of tasks to retrieve
     * @return Cursor positioned to matching task, if found
     * @throws SQLException if task could not be found/retrieved
     */
    public Cursor fetchCategory(long rowId)
    {
      SQLiteDatabase localSQLiteDatabase = getDB();
      String[] arrayOfStrings = new String[2];
      arrayOfStrings[0] = KEY_CAT_NAME;
      arrayOfStrings[1] = KEY_CAT_ISEDITABLE;
      String str = KEY_CAT_ID + "=" + rowId;
      return localSQLiteDatabase.query(DATABASE_TABLE_CATEGORIES, arrayOfStrings, str, null, null, null, null);
    }
    
    
    public Cursor fetchPriority(long rowId)
    {
        SQLiteDatabase localSQLiteDatabase = getDB();
        String[] arrayOfStrings = new String[2];
        arrayOfStrings[0] = KEY_PRI_NAME;
        arrayOfStrings[1] = KEY_PRI_COLOR;
        String str = KEY_PRI_ID + "=" + rowId;
        return localSQLiteDatabase.query(DATABASE_TABLE_PRIORITY, arrayOfStrings, str, null, null, null, null);
    }
    
	public Cursor fetchUncheckedByCategory(long catId)
	{
		SQLiteDatabase localSQLiteDatabase = getDB();
		String[] arrayOfStrings = new String[12];
		
		arrayOfStrings[0] = KEY_TASK_LOCID;
		arrayOfStrings[1] = KEY_TASK_TITLE;
		arrayOfStrings[2] = KEY_TASK_NOTES;
		arrayOfStrings[3] = KEY_TASK_ISCHECKED;
		arrayOfStrings[4] = KEY_TASK_CATID;
		arrayOfStrings[5] = KEY_TASK_DUE;
		arrayOfStrings[6] = KEY_TASK_PRIORITY;
		arrayOfStrings[7] = KEY_TASK_WEBID;
		arrayOfStrings[8] = KEY_TASK_ISMINE;
		arrayOfStrings[9] = KEY_TASK_ISSYNCED;
		arrayOfStrings[10] = KEY_TASK_ISUPTODATE;
		arrayOfStrings[11] = KEY_TASK_VERSION;
		
		String str = "(" + KEY_TASK_CATID + "=" + catId + ") AND (" + KEY_TASK_ISDELETED + "='0')";
		//"(category=" + paramLong + ") AND (" + "is_deleted" + "='false')";
		return localSQLiteDatabase.query(DATABASE_TABLE_TASKS, arrayOfStrings, str, null, null, null, null, null);
	}
	
	public Cursor fetchUncheckedByPriority(long priId)
	{
		SQLiteDatabase localSQLiteDatabase = getDB();
		String[] arrayOfStrings = new String[12];
		
		arrayOfStrings[0] = KEY_TASK_LOCID;
		arrayOfStrings[1] = KEY_TASK_TITLE;
		arrayOfStrings[2] = KEY_TASK_NOTES;
		arrayOfStrings[3] = KEY_TASK_ISCHECKED;
		arrayOfStrings[4] = KEY_TASK_CATID;
		arrayOfStrings[5] = KEY_TASK_DUE;
		arrayOfStrings[6] = KEY_TASK_PRIORITY;
		arrayOfStrings[7] = KEY_TASK_WEBID;
		arrayOfStrings[8] = KEY_TASK_ISMINE;
		arrayOfStrings[9] = KEY_TASK_ISSYNCED;
		arrayOfStrings[10] = KEY_TASK_ISUPTODATE;
		arrayOfStrings[11] = KEY_TASK_VERSION;
		
		String str = "(" + KEY_TASK_PRIORITY + "=" + priId + ") AND (" + KEY_TASK_ISDELETED + "='0')";
		//"(importance=" + paramLong + ") AND (" + "is_deleted" + "='false')";
		return localSQLiteDatabase.query(DATABASE_TABLE_TASKS, arrayOfStrings, str, null, null, null, null, null);
	}

	public long countUncheckedTasksByCategory(long catId)
	{
		SQLiteDatabase localSQLiteDatabase = getDB();
		String str = "SELECT COUNT(*) FROM " + DATABASE_TABLE_TASKS + " WHERE (" + KEY_TASK_CATID + "=" + catId + ") AND (" + KEY_TASK_ISCHECKED + "='0');";
		SQLiteStatement localSQLiteStatement = localSQLiteDatabase.compileStatement(str);
		long l = localSQLiteStatement.simpleQueryForLong();
		localSQLiteStatement.close();
		return l;
	}
	
	public long countUncheckedTasksByImportance(long priId)
	{
		SQLiteDatabase localSQLiteDatabase = getDB();
		String str = "SELECT COUNT(*) FROM " + DATABASE_TABLE_TASKS + " WHERE (" + KEY_TASK_PRIORITY + "=" + priId + ") AND (" + KEY_TASK_ISDELETED + "='0');";
		SQLiteStatement localSQLiteStatement = localSQLiteDatabase.compileStatement(str);
		long l = localSQLiteStatement.simpleQueryForLong();
		localSQLiteStatement.close();
		return l;
	}
	
	public long countCollaboratorsByCategory(long catId)
	{
		SQLiteDatabase localSQLiteDatabase = getDB();
		String str = "SELECT COUNT(*) FROM " + DATABASE_TABLE_CAT_USER + " WHERE (" + KEY_CAT_ID + "=" + catId + ");";
		SQLiteStatement localSQLiteStatement = localSQLiteDatabase.compileStatement(str);
		long l = localSQLiteStatement.simpleQueryForLong();
		localSQLiteStatement.close();
		return l;
	}
	
	public long countCollaboratorsByTask(long locId)
	{
		SQLiteDatabase localSQLiteDatabase = getDB();
		String str = "SELECT COUNT(*) FROM " + DATABASE_TABLE_TASK_USER + " WHERE (" + KEY_TASK_LOCID + "=" + locId + ");";
		SQLiteStatement localSQLiteStatement = localSQLiteDatabase.compileStatement(str);
		long l = localSQLiteStatement.simpleQueryForLong();
		localSQLiteStatement.close();
		return l;
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
    
    private long getDefaultCategoryId()
    {
      String[] arrayOfString = new String[1];
      arrayOfString[0] = KEY_CAT_ID;
      String str = KEY_CAT_NAME + "='General'";
      Cursor localCursor = db.query(DATABASE_TABLE_CATEGORIES, arrayOfString, str, null, null, null, null);
      long l = 1;
      if (localCursor.moveToFirst())
      {
        int i = localCursor.getColumnIndexOrThrow(KEY_CAT_ID);
        l = localCursor.getLong(i);
      }
      localCursor.close();
      return l;
    }
    
    private long getDefaultPriorityId()
    {
      String[] arrayOfString = new String[1];
      arrayOfString[0] = KEY_PRI_ID;
      String str = KEY_PRI_NAME + "='None'";
      Cursor localCursor = db.query(DATABASE_TABLE_PRIORITY, arrayOfString, str, null, null, null, null);
      long l = 1;
      if (localCursor.moveToFirst())
      {
        int i = localCursor.getColumnIndexOrThrow(KEY_PRI_ID);
        l = localCursor.getLong(i);
      }
	localCursor.close();
	return l;
    }
    
    public Date getDueDate(long rowId)
    {
		String[] arrayOfString = new String[1];
		arrayOfString[0] = KEY_TASK_DUE;
		String str = KEY_TASK_LOCID + "=" + rowId;
		Cursor localCursor = db.query(DATABASE_TABLE_TASKS, arrayOfString, str, null, null, null, null);
		long l = 0;
		if (localCursor.moveToFirst())
		{
		  int i = localCursor.getColumnIndexOrThrow(KEY_TASK_DUE);
		  l = localCursor.getLong(i);
		}
      localCursor.close();
      return new Date(l);
    }

    public int getPriorityImage(long rowId, boolean paramBoolean)
    {
      SQLiteDatabase localSQLiteDatabase = getDB();
      String[] arrayOfString = new String[1];
      arrayOfString[0] = KEY_PRI_COLOR;
      String str = KEY_PRI_ID + "=" + rowId;
      Cursor localCursor = localSQLiteDatabase.query(DATABASE_TABLE_PRIORITY, arrayOfString, str, null, null, null, null);
      int i = 1;
      String str5;
      Resources localResources1;
      String str6;
      if (localCursor.moveToFirst())
      {
        int j = localCursor.getColumnIndexOrThrow(KEY_PRI_COLOR);
        str5 = localCursor.getString(j);
        localResources1 = this.context.getResources();
        str6 = "com.taskos:drawable/" + str5;
        i = localResources1.getIdentifier(str6, null, null);
      }
      localCursor.close();
      return i;
    }

    public String getNotes(long rowId)
    {
		String[] arrayOfString = new String[1];
		arrayOfString[0] = KEY_TASK_NOTES;
		String str = KEY_TASK_LOCID + "=" + rowId;
		Cursor localCursor = db.query(DATABASE_TABLE_TASKS, arrayOfString, str, null, null, null, null);
		String notes = "";
		if (localCursor.moveToFirst())
		{
		  int i = localCursor.getColumnIndexOrThrow(KEY_TASK_NOTES);
		  notes = localCursor.getString(i);
		}
      localCursor.close();
      return notes;
    }
}
