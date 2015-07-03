package ru.obelisk.monitor.database.models.services;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ru.obelisk.monitor.database.models.entity.User;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

public interface UserService {

	@Transactional
	User addUser(User user);
	@Transactional
    void deleteUser(int id);
	@Transactional
	User editUser(User user);
	
    User getUserByName(String name);
    User getUserByUsername(String username);
    User getUserById(int id);
    List<User> getAllUsers();
    
    List<Select2Result> findUserByTerm(String term);
    List<User> findUserWithDatatablesCriterias(DatatablesCriterias criterias);
	Long getFilteredCount(DatatablesCriterias criterias);
	Long getTotalCount();
}