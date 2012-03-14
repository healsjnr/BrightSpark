package com.healsjnr.brightspark.lib;

import com.google.android.maps.GeoPoint;

public class GeoPosition {
	private GeoPoint m_geoPoint;
	private double m_latitude;
	private double m_longitude;
	
	public double getLatitude() {
		return m_latitude;
	}
	public void setLatitude(double latitude) {
		this.m_latitude = latitude;
	}
	
	public double getLongitude() {
		return m_longitude;
	}
	public void setLongitude(double longitude) {
		this.m_longitude = longitude;
	}
	
	public GeoPoint getGeoPoint()
	{
		return m_geoPoint;
	}
	
	public GeoPosition(double latitude, double longitude)
	{
		m_latitude = latitude;
		m_longitude = longitude;
		m_geoPoint = toGeoPoint(m_latitude, m_longitude);
	}
	
	// Creates a GeoPosition based on a position string in the form "lat;long"
	public GeoPosition(String position) throws Exception
	{
		String[] tempstring = position.split(",");
		if (tempstring.length != 2)
		{
			throw new Exception("Malformed GeoPoint String: " + position);
		}
		
		m_latitude = Double.parseDouble(tempstring[0]);
		m_longitude = Double.parseDouble(tempstring[1]);
		m_geoPoint = toGeoPoint(m_latitude, m_longitude);
	
	}
	
	public GeoPosition(GeoPoint point)
	{
		m_geoPoint = point;
		m_latitude = point.getLatitudeE6() / 1E6;
		m_longitude = point.getLongitudeE6() / 1E6;
	}
	
	public static GeoPoint toGeoPoint(double latitude, double longitude)
	{
		return new GeoPoint((int)(latitude*1E6), (int)(longitude*1E6));
	}

	@Override
	public String toString()
	{
		return m_latitude + "," + m_longitude;
	}
}
