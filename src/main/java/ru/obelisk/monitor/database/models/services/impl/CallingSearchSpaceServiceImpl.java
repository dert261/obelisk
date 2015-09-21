package ru.obelisk.monitor.database.models.services.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.obelisk.monitor.database.models.entity.CallingSearchSpace;
import ru.obelisk.monitor.database.models.repository.CallingSearchSpaceRepository;
import ru.obelisk.monitor.database.models.services.CallingSearchSpaceService;
import ru.obelisk.monitor.database.models.services.utils.CallingSearchSpaceServiceUtils;
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
import javax.transaction.Transactional;
 
@Service
public class CallingSearchSpaceServiceImpl implements CallingSearchSpaceService {
 
    @Autowired
    private CallingSearchSpaceRepository cssRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    @Transactional
    public CallingSearchSpace addCallingSearchSpace(CallingSearchSpace css) {
    	CallingSearchSpace savedCallingSearchSpace = cssRepository.saveAndFlush(css);
        return savedCallingSearchSpace;
    }
 
    @Override
    @Transactional
    public void deleteCallingSearchSpace(int id) {
    	cssRepository.delete(id);
    }
    
    @Override
    public CallingSearchSpace getCallingSearchSpaceByName(String name) {
    	return cssRepository.findByName(name);
    }
 
    @Override
    @Transactional
    public CallingSearchSpace editCallingSearchSpace(CallingSearchSpace formCallingSearchSpace) {
    	CallingSearchSpace css = new CallingSearchSpace();
    	BeanUtils.copyProperties(formCallingSearchSpace, css);
    	return cssRepository.saveAndFlush(css);
    }
 
    @Override
    public List<CallingSearchSpace> getAllCallingSearchSpaces() {
    	return cssRepository.findAll();
    }

	@Override
	public CallingSearchSpace getCallingSearchSpaceById(int id) {
		return cssRepository.findOne(id);
	}
	
	public List<Select2Result> findCallingSearchSpaceByTerm(String term) {
		
		List<Select2Result> resultList = entityManager.createQuery(
                "SELECT NEW ru.obelisk.monitor.web.ui.select2.Select2Result(css.id, css.name) FROM CallingSearchSpace css" 
                		+ " WHERE "
                        + " css.name LIKE :term", Select2Result.class)
        .setParameter("term", "%" + term.toLowerCase() + "%")
        .setHint("org.hibernate.cacheable", true)
        .getResultList();
        return resultList;
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
	@Override
	public List<CallingSearchSpace> findCallingSearchSpaceWithDatatablesCriterias(DatatablesCriterias criterias) {
		StringBuilder queryBuilder = new StringBuilder("SELECT css FROM CallingSearchSpace css");
		
		/**
		* Step 1: global and individual column filtering
		*/
		queryBuilder.append(CallingSearchSpaceServiceUtils.getFilterQuery(criterias));
				
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
				orderParams.add("css." + columnName + " " + columnDef.getSortDirection());
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
		
		TypedQuery<CallingSearchSpace> query = entityManager.createQuery(queryBuilder.toString(), CallingSearchSpace.class);
		
		/**
		* Step 3: paging
		*/
		query.setFirstResult(criterias.getStart());
		if(criterias.getLength()>0)
			query.setMaxResults(criterias.getLength());
		query.setHint("org.hibernate.cacheable", true);
		return idGenerate(query.getResultList(),criterias.getStart());
	}
	
	private List<CallingSearchSpace> idGenerate(List<CallingSearchSpace> CallingSearchSpaces, int start){
		
		for(int i=0;i<CallingSearchSpaces.size();i++){
			CallingSearchSpaces.get(i).setNumberLocalized(start+i+1);
		}
		return CallingSearchSpaces;
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
		StringBuilder queryBuilder = new StringBuilder("SELECT css FROM CallingSearchSpace css");
		queryBuilder.append(CallingSearchSpaceServiceUtils.getFilterQuery(criterias));
		Query query = entityManager.createQuery(queryBuilder.toString());
		query.setHint("org.hibernate.cacheable", true);
		return Long.parseLong(String.valueOf(query.getResultList().size()));
	}
	/**
	* @return the total count of persons.
	*/
	public Long getTotalCount() {
		Query query = entityManager.createQuery("SELECT COUNT(css) FROM CallingSearchSpace css");
		query.setHint("org.hibernate.cacheable", true);
		return (Long) query.getSingleResult();
	}
}