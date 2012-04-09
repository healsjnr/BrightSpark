package com.healsjnr.brightspark.database;

import com.healsjnr.brightspark.BrightSparkActivity;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CountdownJourneyTable {
	
	public static final String TABLE_NAME = "countdown_table";
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_ORIGIN_FAV_ID = "origin_fav_id";
	public static final String KEY_DEST_FAV_ID = "dest_fav_id";
	public static final String KEY_DAYFLAGS = "dayflags";
	public static final String KEY_START_TIME = "start_time";
	public static final String KEY_END_TIME = "end_time";
	
	
	public static final String DATABASE_CREATE = "create table "
			+ TABLE_NAME
			+ " ("
			+ KEY_ID + " integer primary key autoincrement,"
			+ KEY_NAME + " text not null,"
			+ KEY_ORIGIN_FAV_ID + " integer,"
			+ KEY_DEST_FAV_ID + " integer not null,"
			+ KEY_DAYFLAGS + " text not null,"
			+ KEY_START_TIME + " text not null,"
			+ KEY_END_TIME + " text not null"
			+ ");";
	
	public static void onCreate(SQLiteDatabase database)
	{
		database.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
	{
		Log.i(BrightSparkActivity.LOG_TAG, "Upgrading database table " + TABLE_NAME + " from version " + oldVersion + " to " + newVersion + "."
				+ "Old data will be destroyed.");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(database);
	}

	
			

}
