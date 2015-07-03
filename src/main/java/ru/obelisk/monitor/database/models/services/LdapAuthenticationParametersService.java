package ru.obelisk.monitor.database.models.services;

import org.springframework.transaction.annotation.Transactional;

import ru.obelisk.monitor.database.models.entity.LdapAuthenticationParameters;

public interface LdapAuthenticationParametersService {

		LdapAuthenticationParameters getParameters();
		
		@Transactional
		LdapAuthenticationParameters editParameters(LdapAuthenticationParameters parameters);
}
