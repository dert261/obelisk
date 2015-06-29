package ru.obelisk.monitor.database.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import ru.obelisk.monitor.web.validators.NotEmpty;

@Entity
@Table(name = "ldap_authentication_servers")

public class LdapAuthenticationServers {
	
	public interface LdapAuthServersValid{}
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 11, nullable = false)
    private Integer id;
     
    public LdapAuthenticationParameters getLdapAuthParams() {
		return ldapAuthParams;
	}

	@Column(name = "host", length = 200, nullable = false)
    @NotNull
    @NotEmpty
    private String host = null;
    
    @Column(name = "port", length = 5, nullable = false)
    @Min(value = 0, message = "field.validation.error.min")
    @Max(value = 65535, message = "field.validation.error.max")
    private String port;
    
    @Column(name = "use_ssl")
    private boolean useSSL = false;
    
    @ManyToOne
    private LdapAuthenticationParameters ldapAuthParams;
    
    public LdapAuthenticationServers (){
    	
    }
    
    /*public LdapAuthenticationServers (String host, int port, boolean useSSL){
    	this.host = host;
    	this.port = port;
    	this.useSSL = useSSL;
    }*/

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public boolean isUseSSL() {
		return useSSL;
	}

	public void setUseSSL(boolean useSSL) {
		this.useSSL = useSSL;
	}

	@Override
	public String toString() {
		return "LdapAuthenticationServers [id=" + id + ", host=" + host
				+ ", port=" + port + ", useSSL=" + useSSL + "]";
	}
}
