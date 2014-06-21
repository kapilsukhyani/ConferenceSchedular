package com.enlighten.conferencescheduler.models;

public class Talk {
	private double talkDurationInMins;
	private double talkStartTime;
	private String talkTitle;

	public Talk(double talkDurationInMins, String talkTitle) {
		this.talkDurationInMins = talkDurationInMins;
		this.talkTitle = talkTitle;
	}

	public double getTalkDurationInMins() {
		return talkDurationInMins;
	}

	public double getTalkStartTime() {
		return talkStartTime;
	}

	public void setTalkStartTime(double talkStartTime) {
		this.talkStartTime = talkStartTime;
	}

	public String getTalkTitle() {
		return talkTitle;
	}

	@Override
	public String toString() {
		return "{ " + " start time: " + talkStartTime + ",talk duration: "
				+ talkDurationInMins + ",talk title: " + talkTitle + " }";
	}
}
