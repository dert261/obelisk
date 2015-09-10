package ru.obelisk.monitor.database.models.entity.utils;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import ru.obelisk.monitor.database.models.entity.TimePeriod;
import ru.obelisk.monitor.database.models.views.View;

public class MMTimePeriods{		//For Many-to-Many view operation
	
	@JsonView(View.MMTimePeriods.class)
	private List<TimePeriod> availableTimePeriods = new ArrayList<TimePeriod>();
	@JsonView(View.MMTimePeriods.class)
	private List<TimePeriod> selectedTimePeriods = new ArrayList<TimePeriod>();
	public List<TimePeriod> getAvailableTimePeriods() {
		return availableTimePeriods;
	}
	public void setAvailableTimePeriods(List<TimePeriod> availableTimePeriods) {
		this.availableTimePeriods = availableTimePeriods;
	}
	public List<TimePeriod> getSelectedTimePeriods() {
		return selectedTimePeriods;
	}
	public void setSelectedTimePeriods(List<TimePeriod> selectedTimePeriods) {
		this.selectedTimePeriods = selectedTimePeriods;
	}
	
	@Override
	public String toString() {
		return "MMTimePeriods [availableTimePeriods=" + availableTimePeriods
				+ ", selectedTimePeriods=" + selectedTimePeriods + "]";
	}
	
}