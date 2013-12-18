package com.meetme.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public abstract class HttpUtils {

	/*
	 * Private methods
	 */
	private static void handleLogging(Exception e) {
		Log.e(HttpUtils.class.getName(), e.getMessage());
	}
	
	/*
	 * Methods
	 */
	public static JSONObject post(String url, Map<String, String> parameters) {
		String responseString;
		JSONObject responseJSON = null;
		
		// Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(url);
	
	    try {
	        // Add parameters
	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(parameters.size());
	    	
	    	for (Map.Entry<String, String> parameter : parameters.entrySet()) { 
	    		nameValuePairs.add(new BasicNameValuePair(parameter.getKey(), parameter.getValue()));
	    	}
	        
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        
	        // Log request
	        Log.d(HttpUtils.class.getName(), EntityUtils.toString(httppost.getEntity()));
	        
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        responseString = EntityUtils.toString(response.getEntity());
	        
	        // Log response
	        Log.d(HttpUtils.class.getName(), responseString);
	        
	        // Build JSON Object
	        responseJSON = new JSONObject(responseString);
	        
	    } catch (JSONException e) {
	    	handleLogging(e);
		} catch (ClientProtocolException e) {
	    	handleLogging(e);
	    } catch (IOException e) {
	    	handleLogging(e);
	    } catch (Exception e) {
	    	handleLogging(e);
	    }
	    
	    return responseJSON;
	}
}
