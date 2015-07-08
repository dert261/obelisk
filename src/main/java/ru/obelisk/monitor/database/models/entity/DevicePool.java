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
import javax.persistence.ManyToOne;
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
@Table(name = "device_pool")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DevicePool implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1981251271982227994L;

	@Transient
	@JsonView(value={View.DevicePool.class})
    private int numberLocalized;
		
	@JsonView(value={View.Location.class, View.DevicePool.class})
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 11, nullable = false)
	//@JsonView(View.DevicePool.class)
    private Integer id;
	
	@JsonView(value={View.Location.class, View.DevicePool.class})
	@Column(name = "name", length = 50, nullable = false)
	@NotNull 
	@NotEmpty
	private String name;
		
	@JsonView(value={View.DevicePool.class})
	@ManyToOne(fetch=FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Timezone timezone; 
	
	@JsonView(value={View.DevicePool.class})
	@OneToMany(mappedBy="devicePool", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<Location> locations = new ArrayList<Location>();

	@JsonView(value={View.DevicePool.class})
	@Column(name = "description", length = 500, nullable = true)
	private String description;
	
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

	public Timezone getTimezone() {
		return timezone;
	}

	public void setTimezone(Timezone timezone) {
		this.timezone = timezone;
	}

	public List<Location> getLocation() {
		return locations;
	}

	public void setLocation(List<Location> locations) {
		this.locations = locations;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isNew(){
		return (this.id==null);
	}

	@Override
	public String toString() {
		return "DevicePool [numberLocalized=" + numberLocalized + ", id=" + id
				+ ", name=" + name + ", timezone=" + timezone
				+ ", description=" + description + "]";
	}
}
