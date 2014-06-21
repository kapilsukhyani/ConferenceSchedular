package com.enlighten.conferencescheduler;

import java.util.List;
/**
 * A read only object representing rules for tracks in conference. Assuming all tracks in conference will obey same rule and run in parallel
 * @author kapil
 *
 */

public class TrackRule {

	private double trackStartTime;
	private double trackMaxDuration;
	private List<SessionRule> sessions;
	private List<BreakRule> breaks;

	public TrackRule(List<SessionRule> sessions, List<BreakRule> breaks,
			double trackStartTime, double trackMaxDuration) {
		this.sessions = sessions;
		this.breaks = breaks;
		this.trackMaxDuration = trackMaxDuration;
		this.trackStartTime = trackStartTime;
	}

	public List<BreakRule> getBreaks() {
		return breaks;
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

		private double startTime;
		private double minEndTime;
		private double maxEndTime;

		public SessionRule(double startTime, double minEndTime,
				double maxEndTime) {
			this.startTime = startTime;
			this.minEndTime = minEndTime;
			this.maxEndTime = maxEndTime;
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

	public static class BreakRule {
		private double startTime;
		private double endTime;

		public BreakRule(double startTime, double endTime) {

			this.startTime = startTime;
			this.endTime = endTime;
		}

		public double getEndTime() {
			return endTime;
		}

		public double getStartTime() {
			return startTime;
		}
	}

}
