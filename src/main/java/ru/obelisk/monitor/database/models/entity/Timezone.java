package ru.obelisk.monitor.database.models.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import ru.obelisk.monitor.web.validators.NotEmpty;

@Entity
@Table(name = "timezone")
public class Timezone implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -50275105118785822L;

	@Transient
    private int numberLocalized;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 11, nullable = false)
    private Integer id;
	
	@Column(name = "name", length = 50, nullable = false)
	@NotNull 
	@NotEmpty
	private String name;
	
	@OneToMany(mappedBy="timezone")
	private List<DevicePool> devicePools;
	
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

	/*public List<DevicePool> getDevicePools() {
		return devicePools;
	}

	public void setDevicePools(List<DevicePool> devicePools) {
		this.devicePools = devicePools;
	}*/

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Timezone [numberLocalized=" + numberLocalized + ", id=" + id
				+ ", name=" + name + ", description=" + description + "]";
	}

	
	
}
