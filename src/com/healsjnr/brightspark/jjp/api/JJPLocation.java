package com.healsjnr.brightspark.jjp.api;

public abstract class JJPLocation {

	private String m_id;
	private String m_dataSet;
	private GeoPosition m_position;
	private String m_description;

	public String getDescription() {
		return m_description;
	}
	public void setDescription(String desc) {
		this.m_description = desc;
	}
	
	public String getId() {
		return m_id;
	}
	public void setId(String id) {
		this.m_id = id;
	}
	public String getDataSet() {
		return m_dataSet;
	}
	public void setDataSet(String dataSet) {
		this.m_dataSet = dataSet;
	}
	
	public GeoPosition getPosition() {
		return m_position;
	}

	public void setPosition(GeoPosition position) {
		this.m_position = position;
	}
	
	public JJPLocation(String m_id, String m_dataSet, GeoPosition position, String desc) {
		this.m_id = m_id;
		this.m_dataSet = m_dataSet;
		this.m_position = position;
		this.m_description = desc;
	}
	
}
