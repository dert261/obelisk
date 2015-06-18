package ru.obelisk.monitor.database.models.services.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.obelisk.monitor.database.models.entity.UserRole;
import ru.obelisk.monitor.database.models.repository.UserRoleRepository;
import ru.obelisk.monitor.database.models.services.UserRoleService;

@Service
public class UserRoleServiceImpl implements UserRoleService {
	@Autowired
    private UserRoleRepository userRoleRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

	@Override
	public List<UserRole> getAllUserRoles() {
		// TODO Auto-generated method stub
		return userRoleRepository.findAll();
	}
}
