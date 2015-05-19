package ru.obelisk.monitor.database.models.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.github.dandelion.datatables.core.ajax.ColumnDef;
//import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;

import ru.obelisk.monitor.database.models.entity.User;
import ru.obelisk.monitor.database.models.repository.UserRepository;
//import ru.obelisk.monitor.database.models.service.utils.UserServiceUtils;
import ru.obelisk.monitor.database.models.services.UserService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
 
@Service
public class UserServiceImpl implements UserService {
 
    @Autowired
    private UserRepository userRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
 
    @Override
    public User addUser(User user) {
        User savedUser = userRepository.saveAndFlush(user);
        return savedUser;
    }
 
    @Override
    public void deleteUser(int id) {
        userRepository.delete(id);
    }
 
    @Override
    public User getUserByName(String name) {
        return userRepository.findByName(name);
    }
 
    @Override
    public User editUser(User user) {
        return userRepository.saveAndFlush(user);
    }
 
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

	@Override
	public User getUserById(int id) {
		return userRepository.findOne(id);
	}
	
	/**
	* <p>
	* Query used to populate the DataTables that display the list of persons.
	*
	* @param criterias
	* The DataTables criterias used to filter the persons.
	* (maxResult, filtering, paging, ...)
	* @return a filtered list of persons.
	*/
/*	public List<User> findUserWithDatatablesCriterias(DatatablesCriterias criterias) {
		StringBuilder queryBuilder = new StringBuilder("SELECT u FROM User u");
		
		/**
		* Step 1: global and individual column filtering
		*/
/*		queryBuilder.append(UserServiceUtils.getFilterQuery(criterias));
		
		/**
		* Step 2: sorting
		*/
/*		if (criterias.hasOneSortedColumn()) {
			List<String> orderParams = new ArrayList<String>();
			queryBuilder.append(" ORDER BY ");
			for (ColumnDef columnDef : criterias.getSortingColumnDefs()) {
				orderParams.add("u." + columnDef.getName() + " " + columnDef.getSortDirection());
			}
			Iterator<String> itr2 = orderParams.iterator();
			while (itr2.hasNext()) {
				queryBuilder.append(itr2.next());
				if (itr2.hasNext()) {
					queryBuilder.append(" , ");
				}
			}
		}
		
		TypedQuery<User> query = entityManager.createQuery(queryBuilder.toString(), User.class);
		
		/**
		* Step 3: paging
		*/
/*		query.setFirstResult(criterias.getDisplayStart());
		query.setMaxResults(criterias.getDisplaySize());
		return query.getResultList();
	}
	/**
	* <p>
	* Query used to return the number of filtered persons.
	*
	* @param criterias
	* The DataTables criterias used to filter the persons.
	* (maxResult, filtering, paging, ...)
	* @return the number of filtered persons.
	*/
/*	public Long getFilteredCount(DatatablesCriterias criterias) {
		StringBuilder queryBuilder = new StringBuilder("SELECT u FROM User u");
		queryBuilder.append(UserServiceUtils.getFilterQuery(criterias));
		Query query = entityManager.createQuery(queryBuilder.toString());
		return Long.parseLong(String.valueOf(query.getResultList().size()));
	}
	/**
	* @return the total count of persons.
	*/
/*	public Long getTotalCount() {
		Query query = entityManager.createQuery("SELECT COUNT(u) FROM User u");
		return (Long) query.getSingleResult();
	}
*/	
}