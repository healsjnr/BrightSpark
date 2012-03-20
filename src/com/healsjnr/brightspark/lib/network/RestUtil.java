package com.healsjnr.brightspark.lib.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

import com.healsjnr.brightspark.ApplicationState;
import com.healsjnr.brightspark.BrightSparkActivity;

public class RestUtil {
	public static String BuildRestURL(String baseUrl, List<QueryParam> parameters)
	{
    	if (parameters == null || parameters.size() == 0)
    	{
    		return baseUrl;
    	}
    	
    	if (baseUrl.endsWith("/"))
    	{
    		baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
    	}
    	
    	String queryString = baseUrl + "?"; 
    	
    	for(QueryParam param : parameters)
    	{
    		if (queryString.endsWith("?"))
    		{
    			queryString = queryString + param.getName() + "=" + param.getValue();
    		}
    		else
    		{
    			queryString = queryString + "&" + param.getName() + "=" + param.getValue();
    		}
    	}
    	
    	queryString = queryString.replace(" ", "%20");
    	
    	return queryString;

	}
	
	public static RestResponse DoHttpGet(String url)
	{
		HttpGet httpget = new HttpGet(url);
		return DoRESTQuery(httpget);
	}
	
	public static RestResponse DoHttpPut(String url, JSONObject jsonData)
	{
		HttpPut httpPut = new HttpPut(url);
		if (jsonData == null)
		{
			return DoRESTQuery(httpPut);
		}
		try
		{
			StringEntity se = new StringEntity(jsonData.toString());
			httpPut.setEntity(se);
			return DoRESTQuery(httpPut);
		} 
		catch (UnsupportedEncodingException e)
		{
			Log.e(BrightSparkActivity.LOG_TAG, "DoHttpPost - Unsupported encoding found during Rest PUT.", e);
    	}
    	
		RestResponse response = new RestResponse();
		response.setMessage("Error Encoding JSON Object");
    	response.setSuccessful(false);
    	
    	return response;
	}

	public static RestResponse DoHttpPost(String url, JSONObject jsonData)
	{
		HttpPost httpPost = new HttpPost(url);
		if (jsonData == null)
		{
			return DoRESTQuery(httpPost);
		}
		
		try
		{
			StringEntity se = new StringEntity(jsonData.toString());
			httpPost.setEntity(se);
			httpPost.setHeader("Content-Type","text/json;charset=UTF-8");
			return DoRESTQuery(httpPost);
		} 
		catch (UnsupportedEncodingException e)
		{
			Log.e(BrightSparkActivity.LOG_TAG, "DoHttpPost - Unsupported encoding found during Rest POST.", e);
    	}
    	
		RestResponse response = new RestResponse();
		response.setMessage("Error Encoding JSON Object");
    	response.setSuccessful(false);
    	
    	return response;
	}
	
	private static RestResponse DoRESTQuery(HttpUriRequest request)  
    {
		RestResponse response = new RestResponse();
		
    	HttpClient httpClient = new DefaultHttpClient();
       	HttpResponse httpResponse;
    	
    	try
    	{
    		Log.i("queryRESTurl", "Performing Rest Query:");
    		httpResponse = httpClient.execute(request);
    		response.setCode(httpResponse.getStatusLine().getStatusCode());
    		response.setMessage(httpResponse.getStatusLine().getReasonPhrase());
    		
    		Log.i("queryRESTurl", "Status:[" + httpResponse.getStatusLine().toString() + "]"); 
    		
    		HttpEntity entity = httpResponse.getEntity();
    		
    		if (entity != null)
    		{
    			InputStream instream = entity.getContent();
    			response.setData(ConvertStreamToString(instream));
	   			instream.close();
    			response.setSuccessful(true);
    			return response;
    		}
    		
    	} catch (ClientProtocolException e)
    	{
    		Log.e(BrightSparkActivity.LOG_TAG, "DoRESTQuery - There was a protocol based error", e);
    		if (httpClient.getConnectionManager() != null)
    		{
    			httpClient.getConnectionManager().shutdown();
    		}
    	} catch (IOException e)
    	{
    		Log.e(BrightSparkActivity.LOG_TAG, "DoRESTQuery - There was an IO Stream related error", e);
    		if (httpClient.getConnectionManager() != null)
    		{
    			httpClient.getConnectionManager().shutdown();
    		}
    	} catch (Exception e)
    	{
    		Log.e(BrightSparkActivity.LOG_TAG, "DoRESTQuery - Unknown Exception", e);
    		if (httpClient.getConnectionManager() != null)
    		{
    			httpClient.getConnectionManager().shutdown();
    		}
    		
    	}
    	
    	response.setSuccessful(false);
    	
    	return response;
    
    } 
	
	// #TODO: should this throw exceptions rather than swallowing them?
    private static String ConvertStreamToString(InputStream is) throws IOException {
   	 
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return sb.toString();
    }
}
