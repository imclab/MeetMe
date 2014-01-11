package com.meetme.presentation;

import static com.meetme.protocol.store.UserStatusCodeStore.USER_STATUS_ARRIVED;
import static com.meetme.protocol.store.UserStatusCodeStore.USER_STATUS_LEFT;
import static com.meetme.protocol.store.UserStatusCodeStore.USER_STATUS_WAITING;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.meetme.model.entity.Meet;

public class MeetPresentation {
	
	private Map<Integer, Set<Meet>> meetMap;
	StringBuilder arrivedString;
	StringBuilder leftString;
	StringBuilder waitingString;
	
	public MeetPresentation(Set<Meet> meetSet) {
		meetMap = new HashMap<Integer, Set<Meet>>();
		update(meetSet);
	}

	/*
	 * Accessors
	 */
	public int getArrivedCount() {
		return meetMap.get(USER_STATUS_ARRIVED).size();
	}

	public int getLeftCount() {
		return meetMap.get(USER_STATUS_LEFT).size();
	}

	public int getWaitingCount() {
		return meetMap.get(USER_STATUS_WAITING).size();
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
		Set<Meet> arrivedSet = new HashSet<Meet>();
		Set<Meet> leftSet = new HashSet<Meet>();
		Set<Meet> waitingSet = new HashSet<Meet>();
		
		arrivedString = new StringBuilder();
		leftString = new StringBuilder();
		waitingString = new StringBuilder();
		
		// update map
		
		for (Meet meet : meetSet) {
			switch (meet.getUserStatus()) {
				case USER_STATUS_ARRIVED : {
					arrivedSet.add(meet);
					arrivedString.append(meet.getUserId() + "\n");
				}
				break;
				case USER_STATUS_LEFT : {
					leftSet.add(meet);
					leftString.append(meet.getUserId() + "\n");
				}
				break;
				case USER_STATUS_WAITING : {
					waitingSet.add(meet);
					waitingString.append(meet.getUserId() + "\n");
				}
				break;
				default : waitingSet.add(meet);
				break;
			}
		}
		
		meetMap.clear();
		meetMap.put(USER_STATUS_ARRIVED, arrivedSet);
		meetMap.put(USER_STATUS_LEFT, leftSet);
		meetMap.put(USER_STATUS_WAITING, waitingSet);
	}
}
