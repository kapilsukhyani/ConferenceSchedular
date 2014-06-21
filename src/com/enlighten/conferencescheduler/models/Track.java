package com.enlighten.conferencescheduler.models;

import java.util.List;

public class Track {

	private List<Session> sessions;

	public Track(List<Session> sessions) {
		this.sessions = sessions;
	}

	public List<Session> getSessions() {
		return sessions;
	}

	@Override
	public String toString() {
		return "{ " + sessions.toString() + " }";
	}

}
