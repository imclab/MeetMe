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
import com.meetme.model.entity.FriendInviteNotification;

public class FriendInviteNotificationListArrayAdapter 
	extends ArrayAdapter<FriendInviteNotification> {
	
	private List<FriendInviteNotification> list;
	private final Activity context;
	
	static class ViewHolder {
		protected TextView friendName;
	}
	
	public FriendInviteNotificationListArrayAdapter(Activity context, List<FriendInviteNotification> list) {
		super(context, R.layout.notifications_friend_invite_item, list);
	    this.context = context;
	    this.list = new ArrayList<FriendInviteNotification>(list);
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		
		if (rowView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			rowView = inflator.inflate(R.layout.notifications_friend_invite_item, null);
			ViewHolder viewHolder = new ViewHolder();
			
			viewHolder.friendName = (TextView) rowView.findViewById(R.id.friendName);
			rowView.setTag(viewHolder);
		} 
	    
		FriendInviteNotification meetingNotification = list.get(position);
		ViewHolder holder = (ViewHolder) rowView.getTag();
		
		StringBuilder friendNameBuilder = new StringBuilder();
		friendNameBuilder.append(meetingNotification.getInviterFirstname()); 
		friendNameBuilder.append(" ");
		friendNameBuilder.append(meetingNotification.getInviterLastname());
		
		holder.friendName.setText(friendNameBuilder.toString());
	    
	    return rowView;
	}
	
	/*
	 * Accessors
	 */
	public List<FriendInviteNotification> getFriendInviteNotificationList() {
		return this.list;
	}
}

