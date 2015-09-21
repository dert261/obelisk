package ru.obelisk.monitor.database.models.services.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.obelisk.monitor.database.models.entity.PbxStationGroup;
import ru.obelisk.monitor.database.models.repository.PbxStationGroupRepository;
import ru.obelisk.monitor.database.models.services.PbxStationGroupService;
import ru.obelisk.monitor.database.models.services.utils.PbxStationGroupServiceUtils;
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
public class PbxStationGroupServiceImpl implements PbxStationGroupService {
 
    @Autowired
    private PbxStationGroupRepository pbxStationGroupRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    @Transactional
    public PbxStationGroup addPbxStationGroup(PbxStationGroup pbxStationGroup) {
    	PbxStationGroup savedPbxStationGroup = pbxStationGroupRepository.saveAndFlush(pbxStationGroup);
        return savedPbxStationGroup;
    }
 
    @Override
    @Transactional
    public void deletePbxStationGroup(int id) {
    	pbxStationGroupRepository.delete(id);
    }
    
    @Override
    public PbxStationGroup getPbxStationGroupByName(String name) {
        return pbxStationGroupRepository.findByName(name);
    }
 
    @Override
    @Transactional
    public PbxStationGroup editPbxStationGroup(PbxStationGroup formPbxStationGroup) {
    	PbxStationGroup timeScheduleGroup = new PbxStationGroup();
    	BeanUtils.copyProperties(formPbxStationGroup, timeScheduleGroup);
    	return pbxStationGroupRepository.saveAndFlush(timeScheduleGroup);
    }
 
    @Override
    public List<PbxStationGroup> getAllPbxStationGroups() {
        return pbxStationGroupRepository.findAll();
    }

	@Override
	public PbxStationGroup getPbxStationGroupById(int id) {
		return pbxStationGroupRepository.findOne(id);
	}
	
	public List<Select2Result> findPbxStationGroupByTerm(String term) {
		
		List<Select2Result> resultList = entityManager.createQuery(
                "SELECT NEW ru.obelisk.monitor.web.ui.select2.Select2Result(tstationgroup.id, tstationgroup.name) FROM PbxStationGroup tstationgroup" 
                		+ " WHERE "
                        + " tstationgroup.name LIKE :term", Select2Result.class)
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
	public List<PbxStationGroup> findPbxStationGroupWithDatatablesCriterias(DatatablesCriterias criterias) {
		StringBuilder queryBuilder = new StringBuilder("SELECT tstationgroup FROM PbxStationGroup tstationgroup");
		
		/**
		* Step 1: global and individual column filtering
		*/
		queryBuilder.append(PbxStationGroupServiceUtils.getFilterQuery(criterias));
				
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
				orderParams.add("tstationgroup." + columnName + " " + columnDef.getSortDirection());
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
		
		TypedQuery<PbxStationGroup> query = entityManager.createQuery(queryBuilder.toString(), PbxStationGroup.class);
		
		/**
		* Step 3: paging
		*/
		query.setFirstResult(criterias.getStart());
		if(criterias.getLength()>0)
			query.setMaxResults(criterias.getLength());
		query.setHint("org.hibernate.cacheable", true);
		return idGenerate(query.getResultList(),criterias.getStart());
	}
	
	private List<PbxStationGroup> idGenerate(List<PbxStationGroup> PbxStationGroups, int start){
		
		for(int i=0;i<PbxStationGroups.size();i++){
			PbxStationGroups.get(i).setNumberLocalized(start+i+1);
		}
		return PbxStationGroups;
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
		StringBuilder queryBuilder = new StringBuilder("SELECT tstationgroup FROM PbxStationGroup tstationgroup");
		queryBuilder.append(PbxStationGroupServiceUtils.getFilterQuery(criterias));
		Query query = entityManager.createQuery(queryBuilder.toString());
		query.setHint("org.hibernate.cacheable", true);
		return Long.parseLong(String.valueOf(query.getResultList().size()));
	}
	/**
	* @return the total count of persons.
	*/
	public Long getTotalCount() {
		Query query = entityManager.createQuery("SELECT COUNT(tstationgroup) FROM PbxStationGroup tstationgroup");
		query.setHint("org.hibernate.cacheable", true);
		return (Long) query.getSingleResult();
	}
}