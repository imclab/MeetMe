package com.meetme.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.meetme.R;
import com.meetme.core.HttpUtils;
import com.meetme.model.entity.Meeting;
import com.meetme.protocol.HttpParameters;
import com.meetme.protocol.ServerUrlStore;

public class MainActivity extends Activity {

	private Set<Meeting> meetingSet;
	private List<String> meetingAdapterList;
	private ListView meetingListView;
	private Button newMeetingButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		meetingListView = (ListView)findViewById(R.id.meetingList);
		newMeetingButton = (Button)findViewById(R.id.newMeetingButton);
		newMeetingButton.setOnClickListener(newMeetingListener);
		
		meetingSet = new HashSet<Meeting>();
		meetingAdapterList = new ArrayList<String>();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, meetingAdapterList);
		
		meetingListView.setAdapter(adapter);		
		
		getMeetingSet();
		updateMeetingList();
	}
	
	/*
	 * Private methods
	 */
	
	/***
	 * Retrieve meeting list from server and fill the Set
	 */
	private void getMeetingSet() {
		JSONObject responseJSON = null;
		String url = ServerUrlStore.MEETING_URL;
		HttpParameters parameters = new HttpParameters();
		
		// Add parameters
		parameters.put("operation", "list");
		parameters.put("token", "usr52ac94a27877f4.60168588");
		
		// Send request
		responseJSON = HttpUtils.post(url, parameters);
		
		// Built meeting list from JSON response
		try {
			JSONArray meetingsJSON = (JSONArray)responseJSON.get("meetings");
			int meetingsSize = meetingsJSON.length();
			
			for (int i = 0; i < meetingsSize; i++) {
				JSONObject meetingJSON = meetingsJSON.getJSONObject(i);
				meetingSet.add(Meeting.getFromJSON(meetingJSON));
			}
			
		} catch (JSONException e) {
			Log.e(MainActivity.class.getName(), e.getMessage());
		} catch (Exception e) {
			Log.e(MainActivity.class.getName(), e.getMessage());
		}
	}
	
	/*
	 * Update the meeting list adapter data
	 */
	private void updateMeetingList() {
		meetingAdapterList.clear();
		
		for (Meeting meeting : meetingSet) {
			meetingAdapterList.add(meeting.getTitle() + "\n" + meeting.getDatetime());
		}
	}
	
	private void newMeeting() {
		
	}
	
	private OnClickListener newMeetingListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			newMeeting();
	    }
	};
	
	
}
