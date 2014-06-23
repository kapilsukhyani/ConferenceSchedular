package com.enlighten.conferencescheduler;

import java.util.List;

/**
 * A read only object representing rules for tracks in conference. Assuming all
 * tracks in conference will obey same rule and run in parallel
 * 
 * @author kapil
 * 
 */

public class TrackRule {

	private double trackStartTime;
	private double trackMaxDuration;
	private List<SessionRule> sessions;
	private double maxTalkDuration;
	private int noOfBreaks;

	public TrackRule(List<SessionRule> sessions, double trackStartTime,
			double trackMaxDuration, double maxTalkDuration, int noOfBreaks) {
		this.sessions = sessions;
		this.trackMaxDuration = trackMaxDuration;
		this.trackStartTime = trackStartTime;
		this.maxTalkDuration = maxTalkDuration;
		this.noOfBreaks = noOfBreaks;
	}

	public int getNoOfBreaks() {
		return noOfBreaks;
	}

	public double getMaxTalkDuration() {
		return maxTalkDuration;
	}

	public double getTrackMaxDuration() {
		return trackMaxDuration;
	}

	public List<SessionRule> getSessions() {
		return sessions;
	}

	public double getTrackStartTime() {
		return trackStartTime;
	}

	public static class SessionRule {

		private boolean conferenceBreak;
		private double startTime;
		private double minEndTime;
		private double maxEndTime;

		public SessionRule(double startTime, double minEndTime,
				double maxEndTime, boolean conferenceBreak) {
			this.startTime = startTime;
			this.minEndTime = minEndTime;
			this.maxEndTime = maxEndTime;
			this.conferenceBreak = conferenceBreak;
		}

		public boolean isConferenceBreak() {
			return conferenceBreak;
		}

		public double getMaxEndTime() {
			return maxEndTime;
		}

		public double getMinEndTime() {
			return minEndTime;
		}

		public double getStartTime() {
			return startTime;
		}
	}

}
