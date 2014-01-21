package com.meetme.core.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

/** A class, to download Places from Geocoding webservice */
public class DownloadTask extends AsyncTask<String, Integer, String>
{
    String data = null;
    public GoogleMap mMap;
    
    public DownloadTask(GoogleMap mMap) {
    	this.mMap = mMap;
	}

	private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
 
            // Connecting to url
            urlConnection.connect();
 
            // Reading data from url
            iStream = urlConnection.getInputStream();
 
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
 
            StringBuffer sb = new StringBuffer();
 
            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }
 
            data = sb.toString();
            br.close();
 
        }catch(Exception e){
            Log.e(DownloadTask.class.getName(), e.getMessage(), e);
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
 
        return data;
    }

    // Invoked by execute() method of this object
    @Override
    protected String doInBackground(String... url) {
        try{
            data = downloadUrl(url[0]);
        }catch(Exception e){
        	Log.e(DownloadTask.class.getName(), e.getMessage(), e);
        }
        return data;
    }

    // Executed after the complete execution of doInBackground() method
    @Override
    protected void onPostExecute(String result){

        // Instantiating ParserTask which parses the json data from Geocoding webservice
        // in a non-ui thread
        ParserTask parserTask = new ParserTask(mMap);

        // Start parsing the places in JSON format
        // Invokes the "doInBackground()" method of the class ParseTask
        parserTask.execute(result);
    }
}