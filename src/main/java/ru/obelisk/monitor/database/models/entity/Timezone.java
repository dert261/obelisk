package ru.obelisk.monitor.database.models.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "timezone")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Timezone implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -50275105118785822L;

	@Transient
	@JsonView(value={View.Timezone.class})
    private int numberLocalized;
	
	@JsonView(value={View.Timezone.class, View.DevicePool.class})
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 11, nullable = false)
    private Integer id;
	
	@JsonView(value={View.Timezone.class, View.DevicePool.class})
	@Column(name = "name", length = 50, nullable = false)
	@NotNull 
	@NotEmpty
	private String name;
	
	@JsonView(value={View.Timezone.class})
	@OneToMany(mappedBy="timezone", fetch=FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<DevicePool> devicePools;
	
	@JsonView(value={View.Timezone.class})
	@OneToMany(mappedBy="timezone", fetch=FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<Partition> partitions;
	
	@JsonView(value={View.Timezone.class, View.DevicePool.class})
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

	public List<DevicePool> getDevicePools() {
		return devicePools;
	}

	public void setDevicePools(List<DevicePool> devicePools) {
		this.devicePools = devicePools;
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
		return "Timezone [numberLocalized=" + numberLocalized + ", id=" + id
				+ ", name=" + name + ", description=" + description + "]";
	}

	
	
}
