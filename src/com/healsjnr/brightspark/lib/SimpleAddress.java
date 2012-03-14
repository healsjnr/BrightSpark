package com.healsjnr.brightspark.lib;

import android.location.Address;

public class SimpleAddress {
	private String m_addressDescription;
	private GeoPosition m_location;
	
	public SimpleAddress(String addrText, GeoPosition pos)
	{
		m_addressDescription = addrText;
		m_location = pos;
	}
	
	public SimpleAddress(String addrText, double lat, double lon)
	{
		m_addressDescription = addrText;
		m_location = new GeoPosition(lat, lon);
	}
	
	public SimpleAddress(Address address)
	{
		String addressDesc = address.getAddressLine(0);
		if (address.getMaxAddressLineIndex() >= 1) {
			addressDesc += ", " + address.getAddressLine(1);
		}
		
		m_addressDescription = addressDesc;
		m_location = new GeoPosition(address.getLatitude(), address.getLongitude());
	}
	
	public String toString()
	{
		return m_addressDescription;
	}
	
	public void setDescription(String desc)
	{
		m_addressDescription = desc;
	}
	
	public GeoPosition getGeoPosition()
	{
		return m_location;
	}
	
	public void setGeoPosition(GeoPosition pos)
	{
		m_location = pos;
	}
}
