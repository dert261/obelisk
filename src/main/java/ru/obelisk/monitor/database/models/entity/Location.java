package ru.obelisk.monitor.database.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.fasterxml.jackson.annotation.JsonView;

import ru.obelisk.monitor.database.models.views.View;
import ru.obelisk.monitor.web.validators.NotEmpty;
import ru.obelisk.monitor.web.validators.NotNullField;
import ru.obelisk.monitor.web.validators.NullOrDigit;

@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Location implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2907768187328162953L;

	@JsonView(View.Location.class)
	@Transient
    private int numberLocalized;
		
	@JsonView(View.Location.class)
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 11, nullable = false)
    private Integer id;
	
	@JsonView(View.Location.class)
	@Column(name = "name", length = 50, nullable = false)
	@NotNull 
	@NotEmpty
	private String name;
		
	@JsonView(View.Location.class)
	@Column(name = "record_dir_name", length = 100, nullable = false)
	@NotNull 
	@NotEmpty
	private String recordDirName;
	
	@JsonView(View.Location.class)
	@ManyToOne(fetch=FetchType.LAZY)
	@NotNullField
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private PbxStation pbxStation;
		
	@JsonView(View.Location.class)
	@ManyToOne(fetch=FetchType.LAZY)
	@NotNullField
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private DevicePool devicePool;
	
	@JsonView(View.Location.class)
	@Column(name = "prefix", length = 1, nullable = true)
	@NullOrDigit(message="field.validation.error.nullordigit")
	private String prefix = null;
	
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
	
	public PbxStation getPbxStation() {
		return pbxStation;
	}

	public void setPbxStation(PbxStation pbxStation) {
		this.pbxStation = pbxStation;
	}

	public String getRecordDirName() {
		return recordDirName;
	}

	public void setRecordDirName(String recordDirName) {
		this.recordDirName = recordDirName;
	}

	public DevicePool getDevicePool() {
		return devicePool;
	}

	public void setDevicePool(DevicePool devicePool) {
		this.devicePool = devicePool;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public boolean isNew(){
		return (this.id==null);
	}

	@Override
	public String toString() {
		return "Location [numberLocalized=" + numberLocalized + ", id=" + id
				+ ", name=" + name + ", pbxStation=" + pbxStation
				+ ", recordDirName=" + recordDirName + ", devicePool="
				+ devicePool + ", prefix=" + prefix + "]";
	}
}
