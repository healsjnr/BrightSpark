package com.jeppesen.brightspark.database;

import com.jeppesen.brightspark.BrightSparkActivity;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FavouriteLocationTable {
	
	public static final String TABLE_NAME = "favourite_location";
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_LAT = "latitude";
	public static final String KEY_LONG = "longitude";
	public static final String KEY_ADDRESS = "address";
	
	public static final String DATABASE_CREATE = "create table " 
		+ TABLE_NAME
		+ "("
		+ KEY_ID + " integer primary key autoincrement, "
		+ KEY_NAME + " text not null, "
		+ KEY_LAT + " text not null, "
		+ KEY_LONG + " text not null, "
		+ KEY_ADDRESS + " text not null"
		+ ");";
	
	public static void onCreate(SQLiteDatabase database)
	{
		database.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
	{
		Log.i(BrightSparkActivity.LOG_TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + "."
				+ "Old data will be destroyed.");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(database);
	}
}
