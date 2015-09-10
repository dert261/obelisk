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

import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonView;

import ru.obelisk.monitor.database.models.views.View;
import ru.obelisk.monitor.web.validators.NotEmpty;

@Entity
@Table(name = "pbx_station")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PbxStation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7656616659864458028L;
	
	@JsonView(value={View.PbxStation.class, View.Location.class, View.PbxStationGroup.class})
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 11, nullable = false)
    private Integer id;
	
	@JsonView(value={View.PbxStation.class, View.Location.class, View.PbxStationGroup.class})
	@Column(name = "name", length = 50, nullable = false)
	@NotNull 
	@NotEmpty
	private String name;
	
	@JsonView(value={View.PbxStation.class, View.Location.class, View.PbxStationGroup.class})
	@Column(name = "host", length = 100, nullable = false)
	@NotNull 
	@NotEmpty
	private String host;

	@JsonView(value={View.PbxStation.class, View.Location.class, View.PbxStationGroup.class})
	@Column(name = "update_flag")
	private boolean updateFlag=false;
	
	@JsonView(value={View.PbxStation.class, View.Location.class, View.PbxStationGroup.class})
	@Column(name = "rabbit_queue", length = 200, nullable = false)
	@NotNull 
	@NotEmpty
	private String rabbitQueue;
	
	@JsonView(value={View.PbxStation.class, View.Location.class, View.PbxStationGroup.class})
	@Column(name = "reinit_flag")
	private boolean reinitFlag=false;
	
	@JsonView(value={View.PbxStation.class})
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(name="pbx_station_group2pbx_station",
    	joinColumns=@JoinColumn(name="tstation_id"),
    	inverseJoinColumns=@JoinColumn(name="tstationgroup_id")
    )
    //@NotNullField(groups=TimePeriod.TimePeriodValidationStepOne.class)  
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<PbxStationGroup> pbxStationGroup = new ArrayList<PbxStationGroup>();
	
	@JsonView(value={View.PbxStation.class, View.Location.class, View.PbxStationGroup.class})
	@Transient
    private int numberLocalized;
	
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

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(boolean updateFlag) {
		this.updateFlag = updateFlag;
	}

	public String getRabbitQueue() {
		return rabbitQueue;
	}

	public void setRabbitQueue(String rabbitQueue) {
		this.rabbitQueue = rabbitQueue;
	}

	public boolean isReinitFlag() {
		return reinitFlag;
	}

	public void setReinitFlag(boolean reinitFlag) {
		this.reinitFlag = reinitFlag;
	}

	public int getNumberLocalized() {
		return numberLocalized;
	}

	public void setNumberLocalized(int numberLocalized) {
		this.numberLocalized = numberLocalized;
	}

	public List<PbxStationGroup> getPbxStationGroup() {
		return pbxStationGroup;
	}

	public void setPbxStationGroup(List<PbxStationGroup> pbxStationGroup) {
		this.pbxStationGroup = pbxStationGroup;
	}

	public boolean isNew(){
		return (this.id == null);
	}

	@Override
	public String toString() {
		return "PbxStation [id=" + id + ", name=" + name + ", host=" + host
				+ ", updateFlag=" + updateFlag + ", rabbitQueue=" + rabbitQueue
				+ ", reinitFlag=" + reinitFlag + ", pbxStationGroup="
				+ pbxStationGroup + ", numberLocalized=" + numberLocalized
				+ "]";
	}

	
}
