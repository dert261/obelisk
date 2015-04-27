package ru.obelisk.monitor.database.models.entity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
 
@Entity
@Table(name = "bank")
public class Bank {
 
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name= "increment", strategy= "increment")
    @Column(name = "id", length = 6, nullable = false)
    private long id;
 
    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;
 
    public Bank() {
    }
 
    public Bank(String name) {
        this.name = name;
    }
 
    public long getId() {
        return id;
    }
 
    public void setId(long id) {
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
}