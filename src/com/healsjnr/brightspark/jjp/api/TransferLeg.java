package com.healsjnr.brightspark.jjp.api;

public class TransferLeg extends Leg {
	
	private String m_mode;
	
	public TransferLeg(int id, JJPLocation origin, JJPLocation destination, String polyline, int duration, String mode)
	{
		super(id, origin, destination, polyline, duration);
		m_mode = mode;
	}
	
	public String getMode()
	{
		return m_mode;
	}
	
	public void setMode(String mode)
	{
		m_mode = mode;
	}
	
	public String toString()
	{
		if (m_mode != "")
		{
			return m_duration + " minute " + m_mode + " transfer." ;
		}
		else
		{
			return m_duration + " minute transfer";	
		}
	}

}
