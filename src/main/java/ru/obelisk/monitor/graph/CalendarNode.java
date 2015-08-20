package ru.obelisk.monitor.graph;

public class CalendarNode extends Node {

	private static final long serialVersionUID = -5363734607450797922L;
	
	private String timePeriod;

	public String getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}

	@Override
	public String toString() {
		return "CalendarNode [timePeriod=" + timePeriod + "]";
	}
		
	
}
