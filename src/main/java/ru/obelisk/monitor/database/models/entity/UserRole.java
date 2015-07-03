package ru.obelisk.monitor.database.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

//import com.fasterxml.jackson.annotation.JsonIgnore;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import ru.obelisk.monitor.web.validators.NotEmpty;

@Entity
@Table(name = "user_roles")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserRole implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7717786064579079292L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 11, nullable = false)
    private Integer id;
     
    @Column(name = "role_name", length = 50, nullable = false)
    @NotNull
    @NotEmpty
    private String roleName=null;
    
    //@JsonIgnore
    //@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    //private User user;
        
    public UserRole() {
    }

    public UserRole(String roleName, User user) {
        this.roleName = roleName;
        //this.user = user;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/*public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}*/

	@Override
	public String toString() {
		return "UserRole [id=" + id + ", roleName=" + roleName + "]";
	}
            
}
