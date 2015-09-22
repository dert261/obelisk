package ru.obelisk.monitor.graph;

import ru.obelisk.monitor.database.models.entity.TimeScheduleGroup;

public class CalendarNode extends Node {

	private static final long serialVersionUID = -5363734607450797922L;
	
	private TimeScheduleGroup timeScheduleGroup;

	public TimeScheduleGroup getTimeScheduleGroup() {
		return timeScheduleGroup;
	}

	public void setTimeScheduleGroup(TimeScheduleGroup timeScheduleGroup) {
		this.timeScheduleGroup = timeScheduleGroup;

	}

	@Override
	public String toString() {

		return "CalendarNode [timeScheduleGroup=" + timeScheduleGroup + "]";
	}

}
