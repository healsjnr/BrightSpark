package com.healsjnr.brightspark.test.mock;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.json.JSONObject;

import android.util.Log;

import com.healsjnr.brightspark.lib.General;
import com.healsjnr.brightspark.lib.network.IRestUtil;
import com.healsjnr.brightspark.lib.network.QueryParam;
import com.healsjnr.brightspark.lib.network.RestResponse;

public class MockRestUtil implements IRestUtil {
	
	public final static String ERROR_RESPONSE_NULL_FIELDS_URL = "http://errorResponse_NullFields";
	public final static String ERROR_RESPONSE_VALID_FIELDS_URL = "http://errorResponse_ValidFields";
	
	public final static String VALID_RESPONSE_URL = "http://validResponse";
	public final static String VALID_RESPONSE_NULL_CONTENT_URL = "http://validResponse_NullContent";
	
		
	
	@Override
	public String BuildRestURL(String baseUrl, List<QueryParam> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestResponse DoHttpGet(String url) {
		
		String testCaseType = url;
		String testCase = "";
		
		if (url.contains(";"))
		{
			String[] testCaseStrings = url.split(";");
			if (testCaseStrings.length != 2)
			{
				Log.e(this.getClass().getName(), "DoHttpGet - Invalid test case URL: " + url);
			}
			
			testCaseType = testCaseStrings[0];
			testCase = testCaseStrings[1];
			
		}
		
		RestResponse response = new RestResponse();
		if(testCaseType.equals(ERROR_RESPONSE_NULL_FIELDS_URL))
		{
			response.setData(null);
			response.setMessage(null);
			response.setSuccessful(false);
			return response;
		}
		
		if(testCaseType.equals(ERROR_RESPONSE_VALID_FIELDS_URL))
		{
			response.setCode(404);
			response.setData(null);
			response.setMessage("Error: " + ERROR_RESPONSE_VALID_FIELDS_URL);
			response.setSuccessful(false);
			return response;
		}
		
		if(testCaseType.equals(VALID_RESPONSE_URL))
		{
			String dataString = getContentFromFile(testCase);
			
			response.setCode(200);
			response.setData(dataString);
			response.setMessage("OK");
			response.setSuccessful(true);
			return response;
		}
		
		if(testCaseType.equals(VALID_RESPONSE_NULL_CONTENT_URL))
		{
			response.setCode(200);
			response.setData(null);
			response.setMessage("OK");
			response.setSuccessful(true);
			return response;
		}
				
		return null;
	}

	@Override
	public RestResponse DoHttpPut(String url, JSONObject jsonData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestResponse DoHttpPost(String url, JSONObject jsonData) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String getContentFromFile(String filePath)
	{
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream(filePath);
		
		String dataString;
		try {
			dataString = General.convertInputStreamToString(stream);
		} catch (IOException e) {
			dataString = ""; 
		}
		
		return dataString;
	}
	
}

