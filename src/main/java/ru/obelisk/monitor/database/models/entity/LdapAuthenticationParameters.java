package ru.obelisk.monitor.database.models.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import ru.obelisk.monitor.web.validators.NotEmpty;

@Entity
@Table(name = "ldap_authentication_parameters")
public class LdapAuthenticationParameters {
 
	    @Id
	    //@GeneratedValue(generator = "increment")
	    //@GenericGenerator(name= "increment", strategy= "increment")
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    @Column(name = "id", length = 11, nullable = false)
	    private Integer id;
	     
	    @Column(name = "distinguished_name", length = 50, nullable = false)
	    @NotNull
	    @NotEmpty
	    private String distinguishedName=null;
	    
	    @Column(name = "password", length = 60)
	    @NotEmpty
	    private String password=null;
	    
	    @Column(name = "active")
	    private boolean active=false;
	        
	    @Column(name = "search_base", length = 1000)
	    private String searchBase=null;
	    
	    @OneToMany(mappedBy = "ldapAuthParams")
	    private Set<LdapAuthenticationServers> ldapServers;
	    	       
	    public LdapAuthenticationParameters(){
	    	
	    }
	    
		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getDistinguishedName() {
			return distinguishedName;
		}

		public void setDistinguishedName(String distinguishedName) {
			this.distinguishedName = distinguishedName;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

		public String getSearchBase() {
			return searchBase;
		}

		public void setSearchBase(String searchBase) {
			this.searchBase = searchBase;
		}

		public Set<LdapAuthenticationServers> getLdapServers() {
			return ldapServers;
		}

		public void setLdapServers(Set<LdapAuthenticationServers> ldapServers) {
			this.ldapServers = ldapServers;
		}

		@Override
		public String toString() {
			return "LdapAuthenticationParametrs [id=" + id
					+ ", distinguishedName=" + distinguishedName
					+ ", password=" + password + ", active=" + active
					+ ", searchBase=" + searchBase + ", ldapServers="
					+ ldapServers + "]";
		}
}
