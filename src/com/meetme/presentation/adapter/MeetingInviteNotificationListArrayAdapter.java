package com.meetme.presentation.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.meetme.R;
import com.meetme.core.DateUtils;
import com.meetme.model.entity.MeetingInviteNotification;

public class MeetingInviteNotificationListArrayAdapter 
	extends ArrayAdapter<MeetingInviteNotification> {
	
	private List<MeetingInviteNotification> list;
	private final Activity context;
	
	static class ViewHolder {
		protected TextView meetingTitle;
		protected TextView meetingLocation;
		protected TextView meetingDateTime;
	}
	
	public MeetingInviteNotificationListArrayAdapter(Activity context, List<MeetingInviteNotification> list) {
		super(context, R.layout.notifications_meeting_invite_item, list);
	    this.context = context;
	    this.list = new ArrayList<MeetingInviteNotification>(list);
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		
		if (rowView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			rowView = inflator.inflate(R.layout.notifications_meeting_invite_item, null);
			ViewHolder viewHolder = new ViewHolder();
			
			viewHolder.meetingTitle = (TextView) rowView.findViewById(R.id.meetingTitle);
			viewHolder.meetingLocation = (TextView) rowView.findViewById(R.id.meetingLocation);
			viewHolder.meetingDateTime = (TextView) rowView.findViewById(R.id.meetingDateTime);
			rowView.setTag(viewHolder);
		} 
	    
		MeetingInviteNotification meetingNotification = list.get(position);
		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.meetingTitle.setText(meetingNotification.getMeetingTitle());
		holder.meetingLocation.setText(meetingNotification.getMeetingLocationText());
	    holder.meetingDateTime.setText(DateUtils.formatDateTime(meetingNotification.getMeetingDateTime()));
	    
	    return rowView;
	}
	
	/*
	 * Accessors
	 */
	public List<MeetingInviteNotification> getMeetingInviteNotificationList() {
		return this.list;
	}
}

