package com.meetme.presentation;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.meetme.R;
import com.meetme.model.entity.Meeting;

public class MeetingListArrayAdapter extends ArrayAdapter<Meeting> implements Filterable {
	
	private List<Meeting> list;
	private final Activity context;
	
	static class ViewHolder {
		protected TextView meetingTitle;
		protected TextView meetingLocation;
		protected TextView meetingDateTime;
	}
	
	public MeetingListArrayAdapter(Activity context, List<Meeting> list) {
		super(context, R.layout.meetings_meeting_item, list);
	    this.context = context;
	    this.list = new ArrayList<Meeting>(list);
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		
		if (rowView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			rowView = inflator.inflate(R.layout.meetings_meeting_item, null);
			ViewHolder viewHolder = new ViewHolder();
			
			viewHolder.meetingTitle = (TextView) rowView.findViewById(R.id.meetingTitle);
			viewHolder.meetingLocation = (TextView) rowView.findViewById(R.id.meetingLocation);
			viewHolder.meetingDateTime = (TextView) rowView.findViewById(R.id.meetingDateTime);
			rowView.setTag(viewHolder);
		} 
	    
		Meeting meeting = list.get(position);
		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.meetingTitle.setText(meeting.getTitle());
		holder.meetingLocation.setText(meeting.getLocationText());
	    holder.meetingDateTime.setText(meeting.getDateTime());
	    
	    return rowView;
	}
	
	/*
	 * Accessors
	 */
	public List<Meeting> getMeetingList() {
		return this.list;
	}
}

