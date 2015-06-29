package ru.obelisk.monitor.database.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import ru.obelisk.monitor.web.validators.NotEmpty;

@Entity
@Table(name = "pbx_station")
public class PbxStation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7656616659864458028L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 11, nullable = false)
    private Integer id;
	
	@Column(name = "name", length = 50, nullable = false)
	@NotNull 
	@NotEmpty
	private String name;
	
	@Column(name = "host", length = 100, nullable = false)
	@NotNull 
	@NotEmpty
	private String host;

	@Column(name = "update_flag")
	private boolean updateFlag=false;
	
	@Column(name = "rabbit_queue", length = 200, nullable = false)
	@NotNull 
	@NotEmpty
	private String rabbitQueue;
	
	@Column(name = "reinit_flag")
	private boolean reinitFlag=false;
	
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

	public boolean isNew(){
		return (this.id == null);
	}

	@Override
	public String toString() {
		return "PbxStation [id=" + id + ", name=" + name + ", host=" + host
				+ ", updateFlag=" + updateFlag + ", rabbitQueue=" + rabbitQueue
				+ ", reinitFlag=" + reinitFlag + "]";
	}
}
