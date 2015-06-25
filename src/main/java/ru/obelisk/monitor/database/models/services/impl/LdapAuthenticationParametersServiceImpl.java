package ru.obelisk.monitor.database.models.services.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.obelisk.monitor.database.models.entity.LdapAuthenticationParameters;
import ru.obelisk.monitor.database.models.repository.LdapAuthenticationParametersRepository;
import ru.obelisk.monitor.database.models.services.LdapAuthenticationParametersService;

@Service
public class LdapAuthenticationParametersServiceImpl implements LdapAuthenticationParametersService {

	@Autowired
    private LdapAuthenticationParametersRepository ldapAuthParamRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    	
	@Override
	public LdapAuthenticationParameters getParameters() {
		
		// TODO Auto-generated method stub
		Query query = entityManager.createQuery("SELECT params FROM LdapAuthenticationParameters params");
		LdapAuthenticationParameters params = null;
		
		try{
			params = (LdapAuthenticationParameters) query.getSingleResult();
			params.setPasswordConfirm(params.getPassword());
		}catch (NoResultException nre){}
		
		if(params == null){
			params = new LdapAuthenticationParameters(); 
		}
		return params;
		//return ldapAuthParamRepository.getSimultanousLdapParamters();
	}

	@Override
	public LdapAuthenticationParameters editParameters(
			LdapAuthenticationParameters formParameters) {
		if(formParameters.isNew()){
			return ldapAuthParamRepository.saveAndFlush(formParameters);
		}
		LdapAuthenticationParameters params = ldapAuthParamRepository.findOne(formParameters.getId());
		params.setActive(formParameters.isActive());
		params.setDistinguishedName(formParameters.getDistinguishedName());
		params.setPassword(formParameters.getPassword());
		params.setSearchBase(formParameters.getSearchBase());
		params.setLdapServers(formParameters.getLdapServers());
		System.out.println(params.getLdapServers());
		return ldapAuthParamRepository.saveAndFlush(params);
	}

}
