package com.enlighten.conferencescheduler;

import android.app.Application;

import com.enlighten.conferencescheduler.models.Conference;

public class ConferenceSchedulerApplication extends Application {

	private Conference conference;

	@Override
	public void onCreate() {
		super.onCreate();

	}

	public void setConference(Conference conference) {
		this.conference = conference;
	}

	public Conference getConference() {
		return conference;
	}

}
