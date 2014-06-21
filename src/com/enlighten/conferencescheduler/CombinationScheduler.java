package com.enlighten.conferencescheduler;

import java.util.ArrayList;
import java.util.List;

import com.enlighten.conferencescheduler.TrackRule.SessionRule;
import com.enlighten.conferencescheduler.models.Conference;
import com.enlighten.conferencescheduler.models.Session;
import com.enlighten.conferencescheduler.models.Talk;
import com.enlighten.conferencescheduler.models.Track;
import com.enlighten.conferencescheduler.utils.Utils;

class CombinationScheduler extends Scheduler {

	public CombinationScheduler(TrackRule rule) {
		super(rule);
	}

	/**
	 * Schedules session by calculating all possible combination of talks and
	 * see which fits best for a particular session
	 */
	@Override
	public Conference scheduleTalks(List<Talk> talkList) {

		List<SessionRule> sessionRules = trackRule.getSessions();
		List<Talk> talks = new ArrayList<Talk>(talkList);
		int sessionRuleIndex = 0;
		List<Session> scheduledSessions = new ArrayList<Session>();

		// TODO sort session rules first to schedule early sessions first, as of
		// now assuming that track rules defines session rules in the order of
		// sessions only

		// run till all the talks are not scheduled
		while (!talks.isEmpty()) {
			SessionRule sessionRule = sessionRules.get(sessionRuleIndex);

			double minSessionDurationInMins = (sessionRule.getMinEndTime() - sessionRule
					.getStartTime()) * Constants.HOUR_TO_MIN_MULTIPLIER;
			double maxSessionDuration = (sessionRule.getMaxEndTime() - sessionRule
					.getStartTime()) * Constants.HOUR_TO_MIN_MULTIPLIER;
			double sessionDurationInMin = maxSessionDuration;

			// if all talks can be covered in minSessionDurationInMins
			if (calculateTalksDuration(talks) <= minSessionDurationInMins) {
				sessionDurationInMin = minSessionDurationInMins;

			}

			// session duration left after assigning a combination of talks to
			// it
			double minSessionDurationLeft = sessionDurationInMin;
			// index array of talks which can most optimally be fit in a session
			int[] optimumTalksIndexArray = null;

			int[] indexArray = new int[talks.size()];
			for (int i = 0; i < talks.size(); i++) {
				indexArray[i] = i;
			}

			// flag indication the best case, when session is completely
			// occupied by a combination of talks
			boolean optimumTalksIndexArrayFound = false;

			// select all combination of talks from 1..talks.size() and check
			// which combiantion of talk fit best in a session
			for (int selectedNoOfItems = 1; selectedNoOfItems <= talks.size(); selectedNoOfItems++) {

				List<int[]> indexCombinations = Utils.combinations(
						indexArray.length, selectedNoOfItems, indexArray, 0);

				for (int[] combination : indexCombinations) {
					// total duration of a combination of talks
					double talkCombinationDuration = calculateTalksDuration(
							talks, combination);

					// record the combination if it is more optimum for this
					// session
					if (talkCombinationDuration <= sessionDurationInMin
							&& (sessionDurationInMin - talkCombinationDuration) < minSessionDurationLeft) {
						optimumTalksIndexArray = combination;
						minSessionDurationLeft = sessionDurationInMin
								- talkCombinationDuration;
						// break loop if most optimum combination of talks if
						// found
						if (minSessionDurationLeft == 0) {
							optimumTalksIndexArrayFound = true;
							break;
						}
					}
				}

				// stop finding combinations of larger sub group of tasks if
				// optimum solution is already found
				if (optimumTalksIndexArrayFound) {
					break;
				}
			}

			// It may happen that there is no talk with duration less than equal
			// to the session defined by current session rule
			if (null != optimumTalksIndexArray) {
				List<Talk> scheduledTalks = new ArrayList<Talk>(
						optimumTalksIndexArray.length);
				for (int index = 0; index < optimumTalksIndexArray.length; index++) {
					scheduledTalks
							.add(talks.get(optimumTalksIndexArray[index]));
				}
				// removes the scheduled talks
				talks.removeAll(scheduledTalks);

				scheduledSessions.add(new Session(scheduledTalks,
						sessionDurationInMin, sessionRule.getStartTime()));
			}

			if (sessionRuleIndex < sessionRules.size() - 1) {
				sessionRuleIndex++;
			} else {
				sessionRuleIndex = 0;
			}

		}

		List<Track> tracks = getTracks(scheduledSessions);
		Conference conference = new Conference(tracks,
				trackRule.getTrackMaxDuration()
						* Constants.HOUR_TO_MIN_MULTIPLIER,
				trackRule.getTrackStartTime());

		return conference;
	}

	/**
	 * Schedule sessions in group of tracks according to track rules
	 * 
	 * @param sessions
	 * @return
	 */
	private List<Track> getTracks(List<Session> sessions) {
		List<Session> sessionListCopy = new ArrayList<Session>(sessions);
		List<Track> tracks = new ArrayList<Track>();
		int maxNoOfSessionsInTrack = trackRule.getSessions().size();
		List<Session> sessionsInTrack;
		while (!sessionListCopy.isEmpty()) {

			sessionsInTrack = new ArrayList<Session>();
			for (int sessionIndex = 0; sessionIndex < maxNoOfSessionsInTrack; sessionIndex++) {
				if (sessionListCopy.isEmpty()) {
					break;
				} else {
					sessionsInTrack.add(sessionListCopy.get(0));
					sessionListCopy.remove(0);
				}
			}
			tracks.add(new Track(sessionsInTrack));

		}

		return tracks;
	}

	/**
	 * Calculates the total duration of a group of talks
	 * 
	 * @param talks
	 * @return Toatal duration of all given talks
	 */
	private double calculateTalksDuration(List<Talk> talks) {
		double result = 0;
		for (Talk talk : talks) {
			result += talk.getTalkDurationInMins();

		}
		return result;
	}

	/**
	 * Calculates total duration of sub group of talks
	 * 
	 * @param talks
	 * @param indexes
	 * @return
	 */
	private double calculateTalksDuration(List<Talk> talks, int[] indexes) {
		double result = 0;
		for (int index = 0; index < indexes.length; index++) {
			result += talks.get(indexes[index]).getTalkDurationInMins();
		}
		return result;
	}
}
