package com.healsjnr.brightspark.database;

import java.util.List;
import java.util.Vector;

import com.healsjnr.brightspark.BrightSparkActivity;
import com.healsjnr.brightspark.lib.FavouriteLocation;
import com.healsjnr.brightspark.lib.GeoPosition;
import com.healsjnr.brightspark.lib.SimpleAddress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FavouriteDatabaseAdapter {
	
	private Context m_context;
	private SQLiteDatabase m_database;
	private DatabaseHelper m_databaseHelper;
	
	private String m_databaseName;
	
	private final String[] ALL_COLUMNS = new String[] {
			FavouriteLocationTable.KEY_ID,
			FavouriteLocationTable.KEY_NAME,
			FavouriteLocationTable.KEY_LAT,
			FavouriteLocationTable.KEY_LONG,
			FavouriteLocationTable.KEY_ADDRESS		
	};
	
	public FavouriteDatabaseAdapter(Context context, String databaseName)
	{
		m_context = context;
		m_databaseName = databaseName;
	}
	
	public FavouriteDatabaseAdapter open() throws SQLException
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

	// Creates a content value set representing the favourite.
	// Note: This doesn't set the ID as this is auto-incremented. 
	private ContentValues createFavouriteLocationValues(FavouriteLocation fav)
	{
		ContentValues values = new ContentValues();
		values.put(FavouriteLocationTable.KEY_NAME, fav.getName());
		values.put(FavouriteLocationTable.KEY_LAT, fav.getLocation().getLatitude());
		values.put(FavouriteLocationTable.KEY_LONG, fav.getLocation().getLongitude());
		values.put(FavouriteLocationTable.KEY_ADDRESS, fav.getAddress().toString());
		
		return values;
	}
	
	public boolean createFavouriteLocation(FavouriteLocation favourite)
	{
		ContentValues values = createFavouriteLocationValues(favourite);
		long insertId = m_database.insert(FavouriteLocationTable.TABLE_NAME, null, values);
		if (insertId == -1)
		{
			return false;
		}
		
		favourite.setId(insertId);
		return true;
	}
	
	public boolean updateFavouriteLocation(FavouriteLocation favourite)
	{
		if (favourite.getId() == -1)
			return false;
		
		ContentValues values = createFavouriteLocationValues(favourite);
		
		return m_database.update(FavouriteLocationTable.TABLE_NAME, values, FavouriteLocationTable.KEY_ID + "=" + favourite.getId(), null) > 0; 
	}
	
	public boolean deleteFavouriteLocation(FavouriteLocation favourite)
	{
		if (favourite.getId() == -1)
		{
			return false;
		}
		
		return m_database.delete(FavouriteLocationTable.TABLE_NAME, FavouriteLocationTable.KEY_ID + "=" + favourite.getId(), null) > 0;
		
	}
	
	public Vector<FavouriteLocation> fetchAllFavourites()
	{
		Cursor c = m_database.query(FavouriteLocationTable.TABLE_NAME, ALL_COLUMNS, null, null, null, null, FavouriteLocationTable.KEY_NAME);
		return convertCurosrToFavourite(c);
	}
	
	public FavouriteLocation fetchFavourite(FavouriteLocation favourite)
	{
		Cursor cursor = m_database.query(FavouriteLocationTable.TABLE_NAME, ALL_COLUMNS, 
				FavouriteLocationTable.KEY_ID + "=" + favourite.getId(), null, null, null, null);
		
		List<FavouriteLocation> locations = convertCurosrToFavourite(cursor);
		
		if (locations == null || locations.size() < 1)
		{
			return null;
		}
		
		return locations.get(0);
		
	}
	
	public boolean containsFavourite(FavouriteLocation favourite)
	{
		return fetchFavourite(favourite) != null;
	}
	
	private Vector<FavouriteLocation> convertCurosrToFavourite(Cursor c)
	{
		if(c == null)
		{
			return null;
		}
		
		Vector<FavouriteLocation> locations = new Vector<FavouriteLocation>();
		
		int idIndex = c.getColumnIndex(FavouriteLocationTable.KEY_ID);
		int nameIndex = c.getColumnIndex(FavouriteLocationTable.KEY_NAME);
		int latIndex = c.getColumnIndex(FavouriteLocationTable.KEY_LAT);
		int longIndex = c.getColumnIndex(FavouriteLocationTable.KEY_LONG);
		int addressIndex = c.getColumnIndex(FavouriteLocationTable.KEY_ADDRESS);
		
		c.moveToFirst();
		
		while (c.isAfterLast() == false)
		{
			int	id = c.getInt(idIndex);
			String name = c.getString(nameIndex);
			String lat = c.getString(latIndex);
			String lon = c.getString(longIndex);
			String address = c.getString(addressIndex);
			
			GeoPosition p;
			try {
				p = new GeoPosition(lat + "," + lon);
			} catch (Exception e) {
				Log.i(BrightSparkActivity.LOG_TAG,
						"convertCursorToFavourite - invalid geo position."
								+ e.toString());
				c.moveToNext();
				continue;
			}
			
			SimpleAddress simpleAddress = new SimpleAddress(address, p);
			FavouriteLocation loc = new FavouriteLocation(name, simpleAddress, id);
			
			locations.add(loc);
			
			c.moveToNext();
		}
		
		return locations;
	}

}
