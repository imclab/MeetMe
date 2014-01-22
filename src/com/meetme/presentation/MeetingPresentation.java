package com.meetme.presentation;

import static com.meetme.store.UserConfirmationCodeStore.USER_CONFIRMATION_ACCEPTED;
import static com.meetme.store.UserConfirmationCodeStore.USER_CONFIRMATION_DECLINED;
import static com.meetme.store.UserConfirmationCodeStore.USER_CONFIRMATION_INVITED;
import static com.meetme.store.UserConfirmationCodeStore.USER_CONFIRMATION_MAYBE;
import static com.meetme.store.UserStatusCodeStore.USER_STATUS_ARRIVED;
import static com.meetme.store.UserStatusCodeStore.USER_STATUS_LEFT;
import static com.meetme.store.UserStatusCodeStore.USER_STATUS_WAITING;
import static com.meetme.store.UserTravelModeStore.TRAVEL_MODE_BICYCLING;
import static com.meetme.store.UserTravelModeStore.TRAVEL_MODE_DRIVING;
import static com.meetme.store.UserTravelModeStore.TRAVEL_MODE_WALKING;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;

import com.meetme.R;
import com.meetme.model.entity.Friend;
import com.meetme.model.entity.Meet;
import com.meetme.model.entity.Meeting;

public class MeetingPresentation {
	
	private Context context;
	private Meeting meeting;
	private Map<Integer, Set<Meet>> statusMeetMap;
	private Map<Integer, Set<Meet>> confirmationMeetMap;
	private StringBuilder goingString;
	private StringBuilder maybeString;
	private StringBuilder invitedString;
	private StringBuilder declinedString;
	private StringBuilder arrivedString;
	private StringBuilder leftString;
	private StringBuilder waitingString;
	
	
	@SuppressLint("UseSparseArrays")
	public MeetingPresentation(Context context, Meeting meeting, Set<Meet> meetSet) {
		this.context = context;
		this.meeting = meeting;
		this.statusMeetMap = new HashMap<Integer, Set<Meet>>();
		this.confirmationMeetMap = new HashMap<Integer, Set<Meet>>();
		update(meetSet);
	}
	
	/*
	 * Accessors
	 */
	public Meeting getMeeting() {
		return this.meeting;
	}
	
	public int getGoingCount() {
		return confirmationMeetMap.get(USER_CONFIRMATION_ACCEPTED).size();
	}
	
	public int getMaybeCount() {
		return confirmationMeetMap.get(USER_CONFIRMATION_MAYBE).size();
	}
	
	public int getInvitedCount() {
		return confirmationMeetMap.get(USER_CONFIRMATION_INVITED).size();
	}
	
	public int getDeclinedCount() {
		return confirmationMeetMap.get(USER_CONFIRMATION_DECLINED).size();
	}
	
	public int getArrivedCount() {
		return statusMeetMap.get(USER_STATUS_ARRIVED).size();
	}

	public int getLeftCount() {
		return statusMeetMap.get(USER_STATUS_LEFT).size();
	}

	public int getWaitingCount() {
		return statusMeetMap.get(USER_STATUS_WAITING).size();
	}
	
	public String getGoingString() {
		return goingString.toString();
	}
	
	public String getMaybeString() {
		return maybeString.toString();
	}
	
	public String getInvitedString() {
		return invitedString.toString();
	}
	
	public String getDeclinedString() {
		return declinedString.toString();
	}
	
	public String getArrivedString() {
		return arrivedString.toString();
	}

	public String getLeftString() {
		return leftString.toString();
	}

	public String getWaitingString() {
		return waitingString.toString();
	}

	/*
	 * Methods 
	 */
	public String getTravelModeString(int travelModeCode) {
		String travelModeString;
		
		switch (travelModeCode) {
			case TRAVEL_MODE_WALKING : travelModeString = context.getString(R.string.walking);
			break;
			case TRAVEL_MODE_BICYCLING : travelModeString = context.getString(R.string.bicycling);
			break;
			case TRAVEL_MODE_DRIVING : travelModeString = context.getString(R.string.driving);
			break;
			default : travelModeString = context.getString(R.string.walking);
			break;
		}
		
		return travelModeString;
	}
	
	public void update(Set<Meet> meetSet) {
		Set<Meet> goingSet = new HashSet<Meet>();
		Set<Meet> maybeSet = new HashSet<Meet>();
		Set<Meet> invitedSet = new HashSet<Meet>();
		Set<Meet> declinedSet = new HashSet<Meet>();
		
		Set<Meet> arrivedSet = new HashSet<Meet>();
		Set<Meet> leftSet = new HashSet<Meet>();
		Set<Meet> waitingSet = new HashSet<Meet>();
		
		goingString = new StringBuilder();
		maybeString = new StringBuilder();
		invitedString = new StringBuilder();
		declinedString = new StringBuilder();
		arrivedString = new StringBuilder();
		leftString = new StringBuilder();
		waitingString = new StringBuilder();
		
		// update map
		for (Meet meet : meetSet) {
			Friend user = meeting.getFriendById(meet.getUserId());
			
			switch (meet.getUserConfirmation()) {
				case USER_CONFIRMATION_ACCEPTED :
					goingSet.add(meet);
					goingString.append(user).append("\n");
				break;
				case USER_CONFIRMATION_MAYBE :
					maybeSet.add(meet);
					maybeString.append(user).append("\n");
				break;
				case USER_CONFIRMATION_INVITED :
					invitedSet.add(meet);
					invitedString.append(user).append("\n");
				break;
				case USER_CONFIRMATION_DECLINED :
					declinedSet.add(meet);
					declinedString.append(user).append("\n");
				break;
				default : invitedSet.add(meet);
				break;
			}
			
			switch (meet.getUserStatus()) {
				case USER_STATUS_ARRIVED :
					arrivedSet.add(meet);
					arrivedString.append(user).append("\n");
				break;
				case USER_STATUS_LEFT :
					leftSet.add(meet);
					leftString.append(user).append("\n");
					leftString.append(meet.getUserEstimatedDistance()).append("\n");
					leftString.append(meet.getUserEstimatedTime()).append("\n");
					leftString.append(getTravelModeString(meet.getUserTravelMode())).append("\n\n");
				break;
				case USER_STATUS_WAITING :
					waitingSet.add(meet);
					waitingString.append(user).append("\n");
				break;
				default : waitingSet.add(meet);
				break;
			}
		}
		
		confirmationMeetMap.clear();
		confirmationMeetMap.put(USER_CONFIRMATION_ACCEPTED, goingSet);
		confirmationMeetMap.put(USER_CONFIRMATION_MAYBE, maybeSet);
		confirmationMeetMap.put(USER_CONFIRMATION_INVITED, invitedSet);
		confirmationMeetMap.put(USER_CONFIRMATION_DECLINED, declinedSet);
		
		statusMeetMap.clear();
		statusMeetMap.put(USER_STATUS_ARRIVED, arrivedSet);
		statusMeetMap.put(USER_STATUS_LEFT, leftSet);
		statusMeetMap.put(USER_STATUS_WAITING, waitingSet);
	}
}
