package com.enlighten.conferencescheduler.models;

import java.util.List;

public class Session {
	private List<Talk> talks;
	private double sessionDurationInMins;
	private double sessionStartTime;
	private boolean conferenceBreak;

	public Session(List<Talk> talks, double sessionDurationInMins,
			double sessionStartTime, boolean conferenceBreak) {

		this.talks = talks;
		this.sessionStartTime = sessionStartTime;
		this.sessionDurationInMins = sessionDurationInMins;
		this.conferenceBreak = conferenceBreak;
	}

	public boolean isConferenceBreak() {
		return conferenceBreak;
	}

	public double getSessionDurationInMins() {
		return sessionDurationInMins;
	}

	public double getSessionStartTime() {
		return sessionStartTime;
	}

	public List<Talk> getTalks() {
		return talks;
	}

	@Override
	public String toString() {
		if (null != talks) {
			return "{ " + talks.toString() + " session start time: "
					+ sessionStartTime + ",session duration:  "
					+ sessionDurationInMins + " }";
		} else {
			return "{ Break break start time: " + sessionStartTime
					+ ",break duration:  " + sessionDurationInMins + " }";
		}
	}
}
