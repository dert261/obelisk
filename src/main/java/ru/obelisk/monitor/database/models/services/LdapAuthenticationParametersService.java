package ru.obelisk.monitor.database.models.services;

import ru.obelisk.monitor.database.models.entity.LdapAuthenticationParameters;

public interface LdapAuthenticationParametersService {

		LdapAuthenticationParameters getParameters();
		LdapAuthenticationParameters editParameters(LdapAuthenticationParameters parameters);
}
