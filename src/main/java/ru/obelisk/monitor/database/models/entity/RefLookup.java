package ru.obelisk.monitor.database.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import ru.obelisk.monitor.web.validators.NotEmpty;

@Entity
@Table(name = "ref_lookup")
public class RefLookup {
	 @Id
	 @GeneratedValue(generator = "increment")
	 @GenericGenerator(name= "increment", strategy= "increment")
	 @Column(name = "id", length = 11, nullable = false)
	 private Integer id;
	     
	 @Column(name = "name", length = 50, nullable = false)
	 @NotNull
	 @NotEmpty
	 private String name=null;
	    
	 @Column(name = "code", length = 50)
	 @NotNull
	 private String code=null;
	 
	 @Column(name = "type", length = 50)
	 @NotNull
	 private String type=null;
	 
	 @Column(name = "position")
	 @NotNull
	 @NotEmpty
	 private int position=0;
}
