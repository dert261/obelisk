package ru.obelisk.monitor.database.models.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.obelisk.monitor.database.models.entity.PbxStation;
import ru.obelisk.monitor.database.models.repository.PbxStationRepository;
import ru.obelisk.monitor.database.models.services.PbxStationService;
import ru.obelisk.monitor.database.models.services.utils.PbxStationServiceUtils;
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
public class PbxStationServiceImpl implements PbxStationService {
 
    @Autowired
    private PbxStationRepository pbxStationRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    @Transactional
    public PbxStation addPbxStation(PbxStation pbxStation) {
    	PbxStation savedPbxStation = pbxStationRepository.saveAndFlush(pbxStation);
        return savedPbxStation;
    }
 
    @Override
    @Transactional
    public void deletePbxStation(int id) {
    	pbxStationRepository.delete(id);
    }
 
    @Override
    public PbxStation getPbxStationByName(String name) {
        return pbxStationRepository.findByName(name);
    }
 
    @Override
    @Transactional
    public PbxStation editPbxStation(PbxStation formPbxStation) {
    	PbxStation pbxStation = getPbxStationById(formPbxStation.getId());
    	pbxStation.setHost(formPbxStation.getHost());
    	pbxStation.setName(formPbxStation.getName());
    	pbxStation.setRabbitQueue(formPbxStation.getRabbitQueue());
    	pbxStation.setReinitFlag(formPbxStation.isReinitFlag());
    	pbxStation.setUpdateFlag(formPbxStation.isUpdateFlag());
 		return pbxStationRepository.saveAndFlush(pbxStation);
    }
 
    @Override
    public List<PbxStation> getAllPbxStations() {
        return pbxStationRepository.findAll();
    }

	@Override
	public PbxStation getPbxStationById(int id) {
		return pbxStationRepository.findOne(id);
	}
	
	public List<Select2Result> findPbxStationByTerm(String term) {
		
		List<Select2Result> reultList = entityManager.createQuery(
                "SELECT NEW ru.obelisk.monitor.web.ui.select2.Select2Result(s.id, CONCAT(s.name,' (',s.host,')')) FROM PbxStation s" 
                		+ " WHERE "
                        + " s.name LIKE :term"
                        + " OR s.host LIKE :term", Select2Result.class)
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
	public List<PbxStation> findPbxStationWithDatatablesCriterias(DatatablesCriterias criterias) {
		StringBuilder queryBuilder = new StringBuilder("SELECT s FROM PbxStation s");
		
		/**
		* Step 1: global and individual column filtering
		*/
		queryBuilder.append(PbxStationServiceUtils.getFilterQuery(criterias));
		
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
				orderParams.add("s." + columnName + " " + columnDef.getSortDirection());
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
		
		TypedQuery<PbxStation> query = entityManager.createQuery(queryBuilder.toString(), PbxStation.class);
		
		/**
		* Step 3: paging
		*/
		query.setFirstResult(criterias.getStart());
		if(criterias.getLength()>0)
			query.setMaxResults(criterias.getLength());
				
		return idGenerate(query.getResultList(),criterias.getStart());
	}
	
	private List<PbxStation> idGenerate(List<PbxStation> pbxStations, int start){
		
		for(int i=0;i<pbxStations.size();i++){
			pbxStations.get(i).setNumberLocalized(start+i+1);
		}
		return pbxStations;
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
		StringBuilder queryBuilder = new StringBuilder("SELECT s FROM PbxStation s");
		queryBuilder.append(PbxStationServiceUtils.getFilterQuery(criterias));
		Query query = entityManager.createQuery(queryBuilder.toString());
		return Long.parseLong(String.valueOf(query.getResultList().size()));
	}
	/**
	* @return the total count of persons.
	*/
	public Long getTotalCount() {
		Query query = entityManager.createQuery("SELECT COUNT(s) FROM PbxStation s");
		return (Long) query.getSingleResult();
	}
}