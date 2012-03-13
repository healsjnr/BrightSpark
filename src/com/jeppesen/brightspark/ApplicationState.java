package com.jeppesen.brightspark;

import com.jeppesen.brightspark.jjp.api.Journey;

public class ApplicationState {
	
	public static final String COUNTRY_CODE = "AU";
	public static final String LANGUAGE = "EN";
	
	public static final String DATABASE_NAME = "brightspark_data";
	
	public static final String PREFERENCES_NAME = "BrightSparkPrefs";
	
	public static final String DATA_SET = "Brisbane";
	
	private Journey m_mapJourney;
	
	private static ApplicationState m_instance;
	
	public static ApplicationState getInstance()
	{
		if (m_instance == null)
		{
			m_instance = new ApplicationState();
		}
		
		return m_instance;
	}
	
	public Journey getMapJourney()
	{
		return m_mapJourney;
	}
	
	public void setMapJourney(Journey jny)
	{
		m_mapJourney = jny;
	}
	

}
