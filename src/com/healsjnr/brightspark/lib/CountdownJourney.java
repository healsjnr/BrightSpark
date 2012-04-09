package com.healsjnr.brightspark.lib;

import java.util.Calendar;

import com.healsjnr.brightspark.jjp.api.TimeMode;

public class CountdownJourney {
	
	private FavouriteLocation m_origin;
	private FavouriteLocation m_destination;

	private DateTimeMask m_dateTimeMask;

	private String m_name;
	
	private long m_id;
	
	public CountdownJourney(FavouriteLocation origin, FavouriteLocation destination, String name)
	{
		m_dateTimeMask = new DateTimeMask();
		m_origin = origin;
		m_destination = destination;
		m_name = name;
		m_id = -1;
	}
	
	public CountdownJourney(FavouriteLocation destination, String name)
	{
		m_dateTimeMask = new DateTimeMask();
		m_origin = null;
		m_destination = destination;
		m_name = name;
		m_id = -1;
	}
	
	public void setId(long id)
	{
		m_id = id;
	}
	
	public long getId()
	{
		return m_id;
	}
	
	public void setName(String name)
	{
		m_name = name;
	}
	
	public String getName()
	{
		return m_name;
	}
	
	
	public void setDateTimeMask(DateTimeMask dtMask)
	{
		m_dateTimeMask = dtMask;
	}
	
	public DateTimeMask getDateTimeMask()
	{
		return m_dateTimeMask;
	}
	
	public SimpleJourneyQuery getNextJourneyQuery()
	{
		SimpleJourneyQuery journeyQuery = new SimpleJourneyQuery();
		if (m_origin == null)
		{
			journeyQuery.setOrigin(null, SimpleJourneyQuery.CURRENT_POSITION_STRING);
		}
		else
		{
			journeyQuery.setOrigin(m_origin.getLocation(), m_origin.getName());
		}
		
		
		journeyQuery.setDestination(m_destination.getLocation(), m_destination.getName());
		
		journeyQuery.setTimeMode(TimeMode.LeaveAfter);
		journeyQuery.setDateTime(Calendar.getInstance().getTime());
		journeyQuery.setNumJourneys(3);
		
		return journeyQuery;
	}
	
	public String getOriginName()
	{
		if (m_origin == null)
		{
			return SimpleJourneyQuery.CURRENT_POSITION_STRING;
		}
		
		return m_origin.getName();
	}
	
	public FavouriteLocation getOriginLocation()
	{
		return m_origin;
	}
	
	public String getDestinationName()
	{
		return m_destination.getName();
	}
	
	public FavouriteLocation getDestinationLocation()
	{
		return m_destination;
	}
}
