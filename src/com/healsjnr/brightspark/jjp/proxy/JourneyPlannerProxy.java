package com.healsjnr.brightspark.jjp.proxy;

import java.util.Vector;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.healsjnr.brightspark.BrightSparkActivity;
import com.healsjnr.brightspark.jjp.api.AvailableDataSetResponse;
import com.healsjnr.brightspark.jjp.api.JourneyPlan;
import com.healsjnr.brightspark.lib.network.IRestUtil;
import com.healsjnr.brightspark.lib.network.QueryParam;
import com.healsjnr.brightspark.lib.network.RestResponse;
import com.healsjnr.brightspark.lib.network.RestUtil;

public class JourneyPlannerProxy {
	
	private static final String API_KEY = "733f873a-1e77-4c14-a84c-55ebf1a23cd7";
	private static final String DATA_SET_TOKEN = "{DATA_SET}";
	public static final String SERVICE_URI = "http://journeyplanner.jeppesen.com/journeyplannerservice/v2/rest/DataSets";
	
	// JSON type string definitions: Location 
	public static final String TYPE_STRING_TRANSIT_LOCATION = "TransitStop:http://www.jeppesen.com/journeyplanner";
	public static final String TYPE_STRING_GEO_LOCATION = "GeoLocation:http://www.jeppesen.com/journeyplanner";

	// JSON type string definitions: Leg
	public static final String TYPE_STRING_WALK_LEG = "WalkLeg:http://www.jeppesen.com/journeyplanner";
	public static final String TYPE_STRING_TRANSFER_LEG = "TransferLeg:http://www.jeppesen.com/journeyplanner";
	public static final String TYPE_STRING_TRIP_LEG = "TripLeg:http://www.jeppesen.com/journeyplanner";
	
	private IRestUtil m_restUtil;
	
	public JourneyPlannerProxy()
	{
		m_restUtil = new RestUtil();
	}
	
	public JourneyPlannerProxy(IRestUtil restUtil)
	{
		m_restUtil = restUtil;
	}
	

	public AvailableDataSetResponse getAvailableDataSets()
	{
		String queryString = generateAvailableDataSetRequestUrl();
		return executeAvailableDataSetRequest(queryString);
	}
	
	/**
	 * Creates a rest URL used to execute a get available data set query. 
	 * @return
	 */
	public String generateAvailableDataSetRequestUrl()
	{
		Vector<QueryParam> params = new Vector<QueryParam>();
		
		params.add(new QueryParam("ApiKey", API_KEY));
		params.add(new QueryParam("Format", "json"));
		
		return m_restUtil.BuildRestURL(SERVICE_URI, params);	
	}
	
	public AvailableDataSetResponse executeAvailableDataSetRequest(String restUrl)
	{
		RestResponse response = m_restUtil.DoHttpGet(restUrl);
		
		Log.i(BrightSparkActivity.LOG_TAG, "executeAvailableDataSetRequest - Result Code: " + response.getCode());
    	
    	if (response.isSuccessful())
    	{
    		AvailableDataSetResponse dataSetResponse = null;
    		try
    		{
    			GsonBuilder gsonBuilder = new GsonBuilder();
    			Gson gsonParser = gsonBuilder.create();
    			
    			dataSetResponse = gsonParser.fromJson(response.getData(), AvailableDataSetResponse.class);
    			
    			if (dataSetResponse.Status.Severity != 0)
    			{
    				String errorString = dataSetResponse.Status.toString();
    				Log.e(BrightSparkActivity.LOG_TAG, "executeAvailableDataSetRequest - " + errorString);
    				return null;
    			}
    			
    		}
    		catch (Exception e)
    		{
    			Log.e(BrightSparkActivity.LOG_TAG, "executeAvailableDataSetRequest - Excpetion parsing response with GSON.");
        		return null;
    		}
    		
    		return dataSetResponse;
    		
    	}
    	else
    	{
    		Log.e(BrightSparkActivity.LOG_TAG, "executeAvailableDataSetRequest - Available Data Set Error." + response.getCode());
    		return null;
    	}
	}

	public JourneyPlan doJourneyPlan(String dataSet, String from, String to, String dateTime, String timeMode, int numJourneys)
	{
		String queryString = generateJourneyPlanningRequestUrl(dataSet, from, to, dateTime, timeMode, numJourneys);
		return executeJourneyPlanRequest(queryString);
	}
	
	/**
	 * Creates a REST url to perform a journey plan from a geo-location to a geo-location
	 * @param from a string in the format "number,number" representing 
	 * @param to a string in the format "number,number" representing
	 * @return a string that can be passed to  
	 */
	public String generateJourneyPlanningRequestUrl(String dataSet, String from, String to, String dateTime, String timeMode, int numJourneys)
	{
		Vector<QueryParam> params = new Vector<QueryParam>();
		
		params.add(new QueryParam("ApiKey", API_KEY));
		params.add(new QueryParam("From", from));
		params.add(new QueryParam("To", to));
		params.add(new QueryParam("Date", dateTime));
		params.add(new QueryParam("TimeMode", timeMode));
		params.add(new QueryParam("MappingDataRequired", "true"));
		params.add(new QueryParam("MaxJourneys", Integer.toString(numJourneys)));
		params.add(new QueryParam("Format", "json"));
		
		String baseUri = SERVICE_URI + "/" + dataSet + "/JourneyPlan";
		
		return m_restUtil.BuildRestURL(baseUri, params);	
	}
	
	/**
	 * Executes a journey plan from a geo-location to a geo-location
	 * @param restUrl a URL generated by generateRequestUrl to perform a journey plan. 
	 * @return A fully formed JourneyPlan object or null. If any error is encountered execute the journey plan or 
	 * parsing the result at any point, null will be returned.  
	 */
	public JourneyPlan executeJourneyPlanRequest(String restUrl)
	{
	
		RestResponse response = m_restUtil.DoHttpGet(restUrl);
		
		Log.i(BrightSparkActivity.LOG_TAG, "executeJourneyPlan - Result Code: " + response.getCode());
    	
    	if (response.isSuccessful())
    	{
    		JourneyPlan jp = new JourneyPlan();
    		try
    		{
    			// Parse Journey planning response 
    			jp.parseJourneyPlan(response.getData());
    		}
    		catch (Exception e)
    		{
    			Log.e(BrightSparkActivity.LOG_TAG, "executeJourneyPlan - Excpetion parsing journey plan.");
        		return null;
    		}
    		
    		return jp;
    		
    	}
    	else
    	{
    		Log.e(BrightSparkActivity.LOG_TAG, "executeJourneyPlan - JourneyPlan Error." + response.getCode());
    		return null;
    	}
	}
	
}
