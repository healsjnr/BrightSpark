package com.jeppesen.brightspark.jjp.api;

import java.util.Vector;

import com.google.android.maps.GeoPoint;

import android.util.Log;

public abstract class Leg {
	
	protected int m_id;
	protected JJPLocation m_origin;
	protected JJPLocation m_destination;
	protected Vector<GeoPoint> m_polyline;
	protected int m_duration;
	
	public Leg(int id, JJPLocation origin, JJPLocation destination, String polyline, int duration)
	{
		m_id = id; 
		m_origin = origin;
		m_destination = destination;
		m_polyline = getGeoPointPolyline(polyline);
		m_duration = duration;
	}
	
	public int getId()
	{
		return m_id;
	}
	
	public JJPLocation getOrigin()
	{
		return m_origin;
	}
	
	public JJPLocation getDestination()
	{
		return m_destination;
	}
	
	public Vector<GeoPoint> getPolyline()
	{
		return m_polyline;
	}
	
	public int getDuration()
	{
		return m_duration;
	}
	
	private Vector<GeoPoint> getGeoPointPolyline(String polyline)
	{
		String[] polylineString = polyline.split(";");
		Vector<GeoPoint> polylineVec = new Vector<GeoPoint>();
		
		if (polylineString.length <= 1)
		{
			polylineVec.add(m_origin.getPosition().getGeoPoint());
			polylineVec.add(m_destination.getPosition().getGeoPoint());
			return polylineVec;
		}
		
		for (int i = 0; i < polylineString.length; i++)
		{
			try
			{
				polylineVec.add(toGeoPoint(polylineString[i]));
			}
			catch (Exception e)
			{
				Log.i("com.android.journeyplanner.jpmodel.Leg","Error Parsing polyline point: " + e.toString());				
			}
		}
		
		return polylineVec;
	}
	
	private GeoPoint toGeoPoint(String position) throws Exception
	{
		String[] tempstring = position.split(",");
		if (tempstring.length != 2)
		{
			throw new Exception("Malformed GeoPoint String: " + position);
		}
		
		int latitude = (int)(Double.parseDouble(tempstring[0]) * 1E6);
		int longitude = (int)(Double.parseDouble(tempstring[1]) * 1E6);
		return new GeoPoint(latitude,longitude);
	}
	
	public abstract String toString();
	
}
