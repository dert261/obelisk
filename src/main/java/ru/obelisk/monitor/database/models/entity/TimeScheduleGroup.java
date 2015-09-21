package ru.obelisk.monitor.database.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonView;

import ru.obelisk.monitor.database.models.views.View;
import ru.obelisk.monitor.web.validators.NotEmpty;

@Entity
@Table(name = "time_schedule_groups")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TimeScheduleGroup implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 806374255537431879L;

	@Transient
	@JsonView(value={View.TimeScheduleGroup.class, View.TimePeriod.class})
    private int numberLocalized;
		
	@JsonView(value={View.TimeScheduleGroup.class, View.TimePeriod.class})
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 11, nullable = false)
	private Integer id;
	
	@JsonView(value={View.TimeScheduleGroup.class, View.TimePeriod.class})
	@Column(name = "name", length = 100, nullable = false)
	@NotNull 
	@NotEmpty
	private String name;
		
	@JsonView(value={View.TimeScheduleGroup.class, View.TimePeriod.class})
	@Column(name = "description", length = 500, nullable = true)
	private String description;
		
	
	@JsonView(value={View.TimeScheduleGroup.class})
	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(name="time_schedule_group2time_period",
    	joinColumns=@JoinColumn(name="tshed_id"),
    	inverseJoinColumns=@JoinColumn(name="tperiod_id")
    )
		    
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<TimePeriod> timePeriods = new ArrayList<TimePeriod>();
	
	@JsonView(value={View.TimeScheduleGroup.class})
	@OneToMany(mappedBy="timeScheduleGroup", fetch=FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<Partition> partitions;
	
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

	public List<TimePeriod> getTimePeriods() {
		return timePeriods;
	}

	public void setTimePeriods(List<TimePeriod> timePeriods) {
		this.timePeriods = timePeriods;
	}

	@Override
	public String toString() {
		return "TimeScheduleGroup [numberLocalized=" + numberLocalized
				+ ", id=" + id + ", name=" + name + ", description="
				+ description + ", timePeriods=" + timePeriods + "]";
	}
	
}
