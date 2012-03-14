package com.healsjnr.brightspark.jjp.proxy;

import java.util.Date;

import com.healsjnr.brightspark.BrightSparkActivity;
import com.healsjnr.brightspark.jjp.api.AvailableDataSetResponse;
import com.healsjnr.brightspark.jjp.api.DataSet;
import com.healsjnr.brightspark.lib.GeoPosition;
import com.healsjnr.brightspark.lib.TimeUtils;

import android.R.bool;
import android.util.Log;

public class RegionInfo {
	
	private String m_regionId;
	private String m_regionName;
	private GeoPosition m_centre;
	private GeoPosition m_topLeft;
	private GeoPosition m_bottomRight;
	
	private Date m_startDate;
	private Date m_endDate;

	public String getId()
	{
		return m_regionId;
	}
	
	public String getName()
	{
		return m_regionName;
	}
	
	public GeoPosition getCentre()
	{
		return m_centre;
	}
	
	public GeoPosition getTopLeft()
	{
		return m_topLeft;
	}
	
	public GeoPosition getBottomRight()
	{
		return m_bottomRight;
	}
	
	public Date getStartDate()
	{
		return m_startDate;
	}
	
	public Date getEndDate()
	{
		return m_endDate;
	}
	
	/**
	 * Return true if the supplied GeoPosition is within the data set.
	 * @param point to be tested
	 * @return true if it's inside the region.
	 */
	public boolean isWithin(GeoPosition point)
	{
		double top_lat = m_topLeft.getLatitude();
		double left_long = m_topLeft.getLongitude();
		double bottom_lat = m_bottomRight.getLatitude();
		double right_long = m_bottomRight.getLongitude();
		
		double point_lat = point.getLatitude();
		double point_long = point.getLongitude();
		
		boolean latitudeOk = false;
		boolean longitudeOk = false;
		
		if (point_lat > bottom_lat || point_lat < top_lat)
		{
			latitudeOk = true;
		}
		
		// If the right longitude is less than the left, we are crossing the date line.
		if (right_long < left_long)
		{
			// In this case the point only needs to  be greater than the left OR less than the right 
			if (point_long > left_long || point_long < right_long)
			{
				longitudeOk = true;
			}
		
		} 
		else if (point_long > left_long && point_long < right_long) // Otherwise the point needs be greater than the left AND less then the right.
		{
			longitudeOk = true;
		}
		
		return latitudeOk && longitudeOk;
		
	}
	
	
	
	/**
	 * Factory method for converting a DataSet from JJP API into a RegionInfo object.
	 * @param dataSet
	 * @return
	 */
	public static RegionInfo parseDataSetToRegionInfo(DataSet dataSet)
	{
		RegionInfo region = new RegionInfo();
		
		region.m_regionId = dataSet.Id;
		region.m_regionName = dataSet.Name;
		
		try {
			region.m_centre = new GeoPosition(dataSet.Centroid);
			
			String[] geoBoundary = dataSet.BoundaryPolyline.split(";");
			if (geoBoundary.length != 2)
			{
				Log.e(BrightSparkActivity.LOG_TAG, "parseDataSetToRegionInfo - Malformed boundary polyline: " + dataSet.BoundaryPolyline);
				return null;
			}
						
			region.m_topLeft = new GeoPosition(geoBoundary[0]);
			region.m_bottomRight = new GeoPosition(geoBoundary[1]);
			
			region.m_startDate = TimeUtils.getDateFromString(dataSet.StartDate, AvailableDataSetResponse.DATE_FORMAT);
			region.m_endDate= TimeUtils.getDateFromString(dataSet.EndDate, AvailableDataSetResponse.DATE_FORMAT);
			
		} catch (Exception e) {
			Log.e(BrightSparkActivity.LOG_TAG, "parseDataSetToRegionInfo - Exception parsing data set info." + e);
			return null;
		}
		
		return region;
	}
	

}
