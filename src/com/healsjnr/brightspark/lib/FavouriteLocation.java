package com.healsjnr.brightspark.lib;

public class FavouriteLocation {
	
	private String m_name;
	private SimpleAddress m_address;
	private long m_id;
	
	public FavouriteLocation(String name, SimpleAddress address)
	{
		m_name = name;
		m_address = address;
		m_id = -1;
	}
	
	public FavouriteLocation(String name, SimpleAddress address, int id)
	{
		m_name = name;
		m_address = address;
		m_id = id;
	}
	
	public void setName(String name)
	{
		m_name = name;
	}
	
	public void setAddress(SimpleAddress address)
	{
		m_address = address;
	}
	
	public void setPosition(GeoPosition position)
	{
		m_address.setGeoPosition(position);
	}
	
	public void setId(long id)
	{
		m_id = id;
	}
	
	public String getName()
	{
		return m_name;
	}
	
	public SimpleAddress getAddress()
	{
		return m_address;
	}
	
	public GeoPosition getLocation()
	{
		return m_address.getGeoPosition();
	}
	
	public long getId()
	{
		return m_id;
	}

}
