package ru.obelisk.monitor.database.models.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.obelisk.monitor.database.models.entity.User;
import ru.obelisk.monitor.database.models.repository.UserRepository;
import ru.obelisk.monitor.database.models.service.utils.UserServiceUtils;
import ru.obelisk.monitor.database.models.services.UserService;
import ru.obelisk.monitor.web.security.PasswordCrypto;
import ru.obelisk.monitor.web.ui.datatables.ColumnDef;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

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
    	user.setPass(PasswordCrypto.getInstance().encrypt(user.getPass()));
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
    public User editUser(User formUser) {
    	User user = getUserById(formUser.getId());
		user.setAdGuid(formUser.getAdGuid());
		user.setAdLocation(formUser.getAdLocation());
		user.setCompany(formUser.getCompany());
		user.setDepartment(formUser.getDepartment());
		user.setEmail(formUser.getEmail());
		user.setFname(formUser.getFname());
		user.setMname(formUser.getMname());
		user.setLname(formUser.getLname());
		user.setName(formUser.getLname()+" "+formUser.getFname()+" "+formUser.getMname());
		
		user.setIpAddress(formUser.getIpAddress());
		user.setLocalUserFlag(formUser.getLocalUserFlag());
		user.setLogin(formUser.getLogin());
		user.setMobile(formUser.getMobile());
				
		String oldPass = user.getPass();
		String newPass = formUser.getPass(); 
		if(!oldPass.equals(newPass))
			user.setPass(PasswordCrypto.getInstance().encrypt(formUser.getPass()));
		
		user.setRole(formUser.getRole());
		user.setStatus(formUser.getStatus());
		user.setStreetAddress(formUser.getStreetAddress());
		user.setTitle(formUser.getTitle());
		
		
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
	
	public List<Select2Result> findUserByTerm(String term) {
		
		List<Select2Result> reultList = entityManager.createQuery(
                "SELECT NEW ru.obelisk.monitor.select2.Select2Result(u.id, CONCAT(u.name,' (',u.login,')')) FROM User u" 
                		+ " WHERE "
                        + " u.name LIKE :term"
                        + " OR u.login LIKE :term", Select2Result.class)
        .setParameter("term", "%" + term.toLowerCase() + "%")
        .getResultList();
        return reultList;
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
	public List<User> findUserWithDatatablesCriterias(DatatablesCriterias criterias) {
		StringBuilder queryBuilder = new StringBuilder("SELECT u FROM User u");
		
		/**
		* Step 1: global and individual column filtering
		*/
		queryBuilder.append(UserServiceUtils.getFilterQuery(criterias));
		
		System.out.print(queryBuilder);
		
		/**
		* Step 2: sorting
		*/
		if (criterias.hasOneSortedColumn()) {
			List<String> orderParams = new ArrayList<String>();
			for (ColumnDef columnDef : criterias.getSortingColumnDefs()) {
				String columnName = null;
				if(columnDef.getName().endsWith("Localized")){
					columnName=columnDef.getName().replaceAll("Localized", "");
				} else {
					columnName=columnDef.getName();
				}
				orderParams.add("u." + columnName + " " + columnDef.getSortDirection());
			}
			if(!orderParams.isEmpty()){
				queryBuilder.append(" ORDER BY ");
				Iterator<String> itr2 = orderParams.iterator();
				while (itr2.hasNext()) {
					queryBuilder.append(itr2.next());
					if (itr2.hasNext()) {
						queryBuilder.append(" , ");
					}
				}
			}
		}
		
		TypedQuery<User> query = entityManager.createQuery(queryBuilder.toString(), User.class);
		
		/**
		* Step 3: paging
		*/
		query.setFirstResult(criterias.getStart());
		if(criterias.getLength()>0)
			query.setMaxResults(criterias.getLength());
				
		return idGenerate(query.getResultList(),criterias.getStart());
	}
	
	private List<User> idGenerate(List<User> users, int start){
		
		for(int i=0;i<users.size();i++){
			users.get(i).setNumberLocalized(start+i+1);
		}
		return users;
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
	public Long getFilteredCount(DatatablesCriterias criterias) {
		StringBuilder queryBuilder = new StringBuilder("SELECT u FROM User u");
		queryBuilder.append(UserServiceUtils.getFilterQuery(criterias));
		Query query = entityManager.createQuery(queryBuilder.toString());
		return Long.parseLong(String.valueOf(query.getResultList().size()));
	}
	/**
	* @return the total count of persons.
	*/
	public Long getTotalCount() {
		Query query = entityManager.createQuery("SELECT COUNT(u) FROM User u");
		return (Long) query.getSingleResult();
	}
	
}