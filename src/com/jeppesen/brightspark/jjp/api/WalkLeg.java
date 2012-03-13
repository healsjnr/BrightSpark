package com.jeppesen.brightspark.jjp.api;

public class WalkLeg extends Leg {

	private int m_walkDistance;
	
	public int getWalkDistance() {
		return m_walkDistance;
	}

	public void setWalkDistance(int walkDistance) {
		m_walkDistance = walkDistance;
	}

	public WalkLeg(
			int id, 
			JJPLocation origin, 
			JJPLocation destination, 
			String polyline, 
			int duration,
			int walkDistance)
	{
		super(id, origin, destination, polyline, duration);
		m_walkDistance = walkDistance;
	}
	
	public String toString()
	{
		return "Walk " + m_walkDistance + " meters in " + m_duration + " minutes.";
	}
}
