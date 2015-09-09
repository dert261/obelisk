package ru.obelisk.monitor.database.models.services.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.obelisk.monitor.database.models.entity.TimeScheduleGroup;
import ru.obelisk.monitor.database.models.repository.TimeScheduleGroupRepository;
import ru.obelisk.monitor.database.models.services.TimeScheduleGroupService;
import ru.obelisk.monitor.database.models.services.utils.TimeScheduleGroupServiceUtils;
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
public class TimeScheduleGroupServiceImpl implements TimeScheduleGroupService {
 
    @Autowired
    private TimeScheduleGroupRepository TimeScheduleGroupRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    @Transactional
    public TimeScheduleGroup addTimeScheduleGroup(TimeScheduleGroup TimeScheduleGroup) {
    	TimeScheduleGroup savedTimeScheduleGroup = TimeScheduleGroupRepository.saveAndFlush(TimeScheduleGroup);
        return savedTimeScheduleGroup;
    }
 
    @Override
    @Transactional
    public void deleteTimeScheduleGroup(int id) {
    	TimeScheduleGroupRepository.delete(id);
    }
    
    @Override
    public TimeScheduleGroup getTimeScheduleGroupByName(String name) {
        return TimeScheduleGroupRepository.findByName(name);
    }
 
    @Override
    @Transactional
    public TimeScheduleGroup editTimeScheduleGroup(TimeScheduleGroup formTimeScheduleGroup) {
    	TimeScheduleGroup timeScheduleGroup = new TimeScheduleGroup();
    	BeanUtils.copyProperties(formTimeScheduleGroup, timeScheduleGroup);
    	return TimeScheduleGroupRepository.saveAndFlush(timeScheduleGroup);
    }
 
    @Override
    public List<TimeScheduleGroup> getAllTimeScheduleGroups() {
        return TimeScheduleGroupRepository.findAll();
    }

	@Override
	public TimeScheduleGroup getTimeScheduleGroupById(int id) {
		return TimeScheduleGroupRepository.findOne(id);
	}
	
	public List<Select2Result> findTimeScheduleGroupByTerm(String term) {
		
		List<Select2Result> resultList = entityManager.createQuery(
                "SELECT NEW ru.obelisk.monitor.web.ui.select2.Select2Result(tschedgroup.id, tschedgroup.name) FROM TimeScheduleGroup tschedgroup" 
                		+ " WHERE "
                        + " tschedgroup.name LIKE :term", Select2Result.class)
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
	public List<TimeScheduleGroup> findTimeScheduleGroupWithDatatablesCriterias(DatatablesCriterias criterias) {
		StringBuilder queryBuilder = new StringBuilder("SELECT timePeriod FROM TimeScheduleGroup timePeriod");
		
		/**
		* Step 1: global and individual column filtering
		*/
		queryBuilder.append(TimeScheduleGroupServiceUtils.getFilterQuery(criterias));
				
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
				orderParams.add("timePeriod." + columnName + " " + columnDef.getSortDirection());
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
		
		TypedQuery<TimeScheduleGroup> query = entityManager.createQuery(queryBuilder.toString(), TimeScheduleGroup.class);
		
		/**
		* Step 3: paging
		*/
		query.setFirstResult(criterias.getStart());
		if(criterias.getLength()>0)
			query.setMaxResults(criterias.getLength());
		query.setHint("org.hibernate.cacheable", true);
		return idGenerate(query.getResultList(),criterias.getStart());
	}
	
	private List<TimeScheduleGroup> idGenerate(List<TimeScheduleGroup> TimeScheduleGroups, int start){
		
		for(int i=0;i<TimeScheduleGroups.size();i++){
			TimeScheduleGroups.get(i).setNumberLocalized(start+i+1);
		}
		return TimeScheduleGroups;
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
		StringBuilder queryBuilder = new StringBuilder("SELECT timePeriod FROM TimeScheduleGroup timePeriod");
		queryBuilder.append(TimeScheduleGroupServiceUtils.getFilterQuery(criterias));
		Query query = entityManager.createQuery(queryBuilder.toString());
		query.setHint("org.hibernate.cacheable", true);
		return Long.parseLong(String.valueOf(query.getResultList().size()));
	}
	/**
	* @return the total count of persons.
	*/
	public Long getTotalCount() {
		Query query = entityManager.createQuery("SELECT COUNT(timePeriod) FROM TimeScheduleGroup timePeriod");
		query.setHint("org.hibernate.cacheable", true);
		return (Long) query.getSingleResult();
	}
}