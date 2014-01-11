package com.meetme.presentation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.meetme.model.entity.Meet;
import static com.meetme.protocol.store.UserStatusCodeStore.*;

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
		Set<Meet> arrivedSet = new TreeSet<Meet>();
		Set<Meet> leftSet = new TreeSet<Meet>();
		Set<Meet> waitingSet = new TreeSet<Meet>();
		
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
