package ru.obelisk.monitor.graph;

<<<<<<< HEAD
public class CalendarNode extends Node {

	private static final long serialVersionUID = -5363734607450797922L;
	
	private String timePeriod;

	public String getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
=======
import ru.obelisk.monitor.database.models.entity.TimeScheduleGroup;

public class CalendarNode extends Node {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2556439294355308762L;
	private TimeScheduleGroup timeScheduleGroup;

	public TimeScheduleGroup getTimeScheduleGroup() {
		return timeScheduleGroup;
	}

	public void setTimeScheduleGroup(TimeScheduleGroup timeScheduleGroup) {
		this.timeScheduleGroup = timeScheduleGroup;
>>>>>>> develop
	}

	@Override
	public String toString() {
<<<<<<< HEAD
		return "CalendarNode [timePeriod=" + timePeriod + "]";
	}
		
	
=======
		return "CalendarNode [timeScheduleGroup=" + timeScheduleGroup + "]";
	}
>>>>>>> develop
}
