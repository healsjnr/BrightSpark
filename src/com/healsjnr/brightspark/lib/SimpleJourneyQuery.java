package com.jeppesen.brightspark.lib;

import java.util.Date;

import com.jeppesen.brightspark.jjp.api.TimeMode;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Xml.Encoding;

public class SimpleJourneyQuery implements Parcelable {

	private final static int DEFAULT_NUM_JOURNEYS = 3; 
	
	public final static String CURRENT_POSITION_STRING = "Current Position";
	
	private GeoPosition m_origin;
	private String m_originDescription;
	private GeoPosition m_destination;
	private String m_destinationDescription;
	private Date m_dateTime;
	private TimeMode m_timeMode;
	private int m_numJourneys;
	
	public SimpleJourneyQuery()
	{ 
		m_numJourneys = DEFAULT_NUM_JOURNEYS;
	}

	public SimpleJourneyQuery(GeoPosition origin, String originDesc, GeoPosition dest, String destDesc, 
			Date journeyTime, TimeMode timeMode)
	{
		m_origin = origin;
		m_originDescription = originDesc;
		m_destination = dest;
		m_destinationDescription = destDesc;
		
		m_dateTime = journeyTime;
		m_timeMode = timeMode;
		m_numJourneys = DEFAULT_NUM_JOURNEYS;
	}
	
	public GeoPosition getOrigin() {
		return m_origin;
	}
	
	public void setOrigin(GeoPosition pos, String desc)
	{
		this.m_origin = pos;
		this.m_originDescription = desc;
	}

	public void setOriginLocation(GeoPosition origin) {
		this.m_origin = origin;
	}

	public String getOriginDescription() {
		return m_originDescription;
	}

	public void setOriginDescription(String originDescription) {
		this.m_originDescription = originDescription;
	}

	public GeoPosition getDestination() {
		return m_destination;
	}
	
	public void setDestination(GeoPosition pos, String desc)
	{
		this.m_destination = pos;
		this.m_destinationDescription = desc;
	}

	public void setDestinationLocation(GeoPosition destination) {
		this.m_destination = destination;
	}

	public String getDestinationDescription() {
		return m_destinationDescription;
	}

	public void setDestinationDescription(String destinationDescription) {
		this.m_destinationDescription = destinationDescription;
	}

	public Date getDateTime() {
		return m_dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.m_dateTime = dateTime;
	}

	public TimeMode getTimeMode() {
		return m_timeMode;
	}

	public void setTimeMode(TimeMode timeMode) {
		this.m_timeMode = timeMode;
	}
	
	public int getNumJourneys()
	{
		return m_numJourneys;
	}
	
	public void setNumJourneys(int numJourneys)
	{
		m_numJourneys = numJourneys;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public boolean isValid()
	{
		if (m_originDescription == null || m_destinationDescription == null)
			return false;
		
		if (m_originDescription.isEmpty() || m_destinationDescription.isEmpty())
			return false;
		
		if (m_originDescription.equals(CURRENT_POSITION_STRING) && m_destinationDescription.equals(CURRENT_POSITION_STRING))
			return false;
		
		return true;
	}

	public static final Parcelable.Creator<SimpleJourneyQuery> CREATOR = new Parcelable.Creator<SimpleJourneyQuery>() {

		@Override
		public SimpleJourneyQuery createFromParcel(Parcel in) {
			
			SimpleJourneyQuery journeyQuery = new SimpleJourneyQuery();
			journeyQuery.setOriginDescription(in.readString());
			double originLat = in.readDouble();
			double originLong = in.readDouble();
			if (!journeyQuery.getOriginDescription().equals(SimpleJourneyQuery.CURRENT_POSITION_STRING))
			{
				journeyQuery.setOriginLocation(new GeoPosition(originLat, originLong));
			}
			
			journeyQuery.setDestinationDescription(in.readString());
			double destLat = in.readDouble();
			double destLong = in.readDouble();
			if (!journeyQuery.getDestinationDescription().equals(SimpleJourneyQuery.CURRENT_POSITION_STRING))
			{
				journeyQuery.setDestinationLocation(new GeoPosition(destLat, destLong));
			}
			
			journeyQuery.setDateTime(new Date(in.readLong()));
			TimeMode timeMode = TimeMode.valueOf(in.readString());
			journeyQuery.setTimeMode(timeMode);
			journeyQuery.setNumJourneys(in.readInt());
			
			return journeyQuery;
		}

		@Override
		public SimpleJourneyQuery[] newArray(int size) {
			return new SimpleJourneyQuery[size];
		}
		
	};
	
	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(m_originDescription);
		parcel.writeDouble(m_origin == null ? 0 : m_origin.getLatitude());
		parcel.writeDouble(m_origin == null ? 0 : m_origin.getLongitude());
		parcel.writeString(m_destinationDescription);
		parcel.writeDouble(m_destination == null ? 0 : m_destination.getLatitude());
		parcel.writeDouble(m_destination == null ? 0 : m_destination.getLongitude());
		parcel.writeLong(m_dateTime.getTime());
		parcel.writeString(m_timeMode.toString());
		parcel.writeInt(m_numJourneys);
	}

}
