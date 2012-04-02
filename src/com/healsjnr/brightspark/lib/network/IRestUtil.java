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

import com.healsjnr.brightspark.BrightSparkActivity;

public interface IRestUtil {
	
	public String BuildRestURL(String baseUrl, List<QueryParam> parameters);
	
	public RestResponse DoHttpGet(String url);
	
	public RestResponse DoHttpPut(String url, JSONObject jsonData);

	public RestResponse DoHttpPost(String url, JSONObject jsonData);

}
