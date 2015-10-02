package ru.obelisk.monitor.database.models.entity;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import ru.obelisk.monitor.web.validators.FieldMatch;
import ru.obelisk.monitor.web.validators.NotEmpty;
import ru.obelisk.monitor.web.validators.NotNullField;

@Entity
@Table(name = "ldap_authentication_parameters")
@FieldMatch(first = "password", second = "passwordConfirm", groups=LdapAuthenticationParameters.LdapAuthParamsStepTwo.class, message = "field.validation.error.diffpassword")
/*@FieldMatch.List({
    @FieldMatch(first = "password", second = "passwordConfirm", message = "field.validation.error.diffpassword")
    //@FieldMatch(first = "email", second = "confirmEmail", message = "The email fields must match")
})*/

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LdapAuthenticationParameters {
 
		public interface LdapAuthParamsStepOne{}
		public interface LdapAuthParamsStepTwo{}
		
	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    @Column(name = "id", length = 11, nullable = false)
	    private Integer id;
	     
	    @Column(name = "distinguished_name", nullable = false)
	    @NotNullField(groups=LdapAuthenticationParameters.LdapAuthParamsStepOne.class)
	    @NotEmpty(groups=LdapAuthenticationParameters.LdapAuthParamsStepOne.class)
	    private String distinguishedName=null;
	    
	    @Column(name = "password", length = 60)
	    @NotEmpty(groups=LdapAuthenticationParameters.LdapAuthParamsStepOne.class)
	    @NotNullField(groups=LdapAuthenticationParameters.LdapAuthParamsStepOne.class)
	    private String password=null;
	    
	    @Transient
	    @NotEmpty(groups=LdapAuthenticationParameters.LdapAuthParamsStepOne.class)
	    @NotNullField(groups=LdapAuthenticationParameters.LdapAuthParamsStepOne.class)
	    private String passwordConfirm=null;
	    	  
	    @Column(name = "active")
	    private boolean active=false;
	        
	    @Column(name = "search_base")
	    @NotEmpty(groups=LdapAuthenticationParameters.LdapAuthParamsStepOne.class)
	    @NotNullField(groups=LdapAuthenticationParameters.LdapAuthParamsStepOne.class)
	    private String searchBase=null;
	    
	    @OneToMany(fetch=FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	    @JoinColumn(name="ldapAuthParams_id")
	    @Valid
	    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	    private List<LdapAuthenticationServers> ldapServers=new ArrayList<LdapAuthenticationServers>(0);
	    	       
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

		public List<LdapAuthenticationServers> getLdapServers() {
			/*if(ldapServers.isEmpty())
				ldapServers.add(new LdapAuthenticationServers());*/
			return ldapServers;
		}

		public void setLdapServers(List<LdapAuthenticationServers> ldapServers) {
			this.ldapServers = ldapServers;
		}
		
		public String getPasswordConfirm() {
			return passwordConfirm;
		}

		public void setPasswordConfirm(String passwordConfirm) {
			this.passwordConfirm = passwordConfirm;
		}

		public boolean isNew() {
			return (this.id == null);
		}
		
		@Override
		public String toString() {
			return "LdapAuthenticationParameters [id=" + id
					+ ", distinguishedName=" + distinguishedName
					+ ", password=" + password + ", passwordConfirm="
					+ passwordConfirm + ", active=" + active + ", searchBase="
					+ searchBase + ", ldapServers=" + ldapServers + "]";
		}

		
}
