package com.enlighten.conferenceschedular;

import java.util.List;

public class TrackRule {

	private double trackStartTime;
	private double trackDuration;
	private List<SessionRule> sessions;
	private List<BreakRule> breaks;

	public static class SessionRule {

		private double startTime;
		private double minEndTime;
		private double maxEndTime;
	}

	public static class BreakRule {
		private double startTime;
		private double endTime;
	}

}
