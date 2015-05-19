package ru.obelisk.monitor.database.models.services;

import java.util.List;




//import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;

import ru.obelisk.monitor.database.models.entity.User;

public interface UserService {

	User addUser(User user);
    void deleteUser(int id);
    User getUserByName(String name);
    User getUserById(int id);
    User editUser(User user);
    List<User> getAllUsers();
    
    /**
	* <p>
	* Query used to populate the DataTables that display the list of persons.
	*
	* @param criterias
	* The DataTables criterias used to filter the persons.
	* (maxResult, filtering, paging, ...)
	* @return a filtered list of persons.
	*/
//	List<User> findUserWithDatatablesCriterias(DatatablesCriterias criterias);
	
	//List<User> findUserWithDatatablesCriterias(ru.obelisk.monitor.datatables.DatatablesCriterias criterias);
	
	/**
	* <p>
	* Query used to return the number of filtered persons.
	*
	* @param criterias
	* The DataTables criterias used to filter the persons.
	* (maxResult, filtering, paging, ...)
	* @return the number of filtered persons.
	*/
//	Long getFilteredCount(DatatablesCriterias criterias);
	/**
	* @return the total count of persons.
	*/
//	Long getTotalCount();
}