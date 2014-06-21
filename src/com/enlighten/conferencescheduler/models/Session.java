package com.enlighten.conferencescheduler.models;

import java.util.List;

public class Session {
	private List<Talk> talks;
	private double sessionDurationInMins;
	private double sessionStartTime;

	public Session(List<Talk> talks, double sessionDurationInMins,
			double sessionStartTime) {

		this.talks = talks;
		this.sessionStartTime = sessionStartTime;
		this.sessionDurationInMins = sessionDurationInMins;
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
		return "{ " + talks.toString() + " session start time: "
				+ sessionStartTime + ",session duration:  "
				+ sessionDurationInMins + " }";
	}
}
