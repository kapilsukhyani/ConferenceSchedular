package com.enlighten.conferencescheduler.models;

import java.util.List;

public class Conference {
	private List<Track> tracks;
	private double conferenceDurationInMins;
	private double conferenceStartTime;

	public Conference(List<Track> tracks, double conferenceDurationInMins,
			double conferenceStartTime) {
		this.tracks = tracks;
		this.conferenceDurationInMins = conferenceDurationInMins;
		this.conferenceStartTime = conferenceStartTime;
	}

	public double getConferenceDurationInMins() {
		return conferenceDurationInMins;
	}

	public double getConferenceStartTime() {
		return conferenceStartTime;
	}

	public List<Track> getTracks() {
		return tracks;
	}

	@Override
	public String toString() {
		return "{ " + tracks.toString() + "conference start time: "
				+ conferenceStartTime + ",conference duration: "
				+ conferenceDurationInMins + " }";
	}
}
