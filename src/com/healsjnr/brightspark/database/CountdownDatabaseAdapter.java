package com.healsjnr.brightspark.database;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.healsjnr.brightspark.ApplicationState;
import com.healsjnr.brightspark.BrightSparkActivity;
import com.healsjnr.brightspark.lib.CountdownJourney;
import com.healsjnr.brightspark.lib.DateTimeMask;
import com.healsjnr.brightspark.lib.FavouriteLocation;
import com.healsjnr.brightspark.lib.GeoPosition;
import com.healsjnr.brightspark.lib.SimpleAddress;
import com.healsjnr.brightspark.lib.TimeUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CountdownDatabaseAdapter {
	
	private Context m_context;
	private SQLiteDatabase m_database;
	private DatabaseHelper m_databaseHelper;
	private String m_databaseName;
	
	private static int NULL_LOCATION = -1;
	
	private final String[] ALL_COLUMNS = new String[] {
			CountdownJourneyTable.KEY_ID,
			CountdownJourneyTable.KEY_NAME,
			CountdownJourneyTable.KEY_ORIGIN_FAV_ID,
			CountdownJourneyTable.KEY_DEST_FAV_ID,
			CountdownJourneyTable.KEY_DAYFLAGS,
			CountdownJourneyTable.KEY_START_TIME,
			CountdownJourneyTable.KEY_END_TIME
	};
	
	public CountdownDatabaseAdapter(Context context, String databaseName)
	{
		m_context = context;
		m_databaseName = databaseName;
	}
	
	public CountdownDatabaseAdapter open() throws SQLException
	{
		m_databaseHelper = new DatabaseHelper(m_context, m_databaseName);
		m_database = m_databaseHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		m_databaseHelper.close();
	}
	
	public String getDatabaseName()
	{
		return m_databaseName;
	}
	
	private ContentValues createCountdownValues(CountdownJourney countdown)
	{
		ContentValues values = new ContentValues();
		values.put(CountdownJourneyTable.KEY_NAME, countdown.getName());
		values.put(CountdownJourneyTable.KEY_ORIGIN_FAV_ID, countdown.getOriginLocation() == null ? NULL_LOCATION : countdown.getOriginLocation().getId());
		values.put(CountdownJourneyTable.KEY_DEST_FAV_ID, countdown.getDestinationLocation().getId());
		values.put(CountdownJourneyTable.KEY_DAYFLAGS, countdown.getDateTimeMask().getDayMaskString());
		
		String startTimeString = TimeUtils.getDateStringUTC(countdown.getDateTimeMask().getStartTime());
		values.put(CountdownJourneyTable.KEY_START_TIME, startTimeString);
		String endTimeString = TimeUtils.getDateStringUTC(countdown.getDateTimeMask().getEndTime());
		values.put(CountdownJourneyTable.KEY_END_TIME, endTimeString);

		return values;
	}
	
	public boolean createCountdown(CountdownJourney countdown)
	{
		ContentValues values = createCountdownValues(countdown);
		long insertId = m_database.insert(CountdownJourneyTable.TABLE_NAME, null, values);
		if (insertId == -1)
		{
			return false;
		}
		
		countdown.setId(insertId);
		return true;
	}
	
	public boolean updateCountdown(CountdownJourney countdown)
	{
		if (countdown.getId() == -1)
			return false;
		
		ContentValues values = createCountdownValues(countdown);
		
		return m_database.update(CountdownJourneyTable.TABLE_NAME, values, CountdownJourneyTable.KEY_ID + "=" + countdown.getId(), null) > 0; 
	}
	
	public boolean deleteFavouriteLocation(CountdownJourney countdown)
	{
		if (countdown.getId() == -1)
		{
			return false;
		}
		
		return m_database.delete(CountdownJourneyTable.TABLE_NAME, CountdownJourneyTable.KEY_ID + "=" + countdown.getId(), null) > 0;
	}
	
	public Vector<CountdownJourney> fetchAllCountdowns() {
		Cursor c = m_database.query(CountdownJourneyTable.TABLE_NAME,
				ALL_COLUMNS, null, null, null, null,
				CountdownJourneyTable.KEY_NAME);
		return convertCurosrToCountdown(c);
	}

	public CountdownJourney fetchCountdown(CountdownJourney countdown) {
		return fetchCountdown(countdown.getId());

	}
	
	public CountdownJourney fetchCountdown(long id) {
		Cursor cursor = m_database.query(CountdownJourneyTable.TABLE_NAME,
				ALL_COLUMNS,
				CountdownJourneyTable.KEY_ID + "=" + id, null,
				null, null, null);

		List<CountdownJourney> countdowns = convertCurosrToCountdown(cursor);

		if (countdowns == null || countdowns.size() < 1) {
			return null;
		}

		return countdowns.get(0);

	}

	public boolean containsFavourite(CountdownJourney countdown) {
		return fetchCountdown(countdown) != null;
	}
	
	private Vector<CountdownJourney> convertCurosrToCountdown(Cursor c)
	{
		if(c == null)
		{
			return null;
		}
		
		Vector<CountdownJourney> countdownJourneys = new Vector<CountdownJourney>();
		
		int idIndex = c.getColumnIndex(CountdownJourneyTable.KEY_ID);
		int nameIndex = c.getColumnIndex(CountdownJourneyTable.KEY_NAME);
		int originIndex = c.getColumnIndex(CountdownJourneyTable.KEY_ORIGIN_FAV_ID);
		int destIndex = c.getColumnIndex(CountdownJourneyTable.KEY_DEST_FAV_ID);
		int dayFlagsIndex= c.getColumnIndex(CountdownJourneyTable.KEY_DAYFLAGS);
		int startTimeIndex = c.getColumnIndex(CountdownJourneyTable.KEY_START_TIME);
		int endTimeIndex = c.getColumnIndex(CountdownJourneyTable.KEY_END_TIME);
		
		c.moveToFirst();
		
		FavouriteDatabaseAdapter favouriteLocationDB = new FavouriteDatabaseAdapter(m_context, ApplicationState.DATABASE_NAME);
		if(!favouriteLocationDB.open(m_database))
		{
			return countdownJourneys;
		}
	
		while (c.isAfterLast() == false)
		{
			int	id = c.getInt(idIndex);
			String name = c.getString(nameIndex);
			int originId = c.getInt(originIndex);
			int destId = c.getInt(destIndex);
			String dayFlags = c.getString(dayFlagsIndex);
			String startTimeString = c.getString(startTimeIndex);
			String endTimeString = c.getString(endTimeIndex);

			Date startTime;
			Date endTime;
			try {
				startTime = TimeUtils.getDateFromStringUTC(startTimeString);
				endTime = TimeUtils.getDateFromStringUTC(endTimeString);
			} catch (Exception e) {
				Log.i(BrightSparkActivity.LOG_TAG,
						"convertCurosrToCountdown - invalid date time string."
								+ e.toString());
				continue;
			}
			
	
			CountdownJourney countdown;
			
			DateTimeMask dtMask = new DateTimeMask();
			dtMask.setStartTime(startTime);
			dtMask.setEndTime(endTime);
			dtMask.setDayMaskFromString(dayFlags);
			
			FavouriteLocation origin;
			FavouriteLocation destination;
			
			destination = favouriteLocationDB.fetchFavourite(destId);
			
			if (originId != NULL_LOCATION)
			{
				origin = favouriteLocationDB.fetchFavourite(originId);
				
				if (origin == null)
				{
					continue;
				}
				
				countdown = new CountdownJourney(origin, destination, name);
			}
			else
			{
				if (destination == null)
				{
					continue;
				}
				
				countdown = new CountdownJourney(destination, name);
			}
			
			countdown.setDateTimeMask(dtMask);
			countdown.setId(id);
			
			countdownJourneys.add(countdown);
				
			c.moveToNext();
		}
		
		return countdownJourneys;
	}

			
	

}


