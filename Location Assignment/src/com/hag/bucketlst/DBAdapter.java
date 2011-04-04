package com.hag.bucketlst;



import android.content.ContentValues;

import android.content.Context;

import android.database.Cursor;

import android.database.SQLException;

import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;



public class DBAdapter {



	int id = 0;

	public static final String KEY_ROWID = "_id";

    public static final String KEY_LAT = "Latitude";
    
    public static final String KEY_LONG = "Longitude";

    public static final String KEY_ERROR = "AccuracyError";
    
    public static final String KEY_WIFI = "wifi";
    
    public static final String KEY_GPS = "gps";

    private static final String TAG = "DBAdapter";

    

    private static final String DATABASE_NAME = "LocationAssignment";

    private static final String DATABASE_TABLE = "tblLocations";

    private static final int DATABASE_VERSION = 2;



    private static final String DATABASE_CREATE =

        "create table " + DATABASE_TABLE + "(" + 

        KEY_ROWID + " integer primary key autoincrement, " + 

        KEY_LAT + KEY_LONG + KEY_ERROR + KEY_WIFI + KEY_GPS + " text not null );";



    private final Context context;  

    

    private DatabaseHelper DBHelper;

    private SQLiteDatabase db;

    

    public DBAdapter(Context ctx) 

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

    public DBAdapter open() throws SQLException 

    {

        db = DBHelper.getWritableDatabase();

        return this;

    }



    //---closes the database---    

    public void close() 

    {

        DBHelper.close();

    }

    

    //---insert a location into the database---

    public long insertLocation(double lat, double longi, double error, boolean wifi, boolean gps) 

    {

        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_LAT, lat); 
        initialValues.put(KEY_LONG, longi);
        initialValues.put(KEY_LONG, longi);
        initialValues.put(KEY_ERROR, error);
        initialValues.put(KEY_WIFI, wifi); 
        initialValues.put(KEY_GPS, gps);
        return db.insert(DATABASE_TABLE, null, initialValues);

    }

    

    /**

     * Return a Cursor over the list of all Tasks in the database

     * 

     * @return Cursor over all tasks

     */

    public Cursor getAllLocation() {



        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_LAT, KEY_LONG, KEY_ERROR, KEY_WIFI, KEY_GPS}

        				, null, null, null, null, null);

    }

    

    /**

     * Return a Cursor positioned at the task that matches the given rowId

     * 

     * @param rowId id of tasks to retrieve

     * @return Cursor positioned to matching task, if found

     * @throws SQLException if task could not be found/retrieved

     */

    public Cursor getLocaiton(long rowId) throws SQLException {



        Cursor mCursor =



                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,

                        KEY_LAT, KEY_LONG, KEY_ERROR, KEY_WIFI, KEY_GPS}, KEY_ROWID + "=" + rowId, null,

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

        args.put(KEY_LAT, title);



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

                    "SELECT COUNT(_id) FROM tblLocations", null);

                if(cursor.moveToFirst()) {

                    return cursor.getInt(0);

                }

                return cursor.getInt(0);



    }

}

