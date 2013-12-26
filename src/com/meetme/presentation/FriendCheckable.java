package com.meetme.presentation;

import com.meetme.model.entity.Friend;

/*
 * Used to display a friend in a friend list with multiple choice
 */
public class FriendCheckable extends Friend {
	
	private boolean selected = false;

	public FriendCheckable(Friend friend) {
		super(friend.getId(), friend.getFirstname(), friend.getLastname());
		this.selected = false;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
