package com.jeppesen.brightspark.jjp.api;

import java.util.Date;
import java.util.Vector;

import com.jeppesen.brightspark.lib.TimeUtils;

public class Journey {

	private int m_id;
	private JJPLocation m_origin;
	private JJPLocation m_destination;
	private Date m_departTime;
	private Date m_arriveTime;
	private Vector<Leg> m_legList;
	private int m_duration;
	private String m_journeyString;
	private int m_changes;
	

	public void setChanges(int changes)
	{
		m_changes = changes;
	}
	
	public int getChanges()
	{
		return m_changes;
	}
	
	public void setDuration(int duration)
	{
		m_duration = duration;
	}
	
	public int getDuration()
	{
		return m_duration;
	}
	
	public void setId(int id)
	{
		m_id = id;
	}
	
	public int getId()
	{
		return m_id;
	}
	
	public void setOrigin(JJPLocation origin)
	{
		m_origin = origin;
	}
	
	public JJPLocation getOrigin()
	{
		return m_origin;
	}
		
	public void setDestination(JJPLocation destination)
	{
		m_destination= destination;
	}
	
	public JJPLocation getDestination()
	{
		return m_destination;
	}	
		
	public void setDepartTime(Date departTime)
	{
		m_departTime = departTime;
	}
	
	public Date getDepartTime()
	{
		return m_departTime;
	}
		
	public void setArriveTime(Date arriveTime)
	{
		m_arriveTime = arriveTime;
	}
	
	public Date getArriveTime()
	{
		return m_arriveTime;
	}
		
	public void setLegList(Vector<Leg> legList)
	{
		m_legList = legList;
	}
	
	public void addLegToList(Leg leg)
	{
		m_legList.add(leg);
	}
		
	public Vector<Leg> getLegList()
	{
		return m_legList;
	}
	
	public Journey(int id, JJPLocation origin, JJPLocation destination, Date departTime, Date arriveTime, int duration, int changeCount)
	{
		m_id = id;

		m_origin = origin;
		m_destination = destination;
		
		m_departTime = departTime;
		m_arriveTime = arriveTime;
		m_duration = duration;
		m_legList = new Vector<Leg>();
		
		m_changes = changeCount;
		
	}
	
	@Override
	public String toString()
	{
		String returnString;
		
		try
		{
			returnString = TimeUtils.getSimpleTimeString(m_departTime) +
				" to " + TimeUtils.getSimpleTimeString(m_arriveTime) + ", " + m_changes + " changes";
			return returnString;
		}
		catch (Exception e)
		{
			return "";
		}

	}
	
	

}

