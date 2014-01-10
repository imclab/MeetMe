package com.meetme.core;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.meetme.protocol.HttpParameters;

public abstract class HttpUtils {

	private HttpUtils() {
	}
	
	/*
	 * Methods
	 */
	public static JSONObject post(String url, HttpParameters parameters) {
		String responseString;
		JSONObject responseJSON = null;
		
		// Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(url);
	
	    try {
	        // Add parameters
	        httppost.setEntity(new UrlEncodedFormEntity(parameters));
	        
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
	    	Log.e(HttpUtils.class.getName(), e.getMessage(), e);
		} catch (ClientProtocolException e) {
			Log.e(HttpUtils.class.getName(), e.getMessage(), e);
	    } catch (IOException e) {
	    	Log.e(HttpUtils.class.getName(), e.getMessage(), e);
	    } catch (Exception e) {
	    	Log.e(HttpUtils.class.getName(), e.getMessage(), e);
	    }
	    
	    return responseJSON;
	}
}
