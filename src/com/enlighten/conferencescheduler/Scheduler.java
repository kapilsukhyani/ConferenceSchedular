package com.enlighten.conferencescheduler;

import java.util.List;

import com.enlighten.conferencescheduler.models.Conference;
import com.enlighten.conferencescheduler.models.Talk;

public abstract class Scheduler {

	protected TrackRule trackRule;

	public Scheduler(TrackRule rule) {
		this.trackRule = rule;
	}

	public abstract Conference scheduleTalks(List<Talk> talks);

}
