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
@Table(name = "ldap_authentication_servers")
public class LdapAuthenticationServers {
	@Id
    //@GeneratedValue(generator = "increment")
    //@GenericGenerator(name= "increment", strategy= "increment")
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 11, nullable = false)
    private Integer id;
     
    @Column(name = "host", length = 200, nullable = false)
    @NotNull
    @NotEmpty
    private String host=null;
    
    @Column(name = "port", length = 5, nullable = false)
    @NotNull
    @NotEmpty
    private int port;
    
    @Column(name = "use_ssl")
    private boolean useSSL=false;
    
    @ManyToOne
    private LdapAuthenticationParameters ldapAuthParams;
    
    public LdapAuthenticationServers (){
    	
    }

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

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isUseSSL() {
		return useSSL;
	}

	public void setUseSSL(boolean useSSL) {
		this.useSSL = useSSL;
	}

	public LdapAuthenticationParameters getLdapAuthParams() {
		return ldapAuthParams;
	}

	public void setLdapAuthParams(LdapAuthenticationParameters ldapAuthParams) {
		this.ldapAuthParams = ldapAuthParams;
	}

	@Override
	public String toString() {
		return "LdapAuthenticationServers [id=" + id + ", host=" + host
				+ ", port=" + port + ", useSSL=" + useSSL + ", ldapAuthParams="
				+ ldapAuthParams + "]";
	}
}
