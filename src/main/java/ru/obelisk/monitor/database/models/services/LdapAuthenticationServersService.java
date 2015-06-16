package ru.obelisk.monitor.database.models.services;

import ru.obelisk.monitor.database.models.entity.LdapAuthenticationServers;

public interface LdapAuthenticationServersService {

	LdapAuthenticationServers getservers();
	LdapAuthenticationServers editServers(LdapAuthenticationServers servers);
	
}
