package com.hag.bucketlst;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class TasksDatabaseHelper extends SQLiteOpenHelper
{
  private static final String DATABASE_CREATE_CATEGORIES = "create table taskos_categories(_id integer primary key autoincrement, name text not null, is_editable text not null);";
  private static final String DATABASE_CREATE_DUE_GROUPS = "create table taskos_due_groups(_id integer primary key autoincrement, due_group_name text not null);";
  private static final String DATABASE_CREATE_IMPORTANCE = "create table taskos_importance(_id integer primary key autoincrement, importance_name text not null, importance_color text not null);";
  private static final String DATABASE_CREATE_SYNC_EXTRAS = "create table taskos_sync_extras(_id integer primary key autoincrement, taskId integer not null, service text not null,key text,value text);";
  private static final String DATABASE_CREATE_TASKS = "create table taskos_tasks(_id integer primary key autoincrement, title text not null, due_date integer not null, creation_time integer not null, modification_time integer not null, notes text not null, is_synced text not null, is_checked text not null, is_deleted text not null, is_quick_edit_visible text not null, checked_time integer not null default 0, contact text not null, alert_status integer not null default 0, alert_one_time integer not null default 0, alert_repeating_interval integer not null default 0, alert_repeating_interval_amount integer not null default 1, alert_repeating_days_of_week integer not null default 0, alert_repeating_day_of_month integer not null default 1, alert_repeating_time integer not null default 480, alert_ids text not null, alert_times text not null, importance integer not null, category integer not null, action text not null, action_param text not null, button_resource text not null);";
  private static final String DATABASE_CREATE_TASKS_V4 = "create table taskos_tasks(_id integer primary key autoincrement, title text not null, due_date text not null, notes text not null, is_synced text not null, is_checked text not null, contact text not null, alert_spinner_index integer not null, importance integer not null, category integer not null, action text not null, action_param text not null, button_resource text not null);";
  private static final String DATABASE_CREATE_TASKS_V7 = "create table taskos_tasks(_id integer primary key autoincrement, title text not null, due_date integer not null, creation_time integer not null, modification_time integer not null, notes text not null, is_synced text not null, is_checked text not null, checked_time integer not null default 0, contact text not null, alert_status integer not null default 0, alert_one_time integer not null default 0, alert_repeating_interval integer not null default 0, alert_repeating_interval_amount integer not null default 1, alert_repeating_days_of_week integer not null default 0, alert_repeating_day_of_month integer not null default 1, alert_repeating_time integer not null default 480, alert_ids text not null, alert_times text not null, importance integer not null, category integer not null, action text not null, action_param text not null, button_resource text not null);";
  private static final String DATABASE_NAME = "data";
  private static final String DATABASE_TABLE_CATEGORIES = "taskos_categories";
  private static final String DATABASE_TABLE_DUE_GROUPS = "taskos_due_groups";
  private static final String DATABASE_TABLE_IMPORTANCE = "taskos_importance";
  private static final String DATABASE_TABLE_SYNC_EXTRAS = "taskos_sync_extras";
  private static final String DATABASE_TABLE_TASKS = "taskos_tasks";
  private static final String DATABASE_TABLE_TASKS_TEMP = "taskos_tasks_temp";
  private static final int DATABASE_VERSION = 9;
  private static final String DEFAULT_CATEGORY_NAME = "General";
  private static final String DEFAULT_IMPORTANCE_NAME = "Normal";
  public static String DISABLE_REFRESHING_INTENT = ;
  public static final String DUE_GROUP_LATER = "Later";
  public static final String DUE_GROUP_THIS_WEEK = "This Week";
  public static final String DUE_GROUP_TODAY = "Today";
  private static final String HIGH_IMPORTANCE = "High";
  public static final String KEY_CATEGORIES_IS_EDITABLE = "is_editable";
  public static final String KEY_CATEGORIES_NAME = "name";
  public static final String KEY_CATEGORIES_ROWID = "_id";
  public static final String KEY_DUE_GROUPS_NAME = "due_group_name";
  public static final String KEY_DUE_GROUPS_ROWID = "_id";
  public static final String KEY_IMPORTANCE_COLOR = "importance_color";
  public static final String KEY_IMPORTANCE_NAME = "importance_name";
  public static final String KEY_IMPORTANCE_ROWID = "_id";
  public static final String KEY_SYNC_EXTRAS_KEY = "key";
  public static final String KEY_SYNC_EXTRAS_ROWID = "_id";
  public static final String KEY_SYNC_EXTRAS_SERVICE = "service";
  public static final String KEY_SYNC_EXTRAS_TASK_ID = "taskId";
  public static final String KEY_SYNC_EXTRAS_VALUE = "value";
  public static final String KEY_TASKS_ACTION = "action";
  public static final String KEY_TASKS_ACTION_PARAM = "action_param";
  public static final String KEY_TASKS_ALERT_IDS = "alert_ids";
  public static final String KEY_TASKS_ALERT_ONE_TIME = "alert_one_time";
  public static final String KEY_TASKS_ALERT_REPEATING_DAYS_OF_WEEK = "alert_repeating_days_of_week";
  public static final String KEY_TASKS_ALERT_REPEATING_DAY_OF_MONTH = "alert_repeating_day_of_month";
  public static final String KEY_TASKS_ALERT_REPEATING_INTERVAL = "alert_repeating_interval";
  public static final String KEY_TASKS_ALERT_REPEATING_INTERVAL_AMOUNT = "alert_repeating_interval_amount";
  public static final String KEY_TASKS_ALERT_REPEATING_TIME = "alert_repeating_time";
  public static final String KEY_TASKS_ALERT_SPINNER_INDEX = "alert_spinner_index";
  public static final String KEY_TASKS_ALERT_STATUS = "alert_status";
  public static final String KEY_TASKS_ALERT_TIMES = "alert_times";
  public static final String KEY_TASKS_BUTTON_RESOURCE = "button_resource";
  public static final String KEY_TASKS_CATEGORY = "category";
  public static final String KEY_TASKS_CHECKED_TIME = "checked_time";
  public static final String KEY_TASKS_CONTACT = "contact";
  public static final String KEY_TASKS_CREATION_TIME = "creation_time";
  public static final String KEY_TASKS_DUE_DATE = "due_date";
  public static final String KEY_TASKS_IMPORTANCE = "importance";
  public static final String KEY_TASKS_IS_CHECKED = "is_checked";
  public static final String KEY_TASKS_IS_DELETED = "is_deleted";
  public static final String KEY_TASKS_IS_QUICK_EDIT_VISIBLE = "is_quick_edit_visible";
  public static final String KEY_TASKS_IS_SYNCED = "is_synced";
  public static final String KEY_TASKS_MODIFICATION_TIME = "modification_time";
  public static final String KEY_TASKS_NOTES = "notes";
  public static final String KEY_TASKS_ROWID = "_id";
  public static final String KEY_TASKS_TITLE = "title";
  private static final String LOW_IMPORTANCE = "Low";
  private boolean disableRefreshing = 0;
  private Context mCtx;

  public TasksDatabaseHelper(Context paramContext)
  {
    super(paramContext, "data", null, 9);
    this.mCtx = paramContext;
  }

  private void addCategory(SQLiteDatabase paramSQLiteDatabase, String paramString, boolean paramBoolean)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("name", paramString);
    String str = Boolean.toString(paramBoolean);
    localContentValues.put("is_editable", str);
    long l = paramSQLiteDatabase.insert("taskos_categories", null, localContentValues);
  }

  private void addDueGroup(SQLiteDatabase paramSQLiteDatabase, String paramString)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("due_group_name", paramString);
    long l = paramSQLiteDatabase.insert("taskos_due_groups", null, localContentValues);
  }

  private void addImportance(SQLiteDatabase paramSQLiteDatabase, String paramString1, String paramString2)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("importance_name", paramString1);
    localContentValues.put("importance_color", paramString2);
    long l = paramSQLiteDatabase.insert("taskos_importance", null, localContentValues);
  }

  private long getDefaultCategoryId()
  {
    SQLiteDatabase localSQLiteDatabase1 = TaskosApp.getDB();
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "_id";
    String str1 = null;
    String str2 = null;
    String str3 = null;
    Cursor localCursor = localSQLiteDatabase1.query("taskos_categories", arrayOfString1, "name='General'", null, str1, str2, str3);
    long l;
    if (localCursor.moveToFirst())
    {
      int i = localCursor.getColumnIndexOrThrow("_id");
      l = localCursor.getLong(i);
    }
    while (true)
    {
      localCursor.close();
      return l;
      SQLiteDatabase localSQLiteDatabase2 = TaskosApp.getDB();
      String[] arrayOfString2 = new String[1];
      arrayOfString2[0] = "_id";
      String[] arrayOfString3 = null;
      String str4 = null;
      String str5 = null;
      String str6 = null;
      localCursor = localSQLiteDatabase2.query("taskos_categories", arrayOfString2, null, arrayOfString3, str4, str5, str6);
      int j = localCursor.getColumnIndexOrThrow("_id");
      l = localCursor.getLong(j);
    }
  }

  private long getDefaultImportanceId()
  {
    SQLiteDatabase localSQLiteDatabase1 = TaskosApp.getDB();
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "_id";
    String str1 = null;
    String str2 = null;
    String str3 = null;
    Cursor localCursor = localSQLiteDatabase1.query("taskos_importance", arrayOfString1, "importance_name='Normal'", null, str1, str2, str3);
    long l;
    if (localCursor.moveToFirst())
    {
      int i = localCursor.getColumnIndexOrThrow("_id");
      l = localCursor.getLong(i);
    }
    while (true)
    {
      localCursor.close();
      return l;
      SQLiteDatabase localSQLiteDatabase2 = TaskosApp.getDB();
      String[] arrayOfString2 = new String[1];
      arrayOfString2[0] = "_id";
      String[] arrayOfString3 = null;
      String str4 = null;
      String str5 = null;
      String str6 = null;
      localCursor = localSQLiteDatabase2.query("taskos_importance", arrayOfString2, null, arrayOfString3, str4, str5, str6);
      int j = localCursor.getColumnIndexOrThrow("_id");
      l = localCursor.getLong(j);
    }
  }

  public long countUncheckedTasksByCategory(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str = "SELECT COUNT(*) FROM taskos_tasks WHERE ( category = " + paramLong + " ) AND ( " + "is_checked" + " = 'false');";
    SQLiteStatement localSQLiteStatement = localSQLiteDatabase.compileStatement(str);
    long l = localSQLiteStatement.simpleQueryForLong();
    localSQLiteStatement.close();
    return l;
  }

  public long countUncheckedTasksByImportance(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str = "SELECT COUNT(*) FROM taskos_tasks WHERE ( importance = " + paramLong + " ) AND ( " + "is_checked" + " = 'false');";
    SQLiteStatement localSQLiteStatement = localSQLiteDatabase.compileStatement(str);
    long l = localSQLiteStatement.simpleQueryForLong();
    localSQLiteStatement.close();
    return l;
  }

  public long countUncheckedTasksDueLater()
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.add(6, 7);
    Date localDate = new Date();
    int i = localCalendar.get(1) - 1900;
    localDate.setYear(i);
    int j = localCalendar.get(2);
    localDate.setMonth(j);
    int k = localCalendar.get(5);
    localDate.setDate(k);
    localDate.setHours(23);
    localDate.setMinutes(59);
    localDate.setSeconds(59);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    StringBuilder localStringBuilder = new StringBuilder("SELECT COUNT(*) FROM taskos_tasks WHERE ( due_date > ");
    long l1 = localDate.getTime();
    String str = l1 + " ) AND ( " + "is_checked" + " = 'false');";
    SQLiteStatement localSQLiteStatement = localSQLiteDatabase.compileStatement(str);
    long l2 = localSQLiteStatement.simpleQueryForLong();
    localSQLiteStatement.close();
    return l2;
  }

  public long countUncheckedTasksDueThisWeek()
  {
    Calendar localCalendar = Calendar.getInstance();
    Date localDate1 = new Date();
    int i = localCalendar.get(1) - 1900;
    localDate1.setYear(i);
    int j = localCalendar.get(2);
    localDate1.setMonth(j);
    int k = localCalendar.get(5);
    localDate1.setDate(k);
    localDate1.setHours(23);
    localDate1.setMinutes(59);
    localDate1.setSeconds(59);
    localCalendar.add(6, 7);
    Date localDate2 = new Date();
    int m = localCalendar.get(1) - 1900;
    localDate2.setYear(m);
    int n = localCalendar.get(2);
    localDate2.setMonth(n);
    int i1 = localCalendar.get(5);
    localDate2.setDate(i1);
    localDate2.setHours(23);
    localDate2.setMinutes(59);
    localDate2.setSeconds(59);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    StringBuilder localStringBuilder1 = new StringBuilder("SELECT COUNT(*) FROM taskos_tasks WHERE ( due_date > ");
    long l1 = localDate1.getTime();
    StringBuilder localStringBuilder2 = localStringBuilder1.append(l1).append(" ) AND ( ").append("due_date").append(" < ");
    long l2 = localDate2.getTime();
    String str = l2 + " ) AND ( " + "is_checked" + " = 'false');";
    SQLiteStatement localSQLiteStatement = localSQLiteDatabase.compileStatement(str);
    long l3 = localSQLiteStatement.simpleQueryForLong();
    localSQLiteStatement.close();
    return l3;
  }

  public long countUncheckedTasksDueToday()
  {
    Calendar localCalendar = Calendar.getInstance();
    Date localDate = new Date();
    int i = localCalendar.get(1) - 1900;
    localDate.setYear(i);
    int j = localCalendar.get(2);
    localDate.setMonth(j);
    int k = localCalendar.get(5);
    localDate.setDate(k);
    localDate.setHours(23);
    localDate.setMinutes(59);
    localDate.setSeconds(59);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    StringBuilder localStringBuilder = new StringBuilder("SELECT COUNT(*) FROM taskos_tasks WHERE ( due_date < ");
    long l1 = localDate.getTime();
    String str = l1 + " ) AND ( " + "is_checked" + " = 'false');";
    SQLiteStatement localSQLiteStatement = localSQLiteDatabase.compileStatement(str);
    long l2 = localSQLiteStatement.simpleQueryForLong();
    localSQLiteStatement.close();
    return l2;
  }

  public boolean createCategory(String paramString, boolean paramBoolean)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("name", paramString);
    String str = Boolean.toString(paramBoolean);
    localContentValues.put("is_editable", str);
    long l = TaskosApp.getDB().insert("taskos_categories", null, localContentValues);
    if (65535L == l);
    for (int i = 0; ; i = 1)
      return i;
  }

  public long createTask(String paramString1, String paramString2, long paramLong, boolean paramBoolean1, ActionType paramActionType, String paramString3, String paramString4, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, String paramString5)
  {
    long l1 = getDefaultCategoryId();
    long l2 = getDefaultImportanceId();
    ContentValues localContentValues1 = new ContentValues();
    localContentValues1.put("title", paramString1);
    localContentValues1.put("notes", paramString2);
    Long localLong1 = Long.valueOf(l2);
    localContentValues1.put("importance", localLong1);
    Long localLong2 = Long.valueOf(l1);
    localContentValues1.put("category", localLong2);
    Long localLong3 = Long.valueOf(paramLong);
    localContentValues1.put("due_date", localLong3);
    String str1 = Boolean.toString(paramBoolean1);
    localContentValues1.put("is_synced", str1);
    String str2 = paramActionType.name();
    localContentValues1.put("action", str2);
    ContentValues localContentValues2 = localContentValues1;
    String str3 = "action_param";
    String str4 = paramString3;
    localContentValues2.put(str3, str4);
    ContentValues localContentValues3 = localContentValues1;
    String str5 = "button_resource";
    String str6 = paramString4;
    localContentValues3.put(str5, str6);
    String str7 = Boolean.toString(paramBoolean2);
    localContentValues1.put("is_checked", str7);
    String str8 = Boolean.toString(paramBoolean3);
    localContentValues1.put("is_deleted", str8);
    String str9 = Boolean.toString(paramBoolean4);
    localContentValues1.put("is_quick_edit_visible", str9);
    ContentValues localContentValues4 = localContentValues1;
    String str10 = "contact";
    int i = paramString5;
    localContentValues4.put(str10, i);
    localContentValues1.put("alert_ids", "");
    localContentValues1.put("alert_times", "");
    Long localLong4 = Long.valueOf(System.currentTimeMillis());
    localContentValues1.put("creation_time", localLong4);
    Long localLong5 = Long.valueOf(System.currentTimeMillis());
    localContentValues1.put("modification_time", localLong5);
    long l3 = TaskosApp.getDB().insert("taskos_tasks", null, localContentValues1);
    refreshTasks();
    return l3;
  }

  public boolean deleteCategory(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase1 = TaskosApp.getDB();
    String[] arrayOfString = new String[2];
    arrayOfString[0] = "is_editable";
    arrayOfString[1] = "_id";
    String str1 = "_id=" + paramLong;
    Cursor localCursor = localSQLiteDatabase1.query("taskos_categories", arrayOfString, str1, null, null, null, null);
    if (localCursor.moveToFirst())
    {
      int i = localCursor.getColumnIndexOrThrow("is_editable");
      if (Boolean.valueOf(localCursor.getString(i)).booleanValue())
      {
        long l = getDefaultCategoryId();
        ContentValues localContentValues = new ContentValues();
        Long localLong = Long.valueOf(l);
        localContentValues.put("category", localLong);
        SQLiteDatabase localSQLiteDatabase2 = TaskosApp.getDB();
        String str2 = "category=" + paramLong;
        int j = localSQLiteDatabase2.update("taskos_tasks", localContentValues, str2, null);
        SQLiteDatabase localSQLiteDatabase3 = TaskosApp.getDB();
        String str3 = "_id=" + paramLong;
        int k = localSQLiteDatabase3.delete("taskos_categories", str3, null);
      }
    }
    localCursor.close();
    return true;
  }

  public void deleteSyncExtras(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str = "taskId=" + paramLong;
    int i = localSQLiteDatabase.delete("taskos_sync_extras", str, null);
  }

  public boolean deleteTask(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str = "_id=" + paramLong;
    if (localSQLiteDatabase.delete("taskos_tasks", str, null) > 0);
    for (int i = 1; ; i = 0)
    {
      refreshTasks();
      return i;
    }
  }

  public void disableRefreshing(long paramLong)
  {
    TaskosLog.d("DBHelper", "disable refreshing and set alarm");
    AlarmManager localAlarmManager = (AlarmManager)this.mCtx.getSystemService("alarm");
    Context localContext = this.mCtx;
    Intent localIntent = new Intent(localContext, DisableRefreshingReceiver.class);
    PendingIntent localPendingIntent = PendingIntent.getBroadcast(this.mCtx, 0, localIntent, 134217728);
    long l = new Date().getTime() + paramLong;
    localAlarmManager.set(0, l, localPendingIntent);
    this.disableRefreshing = 1;
  }

  public void enableRefreshing()
  {
    TaskosLog.d("DBHelper", "enable refreshing");
    this.disableRefreshing = 0;
  }

  public Cursor fetchAllCategories()
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString1 = new String[3];
    arrayOfString1[0] = "_id";
    arrayOfString1[1] = "name";
    arrayOfString1[2] = "is_editable";
    String[] arrayOfString2 = null;
    String str1 = null;
    String str2 = null;
    String str3 = null;
    return localSQLiteDatabase.query("taskos_categories", arrayOfString1, null, arrayOfString2, str1, str2, str3);
  }

  public Cursor fetchAllImportances()
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString1 = new String[2];
    arrayOfString1[0] = "_id";
    arrayOfString1[1] = "importance_name";
    String[] arrayOfString2 = null;
    String str1 = null;
    String str2 = null;
    String str3 = null;
    return localSQLiteDatabase.query("taskos_importance", arrayOfString1, null, arrayOfString2, str1, str2, str3);
  }

  public Cursor fetchAllTasks()
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString1 = new String[4];
    arrayOfString1[0] = "_id";
    arrayOfString1[1] = "title";
    arrayOfString1[2] = "notes";
    arrayOfString1[3] = "is_checked";
    String[] arrayOfString2 = null;
    String str1 = null;
    String str2 = null;
    String str3 = null;
    return localSQLiteDatabase.query("taskos_tasks", arrayOfString1, null, arrayOfString2, str1, str2, str3);
  }

  public Cursor fetchCategory(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[2];
    arrayOfString[0] = "name";
    arrayOfString[1] = "is_editable";
    String str1 = "_id=" + paramLong;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    return localSQLiteDatabase.query("taskos_categories", arrayOfString, str1, null, str2, str3, str4);
  }

  public Cursor fetchCheckedTasks()
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[10];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "title";
    arrayOfString[2] = "due_date";
    arrayOfString[3] = "category";
    arrayOfString[4] = "is_checked";
    arrayOfString[5] = "importance";
    arrayOfString[6] = "is_synced";
    arrayOfString[7] = "action";
    arrayOfString[8] = "action_param";
    arrayOfString[9] = "button_resource";
    String str1 = null;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    return localSQLiteDatabase.query(1, "taskos_tasks", arrayOfString, "is_checked='true' AND is_deleted='false'", null, str1, str2, str3, str4);
  }

  public Cursor fetchCreatedTasks(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[5];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "title";
    arrayOfString[2] = "is_checked";
    arrayOfString[3] = "notes";
    arrayOfString[4] = "due_date";
    StringBuilder localStringBuilder = new StringBuilder("(creation_time>");
    String str1 = Long.toString(paramLong);
    String str2 = str1 + ") AND (" + "is_deleted" + " = 'false')";
    String str3 = null;
    String str4 = null;
    String str5 = null;
    String str6 = null;
    return localSQLiteDatabase.query(1, "taskos_tasks", arrayOfString, str2, null, str3, str4, str5, str6);
  }

  public Cursor fetchDeletedTasks()
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[9];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "title";
    arrayOfString[2] = "category";
    arrayOfString[3] = "is_checked";
    arrayOfString[4] = "importance";
    arrayOfString[5] = "is_synced";
    arrayOfString[6] = "action";
    arrayOfString[7] = "action_param";
    arrayOfString[8] = "is_deleted";
    String str1 = null;
    String str2 = null;
    String str3 = null;
    return localSQLiteDatabase.query(1, "taskos_tasks", arrayOfString, "is_deleted='true'", null, str1, str2, "checked_time ASC", str3);
  }

  public Cursor fetchImportance(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[2];
    arrayOfString[0] = "importance_name";
    arrayOfString[1] = "importance_color";
    String str1 = "_id=" + paramLong;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    return localSQLiteDatabase.query("taskos_importance", arrayOfString, str1, null, str2, str3, str4);
  }

  public Cursor fetchTask(long paramLong)
    throws SQLException
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[10];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "title";
    arrayOfString[2] = "notes";
    arrayOfString[3] = "due_date";
    arrayOfString[4] = "contact";
    arrayOfString[5] = "alert_status";
    arrayOfString[6] = "importance";
    arrayOfString[7] = "category";
    arrayOfString[8] = "is_checked";
    arrayOfString[9] = "is_synced";
    String str1 = "_id=" + paramLong;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    String str5 = null;
    Cursor localCursor = localSQLiteDatabase.query(1, "taskos_tasks", arrayOfString, str1, null, str2, str3, str4, str5);
    if (localCursor != null)
      boolean bool = localCursor.moveToFirst();
    return localCursor;
  }

  public Cursor fetchTaskListGroupsCursor(String paramString)
  {
    Cursor localCursor;
    if (paramString.equals("due_date"))
    {
      SQLiteDatabase localSQLiteDatabase1 = TaskosApp.getDB();
      String[] arrayOfString1 = new String[2];
      arrayOfString1[0] = "_id";
      arrayOfString1[1] = "due_group_name";
      String[] arrayOfString2 = null;
      String str1 = null;
      String str2 = null;
      String str3 = null;
      localCursor = localSQLiteDatabase1.query("taskos_due_groups", arrayOfString1, null, arrayOfString2, str1, str2, str3);
    }
    while (true)
    {
      return localCursor;
      if (paramString.equals("importance"))
      {
        SQLiteDatabase localSQLiteDatabase2 = TaskosApp.getDB();
        String[] arrayOfString3 = new String[2];
        arrayOfString3[0] = "_id";
        arrayOfString3[1] = "importance_name";
        String[] arrayOfString4 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        localCursor = localSQLiteDatabase2.query("taskos_importance", arrayOfString3, null, arrayOfString4, str4, str5, str6);
        continue;
      }
      if (paramString.equals("category"))
      {
        SQLiteDatabase localSQLiteDatabase3 = TaskosApp.getDB();
        String[] arrayOfString5 = new String[3];
        arrayOfString5[0] = "_id";
        arrayOfString5[1] = "name";
        arrayOfString5[2] = "is_editable";
        String[] arrayOfString6 = null;
        String str7 = null;
        String str8 = null;
        String str9 = null;
        localCursor = localSQLiteDatabase3.query("taskos_categories", arrayOfString5, null, arrayOfString6, str7, str8, str9);
        continue;
      }
      localCursor = null;
    }
  }

  public Cursor fetchUnCheckedTasks()
  {
    String str1 = PreferencesHelper.getPrefString(this.mCtx, "sort_by", "due_date");
    String str2 = PreferencesHelper.getPrefString(this.mCtx, "sort_direction", "ASC");
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[10];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "title";
    arrayOfString[2] = "due_date";
    arrayOfString[3] = "category";
    arrayOfString[4] = "is_checked";
    arrayOfString[5] = "importance";
    arrayOfString[6] = "is_synced";
    arrayOfString[7] = "action";
    arrayOfString[8] = "action_param";
    arrayOfString[9] = "button_resource";
    String str3 = String.valueOf(str1);
    String str4 = str3 + " " + str2;
    String str5 = null;
    String str6 = null;
    String str7 = null;
    return localSQLiteDatabase.query(1, "taskos_tasks", arrayOfString, "is_checked='false'", null, str5, str6, str4, str7);
  }

  public Cursor fetchUnCheckedTasksWithAlarms()
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[4];
    arrayOfString[0] = "alert_status";
    arrayOfString[1] = "due_date";
    arrayOfString[2] = "title";
    arrayOfString[3] = "_id";
    String str1 = null;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    return localSQLiteDatabase.query(1, "taskos_tasks", arrayOfString, "(action > 0) AND (is_checked='false')", null, str1, str2, str3, str4);
  }

  public Cursor fetchUnDeletedTasks()
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[11];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "title";
    arrayOfString[2] = "due_date";
    arrayOfString[3] = "category";
    arrayOfString[4] = "is_checked";
    arrayOfString[5] = "is_quick_edit_visible";
    arrayOfString[6] = "importance";
    arrayOfString[7] = "is_synced";
    arrayOfString[8] = "action";
    arrayOfString[9] = "action_param";
    arrayOfString[10] = "button_resource";
    String str1 = null;
    String str2 = null;
    String str3 = null;
    return localSQLiteDatabase.query(1, "taskos_tasks", arrayOfString, "is_deleted='false'", null, str1, str2, "is_checked,creation_time DESC", str3);
  }

  public Cursor fetchUnSyncedTasks()
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "_id";
    String str1 = null;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    return localSQLiteDatabase.query(1, "taskos_tasks", arrayOfString, "is_synced='false'", null, str1, str2, str3, str4);
  }

  public Cursor fetchUncheckedTasksByCategory(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[11];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "title";
    arrayOfString[2] = "due_date";
    arrayOfString[3] = "category";
    arrayOfString[4] = "is_checked";
    arrayOfString[5] = "is_quick_edit_visible";
    arrayOfString[6] = "importance";
    arrayOfString[7] = "is_synced";
    arrayOfString[8] = "action";
    arrayOfString[9] = "action_param";
    arrayOfString[10] = "button_resource";
    String str1 = "(category=" + paramLong + ") AND (" + "is_deleted" + "='false')";
    String str2 = null;
    String str3 = null;
    String str4 = null;
    return localSQLiteDatabase.query(1, "taskos_tasks", arrayOfString, str1, null, str2, str3, "is_checked,creation_time DESC", str4);
  }

  public Cursor fetchUncheckedTasksByImportance(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[11];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "title";
    arrayOfString[2] = "due_date";
    arrayOfString[3] = "category";
    arrayOfString[4] = "is_checked";
    arrayOfString[5] = "is_quick_edit_visible";
    arrayOfString[6] = "importance";
    arrayOfString[7] = "is_synced";
    arrayOfString[8] = "action";
    arrayOfString[9] = "action_param";
    arrayOfString[10] = "button_resource";
    String str1 = "(importance=" + paramLong + ") AND (" + "is_deleted" + "='false')";
    String str2 = null;
    String str3 = null;
    String str4 = null;
    return localSQLiteDatabase.query(1, "taskos_tasks", arrayOfString, str1, null, str2, str3, "is_checked,creation_time DESC", str4);
  }

  public Cursor fetchUncheckedTasksDueLater()
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.add(6, 7);
    Date localDate = new Date();
    int i = localCalendar.get(1) - 1900;
    localDate.setYear(i);
    int j = localCalendar.get(2);
    localDate.setMonth(j);
    int k = localCalendar.get(5);
    localDate.setDate(k);
    localDate.setHours(23);
    localDate.setMinutes(59);
    localDate.setSeconds(59);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[11];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "title";
    arrayOfString[2] = "due_date";
    arrayOfString[3] = "category";
    arrayOfString[4] = "is_checked";
    arrayOfString[5] = "is_quick_edit_visible";
    arrayOfString[6] = "importance";
    arrayOfString[7] = "is_synced";
    arrayOfString[8] = "action";
    arrayOfString[9] = "action_param";
    arrayOfString[10] = "button_resource";
    StringBuilder localStringBuilder = new StringBuilder("(due_date>");
    long l = localDate.getTime();
    String str1 = l + ") AND (" + "is_deleted" + "='false')";
    String str2 = null;
    String str3 = null;
    String str4 = null;
    return localSQLiteDatabase.query(1, "taskos_tasks", arrayOfString, str1, null, str2, str3, "is_checked,due_date ASC", str4);
  }

  public Cursor fetchUncheckedTasksDueThisWeek()
  {
    Calendar localCalendar = Calendar.getInstance();
    Date localDate1 = new Date();
    int i = localCalendar.get(1) - 1900;
    localDate1.setYear(i);
    int j = localCalendar.get(2);
    localDate1.setMonth(j);
    int k = localCalendar.get(5);
    localDate1.setDate(k);
    localDate1.setHours(23);
    localDate1.setMinutes(59);
    localDate1.setSeconds(59);
    localCalendar.add(6, 7);
    Date localDate2 = new Date();
    int m = localCalendar.get(1) - 1900;
    localDate2.setYear(m);
    int n = localCalendar.get(2);
    localDate2.setMonth(n);
    int i1 = localCalendar.get(5);
    localDate2.setDate(i1);
    localDate2.setHours(23);
    localDate2.setMinutes(59);
    localDate2.setSeconds(59);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[11];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "title";
    arrayOfString[2] = "due_date";
    arrayOfString[3] = "category";
    arrayOfString[4] = "is_checked";
    arrayOfString[5] = "is_quick_edit_visible";
    arrayOfString[6] = "importance";
    arrayOfString[7] = "is_synced";
    arrayOfString[8] = "action";
    arrayOfString[9] = "action_param";
    arrayOfString[10] = "button_resource";
    StringBuilder localStringBuilder1 = new StringBuilder("(due_date>");
    long l1 = localDate1.getTime();
    StringBuilder localStringBuilder2 = localStringBuilder1.append(l1).append(") AND (").append("due_date").append("<");
    long l2 = localDate2.getTime();
    String str1 = l2 + ") AND (" + "is_deleted" + "='false')";
    String str2 = null;
    String str3 = null;
    String str4 = null;
    return localSQLiteDatabase.query(1, "taskos_tasks", arrayOfString, str1, null, str2, str3, "is_checked,due_date ASC", str4);
  }

  public Cursor fetchUncheckedTasksDueToday()
  {
    Calendar localCalendar = Calendar.getInstance();
    Date localDate = new Date();
    int i = localCalendar.get(1) - 1900;
    localDate.setYear(i);
    int j = localCalendar.get(2);
    localDate.setMonth(j);
    int k = localCalendar.get(5);
    localDate.setDate(k);
    localDate.setHours(23);
    localDate.setMinutes(59);
    localDate.setSeconds(59);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[11];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "title";
    arrayOfString[2] = "due_date";
    arrayOfString[3] = "category";
    arrayOfString[4] = "is_checked";
    arrayOfString[5] = "is_quick_edit_visible";
    arrayOfString[6] = "importance";
    arrayOfString[7] = "is_synced";
    arrayOfString[8] = "action";
    arrayOfString[9] = "action_param";
    arrayOfString[10] = "button_resource";
    StringBuilder localStringBuilder = new StringBuilder("(due_date<");
    long l = localDate.getTime();
    String str1 = l + ") AND (" + "is_deleted" + "='false')";
    String str2 = null;
    String str3 = null;
    String str4 = null;
    return localSQLiteDatabase.query(1, "taskos_tasks", arrayOfString, str1, null, str2, str3, "is_checked,due_date ASC", str4);
  }

  public Cursor fetchUpdatedTasks(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[5];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "title";
    arrayOfString[2] = "is_checked";
    arrayOfString[3] = "notes";
    arrayOfString[4] = "due_date";
    StringBuilder localStringBuilder1 = new StringBuilder("(modification_time>");
    String str1 = Long.toString(paramLong);
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append(") AND (").append("creation_time").append("<");
    String str2 = Long.toString(paramLong);
    String str3 = str2 + ") AND (" + "is_deleted" + " = 'false')";
    String str4 = null;
    String str5 = null;
    String str6 = null;
    String str7 = null;
    return localSQLiteDatabase.query(1, "taskos_tasks", arrayOfString, str3, null, str4, str5, str6, str7);
  }

  public Cursor getAlertInfo(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[9];
    arrayOfString[0] = "alert_status";
    arrayOfString[1] = "alert_one_time";
    arrayOfString[2] = "alert_repeating_interval_amount";
    arrayOfString[3] = "alert_repeating_interval";
    arrayOfString[4] = "alert_repeating_time";
    arrayOfString[5] = "alert_repeating_days_of_week";
    arrayOfString[6] = "alert_repeating_day_of_month";
    arrayOfString[7] = "alert_ids";
    arrayOfString[8] = "alert_times";
    String str1 = "_id=" + paramLong;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    return localSQLiteDatabase.query("taskos_tasks", arrayOfString, str1, null, str2, str3, str4);
  }

  public Date getDueDate(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "due_date";
    String str1 = "_id=" + paramLong;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    String str5 = null;
    Cursor localCursor = localSQLiteDatabase.query("taskos_tasks", arrayOfString, str1, null, str2, str3, str4, str5);
    long l = 0L;
    if (localCursor.moveToFirst())
    {
      int i = localCursor.getColumnIndexOrThrow("due_date");
      l = localCursor.getLong(i);
    }
    localCursor.close();
    return new Date(l);
  }

  public int getImportanceResource(long paramLong, boolean paramBoolean)
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "importance_color";
    String str1 = "_id=" + paramLong;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    Cursor localCursor = localSQLiteDatabase.query("taskos_importance", arrayOfString, str1, null, str2, str3, str4);
    int i = 1;
    String str5;
    Resources localResources1;
    String str6;
    if (localCursor.moveToFirst())
    {
      int j = localCursor.getColumnIndexOrThrow("importance_color");
      str5 = localCursor.getString(j);
      if (!paramBoolean)
        break label160;
      localResources1 = this.mCtx.getResources();
      str6 = "com.taskos:drawable/" + str5 + "_grayed";
    }
    label160: Resources localResources2;
    String str7;
    for (i = localResources1.getIdentifier(str6, null, null); ; i = localResources2.getIdentifier(str7, null, null))
    {
      localCursor.close();
      return i;
      localResources2 = this.mCtx.getResources();
      str7 = "com.taskos:drawable/" + str5;
    }
  }

  public String getNotes(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "notes";
    String str1 = "_id=" + paramLong;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    String str5 = null;
    Cursor localCursor = localSQLiteDatabase.query("taskos_tasks", arrayOfString, str1, null, str2, str3, str4, str5);
    String str6 = "";
    if (localCursor.moveToFirst())
    {
      int i = localCursor.getColumnIndexOrThrow("notes");
      str6 = localCursor.getString(i);
    }
    localCursor.close();
    return str6;
  }

  public Cursor getSyncExtrasForSyncService(String paramString)
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[4];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "taskId";
    arrayOfString[2] = "key";
    arrayOfString[3] = "value";
    String str1 = "service='" + paramString + "'";
    String str2 = null;
    String str3 = null;
    String str4 = null;
    return localSQLiteDatabase.query("taskos_sync_extras", arrayOfString, str1, null, str2, str3, str4);
  }

  public String getSyncServiceValueForTask(String paramString1, String paramString2, long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "value";
    String str1 = "(service='" + paramString1 + "') AND (" + "key" + "='" + paramString2 + "') AND (" + "taskId" + "=" + paramLong + ")";
    String str2 = null;
    String str3 = null;
    String str4 = null;
    Cursor localCursor = localSQLiteDatabase.query("taskos_sync_extras", arrayOfString, str1, null, str2, str3, str4);
    String str5 = null;
    if (localCursor.moveToFirst())
    {
      int i = localCursor.getColumnIndexOrThrow("value");
      str5 = localCursor.getString(i);
    }
    localCursor.close();
    return str5;
  }

  public long getTaskForSyncServiceValue(String paramString1, String paramString2, String paramString3)
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "taskId";
    String str1 = "(service='" + paramString1 + "') AND (" + "key" + "='" + paramString2 + "') AND (" + "value" + "='" + paramString3 + "')";
    String str2 = null;
    String str3 = null;
    String str4 = null;
    Cursor localCursor = localSQLiteDatabase.query("taskos_sync_extras", arrayOfString, str1, null, str2, str3, str4);
    long l;
    if (localCursor.moveToFirst())
    {
      int i = localCursor.getColumnIndexOrThrow("taskId");
      l = localCursor.getLong(i);
    }
    while (true)
    {
      localCursor.close();
      return l;
      l = 65535L;
    }
  }

  public void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("create table taskos_tasks(_id integer primary key autoincrement, title text not null, due_date integer not null, creation_time integer not null, modification_time integer not null, notes text not null, is_synced text not null, is_checked text not null, is_deleted text not null, is_quick_edit_visible text not null, checked_time integer not null default 0, contact text not null, alert_status integer not null default 0, alert_one_time integer not null default 0, alert_repeating_interval integer not null default 0, alert_repeating_interval_amount integer not null default 1, alert_repeating_days_of_week integer not null default 0, alert_repeating_day_of_month integer not null default 1, alert_repeating_time integer not null default 480, alert_ids text not null, alert_times text not null, importance integer not null, category integer not null, action text not null, action_param text not null, button_resource text not null);");
    paramSQLiteDatabase.execSQL("create table taskos_categories(_id integer primary key autoincrement, name text not null, is_editable text not null);");
    paramSQLiteDatabase.execSQL("create table taskos_importance(_id integer primary key autoincrement, importance_name text not null, importance_color text not null);");
    paramSQLiteDatabase.execSQL("create table taskos_due_groups(_id integer primary key autoincrement, due_group_name text not null);");
    paramSQLiteDatabase.execSQL("create table taskos_sync_extras(_id integer primary key autoincrement, taskId integer not null, service text not null,key text,value text);");
    addImportance(paramSQLiteDatabase, "High", "red_grad_bg");
    addImportance(paramSQLiteDatabase, "Normal", "yellow_grad_bg");
    addImportance(paramSQLiteDatabase, "Low", "green_grad_bg");
    addCategory(paramSQLiteDatabase, "General", 0);
    addCategory(paramSQLiteDatabase, "Work", 1);
    addCategory(paramSQLiteDatabase, "Home", 1);
    addDueGroup(paramSQLiteDatabase, "Today");
    addDueGroup(paramSQLiteDatabase, "This Week");
    addDueGroup(paramSQLiteDatabase, "Later");
  }

  // ERROR //
  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
  {
    // Byte code:
    //   0: new 273	java/lang/StringBuilder
    //   3: dup
    //   4: ldc_w 626
    //   7: invokespecial 278	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   10: astore 4
    //   12: iload_2
    //   13: istore 5
    //   15: aload 4
    //   17: iload 5
    //   19: invokevirtual 629	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   22: ldc_w 631
    //   25: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: astore 6
    //   30: iload_3
    //   31: istore 7
    //   33: aload 6
    //   35: iload 7
    //   37: invokevirtual 629	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   40: invokevirtual 292	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   43: astore 8
    //   45: ldc_w 632
    //   48: aload 8
    //   50: invokestatic 425	com/taskos/utils/TaskosLog:d	(Ljava/lang/String;Ljava/lang/Object;)V
    //   53: iload_2
    //   54: istore 9
    //   56: iconst_3
    //   57: istore 10
    //   59: iload 9
    //   61: iload 10
    //   63: if_icmpge +100 -> 163
    //   66: aload_1
    //   67: astore 11
    //   69: ldc_w 634
    //   72: astore 12
    //   74: aload 11
    //   76: aload 12
    //   78: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   81: aload_1
    //   82: astore 13
    //   84: ldc_w 636
    //   87: astore 14
    //   89: aload 13
    //   91: aload 14
    //   93: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   96: new 273	java/lang/StringBuilder
    //   99: dup
    //   100: ldc_w 638
    //   103: invokespecial 278	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   106: astore 15
    //   108: getstatic 642	com/taskos/execution/ActionType:NO_ACTION	Lcom/taskos/execution/ActionType;
    //   111: invokevirtual 376	com/taskos/execution/ActionType:name	()Ljava/lang/String;
    //   114: astore 16
    //   116: aload 15
    //   118: aload 16
    //   120: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   123: ldc_w 644
    //   126: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   129: invokevirtual 292	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   132: astore 17
    //   134: aload_1
    //   135: astore 18
    //   137: aload 17
    //   139: astore 19
    //   141: aload 18
    //   143: aload 19
    //   145: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   148: aload_1
    //   149: astore 20
    //   151: ldc_w 646
    //   154: astore 21
    //   156: aload 20
    //   158: aload 21
    //   160: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   163: iload_2
    //   164: istore 22
    //   166: iconst_4
    //   167: istore 23
    //   169: iload 22
    //   171: iload 23
    //   173: if_icmpge +1162 -> 1335
    //   176: aload_1
    //   177: invokevirtual 649	android/database/sqlite/SQLiteDatabase:beginTransaction	()V
    //   180: aload_1
    //   181: astore 24
    //   183: ldc_w 651
    //   186: astore 25
    //   188: aload 24
    //   190: aload 25
    //   192: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   195: aload_1
    //   196: astore 26
    //   198: ldc_w 653
    //   201: astore 27
    //   203: aload 26
    //   205: aload 27
    //   207: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   210: aload_1
    //   211: astore 28
    //   213: ldc_w 655
    //   216: astore 29
    //   218: aload 28
    //   220: aload 29
    //   222: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   225: aload_1
    //   226: astore 30
    //   228: ldc 24
    //   230: astore 31
    //   232: aload 30
    //   234: aload 31
    //   236: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   239: new 657	java/text/SimpleDateFormat
    //   242: dup
    //   243: ldc_w 659
    //   246: invokespecial 660	java/text/SimpleDateFormat:<init>	(Ljava/lang/String;)V
    //   249: astore 32
    //   251: new 657	java/text/SimpleDateFormat
    //   254: dup
    //   255: ldc_w 662
    //   258: invokespecial 660	java/text/SimpleDateFormat:<init>	(Ljava/lang/String;)V
    //   261: astore 33
    //   263: new 657	java/text/SimpleDateFormat
    //   266: dup
    //   267: ldc_w 664
    //   270: invokespecial 660	java/text/SimpleDateFormat:<init>	(Ljava/lang/String;)V
    //   273: astore 34
    //   275: aload_1
    //   276: ldc 48
    //   278: aconst_null
    //   279: aconst_null
    //   280: aconst_null
    //   281: aconst_null
    //   282: aconst_null
    //   283: aconst_null
    //   284: invokevirtual 249	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   287: astore 35
    //   289: aload 35
    //   291: invokeinterface 255 1 0
    //   296: ifeq +1009 -> 1305
    //   299: aload 35
    //   301: astore 36
    //   303: ldc 184
    //   305: astore 37
    //   307: aload 36
    //   309: aload 37
    //   311: invokeinterface 259 2 0
    //   316: istore 38
    //   318: aload 35
    //   320: astore 39
    //   322: iload 38
    //   324: istore 40
    //   326: aload 39
    //   328: iload 40
    //   330: invokeinterface 394 2 0
    //   335: astore 41
    //   337: aload 35
    //   339: astore 42
    //   341: ldc 159
    //   343: astore 43
    //   345: aload 42
    //   347: aload 43
    //   349: invokeinterface 259 2 0
    //   354: istore 44
    //   356: aload 35
    //   358: astore 45
    //   360: iload 44
    //   362: istore 46
    //   364: aload 45
    //   366: iload 46
    //   368: invokeinterface 394 2 0
    //   373: astore 47
    //   375: aload 35
    //   377: astore 48
    //   379: ldc_w 666
    //   382: astore 49
    //   384: aload 48
    //   386: aload 49
    //   388: invokeinterface 259 2 0
    //   393: istore 50
    //   395: aload 35
    //   397: astore 51
    //   399: iload 50
    //   401: istore 52
    //   403: aload 51
    //   405: iload 52
    //   407: invokeinterface 394 2 0
    //   412: astore 53
    //   414: aload 35
    //   416: astore 54
    //   418: ldc 180
    //   420: astore 55
    //   422: aload 54
    //   424: aload 55
    //   426: invokeinterface 259 2 0
    //   431: istore 56
    //   433: aload 35
    //   435: astore 57
    //   437: iload 56
    //   439: istore 58
    //   441: aload 57
    //   443: iload 58
    //   445: invokeinterface 394 2 0
    //   450: astore 59
    //   452: aload 35
    //   454: astore 60
    //   456: ldc 174
    //   458: astore 61
    //   460: aload 60
    //   462: aload 61
    //   464: invokeinterface 259 2 0
    //   469: istore 62
    //   471: aload 35
    //   473: astore 63
    //   475: iload 62
    //   477: istore 64
    //   479: aload 63
    //   481: iload 64
    //   483: invokeinterface 394 2 0
    //   488: astore 65
    //   490: aload 35
    //   492: astore 66
    //   494: ldc 165
    //   496: astore 67
    //   498: aload 66
    //   500: aload 67
    //   502: invokeinterface 259 2 0
    //   507: istore 68
    //   509: aload 35
    //   511: astore 69
    //   513: iload 68
    //   515: istore 70
    //   517: aload 69
    //   519: iload 70
    //   521: invokeinterface 394 2 0
    //   526: astore 71
    //   528: aload 35
    //   530: astore 72
    //   532: ldc 153
    //   534: astore 73
    //   536: aload 72
    //   538: aload 73
    //   540: invokeinterface 259 2 0
    //   545: istore 74
    //   547: aload 35
    //   549: astore 75
    //   551: iload 74
    //   553: istore 76
    //   555: aload 75
    //   557: iload 76
    //   559: invokeinterface 394 2 0
    //   564: astore 77
    //   566: aload 35
    //   568: astore 78
    //   570: ldc 135
    //   572: astore 79
    //   574: aload 78
    //   576: aload 79
    //   578: invokeinterface 259 2 0
    //   583: istore 80
    //   585: aload 35
    //   587: astore 81
    //   589: iload 80
    //   591: istore 82
    //   593: aload 81
    //   595: iload 82
    //   597: invokeinterface 669 2 0
    //   602: istore 83
    //   604: aload 35
    //   606: astore 84
    //   608: ldc 162
    //   610: astore 85
    //   612: aload 84
    //   614: aload 85
    //   616: invokeinterface 259 2 0
    //   621: istore 86
    //   623: aload 35
    //   625: astore 87
    //   627: iload 86
    //   629: istore 88
    //   631: aload 87
    //   633: iload 88
    //   635: invokeinterface 669 2 0
    //   640: istore 89
    //   642: aload 35
    //   644: astore 90
    //   646: ldc 147
    //   648: astore 91
    //   650: aload 90
    //   652: aload 91
    //   654: invokeinterface 259 2 0
    //   659: istore 92
    //   661: aload 35
    //   663: astore 93
    //   665: iload 92
    //   667: istore 94
    //   669: aload 93
    //   671: iload 94
    //   673: invokeinterface 669 2 0
    //   678: istore 95
    //   680: aload 35
    //   682: astore 96
    //   684: ldc 108
    //   686: astore 97
    //   688: aload 96
    //   690: aload 97
    //   692: invokeinterface 259 2 0
    //   697: istore 98
    //   699: aload 35
    //   701: astore 99
    //   703: iload 98
    //   705: istore 100
    //   707: aload 99
    //   709: iload 100
    //   711: invokeinterface 394 2 0
    //   716: astore 101
    //   718: aload 35
    //   720: astore 102
    //   722: ldc 111
    //   724: astore 103
    //   726: aload 102
    //   728: aload 103
    //   730: invokeinterface 259 2 0
    //   735: istore 104
    //   737: aload 35
    //   739: astore 105
    //   741: iload 104
    //   743: istore 106
    //   745: aload 105
    //   747: iload 106
    //   749: invokeinterface 394 2 0
    //   754: astore 107
    //   756: aload 35
    //   758: astore 108
    //   760: ldc 144
    //   762: astore 109
    //   764: aload 108
    //   766: aload 109
    //   768: invokeinterface 259 2 0
    //   773: istore 110
    //   775: aload 35
    //   777: astore 111
    //   779: iload 110
    //   781: istore 112
    //   783: aload 111
    //   785: iload 112
    //   787: invokeinterface 394 2 0
    //   792: astore 113
    //   794: aload 113
    //   796: astore 114
    //   798: aload 33
    //   800: astore 115
    //   802: aload 47
    //   804: astore 116
    //   806: aload 115
    //   808: aload 116
    //   810: invokevirtual 673	java/text/SimpleDateFormat:parse	(Ljava/lang/String;)Ljava/util/Date;
    //   813: astore 117
    //   815: aload 34
    //   817: astore 118
    //   819: aload 53
    //   821: astore 119
    //   823: aload 118
    //   825: aload 119
    //   827: invokevirtual 673	java/text/SimpleDateFormat:parse	(Ljava/lang/String;)Ljava/util/Date;
    //   830: astore 120
    //   832: new 318	java/util/Date
    //   835: dup
    //   836: invokespecial 319	java/util/Date:<init>	()V
    //   839: astore 121
    //   841: aload 117
    //   843: invokevirtual 677	java/util/Date:getYear	()I
    //   846: istore 122
    //   848: aload 121
    //   850: astore 123
    //   852: iload 122
    //   854: istore 124
    //   856: aload 123
    //   858: iload 124
    //   860: invokevirtual 327	java/util/Date:setYear	(I)V
    //   863: aload 117
    //   865: invokevirtual 680	java/util/Date:getMonth	()I
    //   868: istore 125
    //   870: aload 121
    //   872: astore 126
    //   874: iload 125
    //   876: istore 127
    //   878: aload 126
    //   880: iload 127
    //   882: invokevirtual 330	java/util/Date:setMonth	(I)V
    //   885: aload 117
    //   887: invokevirtual 683	java/util/Date:getDate	()I
    //   890: istore 128
    //   892: aload 121
    //   894: astore 129
    //   896: iload 128
    //   898: istore 130
    //   900: aload 129
    //   902: iload 130
    //   904: invokevirtual 333	java/util/Date:setDate	(I)V
    //   907: aload 120
    //   909: invokevirtual 686	java/util/Date:getHours	()I
    //   912: istore 131
    //   914: aload 121
    //   916: astore 132
    //   918: iload 131
    //   920: istore 133
    //   922: aload 132
    //   924: iload 133
    //   926: invokevirtual 336	java/util/Date:setHours	(I)V
    //   929: aload 120
    //   931: invokevirtual 689	java/util/Date:getMinutes	()I
    //   934: istore 134
    //   936: aload 121
    //   938: astore 135
    //   940: iload 134
    //   942: istore 136
    //   944: aload 135
    //   946: iload 136
    //   948: invokevirtual 339	java/util/Date:setMinutes	(I)V
    //   951: aload 121
    //   953: astore 137
    //   955: iconst_0
    //   956: istore 138
    //   958: aload 137
    //   960: iload 138
    //   962: invokevirtual 342	java/util/Date:setSeconds	(I)V
    //   965: aload 32
    //   967: astore 139
    //   969: aload 121
    //   971: astore 140
    //   973: aload 139
    //   975: aload 140
    //   977: invokevirtual 693	java/text/SimpleDateFormat:format	(Ljava/util/Date;)Ljava/lang/String;
    //   980: astore 113
    //   982: aload 113
    //   984: astore 141
    //   986: new 211	android/content/ContentValues
    //   989: dup
    //   990: invokespecial 213	android/content/ContentValues:<init>	()V
    //   993: astore 142
    //   995: aload 142
    //   997: astore 143
    //   999: ldc 184
    //   1001: astore 144
    //   1003: aload 41
    //   1005: astore 145
    //   1007: aload 143
    //   1009: aload 144
    //   1011: aload 145
    //   1013: invokevirtual 217	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1016: aload 142
    //   1018: astore 146
    //   1020: ldc 180
    //   1022: astore 147
    //   1024: aload 59
    //   1026: astore 148
    //   1028: aload 146
    //   1030: aload 147
    //   1032: aload 148
    //   1034: invokevirtual 217	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1037: iload 89
    //   1039: invokestatic 698	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   1042: astore 149
    //   1044: aload 142
    //   1046: astore 150
    //   1048: ldc 162
    //   1050: astore 151
    //   1052: aload 149
    //   1054: astore 152
    //   1056: aload 150
    //   1058: aload 151
    //   1060: aload 152
    //   1062: invokevirtual 701	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   1065: iload 95
    //   1067: invokestatic 698	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   1070: astore 153
    //   1072: aload 142
    //   1074: astore 154
    //   1076: ldc 147
    //   1078: astore 155
    //   1080: aload 153
    //   1082: astore 156
    //   1084: aload 154
    //   1086: aload 155
    //   1088: aload 156
    //   1090: invokevirtual 701	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   1093: aload 142
    //   1095: astore 157
    //   1097: ldc 159
    //   1099: astore 158
    //   1101: aload 141
    //   1103: astore 159
    //   1105: aload 157
    //   1107: aload 158
    //   1109: aload 159
    //   1111: invokevirtual 217	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1114: aload 142
    //   1116: astore 160
    //   1118: ldc 174
    //   1120: astore 161
    //   1122: aload 65
    //   1124: astore 162
    //   1126: aload 160
    //   1128: aload 161
    //   1130: aload 162
    //   1132: invokevirtual 217	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1135: aload 142
    //   1137: astore 163
    //   1139: ldc 108
    //   1141: astore 164
    //   1143: aload 101
    //   1145: astore 165
    //   1147: aload 163
    //   1149: aload 164
    //   1151: aload 165
    //   1153: invokevirtual 217	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1156: aload 142
    //   1158: astore 166
    //   1160: ldc 111
    //   1162: astore 167
    //   1164: aload 107
    //   1166: astore 168
    //   1168: aload 166
    //   1170: aload 167
    //   1172: aload 168
    //   1174: invokevirtual 217	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1177: aload 142
    //   1179: astore 169
    //   1181: ldc 144
    //   1183: astore 170
    //   1185: aload 114
    //   1187: astore 171
    //   1189: aload 169
    //   1191: aload 170
    //   1193: aload 171
    //   1195: invokevirtual 217	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1198: aload 142
    //   1200: astore 172
    //   1202: ldc 165
    //   1204: astore 173
    //   1206: aload 71
    //   1208: astore 174
    //   1210: aload 172
    //   1212: aload 173
    //   1214: aload 174
    //   1216: invokevirtual 217	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1219: aload 142
    //   1221: astore 175
    //   1223: ldc 153
    //   1225: astore 176
    //   1227: aload 77
    //   1229: astore 177
    //   1231: aload 175
    //   1233: aload 176
    //   1235: aload 177
    //   1237: invokevirtual 217	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1240: iload 83
    //   1242: invokestatic 698	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   1245: astore 178
    //   1247: aload 142
    //   1249: astore 179
    //   1251: ldc 135
    //   1253: astore 180
    //   1255: aload 178
    //   1257: astore 181
    //   1259: aload 179
    //   1261: aload 180
    //   1263: aload 181
    //   1265: invokevirtual 701	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   1268: aload_1
    //   1269: astore 182
    //   1271: ldc 45
    //   1273: astore 183
    //   1275: aconst_null
    //   1276: astore 184
    //   1278: aload 142
    //   1280: astore 185
    //   1282: aload 182
    //   1284: aload 183
    //   1286: aload 184
    //   1288: aload 185
    //   1290: invokevirtual 229	android/database/sqlite/SQLiteDatabase:insert	(Ljava/lang/String;Ljava/lang/String;Landroid/content/ContentValues;)J
    //   1293: lstore 186
    //   1295: aload 35
    //   1297: invokeinterface 704 1 0
    //   1302: ifne -1003 -> 299
    //   1305: aload 35
    //   1307: invokeinterface 266 1 0
    //   1312: aload_1
    //   1313: astore 188
    //   1315: ldc_w 706
    //   1318: astore 189
    //   1320: aload 188
    //   1322: aload 189
    //   1324: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   1327: aload_1
    //   1328: invokevirtual 709	android/database/sqlite/SQLiteDatabase:setTransactionSuccessful	()V
    //   1331: aload_1
    //   1332: invokevirtual 712	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   1335: iload_2
    //   1336: istore 190
    //   1338: iconst_5
    //   1339: istore 191
    //   1341: iload 190
    //   1343: iload 191
    //   1345: if_icmpge +626 -> 1971
    //   1348: aload_1
    //   1349: invokevirtual 649	android/database/sqlite/SQLiteDatabase:beginTransaction	()V
    //   1352: aload_1
    //   1353: astore 192
    //   1355: ldc 9
    //   1357: astore 193
    //   1359: aload 192
    //   1361: aload 193
    //   1363: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   1366: aload_0
    //   1367: astore 194
    //   1369: aload_1
    //   1370: astore 195
    //   1372: ldc 54
    //   1374: astore 196
    //   1376: ldc 203
    //   1378: istore 197
    //   1380: aload 194
    //   1382: aload 195
    //   1384: aload 196
    //   1386: iload 197
    //   1388: invokespecial 610	com/taskos/db/TasksDatabaseHelper:addCategory	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;Z)V
    //   1391: aload_0
    //   1392: astore 198
    //   1394: aload_1
    //   1395: astore 199
    //   1397: ldc_w 612
    //   1400: astore 200
    //   1402: ldc_w 454
    //   1405: istore 201
    //   1407: aload 198
    //   1409: aload 199
    //   1411: aload 200
    //   1413: iload 201
    //   1415: invokespecial 610	com/taskos/db/TasksDatabaseHelper:addCategory	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;Z)V
    //   1418: aload_0
    //   1419: astore 202
    //   1421: aload_1
    //   1422: astore 203
    //   1424: ldc_w 614
    //   1427: astore 204
    //   1429: ldc_w 454
    //   1432: istore 205
    //   1434: aload 202
    //   1436: aload 203
    //   1438: aload 204
    //   1440: iload 205
    //   1442: invokespecial 610	com/taskos/db/TasksDatabaseHelper:addCategory	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;Z)V
    //   1445: aload_0
    //   1446: astore 206
    //   1448: aload_1
    //   1449: astore 207
    //   1451: ldc_w 714
    //   1454: astore 208
    //   1456: ldc_w 454
    //   1459: istore 209
    //   1461: aload 206
    //   1463: aload 207
    //   1465: aload 208
    //   1467: iload 209
    //   1469: invokespecial 610	com/taskos/db/TasksDatabaseHelper:addCategory	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;Z)V
    //   1472: aload_0
    //   1473: astore 210
    //   1475: aload_1
    //   1476: astore 211
    //   1478: ldc_w 716
    //   1481: astore 212
    //   1483: ldc_w 454
    //   1486: istore 213
    //   1488: aload 210
    //   1490: aload 211
    //   1492: aload 212
    //   1494: iload 213
    //   1496: invokespecial 610	com/taskos/db/TasksDatabaseHelper:addCategory	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;Z)V
    //   1499: aload_0
    //   1500: astore 214
    //   1502: aload_1
    //   1503: astore 215
    //   1505: ldc_w 718
    //   1508: astore 216
    //   1510: ldc_w 454
    //   1513: istore 217
    //   1515: aload 214
    //   1517: aload 215
    //   1519: aload 216
    //   1521: iload 217
    //   1523: invokespecial 610	com/taskos/db/TasksDatabaseHelper:addCategory	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;Z)V
    //   1526: aload_0
    //   1527: astore 218
    //   1529: aload_1
    //   1530: astore 219
    //   1532: ldc_w 720
    //   1535: astore 220
    //   1537: ldc_w 454
    //   1540: istore 221
    //   1542: aload 218
    //   1544: aload 219
    //   1546: aload 220
    //   1548: iload 221
    //   1550: invokespecial 610	com/taskos/db/TasksDatabaseHelper:addCategory	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;Z)V
    //   1553: aload_0
    //   1554: astore 222
    //   1556: aload_1
    //   1557: astore 223
    //   1559: ldc_w 722
    //   1562: astore 224
    //   1564: ldc_w 454
    //   1567: istore 225
    //   1569: aload 222
    //   1571: aload 223
    //   1573: aload 224
    //   1575: iload 225
    //   1577: invokespecial 610	com/taskos/db/TasksDatabaseHelper:addCategory	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;Z)V
    //   1580: bipush 8
    //   1582: anewarray 243	java/lang/String
    //   1585: astore 226
    //   1587: aload 226
    //   1589: iconst_0
    //   1590: ldc 54
    //   1592: aastore
    //   1593: aload 226
    //   1595: iconst_1
    //   1596: ldc_w 612
    //   1599: aastore
    //   1600: aload 226
    //   1602: iconst_2
    //   1603: ldc_w 614
    //   1606: aastore
    //   1607: aload 226
    //   1609: iconst_3
    //   1610: ldc_w 714
    //   1613: aastore
    //   1614: aload 226
    //   1616: iconst_4
    //   1617: ldc_w 716
    //   1620: aastore
    //   1621: aload 226
    //   1623: iconst_5
    //   1624: ldc_w 718
    //   1627: aastore
    //   1628: aload 226
    //   1630: bipush 6
    //   1632: ldc_w 720
    //   1635: aastore
    //   1636: aload 226
    //   1638: bipush 7
    //   1640: ldc_w 722
    //   1643: aastore
    //   1644: aload_1
    //   1645: ldc 45
    //   1647: aconst_null
    //   1648: aconst_null
    //   1649: aconst_null
    //   1650: aconst_null
    //   1651: aconst_null
    //   1652: aconst_null
    //   1653: invokevirtual 249	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   1656: astore 35
    //   1658: aload 35
    //   1660: invokeinterface 255 1 0
    //   1665: ifeq +291 -> 1956
    //   1668: aload 35
    //   1670: astore 227
    //   1672: ldc 81
    //   1674: astore 228
    //   1676: aload 227
    //   1678: aload 228
    //   1680: invokeinterface 259 2 0
    //   1685: istore 229
    //   1687: aload 35
    //   1689: astore 230
    //   1691: iload 229
    //   1693: istore 231
    //   1695: aload 230
    //   1697: iload 231
    //   1699: invokeinterface 669 2 0
    //   1704: istore 232
    //   1706: aload 35
    //   1708: astore 233
    //   1710: ldc 147
    //   1712: astore 234
    //   1714: aload 233
    //   1716: aload 234
    //   1718: invokeinterface 259 2 0
    //   1723: istore 235
    //   1725: aload 35
    //   1727: astore 236
    //   1729: iload 235
    //   1731: istore 237
    //   1733: aload 236
    //   1735: iload 237
    //   1737: invokeinterface 669 2 0
    //   1742: istore 238
    //   1744: iconst_1
    //   1745: anewarray 243	java/lang/String
    //   1748: astore 239
    //   1750: aload 239
    //   1752: iconst_0
    //   1753: ldc 81
    //   1755: aastore
    //   1756: new 273	java/lang/StringBuilder
    //   1759: dup
    //   1760: ldc_w 724
    //   1763: invokespecial 278	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   1766: astore 240
    //   1768: aload 226
    //   1770: iload 238
    //   1772: aaload
    //   1773: astore 241
    //   1775: aload 240
    //   1777: aload 241
    //   1779: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1782: ldc_w 579
    //   1785: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1788: invokevirtual 292	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1791: astore 242
    //   1793: aload_1
    //   1794: ldc 33
    //   1796: aload 239
    //   1798: aload 242
    //   1800: aconst_null
    //   1801: aconst_null
    //   1802: aconst_null
    //   1803: aconst_null
    //   1804: invokevirtual 249	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   1807: astore 243
    //   1809: aload 243
    //   1811: invokeinterface 255 1 0
    //   1816: ifeq +3789 -> 5605
    //   1819: aload 243
    //   1821: astore 244
    //   1823: ldc 81
    //   1825: astore 245
    //   1827: aload 244
    //   1829: aload 245
    //   1831: invokeinterface 259 2 0
    //   1836: istore 246
    //   1838: aload 243
    //   1840: astore 247
    //   1842: iload 246
    //   1844: istore 248
    //   1846: aload 247
    //   1848: iload 248
    //   1850: invokeinterface 669 2 0
    //   1855: istore 249
    //   1857: aload 243
    //   1859: invokeinterface 266 1 0
    //   1864: new 273	java/lang/StringBuilder
    //   1867: dup
    //   1868: ldc_w 726
    //   1871: invokespecial 278	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   1874: astore 250
    //   1876: iload 249
    //   1878: istore 251
    //   1880: aload 250
    //   1882: iload 251
    //   1884: invokevirtual 629	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1887: ldc_w 728
    //   1890: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1893: ldc 81
    //   1895: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1898: ldc_w 730
    //   1901: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1904: astore 252
    //   1906: iload 232
    //   1908: istore 253
    //   1910: aload 252
    //   1912: iload 253
    //   1914: invokevirtual 629	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1917: ldc_w 644
    //   1920: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1923: invokevirtual 292	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1926: astore 254
    //   1928: aload_1
    //   1929: astore 255
    //   1931: aload 254
    //   1933: wide
    //   1937: aload 255
    //   1939: wide
    //   1943: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   1946: aload 35
    //   1948: invokeinterface 704 1 0
    //   1953: ifne -285 -> 1668
    //   1956: aload 35
    //   1958: invokeinterface 266 1 0
    //   1963: aload_1
    //   1964: invokevirtual 709	android/database/sqlite/SQLiteDatabase:setTransactionSuccessful	()V
    //   1967: aload_1
    //   1968: invokevirtual 712	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   1971: iload_2
    //   1972: wide
    //   1976: bipush 6
    //   1978: wide
    //   1982: wide
    //   1986: wide
    //   1990: if_icmpge +83 -> 2073
    //   1993: aload_1
    //   1994: invokevirtual 649	android/database/sqlite/SQLiteDatabase:beginTransaction	()V
    //   1997: aload_1
    //   1998: wide
    //   2002: ldc 18
    //   2004: wide
    //   2008: wide
    //   2012: wide
    //   2016: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   2019: aload_1
    //   2020: wide
    //   2024: ldc_w 732
    //   2027: wide
    //   2031: wide
    //   2035: wide
    //   2039: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   2042: aload_1
    //   2043: wide
    //   2047: ldc_w 734
    //   2050: wide
    //   2054: wide
    //   2058: wide
    //   2062: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   2065: aload_1
    //   2066: invokevirtual 709	android/database/sqlite/SQLiteDatabase:setTransactionSuccessful	()V
    //   2069: aload_1
    //   2070: invokevirtual 712	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   2073: iload_2
    //   2074: wide
    //   2078: bipush 7
    //   2080: wide
    //   2084: wide
    //   2088: wide
    //   2092: if_icmpge +2073 -> 4165
    //   2095: aload_1
    //   2096: invokevirtual 649	android/database/sqlite/SQLiteDatabase:beginTransaction	()V
    //   2099: aload_1
    //   2100: wide
    //   2104: ldc_w 655
    //   2107: wide
    //   2111: wide
    //   2115: wide
    //   2119: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   2122: aload_1
    //   2123: wide
    //   2127: ldc 27
    //   2129: wide
    //   2133: wide
    //   2137: wide
    //   2141: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   2144: aload_1
    //   2145: ldc 48
    //   2147: aconst_null
    //   2148: aconst_null
    //   2149: aconst_null
    //   2150: aconst_null
    //   2151: aconst_null
    //   2152: aconst_null
    //   2153: invokevirtual 249	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   2156: astore 35
    //   2158: aload 35
    //   2160: invokeinterface 255 1 0
    //   2165: ifeq +1962 -> 4127
    //   2168: aload 35
    //   2170: wide
    //   2174: ldc 184
    //   2176: wide
    //   2180: wide
    //   2184: wide
    //   2188: invokeinterface 259 2 0
    //   2193: wide
    //   2197: aload 35
    //   2199: wide
    //   2203: wide
    //   2207: wide
    //   2211: wide
    //   2215: wide
    //   2219: invokeinterface 394 2 0
    //   2224: astore 41
    //   2226: aload 35
    //   2228: wide
    //   2232: ldc 159
    //   2234: wide
    //   2238: wide
    //   2242: wide
    //   2246: invokeinterface 259 2 0
    //   2251: wide
    //   2255: aload 35
    //   2257: wide
    //   2261: wide
    //   2265: wide
    //   2269: wide
    //   2273: wide
    //   2277: invokeinterface 394 2 0
    //   2282: astore 47
    //   2284: aload 35
    //   2286: wide
    //   2290: ldc 180
    //   2292: wide
    //   2296: wide
    //   2300: wide
    //   2304: invokeinterface 259 2 0
    //   2309: wide
    //   2313: aload 35
    //   2315: wide
    //   2319: wide
    //   2323: wide
    //   2327: wide
    //   2331: wide
    //   2335: invokeinterface 394 2 0
    //   2340: astore 59
    //   2342: aload 35
    //   2344: wide
    //   2348: ldc 174
    //   2350: wide
    //   2354: wide
    //   2358: wide
    //   2362: invokeinterface 259 2 0
    //   2367: wide
    //   2371: aload 35
    //   2373: wide
    //   2377: wide
    //   2381: wide
    //   2385: wide
    //   2389: wide
    //   2393: invokeinterface 394 2 0
    //   2398: astore 65
    //   2400: aload 35
    //   2402: wide
    //   2406: ldc 165
    //   2408: wide
    //   2412: wide
    //   2416: wide
    //   2420: invokeinterface 259 2 0
    //   2425: wide
    //   2429: aload 35
    //   2431: wide
    //   2435: wide
    //   2439: wide
    //   2443: wide
    //   2447: wide
    //   2451: invokeinterface 394 2 0
    //   2456: astore 71
    //   2458: aload 35
    //   2460: wide
    //   2464: ldc 153
    //   2466: wide
    //   2470: wide
    //   2474: wide
    //   2478: invokeinterface 259 2 0
    //   2483: wide
    //   2487: aload 35
    //   2489: wide
    //   2493: wide
    //   2497: wide
    //   2501: wide
    //   2505: wide
    //   2509: invokeinterface 394 2 0
    //   2514: astore 77
    //   2516: aload 35
    //   2518: wide
    //   2522: ldc 135
    //   2524: wide
    //   2528: wide
    //   2532: wide
    //   2536: invokeinterface 259 2 0
    //   2541: wide
    //   2545: aload 35
    //   2547: wide
    //   2551: wide
    //   2555: wide
    //   2559: wide
    //   2563: wide
    //   2567: invokeinterface 669 2 0
    //   2572: istore 83
    //   2574: aload 35
    //   2576: wide
    //   2580: ldc 162
    //   2582: wide
    //   2586: wide
    //   2590: wide
    //   2594: invokeinterface 259 2 0
    //   2599: wide
    //   2603: aload 35
    //   2605: wide
    //   2609: wide
    //   2613: wide
    //   2617: wide
    //   2621: wide
    //   2625: invokeinterface 669 2 0
    //   2630: istore 89
    //   2632: aload 35
    //   2634: wide
    //   2638: ldc 147
    //   2640: wide
    //   2644: wide
    //   2648: wide
    //   2652: invokeinterface 259 2 0
    //   2657: wide
    //   2661: aload 35
    //   2663: wide
    //   2667: wide
    //   2671: wide
    //   2675: wide
    //   2679: wide
    //   2683: invokeinterface 669 2 0
    //   2688: istore 95
    //   2690: aload 35
    //   2692: wide
    //   2696: ldc 108
    //   2698: wide
    //   2702: wide
    //   2706: wide
    //   2710: invokeinterface 259 2 0
    //   2715: wide
    //   2719: aload 35
    //   2721: wide
    //   2725: wide
    //   2729: wide
    //   2733: wide
    //   2737: wide
    //   2741: invokeinterface 394 2 0
    //   2746: astore 101
    //   2748: aload 35
    //   2750: wide
    //   2754: ldc 111
    //   2756: wide
    //   2760: wide
    //   2764: wide
    //   2768: invokeinterface 259 2 0
    //   2773: wide
    //   2777: aload 35
    //   2779: wide
    //   2783: wide
    //   2787: wide
    //   2791: wide
    //   2795: wide
    //   2799: invokeinterface 394 2 0
    //   2804: astore 107
    //   2806: aload 35
    //   2808: wide
    //   2812: ldc 144
    //   2814: wide
    //   2818: wide
    //   2822: wide
    //   2826: invokeinterface 259 2 0
    //   2831: wide
    //   2835: aload 35
    //   2837: wide
    //   2841: wide
    //   2845: wide
    //   2849: wide
    //   2853: wide
    //   2857: invokeinterface 394 2 0
    //   2862: astore 113
    //   2864: aload 113
    //   2866: astore 114
    //   2868: aload 35
    //   2870: wide
    //   2874: ldc 156
    //   2876: wide
    //   2880: wide
    //   2884: wide
    //   2888: invokeinterface 259 2 0
    //   2893: wide
    //   2897: aload 35
    //   2899: wide
    //   2903: wide
    //   2907: wide
    //   2911: wide
    //   2915: wide
    //   2919: invokeinterface 263 2 0
    //   2924: lstore 113
    //   2926: lload 113
    //   2928: wide
    //   2932: aload 35
    //   2934: wide
    //   2938: ldc 177
    //   2940: wide
    //   2944: wide
    //   2948: wide
    //   2952: invokeinterface 259 2 0
    //   2957: wide
    //   2961: aload 35
    //   2963: wide
    //   2967: wide
    //   2971: wide
    //   2975: wide
    //   2979: wide
    //   2983: invokeinterface 263 2 0
    //   2988: wide
    //   2992: new 657	java/text/SimpleDateFormat
    //   2995: dup
    //   2996: ldc_w 659
    //   2999: invokespecial 660	java/text/SimpleDateFormat:<init>	(Ljava/lang/String;)V
    //   3002: astore 32
    //   3004: aload 32
    //   3006: wide
    //   3010: aload 47
    //   3012: wide
    //   3016: wide
    //   3020: wide
    //   3024: invokevirtual 673	java/text/SimpleDateFormat:parse	(Ljava/lang/String;)Ljava/util/Date;
    //   3027: invokevirtual 347	java/util/Date:getTime	()J
    //   3030: lstore 113
    //   3032: lload 113
    //   3034: wide
    //   3038: iconst_0
    //   3039: wide
    //   3043: iconst_1
    //   3044: wide
    //   3048: iconst_0
    //   3049: wide
    //   3053: iconst_1
    //   3054: wide
    //   3058: sipush 480
    //   3061: wide
    //   3065: iload 83
    //   3067: ifne +2599 -> 5666
    //   3070: iconst_0
    //   3071: wide
    //   3075: ldc2_w 554
    //   3078: wide
    //   3082: iload 89
    //   3084: ifne +2761 -> 5845
    //   3087: iconst_2
    //   3088: istore 89
    //   3090: new 211	android/content/ContentValues
    //   3093: dup
    //   3094: invokespecial 213	android/content/ContentValues:<init>	()V
    //   3097: wide
    //   3101: wide
    //   3105: wide
    //   3109: ldc 184
    //   3111: wide
    //   3115: aload 41
    //   3117: wide
    //   3121: wide
    //   3125: wide
    //   3129: wide
    //   3133: invokevirtual 217	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   3136: wide
    //   3140: wide
    //   3144: ldc 180
    //   3146: wide
    //   3150: aload 59
    //   3152: wide
    //   3156: wide
    //   3160: wide
    //   3164: wide
    //   3168: invokevirtual 217	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   3171: iload 89
    //   3173: invokestatic 698	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   3176: wide
    //   3180: wide
    //   3184: wide
    //   3188: ldc 162
    //   3190: wide
    //   3194: wide
    //   3198: wide
    //   3202: wide
    //   3206: wide
    //   3210: wide
    //   3214: invokevirtual 701	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   3217: iload 95
    //   3219: invokestatic 698	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   3222: wide
    //   3226: wide
    //   3230: wide
    //   3234: ldc 147
    //   3236: wide
    //   3240: wide
    //   3244: wide
    //   3248: wide
    //   3252: wide
    //   3256: wide
    //   3260: invokevirtual 701	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   3263: wide
    //   3267: invokestatic 369	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   3270: wide
    //   3274: wide
    //   3278: wide
    //   3282: ldc 159
    //   3284: wide
    //   3288: wide
    //   3292: wide
    //   3296: wide
    //   3300: wide
    //   3304: wide
    //   3308: invokevirtual 372	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Long;)V
    //   3311: wide
    //   3315: wide
    //   3319: ldc 174
    //   3321: wide
    //   3325: aload 65
    //   3327: wide
    //   3331: wide
    //   3335: wide
    //   3339: wide
    //   3343: invokevirtual 217	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   3346: wide
    //   3350: wide
    //   3354: ldc 108
    //   3356: wide
    //   3360: aload 101
    //   3362: wide
    //   3366: wide
    //   3370: wide
    //   3374: wide
    //   3378: invokevirtual 217	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   3381: wide
    //   3385: wide
    //   3389: ldc 111
    //   3391: wide
    //   3395: aload 107
    //   3397: wide
    //   3401: wide
    //   3405: wide
    //   3409: wide
    //   3413: invokevirtual 217	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   3416: wide
    //   3420: wide
    //   3424: ldc 144
    //   3426: wide
    //   3430: aload 114
    //   3432: wide
    //   3436: wide
    //   3440: wide
    //   3444: wide
    //   3448: invokevirtual 217	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   3451: wide
    //   3455: wide
    //   3459: ldc 165
    //   3461: wide
    //   3465: aload 71
    //   3467: wide
    //   3471: wide
    //   3475: wide
    //   3479: wide
    //   3483: invokevirtual 217	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   3486: iconst_0
    //   3487: invokestatic 698	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   3490: wide
    //   3494: wide
    //   3498: wide
    //   3502: ldc 150
    //   3504: wide
    //   3508: wide
    //   3512: wide
    //   3516: wide
    //   3520: wide
    //   3524: wide
    //   3528: invokevirtual 701	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   3531: wide
    //   3535: wide
    //   3539: ldc 153
    //   3541: wide
    //   3545: aload 77
    //   3547: wide
    //   3551: wide
    //   3555: wide
    //   3559: wide
    //   3563: invokevirtual 217	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   3566: wide
    //   3570: invokestatic 369	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   3573: wide
    //   3577: wide
    //   3581: wide
    //   3585: ldc 156
    //   3587: wide
    //   3591: wide
    //   3595: wide
    //   3599: wide
    //   3603: wide
    //   3607: wide
    //   3611: invokevirtual 372	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Long;)V
    //   3614: wide
    //   3618: invokestatic 369	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   3621: wide
    //   3625: wide
    //   3629: wide
    //   3633: ldc 177
    //   3635: wide
    //   3639: wide
    //   3643: wide
    //   3647: wide
    //   3651: wide
    //   3655: wide
    //   3659: invokevirtual 372	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Long;)V
    //   3662: wide
    //   3666: invokestatic 698	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   3669: wide
    //   3673: wide
    //   3677: wide
    //   3681: ldc 138
    //   3683: wide
    //   3687: wide
    //   3691: wide
    //   3695: wide
    //   3699: wide
    //   3703: wide
    //   3707: invokevirtual 701	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   3710: wide
    //   3714: invokestatic 369	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   3717: wide
    //   3721: wide
    //   3725: wide
    //   3729: ldc 117
    //   3731: wide
    //   3735: wide
    //   3739: wide
    //   3743: wide
    //   3747: wide
    //   3751: wide
    //   3755: invokevirtual 372	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Long;)V
    //   3758: wide
    //   3762: invokestatic 698	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   3765: wide
    //   3769: wide
    //   3773: wide
    //   3777: ldc 126
    //   3779: wide
    //   3783: wide
    //   3787: wide
    //   3791: wide
    //   3795: wide
    //   3799: wide
    //   3803: invokevirtual 701	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   3806: wide
    //   3810: invokestatic 698	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   3813: wide
    //   3817: wide
    //   3821: wide
    //   3825: ldc 129
    //   3827: wide
    //   3831: wide
    //   3835: wide
    //   3839: wide
    //   3843: wide
    //   3847: wide
    //   3851: invokevirtual 701	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   3854: wide
    //   3858: invokestatic 698	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   3861: wide
    //   3865: wide
    //   3869: wide
    //   3873: ldc 120
    //   3875: wide
    //   3879: wide
    //   3883: wide
    //   3887: wide
    //   3891: wide
    //   3895: wide
    //   3899: invokevirtual 701	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   3902: wide
    //   3906: invokestatic 698	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   3909: wide
    //   3913: wide
    //   3917: wide
    //   3921: ldc 123
    //   3923: wide
    //   3927: wide
    //   3931: wide
    //   3935: wide
    //   3939: wide
    //   3943: wide
    //   3947: invokevirtual 701	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   3950: wide
    //   3954: invokestatic 698	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   3957: wide
    //   3961: wide
    //   3965: wide
    //   3969: ldc 132
    //   3971: wide
    //   3975: wide
    //   3979: wide
    //   3983: wide
    //   3987: wide
    //   3991: wide
    //   3995: invokevirtual 701	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   3998: wide
    //   4002: wide
    //   4006: ldc 114
    //   4008: wide
    //   4012: ldc_w 378
    //   4015: wide
    //   4019: wide
    //   4023: wide
    //   4027: wide
    //   4031: invokevirtual 217	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   4034: wide
    //   4038: wide
    //   4042: ldc 141
    //   4044: wide
    //   4048: ldc_w 378
    //   4051: wide
    //   4055: wide
    //   4059: wide
    //   4063: wide
    //   4067: invokevirtual 217	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   4070: aload_1
    //   4071: wide
    //   4075: ldc 45
    //   4077: wide
    //   4081: aconst_null
    //   4082: wide
    //   4086: wide
    //   4090: wide
    //   4094: wide
    //   4098: wide
    //   4102: wide
    //   4106: wide
    //   4110: invokevirtual 229	android/database/sqlite/SQLiteDatabase:insert	(Ljava/lang/String;Ljava/lang/String;Landroid/content/ContentValues;)J
    //   4113: wide
    //   4117: aload 35
    //   4119: invokeinterface 704 1 0
    //   4124: ifne -1956 -> 2168
    //   4127: aload 35
    //   4129: invokeinterface 266 1 0
    //   4134: aload_1
    //   4135: wide
    //   4139: ldc_w 706
    //   4142: wide
    //   4146: wide
    //   4150: wide
    //   4154: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   4157: aload_1
    //   4158: invokevirtual 709	android/database/sqlite/SQLiteDatabase:setTransactionSuccessful	()V
    //   4161: aload_1
    //   4162: invokevirtual 712	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   4165: iload_2
    //   4166: wide
    //   4170: bipush 8
    //   4172: wide
    //   4176: wide
    //   4180: wide
    //   4184: if_icmpge +950 -> 5134
    //   4187: aload_1
    //   4188: invokevirtual 649	android/database/sqlite/SQLiteDatabase:beginTransaction	()V
    //   4191: iconst_3
    //   4192: wide
    //   4196: wide
    //   4200: anewarray 243	java/lang/String
    //   4203: wide
    //   4207: wide
    //   4211: iconst_0
    //   4212: ldc 72
    //   4214: aastore
    //   4215: wide
    //   4219: iconst_1
    //   4220: ldc 57
    //   4222: aastore
    //   4223: wide
    //   4227: iconst_2
    //   4228: ldc 187
    //   4230: aastore
    //   4231: aload_1
    //   4232: wide
    //   4236: ldc 15
    //   4238: wide
    //   4242: wide
    //   4246: wide
    //   4250: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   4253: aload_1
    //   4254: wide
    //   4258: ldc 12
    //   4260: wide
    //   4264: wide
    //   4268: wide
    //   4272: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   4275: aload_1
    //   4276: wide
    //   4280: ldc_w 736
    //   4283: wide
    //   4287: wide
    //   4291: wide
    //   4295: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   4298: aload_0
    //   4299: wide
    //   4303: aload_1
    //   4304: wide
    //   4308: ldc 72
    //   4310: wide
    //   4314: ldc_w 602
    //   4317: wide
    //   4321: wide
    //   4325: wide
    //   4329: wide
    //   4333: wide
    //   4337: invokespecial 604	com/taskos/db/TasksDatabaseHelper:addImportance	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;Ljava/lang/String;)V
    //   4340: aload_0
    //   4341: wide
    //   4345: aload_1
    //   4346: wide
    //   4350: ldc 57
    //   4352: wide
    //   4356: ldc_w 606
    //   4359: wide
    //   4363: wide
    //   4367: wide
    //   4371: wide
    //   4375: wide
    //   4379: invokespecial 604	com/taskos/db/TasksDatabaseHelper:addImportance	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;Ljava/lang/String;)V
    //   4382: aload_0
    //   4383: wide
    //   4387: aload_1
    //   4388: wide
    //   4392: ldc 187
    //   4394: wide
    //   4398: ldc_w 608
    //   4401: wide
    //   4405: wide
    //   4409: wide
    //   4413: wide
    //   4417: wide
    //   4421: invokespecial 604	com/taskos/db/TasksDatabaseHelper:addImportance	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;Ljava/lang/String;)V
    //   4424: aload_0
    //   4425: wide
    //   4429: aload_1
    //   4430: wide
    //   4434: ldc 69
    //   4436: wide
    //   4440: wide
    //   4444: wide
    //   4448: wide
    //   4452: invokespecial 616	com/taskos/db/TasksDatabaseHelper:addDueGroup	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;)V
    //   4455: aload_0
    //   4456: wide
    //   4460: aload_1
    //   4461: wide
    //   4465: ldc 66
    //   4467: wide
    //   4471: wide
    //   4475: wide
    //   4479: wide
    //   4483: invokespecial 616	com/taskos/db/TasksDatabaseHelper:addDueGroup	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;)V
    //   4486: aload_0
    //   4487: wide
    //   4491: aload_1
    //   4492: wide
    //   4496: ldc 63
    //   4498: wide
    //   4502: wide
    //   4506: wide
    //   4510: wide
    //   4514: invokespecial 616	com/taskos/db/TasksDatabaseHelper:addDueGroup	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;)V
    //   4517: aload_1
    //   4518: ldc 45
    //   4520: aconst_null
    //   4521: aconst_null
    //   4522: aconst_null
    //   4523: aconst_null
    //   4524: aconst_null
    //   4525: aconst_null
    //   4526: invokevirtual 249	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   4529: astore 35
    //   4531: aload 35
    //   4533: invokeinterface 255 1 0
    //   4538: ifeq +581 -> 5119
    //   4541: aload 35
    //   4543: wide
    //   4547: ldc 81
    //   4549: wide
    //   4553: wide
    //   4557: wide
    //   4561: invokeinterface 259 2 0
    //   4566: wide
    //   4570: aload 35
    //   4572: wide
    //   4576: wide
    //   4580: wide
    //   4584: wide
    //   4588: wide
    //   4592: invokeinterface 669 2 0
    //   4597: istore 232
    //   4599: aload 35
    //   4601: wide
    //   4605: ldc 162
    //   4607: wide
    //   4611: wide
    //   4615: wide
    //   4619: invokeinterface 259 2 0
    //   4624: wide
    //   4628: aload 35
    //   4630: wide
    //   4634: wide
    //   4638: wide
    //   4642: wide
    //   4646: wide
    //   4650: invokeinterface 669 2 0
    //   4655: wide
    //   4659: iconst_1
    //   4660: anewarray 243	java/lang/String
    //   4663: wide
    //   4667: wide
    //   4671: iconst_0
    //   4672: ldc 81
    //   4674: aastore
    //   4675: new 273	java/lang/StringBuilder
    //   4678: dup
    //   4679: ldc_w 738
    //   4682: invokespecial 278	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   4685: wide
    //   4689: wide
    //   4693: wide
    //   4697: aaload
    //   4698: wide
    //   4702: wide
    //   4706: wide
    //   4710: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4713: ldc_w 579
    //   4716: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4719: invokevirtual 292	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   4722: wide
    //   4726: aload_1
    //   4727: ldc 39
    //   4729: wide
    //   4733: wide
    //   4737: aconst_null
    //   4738: aconst_null
    //   4739: aconst_null
    //   4740: aconst_null
    //   4741: invokevirtual 249	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   4744: wide
    //   4748: wide
    //   4752: invokeinterface 255 1 0
    //   4757: ifeq +1129 -> 5886
    //   4760: wide
    //   4764: wide
    //   4768: ldc 81
    //   4770: wide
    //   4774: wide
    //   4778: wide
    //   4782: invokeinterface 259 2 0
    //   4787: wide
    //   4791: wide
    //   4795: wide
    //   4799: wide
    //   4803: wide
    //   4807: wide
    //   4811: wide
    //   4815: invokeinterface 669 2 0
    //   4820: wide
    //   4824: wide
    //   4828: invokeinterface 266 1 0
    //   4833: new 273	java/lang/StringBuilder
    //   4836: dup
    //   4837: ldc_w 740
    //   4840: invokespecial 278	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   4843: wide
    //   4847: wide
    //   4851: wide
    //   4855: wide
    //   4859: wide
    //   4863: invokevirtual 629	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   4866: ldc_w 728
    //   4869: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4872: ldc 81
    //   4874: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4877: ldc_w 730
    //   4880: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4883: wide
    //   4887: iload 232
    //   4889: wide
    //   4893: wide
    //   4897: wide
    //   4901: invokevirtual 629	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   4904: ldc_w 644
    //   4907: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4910: invokevirtual 292	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   4913: wide
    //   4917: aload_1
    //   4918: wide
    //   4922: wide
    //   4926: wide
    //   4930: wide
    //   4934: wide
    //   4938: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   4941: aload 35
    //   4943: wide
    //   4947: ldc 165
    //   4949: wide
    //   4953: wide
    //   4957: wide
    //   4961: invokeinterface 259 2 0
    //   4966: wide
    //   4970: aload 35
    //   4972: wide
    //   4976: wide
    //   4980: wide
    //   4984: wide
    //   4988: wide
    //   4992: invokeinterface 394 2 0
    //   4997: wide
    //   5001: new 273	java/lang/StringBuilder
    //   5004: dup
    //   5005: ldc_w 742
    //   5008: invokespecial 278	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   5011: wide
    //   5015: wide
    //   5019: wide
    //   5023: wide
    //   5027: wide
    //   5031: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5034: ldc_w 744
    //   5037: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5040: ldc 81
    //   5042: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5045: ldc_w 730
    //   5048: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5051: wide
    //   5055: iload 232
    //   5057: wide
    //   5061: wide
    //   5065: wide
    //   5069: invokevirtual 629	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   5072: ldc_w 644
    //   5075: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5078: invokevirtual 292	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   5081: wide
    //   5085: aload_1
    //   5086: wide
    //   5090: wide
    //   5094: wide
    //   5098: wide
    //   5102: wide
    //   5106: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   5109: aload 35
    //   5111: invokeinterface 704 1 0
    //   5116: ifne -575 -> 4541
    //   5119: aload 35
    //   5121: invokeinterface 266 1 0
    //   5126: aload_1
    //   5127: invokevirtual 709	android/database/sqlite/SQLiteDatabase:setTransactionSuccessful	()V
    //   5130: aload_1
    //   5131: invokevirtual 712	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   5134: iload_2
    //   5135: wide
    //   5139: bipush 9
    //   5141: wide
    //   5145: wide
    //   5149: wide
    //   5153: if_icmpge +426 -> 5579
    //   5156: aload_1
    //   5157: invokevirtual 649	android/database/sqlite/SQLiteDatabase:beginTransaction	()V
    //   5160: aload_1
    //   5161: wide
    //   5165: ldc_w 746
    //   5168: wide
    //   5172: wide
    //   5176: wide
    //   5180: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   5183: iconst_3
    //   5184: anewarray 243	java/lang/String
    //   5187: wide
    //   5191: wide
    //   5195: iconst_0
    //   5196: ldc 81
    //   5198: aastore
    //   5199: wide
    //   5203: iconst_1
    //   5204: ldc 117
    //   5206: aastore
    //   5207: wide
    //   5211: iconst_2
    //   5212: ldc 159
    //   5214: aastore
    //   5215: aload_1
    //   5216: ldc 45
    //   5218: wide
    //   5222: aconst_null
    //   5223: aconst_null
    //   5224: aconst_null
    //   5225: aconst_null
    //   5226: aconst_null
    //   5227: invokevirtual 249	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   5230: astore 35
    //   5232: aload 35
    //   5234: invokeinterface 255 1 0
    //   5239: ifeq +332 -> 5571
    //   5242: aload 35
    //   5244: wide
    //   5248: ldc 81
    //   5250: wide
    //   5254: wide
    //   5258: wide
    //   5262: invokeinterface 259 2 0
    //   5267: wide
    //   5271: aload 35
    //   5273: wide
    //   5277: wide
    //   5281: wide
    //   5285: wide
    //   5289: wide
    //   5293: invokeinterface 263 2 0
    //   5298: wide
    //   5302: aload 35
    //   5304: wide
    //   5308: ldc 117
    //   5310: wide
    //   5314: wide
    //   5318: wide
    //   5322: invokeinterface 259 2 0
    //   5327: wide
    //   5331: aload 35
    //   5333: wide
    //   5337: wide
    //   5341: wide
    //   5345: wide
    //   5349: wide
    //   5353: invokeinterface 669 2 0
    //   5358: wide
    //   5362: aload 35
    //   5364: wide
    //   5368: ldc 159
    //   5370: wide
    //   5374: wide
    //   5378: wide
    //   5382: invokeinterface 259 2 0
    //   5387: wide
    //   5391: aload 35
    //   5393: wide
    //   5397: wide
    //   5401: wide
    //   5405: wide
    //   5409: wide
    //   5413: invokeinterface 263 2 0
    //   5418: wide
    //   5422: wide
    //   5426: bipush 60
    //   5428: imul
    //   5429: sipush 1000
    //   5432: imul
    //   5433: i2l
    //   5434: wide
    //   5438: wide
    //   5442: wide
    //   5446: lsub
    //   5447: wide
    //   5451: new 273	java/lang/StringBuilder
    //   5454: dup
    //   5455: ldc_w 748
    //   5458: invokespecial 278	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   5461: wide
    //   5465: wide
    //   5469: wide
    //   5473: wide
    //   5477: wide
    //   5481: invokevirtual 282	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   5484: ldc_w 728
    //   5487: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5490: ldc 81
    //   5492: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5495: ldc_w 730
    //   5498: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5501: wide
    //   5505: wide
    //   5509: wide
    //   5513: wide
    //   5517: wide
    //   5521: invokevirtual 282	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   5524: ldc_w 644
    //   5527: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5530: invokevirtual 292	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   5533: wide
    //   5537: aload_1
    //   5538: wide
    //   5542: wide
    //   5546: wide
    //   5550: wide
    //   5554: wide
    //   5558: invokevirtual 600	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   5561: aload 35
    //   5563: invokeinterface 704 1 0
    //   5568: ifne -326 -> 5242
    //   5571: aload_1
    //   5572: invokevirtual 709	android/database/sqlite/SQLiteDatabase:setTransactionSuccessful	()V
    //   5575: aload_1
    //   5576: invokevirtual 712	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   5579: return
    //   5580: wide
    //   5584: ldc_w 750
    //   5587: astore 141
    //   5589: goto -4603 -> 986
    //   5592: wide
    //   5596: aload_1
    //   5597: invokevirtual 712	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   5600: wide
    //   5604: athrow
    //   5605: iload 238
    //   5607: istore 249
    //   5609: goto -3752 -> 1857
    //   5612: wide
    //   5616: aload_1
    //   5617: invokevirtual 712	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   5620: wide
    //   5624: athrow
    //   5625: wide
    //   5629: aload_1
    //   5630: invokevirtual 712	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   5633: wide
    //   5637: athrow
    //   5638: wide
    //   5642: ldc2_w 751
    //   5645: wide
    //   5649: goto -2717 -> 2932
    //   5652: wide
    //   5656: ldc2_w 554
    //   5659: wide
    //   5663: goto -2625 -> 3038
    //   5666: iconst_1
    //   5667: wide
    //   5671: bipush 15
    //   5673: wide
    //   5677: wide
    //   5681: newarray int
    //   5683: wide
    //   5687: wide
    //   5691: iconst_1
    //   5692: iconst_5
    //   5693: iastore
    //   5694: wide
    //   5698: iconst_2
    //   5699: bipush 10
    //   5701: iastore
    //   5702: wide
    //   5706: iconst_3
    //   5707: bipush 15
    //   5709: iastore
    //   5710: wide
    //   5714: iconst_4
    //   5715: bipush 20
    //   5717: iastore
    //   5718: wide
    //   5722: iconst_5
    //   5723: bipush 25
    //   5725: iastore
    //   5726: wide
    //   5730: bipush 6
    //   5732: bipush 30
    //   5734: iastore
    //   5735: wide
    //   5739: bipush 7
    //   5741: bipush 45
    //   5743: iastore
    //   5744: wide
    //   5748: bipush 8
    //   5750: bipush 60
    //   5752: iastore
    //   5753: wide
    //   5757: bipush 9
    //   5759: bipush 120
    //   5761: iastore
    //   5762: wide
    //   5766: bipush 10
    //   5768: sipush 180
    //   5771: iastore
    //   5772: wide
    //   5776: bipush 11
    //   5778: sipush 720
    //   5781: iastore
    //   5782: wide
    //   5786: bipush 12
    //   5788: sipush 1440
    //   5791: iastore
    //   5792: wide
    //   5796: bipush 13
    //   5798: sipush 2880
    //   5801: iastore
    //   5802: wide
    //   5806: bipush 14
    //   5808: sipush 10080
    //   5811: iastore
    //   5812: iload 83
    //   5814: iconst_1
    //   5815: isub
    //   5816: wide
    //   5820: wide
    //   5824: wide
    //   5828: iaload
    //   5829: wide
    //   5833: wide
    //   5837: i2l
    //   5838: wide
    //   5842: goto -2760 -> 3082
    //   5845: iload 89
    //   5847: wide
    //   5851: iconst_2
    //   5852: wide
    //   5856: wide
    //   5860: wide
    //   5864: if_icmpne -2774 -> 3090
    //   5867: iconst_0
    //   5868: istore 89
    //   5870: goto -2780 -> 3090
    //   5873: wide
    //   5877: aload_1
    //   5878: invokevirtual 712	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   5881: wide
    //   5885: athrow
    //   5886: iconst_2
    //   5887: wide
    //   5891: goto -1067 -> 4824
    //   5894: wide
    //   5898: aload_1
    //   5899: invokevirtual 712	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   5902: wide
    //   5906: athrow
    //   5907: wide
    //   5911: aload_1
    //   5912: invokevirtual 712	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   5915: wide
    //   5919: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   798	982	5580	java/text/ParseException
    //   180	794	5592	finally
    //   798	982	5592	finally
    //   986	1331	5592	finally
    //   5584	5589	5592	finally
    //   1352	1967	5612	finally
    //   1997	2069	5625	finally
    //   2868	2926	5638	java/lang/IllegalArgumentException
    //   3004	3032	5652	java/lang/Exception
    //   2099	2864	5873	finally
    //   2868	2926	5873	finally
    //   2932	3004	5873	finally
    //   3004	3032	5873	finally
    //   3090	4161	5873	finally
    //   5677	5833	5873	finally
    //   4196	5130	5894	finally
    //   5160	5575	5907	finally
  }

  public void quickEditCollapseAll()
  {
    ContentValues localContentValues = new ContentValues();
    Boolean localBoolean = Boolean.valueOf(0);
    localContentValues.put("is_quick_edit_visible", localBoolean);
    int i = TaskosApp.getDB().update("taskos_tasks", localContentValues, null, null);
  }

  public void quickEditCollapseAllButOne(long paramLong)
  {
    ContentValues localContentValues = new ContentValues();
    Boolean localBoolean = Boolean.valueOf(0);
    localContentValues.put("is_quick_edit_visible", localBoolean);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str = "_id!=" + paramLong;
    int i = localSQLiteDatabase.update("taskos_tasks", localContentValues, str, null);
  }

  public void refreshTasks()
  {
    if (!this.disableRefreshing)
    {
      Context localContext = this.mCtx;
      String str = Main.ACTION_RECEIVER_REFRESH_TASKS;
      Intent localIntent = new Intent(str);
      localContext.sendBroadcast(localIntent);
    }
  }

  public long setSyncServiceValueForTask(long paramLong, String paramString1, String paramString2, String paramString3)
  {
    ContentValues localContentValues = new ContentValues();
    Long localLong = Long.valueOf(paramLong);
    localContentValues.put("taskId", localLong);
    localContentValues.put("service", paramString1);
    localContentValues.put("key", paramString2);
    localContentValues.put("value", paramString3);
    return TaskosApp.getDB().insert("taskos_sync_extras", null, localContentValues);
  }

  public boolean taskIdExistsNotDeleted(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str1 = "(_id=" + paramLong + ") AND (" + "is_deleted" + "='false')";
    String[] arrayOfString = null;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    String str5 = null;
    Cursor localCursor = localSQLiteDatabase.query("taskos_tasks", null, str1, arrayOfString, str2, str3, str4, str5);
    boolean bool = localCursor.moveToFirst();
    localCursor.close();
    return bool;
  }

  public boolean toggleIsQuickEditVisible(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase1 = TaskosApp.getDB();
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "is_quick_edit_visible";
    String str1 = "_id=" + paramLong;
    Cursor localCursor = localSQLiteDatabase1.query("taskos_tasks", arrayOfString, str1, null, null, null, null, null);
    int i = 0;
    boolean bool;
    if (localCursor.moveToFirst())
    {
      int j = localCursor.getColumnIndexOrThrow("is_quick_edit_visible");
      bool = Boolean.valueOf(localCursor.getString(j)).booleanValue();
    }
    localCursor.close();
    ContentValues localContentValues = new ContentValues();
    String str2 = "is_quick_edit_visible";
    if (bool)
    {
      int m = 0;
      String str3 = Boolean.toString(m);
      localContentValues.put(str2, str3);
      SQLiteDatabase localSQLiteDatabase2 = TaskosApp.getDB();
      String str4 = "_id=" + paramLong;
      if (localSQLiteDatabase2.update("taskos_tasks", localContentValues, str4, null) <= 0)
        break label192;
    }
    label192: for (int k = 1; ; k = 0)
    {
      return k;
      int n = 1;
      break;
    }
  }

  public boolean unTrashTask(long paramLong)
  {
    ContentValues localContentValues = new ContentValues();
    String str1 = Boolean.toString(0);
    localContentValues.put("is_deleted", str1);
    String str2 = Boolean.toString(0);
    localContentValues.put("is_checked", str2);
    Long localLong = Long.valueOf(System.currentTimeMillis());
    localContentValues.put("modification_time", localLong);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str3 = "_id=" + paramLong;
    if (localSQLiteDatabase.update("taskos_tasks", localContentValues, str3, null) > 0);
    for (int i = 1; ; i = 0)
      return i;
  }

  public boolean updateAction(long paramLong, ActionType paramActionType, String paramString1, String paramString2)
  {
    ContentValues localContentValues = new ContentValues();
    String str1 = paramActionType.name();
    localContentValues.put("action", str1);
    localContentValues.put("action_param", paramString1);
    localContentValues.put("button_resource", paramString2);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str2 = "_id=" + paramLong;
    if (localSQLiteDatabase.update("taskos_tasks", localContentValues, str2, null) > 0);
    for (int i = 1; ; i = 0)
      return i;
  }

  public boolean updateAlertIds(long paramLong, String paramString)
  {
    TaskosLog.d("updateAlertIds", paramString);
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("alert_ids", paramString);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str = "_id=" + paramLong;
    if (localSQLiteDatabase.update("taskos_tasks", localContentValues, str, null) > 0);
    for (int i = 1; ; i = 0)
      return i;
  }

  public boolean updateAlertInfo(long paramLong1, int paramInt1, long paramLong2, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    ContentValues localContentValues = new ContentValues();
    Integer localInteger1 = Integer.valueOf(paramInt1);
    localContentValues.put("alert_status", localInteger1);
    Long localLong = Long.valueOf(paramLong2);
    localContentValues.put("alert_one_time", localLong);
    Integer localInteger2 = Integer.valueOf(paramInt2);
    localContentValues.put("alert_repeating_interval_amount", localInteger2);
    Integer localInteger3 = Integer.valueOf(paramInt3);
    localContentValues.put("alert_repeating_interval", localInteger3);
    Integer localInteger4 = Integer.valueOf(paramInt4);
    localContentValues.put("alert_repeating_time", localInteger4);
    Integer localInteger5 = Integer.valueOf(paramInt5);
    localContentValues.put("alert_repeating_days_of_week", localInteger5);
    Integer localInteger6 = Integer.valueOf(paramInt6);
    localContentValues.put("alert_repeating_day_of_month", localInteger6);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str = "_id=" + paramLong1;
    if (localSQLiteDatabase.update("taskos_tasks", localContentValues, str, null) > 0);
    for (int i = 1; ; i = 0)
      return i;
  }

  public void updateAlertStatusOff(int paramInt)
  {
    long l1 = paramInt;
    TasksDatabaseHelper localTasksDatabaseHelper = this;
    int i = 0;
    int j = 0;
    int k = 1;
    boolean bool1 = localTasksDatabaseHelper.updateAlertInfo(l1, 0, 0L, 1, i, 480, j, k);
    long l2 = paramInt;
    boolean bool2 = updateAlertIds(l2, "");
    long l3 = paramInt;
    boolean bool3 = updateAlertTimes(l3, "");
  }

  public boolean updateAlertTimes(long paramLong, String paramString)
  {
    TaskosLog.d("updateAlertTimes", paramString);
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("alert_times", paramString);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str = "_id=" + paramLong;
    if (localSQLiteDatabase.update("taskos_tasks", localContentValues, str, null) > 0);
    for (int i = 1; ; i = 0)
      return i;
  }

  public boolean updateCategory(long paramLong1, long paramLong2)
  {
    ContentValues localContentValues = new ContentValues();
    Long localLong = Long.valueOf(paramLong2);
    localContentValues.put("category", localLong);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str = "_id=" + paramLong1;
    if (localSQLiteDatabase.update("taskos_tasks", localContentValues, str, null) > 0);
    for (int i = 1; ; i = 0)
      return i;
  }

  public boolean updateCreationTime(long paramLong1, long paramLong2)
  {
    ContentValues localContentValues = new ContentValues();
    Long localLong = Long.valueOf(paramLong2);
    localContentValues.put("creation_time", localLong);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str = "_id=" + paramLong1;
    if (localSQLiteDatabase.update("taskos_tasks", localContentValues, str, null) > 0);
    for (int i = 1; ; i = 0)
      return i;
  }

  public boolean updateDueDate(long paramLong, Date paramDate)
  {
    ContentValues localContentValues = new ContentValues();
    Long localLong = Long.valueOf(paramDate.getTime());
    localContentValues.put("due_date", localLong);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str = "_id=" + paramLong;
    if (localSQLiteDatabase.update("taskos_tasks", localContentValues, str, null) > 0);
    for (int i = 1; ; i = 0)
      return i;
  }

  public boolean updateImportance(long paramLong1, long paramLong2)
  {
    ContentValues localContentValues = new ContentValues();
    Long localLong = Long.valueOf(paramLong2);
    localContentValues.put("importance", localLong);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str = "_id=" + paramLong1;
    if (localSQLiteDatabase.update("taskos_tasks", localContentValues, str, null) > 0);
    for (int i = 1; ; i = 0)
      return i;
  }

  public boolean updateIsChecked(long paramLong, boolean paramBoolean)
  {
    ContentValues localContentValues = new ContentValues();
    long l = System.currentTimeMillis();
    String str1 = Boolean.toString(paramBoolean);
    localContentValues.put("is_checked", str1);
    Long localLong1 = Long.valueOf(l);
    localContentValues.put("modification_time", localLong1);
    if (paramBoolean)
    {
      Long localLong2 = Long.valueOf(l);
      localContentValues.put("checked_time", localLong2);
      Boolean localBoolean = Boolean.valueOf(0);
      localContentValues.put("is_quick_edit_visible", localBoolean);
    }
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str2 = "_id=" + paramLong;
    if (localSQLiteDatabase.update("taskos_tasks", localContentValues, str2, null) > 0);
    for (int i = 1; ; i = 0)
    {
      refreshTasks();
      return i;
    }
  }

  public boolean updateIsDeleted(long paramLong, boolean paramBoolean)
  {
    ContentValues localContentValues = new ContentValues();
    String str1 = Boolean.toString(paramBoolean);
    localContentValues.put("is_deleted", str1);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str2 = "_id=" + paramLong;
    if (localSQLiteDatabase.update("taskos_tasks", localContentValues, str2, null) > 0);
    for (int i = 1; ; i = 0)
      return i;
  }

  public boolean updateIsQuickEditVisible(long paramLong, boolean paramBoolean)
  {
    ContentValues localContentValues = new ContentValues();
    String str1 = Boolean.toString(paramBoolean);
    localContentValues.put("is_quick_edit_visible", str1);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str2 = "_id=" + paramLong;
    if (localSQLiteDatabase.update("taskos_tasks", localContentValues, str2, null) > 0);
    for (int i = 1; ; i = 0)
      return i;
  }

  public boolean updateIsSynced(long paramLong, boolean paramBoolean)
  {
    ContentValues localContentValues = new ContentValues();
    String str1 = Boolean.toString(paramBoolean);
    localContentValues.put("is_synced", str1);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str2 = "_id=" + paramLong;
    if (localSQLiteDatabase.update("taskos_tasks", localContentValues, str2, null) > 0);
    for (int i = 1; ; i = 0)
      return i;
  }

  public boolean updateModificationTime(long paramLong1, long paramLong2)
  {
    ContentValues localContentValues = new ContentValues();
    Long localLong = Long.valueOf(paramLong2);
    localContentValues.put("modification_time", localLong);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str = "_id=" + paramLong1;
    if (localSQLiteDatabase.update("taskos_tasks", localContentValues, str, null) > 0);
    for (int i = 1; ; i = 0)
      return i;
  }

  public boolean updateNotes(long paramLong, String paramString)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("notes", paramString);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str = "_id=" + paramLong;
    if (localSQLiteDatabase.update("taskos_tasks", localContentValues, str, null) > 0);
    for (int i = 1; ; i = 0)
      return i;
  }

  public boolean updateTask(long paramLong1, String paramString1, String paramString2, long paramLong2, int paramInt, long paramLong3, long paramLong4)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("title", paramString1);
    localContentValues.put("notes", paramString2);
    Long localLong1 = Long.valueOf(paramLong2);
    localContentValues.put("due_date", localLong1);
    Integer localInteger = Integer.valueOf(paramInt);
    localContentValues.put("alert_status", localInteger);
    Long localLong2 = Long.valueOf(paramLong3);
    localContentValues.put("importance", localLong2);
    Long localLong3 = Long.valueOf(paramLong4);
    localContentValues.put("category", localLong3);
    Long localLong4 = Long.valueOf(System.currentTimeMillis());
    localContentValues.put("modification_time", localLong4);
    SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
    String str = "_id=" + paramLong1;
    if (localSQLiteDatabase.update("taskos_tasks", localContentValues, str, null) > 0);
    for (int i = 1; ; i = 0)
    {
      refreshTasks();
      return i;
    }
  }

  public boolean updateTaskFields(long paramLong, Map<String, Object> paramMap)
    throws Exception
  {
    ContentValues localContentValues = new ContentValues();
    Iterator localIterator = paramMap.entrySet().iterator();
    if (!localIterator.hasNext())
    {
      SQLiteDatabase localSQLiteDatabase = TaskosApp.getDB();
      String str1 = "_id=" + paramLong;
      if (localSQLiteDatabase.update("taskos_tasks", localContentValues, str1, null) <= 0)
        break label272;
    }
    label272: for (int i = 1; ; i = 0)
    {
      refreshTasks();
      return i;
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Object localObject = localEntry.getValue();
      if ((localObject instanceof String))
      {
        String str2 = (String)localEntry.getKey();
        String str3 = (String)localObject;
        localContentValues.put(str2, str3);
        break;
      }
      if ((localObject instanceof Integer))
      {
        String str4 = (String)localEntry.getKey();
        Integer localInteger = (Integer)localObject;
        localContentValues.put(str4, localInteger);
        break;
      }
      if ((localObject instanceof Boolean))
      {
        String str5 = (String)localEntry.getKey();
        String str6 = Boolean.toString(((Boolean)localObject).booleanValue());
        localContentValues.put(str5, str6);
        break;
      }
      if ((localObject instanceof Long))
      {
        String str7 = (String)localEntry.getKey();
        Long localLong = (Long)localObject;
        localContentValues.put(str7, localLong);
        break;
      }
      throw new Exception();
    }
  }
}

/* Location:           C:\Users\Mine\Desktop\taskosNEW\classes.jar
 * Qualified Name:     com.taskos.db.TasksDatabaseHelper
 * JD-Core Version:    0.6.0
 */