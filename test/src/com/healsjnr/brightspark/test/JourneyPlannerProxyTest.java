package com.healsjnr.brightspark.test;

import java.io.InputStream;

import com.healsjnr.brightspark.jjp.api.JourneyPlan;
import com.healsjnr.brightspark.jjp.proxy.JourneyPlannerProxy;
import com.healsjnr.brightspark.lib.network.IRestUtil;
import com.healsjnr.brightspark.lib.network.RestResponse;
import com.healsjnr.brightspark.test.mock.MockRestUtil;

import junit.framework.TestCase;

public class JourneyPlannerProxyTest extends TestCase {

	private static final String API_KEY = "733f873a-1e77-4c14-a84c-55ebf1a23cd7";
	private static final String BASE_URL = JourneyPlannerProxy.SERVICE_URI;

	public void testGenerateAvailableDataSetURL() {
		String expectedResult = BASE_URL + "?ApiKey=" + API_KEY
				+ "&Format=json";

		JourneyPlannerProxy jpProxy = new JourneyPlannerProxy();
		String actualResult = jpProxy.generateAvailableDataSetRequestUrl();

		assertEquals(expectedResult, actualResult);

	}

	public void testGenerateJourneyPlanningURL() {

		String dataSet = "Brisbane";
		String from = "1.1";
		String to = "2.2";
		String dateTime = "2012-03-31T10:00";
		String timeMode = "LeaveAfter";
		String mapRequired = "true";
		int numJourneys = 3;

		String expectedResult = BASE_URL + "/" + dataSet + "/JourneyPlan?ApiKey=" + API_KEY + "&From="
				+ from + "&To=" + to + "&Date=" + dateTime + "&TimeMode="
				+ timeMode + "&MappingDataRequired=" + mapRequired
				+ "&MaxJourneys=" + numJourneys + "&Format=json";

		JourneyPlannerProxy jpProxy = new JourneyPlannerProxy();
		String actualResult = jpProxy.generateJourneyPlanningRequestUrl(
				dataSet, from, to, dateTime, timeMode, numJourneys);
		assertEquals(expectedResult, actualResult);
	}
	
	public void testJourneyPlan_ValidResposne()
	{
		String testCaseFile = "assets/valid_jp_response";
		String testCaseURL = MockRestUtil.VALID_RESPONSE_URL + ";" + testCaseFile;
		
		IRestUtil mockRestUtil = new MockRestUtil();
		JourneyPlannerProxy jpProxy = new JourneyPlannerProxy(mockRestUtil);
		JourneyPlan response = jpProxy.executeJourneyPlanRequest(testCaseURL);
		assertNotNull(response.getJourneys());
		assertTrue(response.getJourneys().size() > 0);
		assertTrue(response.getJourneys().get(0).getLegList().size() > 0);
		assertTrue(response.isJourneyPlanSuccessful());
		assertEquals("OK", response.getErrorMessage());
		assertNotNull(response);
	}
	
	public void testJourneyPlan_NoJourneys()
	{
		String testCaseFile = "assets/valid_jp_nojourney_response";
		String testCaseURL = MockRestUtil.VALID_RESPONSE_URL + ";" + testCaseFile;
		
		IRestUtil mockRestUtil = new MockRestUtil();
		JourneyPlannerProxy jpProxy = new JourneyPlannerProxy(mockRestUtil);
		JourneyPlan response = jpProxy.executeJourneyPlanRequest(testCaseURL);
		assertNotNull(response);
		assertNotNull(response.getJourneys());
		assertFalse(response.isJourneyPlanSuccessful());
		assertEquals("No Journeys.", response.getErrorMessage());
		assertEquals(0, response.getJourneys().size());
	}
	
	public void testJourneyPlan_ErrorResposne()
	{
		String testCaseFile = "assets/error_jp_response";
		String testCaseURL = MockRestUtil.VALID_RESPONSE_URL + ";" + testCaseFile;
		
		IRestUtil mockRestUtil = new MockRestUtil();
		JourneyPlannerProxy jpProxy = new JourneyPlannerProxy(mockRestUtil);
		JourneyPlan response = jpProxy.executeJourneyPlanRequest(testCaseURL);
		assertNotNull(response);
		assertFalse(response.isJourneyPlanSuccessful());
		assertEquals("Unknown From station code: null", response.getErrorMessage());
	}
	
	public void testJourneyPlan_BrokenJPResposne()
	{
		String testCaseFile = "assets/broken_jp_response";
		String testCaseURL = MockRestUtil.VALID_RESPONSE_URL + ";" + testCaseFile;
		
		IRestUtil mockRestUtil = new MockRestUtil();
		JourneyPlannerProxy jpProxy = new JourneyPlannerProxy(mockRestUtil);
		JourneyPlan response = jpProxy.executeJourneyPlanRequest(testCaseURL);
		assertNull(response);
	}

}
