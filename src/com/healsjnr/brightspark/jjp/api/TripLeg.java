package com.jeppesen.brightspark.jjp.api;

import java.util.Date;
import java.util.Vector;

import android.util.Log;

import com.google.android.maps.OverlayItem;
import com.jeppesen.brightspark.lib.TimeUtils;

public class TripLeg extends Leg {

	private Date m_departTime;
	private Date m_arriveTime;
	private String m_mode;
	private String m_tripUid;
	private String m_tripCode;
	private String m_routeName;
	private String m_routeCode;
	private ServiceProvider m_serviceProvider;
	private String m_headsign;
	
	public Date getDepartTime() {
		return m_departTime;
	}

	public void setDepartTime(Date departTime) {
		m_departTime = departTime;
	}

	public Date getArriveTime() {
		return m_arriveTime;
	}

	public void setArriveTime(Date arriveTime) {
		m_arriveTime = arriveTime;
	}

	public String getMode() {
		return m_mode;
	}

	public void setMode(String mode) {
		m_mode = mode;
	}

	public String getTripUid() {
		return m_tripUid;
	}

	public void setTripUid(String tripUid) {
		m_tripUid = tripUid;
	}

	public String getTripCode() {
		return m_tripCode;
	}

	public void setTripCode(String tripCode) {
		m_tripCode = tripCode;
	}

	public String getRouteName() {
		return m_routeName;
	}

	public void setRouteName(String routeName) {
		m_routeName = routeName;
	}

	public String getRouteCode() {
		return m_routeCode;
	}

	public void setRouteCode(String routeCode) {
		m_routeCode = routeCode;
	}

	public ServiceProvider getServiceProviderUid() {
		return m_serviceProvider;
	}

	public void setServiceProviderUid(ServiceProvider serviceProviderUid) {
		m_serviceProvider = serviceProviderUid;
	}

	public String getHeadsign() {
		return m_headsign;
	}

	public void setHeadsign(String headsign) {
		m_headsign = headsign;
	}

	public TripLeg(int id, 
			JJPLocation origin, 
			JJPLocation destination, 
			String polyline, 
			int duration,
			Date departTime, 
			Date arriveTime,
			String mode,
			String tripUid,
			String tripCode,
			String routeName,
			String routeCode,
			ServiceProvider serviceProvider,
			String headsign)
	{
		super(id, origin, destination, polyline, duration);
		m_departTime = departTime;
		m_arriveTime = arriveTime;
		m_mode = mode;
		m_tripUid = tripUid;
		m_tripCode = tripCode;
		m_routeName = routeName;
		m_routeCode = routeCode;
		m_serviceProvider = serviceProvider;
		m_headsign = headsign;
	}
	
	
	public String toString()
	{

		String legText = "";
		String legDepart = "";
		String legArrive = "";

		try
		{
			legDepart =  TimeUtils.getSimpleTimeString(m_departTime) + " - depart " + m_origin.getDescription();
		}
		catch (Exception e)
		{
			legDepart = "Depart " + m_origin.getDescription();
			Log.i("TripLeg","Exception parsing depart date / time: " + m_departTime.toLocaleString());
		}
		
 		String tripType = "";
		String direction = "";
		if (m_routeCode != "")
		{
			tripType = m_routeCode;
		} else if (m_routeName != "")
		{
			tripType = m_routeName;
		} 
		
		if (m_headsign != "")
		{
			direction = " - " + m_headsign;
		}
		
		legText = "Take the " + tripType + " " + m_mode + " " + direction;
		
		try
		{
			legArrive = TimeUtils.getSimpleTimeString(m_arriveTime) + " - arrive " + m_destination.getDescription();
		}
		catch (Exception e)
		{
			legArrive = "Arrive " + m_destination.getDescription();
			Log.i("TripLeg","Exception parsing arriate date / time: " + m_departTime.toLocaleString());
		}
		
		return legDepart + "\n" + legText + "\n" + legArrive;
	
	}
	

}
