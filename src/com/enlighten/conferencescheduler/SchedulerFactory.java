package com.enlighten.conferencescheduler;

/**
 * A scheduler factory, added as more algorithm can be implemented as scheduler
 * 
 * @author kapil
 * 
 */
public class SchedulerFactory {

	private static SchedulerFactory schedulerFactory;

	private SchedulerFactory() {

	}

	public static SchedulerFactory getFactory() {
		if (null == schedulerFactory) {
			schedulerFactory = new SchedulerFactory();
		}

		return schedulerFactory;
	}

	public static enum SchedulerType {
		CombinationScheduler
	}

	public Scheduler getScheduler(SchedulerType type, TrackRule rule) {
		Scheduler result = null;
		switch (type) {
		case CombinationScheduler:
			result = new CombinationScheduler(rule);

			break;
		}
		return result;

	}

}
