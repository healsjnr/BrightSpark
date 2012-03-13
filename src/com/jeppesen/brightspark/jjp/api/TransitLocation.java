package com.jeppesen.brightspark.jjp.api;

public class TransitLocation extends JJPLocation {
		
	private String m_stopCode;
	private String m_stopUid;
	
	public String getStopCode()
	{
		return m_stopCode;
	}
	
	public void setStopCode(String code)
	{
		m_stopCode = code;
	}
	
	public String getStopUid()
	{
		return m_stopUid;
	}
	
	public void setStopUid(String uid)
	{
		m_stopUid = uid;
	}
	
	public TransitLocation(String id, String dataSet, String description,
			GeoPosition position, String stopCode, String stopUid) {
		super(id, dataSet, position, description);
		this.m_stopCode = stopCode;
		this.m_stopUid = stopUid;
	}
	

}
