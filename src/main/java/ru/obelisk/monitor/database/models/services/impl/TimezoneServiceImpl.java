package ru.obelisk.monitor.database.models.services.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.obelisk.monitor.database.models.entity.Timezone;
import ru.obelisk.monitor.database.models.repository.TimezoneRepository;
import ru.obelisk.monitor.database.models.services.TimezoneService;
import ru.obelisk.monitor.database.models.services.utils.TimezoneServiceUtils;
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
public class TimezoneServiceImpl implements TimezoneService {
 
    @Autowired
    private TimezoneRepository timezoneRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    @Transactional
    public Timezone addTimezone(Timezone timezone) {
    	Timezone savedTimezone = timezoneRepository.saveAndFlush(timezone);
        return savedTimezone;
    }
 
    @Override
    @Transactional
    public void deleteTimezone(int id) {
    	timezoneRepository.delete(id);
    }
    
    @Override
    public Timezone getTimezoneByName(String name) {
        return timezoneRepository.findByName(name);
    }
 
    @Override
    @Transactional
    public Timezone editTimezone(Timezone formTimezone) {
    	Timezone timezone = new Timezone();
    	BeanUtils.copyProperties(formTimezone, timezone);
    	return timezoneRepository.saveAndFlush(timezone);
    }
 
    @Override
    public List<Timezone> getAllTimezones() {
        return timezoneRepository.findAll();
    }

	@Override
	public Timezone getTimezoneById(int id) {
		return timezoneRepository.findOne(id);
	}
	
	public List<Select2Result> findTimezoneByTerm(String term) {
		
		List<Select2Result> resultList = entityManager.createQuery(
                "SELECT NEW ru.obelisk.monitor.web.ui.select2.Select2Result(tzone.id, tzone.name) FROM Timezone tzone" 
                		+ " WHERE "
                        + " tzone.name LIKE :term", Select2Result.class)
        .setParameter("term", "%" + term.toLowerCase() + "%")
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
	public List<Timezone> findTimezoneWithDatatablesCriterias(DatatablesCriterias criterias) {
		StringBuilder queryBuilder = new StringBuilder("SELECT tzone FROM Timezone tzone");
		
		/**
		* Step 1: global and individual column filtering
		*/
		queryBuilder.append(TimezoneServiceUtils.getFilterQuery(criterias));
				
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
				orderParams.add("tzone." + columnName + " " + columnDef.getSortDirection());
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
		
		TypedQuery<Timezone> query = entityManager.createQuery(queryBuilder.toString(), Timezone.class);
		
		/**
		* Step 3: paging
		*/
		query.setFirstResult(criterias.getStart());
		if(criterias.getLength()>0)
			query.setMaxResults(criterias.getLength());
				
		return idGenerate(query.getResultList(),criterias.getStart());
	}
	
	private List<Timezone> idGenerate(List<Timezone> Timezones, int start){
		
		for(int i=0;i<Timezones.size();i++){
			Timezones.get(i).setNumberLocalized(start+i+1);
		}
		return Timezones;
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
		StringBuilder queryBuilder = new StringBuilder("SELECT tzone FROM Timezone tzone");
		queryBuilder.append(TimezoneServiceUtils.getFilterQuery(criterias));
		Query query = entityManager.createQuery(queryBuilder.toString());
		return Long.parseLong(String.valueOf(query.getResultList().size()));
	}
	/**
	* @return the total count of persons.
	*/
	public Long getTotalCount() {
		Query query = entityManager.createQuery("SELECT COUNT(tzone) FROM Timezone tzone");
		return (Long) query.getSingleResult();
	}
}