package com.meetme.presentation;

import static com.meetme.store.UserConfirmationCodeStore.USER_CONFIRMATION_ACCEPTED;
import static com.meetme.store.UserConfirmationCodeStore.USER_CONFIRMATION_DECLINED;
import static com.meetme.store.UserConfirmationCodeStore.USER_CONFIRMATION_INVITED;
import static com.meetme.store.UserConfirmationCodeStore.USER_CONFIRMATION_MAYBE;
import static com.meetme.store.UserStatusCodeStore.USER_STATUS_ARRIVED;
import static com.meetme.store.UserStatusCodeStore.USER_STATUS_LEFT;
import static com.meetme.store.UserStatusCodeStore.USER_STATUS_WAITING;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.meetme.model.entity.Friend;
import com.meetme.model.entity.Meet;
import com.meetme.model.entity.Meeting;

public class MeetingPresentation {
	
	private Meeting meeting;
	private Map<Integer, Set<Meet>> statusMeetMap;
	private Map<Integer, Set<Meet>> confirmationMeetMap;
	StringBuilder goingString;
	StringBuilder maybeString;
	StringBuilder invitedString;
	StringBuilder declinedString;
	StringBuilder arrivedString;
	StringBuilder leftString;
	StringBuilder waitingString;
	
	
	public MeetingPresentation(Meeting meeting, Set<Meet> meetSet) {
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
				case USER_CONFIRMATION_ACCEPTED : {
					goingSet.add(meet);
					goingString.append(user + "\n");
					break;
				}
				
				case USER_CONFIRMATION_MAYBE : {
					maybeSet.add(meet);
					maybeString.append(user + "\n");
					break;
				}
				
				case USER_CONFIRMATION_INVITED : {
					invitedSet.add(meet);
					invitedString.append(user + "\n");
					break;
				}
				
				case USER_CONFIRMATION_DECLINED : {
					declinedSet.add(meet);
					declinedString.append(user + "\n");
					break;
				}
				default : invitedSet.add(meet);
				break;
			}
			
			switch (meet.getUserStatus()) {
				case USER_STATUS_ARRIVED : {
					arrivedSet.add(meet);
					arrivedString.append(user + "\n");
				}
				break;
				case USER_STATUS_LEFT : {
					leftSet.add(meet);
					leftString.append(user + "\n");
				}
				break;
				case USER_STATUS_WAITING : {
					waitingSet.add(meet);
					waitingString.append(user + "\n");
				}
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
