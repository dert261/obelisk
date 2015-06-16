package ru.obelisk.monitor.database.models.services.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import ru.obelisk.monitor.database.models.entity.LdapAuthenticationParameters;
import ru.obelisk.monitor.database.models.repository.LdapAuthenticationParametersRepository;
import ru.obelisk.monitor.database.models.services.LdapAuthenticationParametersService;

public class LdapAuthenticationParametersServiceImpl implements LdapAuthenticationParametersService {

	@Autowired
    private LdapAuthenticationParametersRepository ldapAuthParamRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    	
	@Override
	public LdapAuthenticationParameters getParameters() {
		// TODO Auto-generated method stub
		Query query = entityManager.createQuery("SELECT params FROM LdapAuthenticationParameters params");
		return (LdapAuthenticationParameters) query.getSingleResult();
		
		//return ldapAuthParamRepository.getSimultanousLdapParamters();
	}

	@Override
	public LdapAuthenticationParameters editParameters(
			LdapAuthenticationParameters formParameters) {
		
		return ldapAuthParamRepository.saveAndFlush(formParameters);
	}

}
