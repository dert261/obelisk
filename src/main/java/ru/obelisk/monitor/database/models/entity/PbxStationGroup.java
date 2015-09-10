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
@Table(name = "pbx_station_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PbxStationGroup implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4533626033456904966L;

	@Transient
	@JsonView(value={View.PbxStationGroup.class, View.PbxStation.class, View.Location.class})
    private int numberLocalized;
	
	@JsonView(value={View.PbxStationGroup.class, View.PbxStation.class, View.Location.class})
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 11, nullable = false)
	private Integer id;
	
	@JsonView(value={View.PbxStationGroup.class, View.PbxStation.class, View.Location.class})
	@Column(name = "name", length = 100, nullable = false)
	@NotNull 
	@NotEmpty
	private String name;
		
	@JsonView(value={View.PbxStationGroup.class, View.PbxStation.class, View.Location.class})
	@Column(name = "description", length = 500, nullable = true)
	private String description;
		
	@JsonView(value={View.PbxStationGroup.class})
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(name="pbx_station_group2pbx_station",
    	joinColumns=@JoinColumn(name="tstationgroup_id"),
    	inverseJoinColumns=@JoinColumn(name="tstation_id")
    )
    
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<PbxStation> pbxStations = new ArrayList<PbxStation>();
	
	@JsonView(value={View.PbxStationGroup.class})
	@OneToMany(mappedBy="pbxStationGroup", fetch=FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<Location> locations;
	
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

	public List<PbxStation> getPbxStations() {
		return pbxStations;
	}

	public void setPbxStations(List<PbxStation> pbxStations) {
		this.pbxStations = pbxStations;
	}
	
	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	@Override
	public String toString() {
		return "PbxStationGroup [numberLocalized=" + numberLocalized + ", id="
				+ id + ", name=" + name + ", description=" + description + "]";
	}
}
