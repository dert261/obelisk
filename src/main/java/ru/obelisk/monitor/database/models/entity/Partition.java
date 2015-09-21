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
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;



import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SortNatural;



import com.fasterxml.jackson.annotation.JsonView;

import ru.obelisk.monitor.database.models.views.View;
import ru.obelisk.monitor.web.validators.NotEmpty;


@Entity
@Table(name = "partition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Partition implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1981251271982227994L;

	@Transient
	@JsonView(value={View.Partition.class, View.CallingSearchSpace.class})
    private int numberLocalized;
		
	@JsonView(value={View.Partition.class, View.CallingSearchSpace.class})
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 11, nullable = false)
	@OrderBy
	private Integer id;
	
	@JsonView(value={View.Partition.class, View.CallingSearchSpace.class})
	@Column(name = "name", length = 100, nullable = false)
	@NotNull 
	@NotEmpty
	@SortNatural
	private String name;
		
	@JsonView(value={View.Partition.class, View.CallingSearchSpace.class})
	@Column(name = "description", length = 500, nullable = true)
	@SortNatural
	private String description;
	
	@JsonView(value={View.Partition.class})	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(name="partition2css",
    	joinColumns=@JoinColumn(name="tpartition_id"),
    	inverseJoinColumns=@JoinColumn(name="tcss_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@SortNatural
	private List<CallingSearchSpace> css = new ArrayList<CallingSearchSpace>();
	
	@JsonView(value={View.Partition.class})
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_timezone")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@SortNatural
	private Timezone timezone; 
	
	@JsonView(value={View.Partition.class})
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_timeschedgroup")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@SortNatural
	private TimeScheduleGroup timeScheduleGroup;

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

	public List<CallingSearchSpace> getCss() {
		return css;
	}

	public void setCss(List<CallingSearchSpace> css) {
		this.css = css;
	}

	public Timezone getTimezone() {
		return timezone;
	}

	public void setTimezone(Timezone timezone) {
		this.timezone = timezone;
	}

	public TimeScheduleGroup getTimeScheduleGroup() {
		return timeScheduleGroup;
	}

	public void setTimeScheduleGroup(TimeScheduleGroup timeScheduleGroup) {
		this.timeScheduleGroup = timeScheduleGroup;
	}
	
	public boolean isNew(){
		return (this.id==null);
	}

	@Override
	public String toString() {
		return "Partition [numberLocalized=" + numberLocalized + ", id=" + id
				+ ", name=" + name + ", description=" + description + "]";
	} 
}
