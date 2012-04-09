package com.healsjnr.brightspark.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private String m_databaseName;
	
	private final static int DATABASE_VERSION = 2; 
	
	public DatabaseHelper(Context context, String databaseName)
	{
		super(context, databaseName, null, DATABASE_VERSION);
		m_databaseName = databaseName;
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		FavouriteLocationTable.onCreate(database);
		CountdownJourneyTable.onCreate(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		FavouriteLocationTable.onUpgrade(database, oldVersion, newVersion);
		CountdownJourneyTable.onUpgrade(database, oldVersion, newVersion);
	}
	
	public String getDatabaseName()
	{
		return m_databaseName;
	}

}
