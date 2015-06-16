package ru.obelisk.monitor.database.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;
import ru.obelisk.monitor.web.validators.NotEmpty;

@Entity
@Table(name = "user_roles")
public class UserRole {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 11, nullable = false)
    private Integer id;
     
    @Column(name = "role_name", length = 50, nullable = false)
    @NotNull
    @NotEmpty
    private String roleName=null;
    
    @Column(name = "description", length = 200)
    private String description=null;

    @ManyToOne
    private User user;
    
    public UserRole() {
    }

    public UserRole(String roleName, User user) {
        this.roleName = roleName;
        this.user = user;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "UserRole [id=" + id + ", roleName=" + roleName
				+ ", description=" + description + ", user=" + user + "]";
	}
            
}
