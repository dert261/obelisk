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
import org.hibernate.annotations.SortNatural;

import ru.obelisk.monitor.database.models.views.View;
import ru.obelisk.monitor.web.validators.NotEmpty;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "calling_search_space")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CallingSearchSpace implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2805477308288170889L;

	@Transient
	@JsonView(value={View.CallingSearchSpace.class, View.Partition.class})
	@SortNatural
    private int numberLocalized;
		
	@JsonView(value={View.CallingSearchSpace.class, View.Partition.class})
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 11, nullable = false)
	private Integer id;
	
	@JsonView(value={View.CallingSearchSpace.class, View.Partition.class})
	@Column(name = "name", length = 100, nullable = false)
	@NotNull 
	@NotEmpty 
	@SortNatural
	private String name;
		
	@JsonView(value={View.CallingSearchSpace.class, View.Partition.class})
	@Column(name = "description", length = 500, nullable = true)
	@SortNatural
	private String description;
	
	@JsonView(value={View.CallingSearchSpace.class})
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(name="partition2css",
    	joinColumns=@JoinColumn(name="tcss_id"),
    	inverseJoinColumns=@JoinColumn(name="tpartition_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@SortNatural
	private List<Partition> partitions = new ArrayList<Partition>();

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

	public List<Partition> getPartitions() {
		return partitions;
	}

	public void setPartitions(List<Partition> partitions) {
		this.partitions = partitions;
	}
	
	public boolean isNew(){
		return (this.id==null);
	}

	@Override
	public String toString() {
		return "CallingSearchSpace [numberLocalized=" + numberLocalized
				+ ", id=" + id + ", name=" + name + ", description="
				+ description + "]";
	}
	
}
