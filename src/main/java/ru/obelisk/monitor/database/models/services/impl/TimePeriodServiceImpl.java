package ru.obelisk.monitor.database.models.services.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.obelisk.monitor.database.models.entity.TimePeriod;
import ru.obelisk.monitor.database.models.repository.TimePeriodRepository;
import ru.obelisk.monitor.database.models.services.TimePeriodService;
import ru.obelisk.monitor.database.models.services.utils.TimePeriodServiceUtils;
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
public class TimePeriodServiceImpl implements TimePeriodService {
 
    @Autowired
    private TimePeriodRepository TimePeriodRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    @Transactional
    public TimePeriod addTimePeriod(TimePeriod TimePeriod) {
    	TimePeriod savedTimePeriod = TimePeriodRepository.saveAndFlush(TimePeriod);
        return savedTimePeriod;
    }
 
    @Override
    @Transactional
    public void deleteTimePeriod(int id) {
    	TimePeriodRepository.delete(id);
    }
    
    @Override
    public TimePeriod getTimePeriodByName(String name) {
        return TimePeriodRepository.findByName(name);
    }
 
    @Override
    @Transactional
    public TimePeriod editTimePeriod(TimePeriod formTimePeriod) {
    	TimePeriod TimePeriod = new TimePeriod();
    	BeanUtils.copyProperties(formTimePeriod, TimePeriod);
    	return TimePeriodRepository.saveAndFlush(TimePeriod);
    }
 
    @Override
    public List<TimePeriod> getAllTimePeriods() {
        return TimePeriodRepository.findAll();
    }

	@Override
	public TimePeriod getTimePeriodById(int id) {
		return TimePeriodRepository.findOne(id);
	}
	
	public List<Select2Result> findTimePeriodByTerm(String term) {
		
		List<Select2Result> resultList = entityManager.createQuery(
                "SELECT NEW ru.obelisk.monitor.web.ui.select2.Select2Result(tperiod.id, tperiod.name) FROM TimePeriod tperiod" 
                		+ " WHERE "
                        + " tperiod.name LIKE :term", Select2Result.class)
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
	public List<TimePeriod> findTimePeriodWithDatatablesCriterias(DatatablesCriterias criterias) {
		StringBuilder queryBuilder = new StringBuilder("SELECT tperiod FROM TimePeriod tperiod");
		
		/**
		* Step 1: global and individual column filtering
		*/
		queryBuilder.append(TimePeriodServiceUtils.getFilterQuery(criterias));
				
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
				orderParams.add("tperiod." + columnName + " " + columnDef.getSortDirection());
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
		
		TypedQuery<TimePeriod> query = entityManager.createQuery(queryBuilder.toString(), TimePeriod.class);
		
		/**
		* Step 3: paging
		*/
		query.setFirstResult(criterias.getStart());
		if(criterias.getLength()>0)
			query.setMaxResults(criterias.getLength());
		query.setHint("org.hibernate.cacheable", true);
		return idGenerate(query.getResultList(),criterias.getStart());
	}
	
	private List<TimePeriod> idGenerate(List<TimePeriod> TimePeriods, int start){
		
		for(int i=0;i<TimePeriods.size();i++){
			TimePeriods.get(i).setNumberLocalized(start+i+1);
		}
		return TimePeriods;
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
		StringBuilder queryBuilder = new StringBuilder("SELECT tperiod FROM TimePeriod tperiod");
		queryBuilder.append(TimePeriodServiceUtils.getFilterQuery(criterias));
		Query query = entityManager.createQuery(queryBuilder.toString());
		query.setHint("org.hibernate.cacheable", true);
		return Long.parseLong(String.valueOf(query.getResultList().size()));
	}
	/**
	* @return the total count of persons.
	*/
	public Long getTotalCount() {
		Query query = entityManager.createQuery("SELECT COUNT(tperiod) FROM TimePeriod tperiod");
		query.setHint("org.hibernate.cacheable", true);
		return (Long) query.getSingleResult();
	}
}