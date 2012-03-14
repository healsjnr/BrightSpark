package com.healsjnr.brightspark.lib.network;

public class QueryParam {
	public QueryParam(String name, String value){
		m_name = name;
		m_value = value;
	}
	
	public String getName() {
		return m_name;
	}
	public void setName(String name) {
		this.m_name = name;
	}
	public String getValue() {
		return m_value;
	}
	public void setValue(String value) {
		this.m_value = value;
	}
	private String m_name;
	private String m_value;
}
