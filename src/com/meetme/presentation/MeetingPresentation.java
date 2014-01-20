package com.meetme.presentation;

import com.meetme.model.entity.Friend;
import com.meetme.model.entity.Meeting;

public class MeetingPresentation {
	
	private Meeting meeting;
	private Friend meetingHost;
	
	/**
	 * 
	 * @param meeting 
	 * @param meetingHost 
	 */
	public MeetingPresentation(Meeting meeting, Friend meetingHost) {
		super();
		this.meeting = meeting;
		this.meetingHost = meetingHost;
	}

	/*
	 * Accessors
	 */
	public Meeting getMeeting() {
		return meeting;
	}

	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}

	public Friend getMeetingHost() {
		return meetingHost;
	}

	public void setMeetingHost(Friend meetingHost) {
		this.meetingHost = meetingHost;
	}
	
	
}
