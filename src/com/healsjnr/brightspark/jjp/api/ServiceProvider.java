package com.healsjnr.brightspark.jjp.api;

public class ServiceProvider {

	private String m_serviceProviderUid;
	private String m_name;
	private String m_timeZone;
	private String m_phoneNumber;
	private String m_webSite;
	
	public String getId() {
		return m_serviceProviderUid;
	}

	public void setId(String serviceProviderUid) {
		m_serviceProviderUid = serviceProviderUid;
	}

	public String getName() {
		return m_name;
	}

	public void setName(String name) {
		m_name = name;
	}

	public String getTimeZone() {
		return m_timeZone;
	}

	public void setTimeZone(String timeZone) {
		m_timeZone = timeZone;
	}

	public String gePhoneNumber() {
		return m_phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		m_phoneNumber = phoneNumber;
	}

	public String getWebSite() {
		return m_webSite;
	}

	public void setWebSite(String webSite) {
		m_webSite = webSite;
	}
	
	public ServiceProvider(String uid, String name, String timeZone, String phNum, String webSite)
	{
		m_serviceProviderUid = uid;
		m_name = name;
		m_timeZone = timeZone;
		m_phoneNumber = phNum;
		m_webSite = webSite;
	}

}
