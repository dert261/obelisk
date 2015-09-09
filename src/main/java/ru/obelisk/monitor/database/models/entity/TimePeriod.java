package ru.obelisk.monitor.database.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonView;

import ru.obelisk.monitor.database.models.entity.enums.CalendarDays;
import ru.obelisk.monitor.database.models.entity.enums.CalendarMonthDays;
import ru.obelisk.monitor.database.models.entity.enums.CalendarMonths;
import ru.obelisk.monitor.database.models.views.View;
import ru.obelisk.monitor.web.validators.NotEmpty;
import ru.obelisk.monitor.web.validators.NotNullField;
import ru.obelisk.monitor.web.validators.TimePeriodFieldMatch;

@Entity
@Table(name = "time_period")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TimePeriodFieldMatch(groups=TimePeriod.TimePeriodValidationStepTwo.class, message = "field.validation.error.timeperiod")
public class TimePeriod implements Serializable{
	/**
	 * 
	 */
	public interface TimePeriodValidationStepOne{}
	public interface TimePeriodValidationStepTwo{}
	
	private static final long serialVersionUID = -1981251271982227994L;

	@Transient
	@JsonView(value={View.TimePeriod.class})
    private int numberLocalized;
		
	@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class, View.MMTimePeriods.class})
	//@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class})
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 11, nullable = false)
	private Integer id;
	
	@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class, View.MMTimePeriods.class})
	//@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class})
	@Column(name = "name", length = 100, nullable = false)
	@NotNull(groups=TimePeriod.TimePeriodValidationStepOne.class) 
	@NotEmpty(groups=TimePeriod.TimePeriodValidationStepOne.class) 
	private String name;
		
	@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class, View.MMTimePeriods.class})
	//@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class})
	@Column(name = "description", length = 500, nullable = true)
	private String description;
	
	@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class, View.MMTimePeriods.class})
	//@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class})
	@Column(name = "time_start", length = 10, nullable = false)
	@NotNull(groups=TimePeriod.TimePeriodValidationStepOne.class) 
	@NotEmpty(groups=TimePeriod.TimePeriodValidationStepOne.class) 
	private String timeStart;
	
	@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class, View.MMTimePeriods.class})
	//@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class})
	@Column(name = "time_stop", length = 10, nullable = false)
	@NotNull(groups=TimePeriod.TimePeriodValidationStepOne.class)  
	@NotEmpty(groups=TimePeriod.TimePeriodValidationStepOne.class) 
	private String timeStop;
	
	@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class, View.MMTimePeriods.class})
	//@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class})
	@Column(name = "weekday_start", nullable = true)
	@Enumerated(EnumType.ORDINAL)
	private CalendarDays weekdayStart=CalendarDays.NOTSELECT;
	
	@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class, View.MMTimePeriods.class})
	//@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class})
	@Column(name = "weekday_stop", nullable = true)
	@Enumerated(EnumType.ORDINAL)
	private CalendarDays weekdayStop=CalendarDays.NOTSELECT;
	
	@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class, View.MMTimePeriods.class})
	//@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class})
	@Column(name = "month_start", nullable = true)
	@Enumerated(EnumType.ORDINAL)
	private CalendarMonths monthStart=CalendarMonths.NOTSELECT;
	
	@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class, View.MMTimePeriods.class})
	//@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class})
	@Column(name = "month_stop", nullable = true)
	@Enumerated(EnumType.ORDINAL)
	private CalendarMonths monthStop=CalendarMonths.NOTSELECT;
	
	@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class, View.MMTimePeriods.class})
	//@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class})
	@Column(name = "month_day_start", nullable = true)
	@Enumerated(EnumType.ORDINAL)
	private CalendarMonthDays monthDayStart=CalendarMonthDays.NOTSELECT;
	
	@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class, View.MMTimePeriods.class})
	//@JsonView(value={View.TimePeriod.class, View.TimeScheduleGroup.class})
	@Column(name = "month_day_stop", nullable = true)
	@Enumerated(EnumType.ORDINAL)
	private CalendarMonthDays monthDayStop=CalendarMonthDays.NOTSELECT;
	
	@JsonView(value={View.TimePeriod.class})
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(name="time_schedule_group2time_period",
    	joinColumns=@JoinColumn(name="tperiod_id"),
    	inverseJoinColumns=@JoinColumn(name="tshed_id")
    )
    @NotNullField(groups=TimePeriod.TimePeriodValidationStepOne.class)  
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<TimeScheduleGroup> timeScheduleGroup = new ArrayList<TimeScheduleGroup>();
			
	public boolean isNew(){
		return (this.id==null);
	}

	public int getNumberLocalized() {
		return numberLocalized;
	}

	public void setNumberLocalized(int numberLocalized) {
		this.numberLocalized = numberLocalized;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}

	public String getTimeStop() {
		return timeStop;
	}

	public void setTimeStop(String timeStop) {
		this.timeStop = timeStop;
	}

	public CalendarDays getWeekdayStart() {
		return weekdayStart;
	}

	public void setWeekdayStart(CalendarDays weekdayStart) {
		this.weekdayStart = weekdayStart;
	}

	public CalendarDays getWeekdayStop() {
		return weekdayStop;
	}

	public void setWeekdayStop(CalendarDays weekdayStop) {
		this.weekdayStop = weekdayStop;
	}

	public CalendarMonths getMonthStart() {
		return monthStart;
	}

	public void setMonthStart(CalendarMonths monthStart) {
		this.monthStart = monthStart;
	}

	public CalendarMonths getMonthStop() {
		return monthStop;
	}

	public void setMonthStop(CalendarMonths monthStop) {
		this.monthStop = monthStop;
	}
	
	public List<TimeScheduleGroup> getTimeScheduleGroup() {
		return timeScheduleGroup;
	}

	public void setTimeScheduleGroup(List<TimeScheduleGroup> timeScheduleGroup) {
		this.timeScheduleGroup = timeScheduleGroup;
	}

	public CalendarMonthDays getMonthDayStart() {
		return monthDayStart;
	}

	public void setMonthDayStart(CalendarMonthDays monthDayStart) {
		this.monthDayStart = monthDayStart;
	}

	public CalendarMonthDays getMonthDayStop() {
		return monthDayStop;
	}

	public void setMonthDayStop(CalendarMonthDays monthDayStop) {
		this.monthDayStop = monthDayStop;
	}

	@Override
	public String toString() {
		return "TimePeriod [numberLocalized=" + numberLocalized + ", id=" + id
				+ ", name=" + name + ", description=" + description
				+ ", timeStart=" + timeStart + ", timeStop=" + timeStop
				+ ", weekdayStart=" + weekdayStart + ", weekdayStop="
				+ weekdayStop + ", monthStart=" + monthStart
				+ ", monthDayStart=" + monthDayStart + ", monthStop="
				+ monthStop + ", monthDayStop=" + monthDayStop + "]";
	}

	
}
