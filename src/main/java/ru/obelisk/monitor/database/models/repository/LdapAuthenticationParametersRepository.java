package ru.obelisk.monitor.database.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
import ru.obelisk.monitor.database.models.entity.LdapAuthenticationParameters;

public interface LdapAuthenticationParametersRepository extends JpaRepository<LdapAuthenticationParameters, Integer> {
	/*@Query("select b from LdapAuthenticationParameters b")
	LdapAuthenticationParameters getSimultanousLdapParamters();*/
}
