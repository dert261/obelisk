package ru.obelisk.monitor.database.models.services.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.obelisk.monitor.database.models.entity.DevicePool;
import ru.obelisk.monitor.database.models.repository.DevicePoolRepository;
import ru.obelisk.monitor.database.models.services.DevicePoolService;
import ru.obelisk.monitor.database.models.services.utils.DevicePoolServiceUtils;
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
public class DevicePoolServiceImpl implements DevicePoolService {
 
    @Autowired
    private DevicePoolRepository devicePoolRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    @Transactional
    public DevicePool addDevicePool(DevicePool devicePool) {
    	DevicePool savedDevicePool = devicePoolRepository.saveAndFlush(devicePool);
        return savedDevicePool;
    }
 
    @Override
    @Transactional
    public void deleteDevicePool(int id) {
    	devicePoolRepository.delete(id);
    }
    
    @Override
    public DevicePool getDevicePoolByName(String name) {
        return devicePoolRepository.findByName(name);
    }
 
    @Override
    @Transactional
    public DevicePool editDevicePool(DevicePool formDevicePool) {
    	DevicePool devicePool = new DevicePool();
    	BeanUtils.copyProperties(formDevicePool, devicePool);
    	return devicePoolRepository.saveAndFlush(devicePool);
    }
 
    @Override
    public List<DevicePool> getAllDevicePools() {
        return devicePoolRepository.findAll();
    }

	@Override
	public DevicePool getDevicePoolById(int id) {
		return devicePoolRepository.findOne(id);
	}
	
	public List<Select2Result> findDevicePoolByTerm(String term) {
		
		List<Select2Result> resultList = entityManager.createQuery(
                "SELECT NEW ru.obelisk.monitor.web.ui.select2.Select2Result(devPool.id, devPool.name) FROM DevicePool devPool" 
                		+ " WHERE "
                        + " devPool.name LIKE :term", Select2Result.class)
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
	public List<DevicePool> findDevicePoolWithDatatablesCriterias(DatatablesCriterias criterias) {
		StringBuilder queryBuilder = new StringBuilder("SELECT devPool FROM DevicePool devPool");
		
		/**
		* Step 1: global and individual column filtering
		*/
		queryBuilder.append(DevicePoolServiceUtils.getFilterQuery(criterias));
				
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
				orderParams.add("devPool." + columnName + " " + columnDef.getSortDirection());
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
		
		TypedQuery<DevicePool> query = entityManager.createQuery(queryBuilder.toString(), DevicePool.class);
		
		/**
		* Step 3: paging
		*/
		query.setFirstResult(criterias.getStart());
		if(criterias.getLength()>0)
			query.setMaxResults(criterias.getLength());
				
		return idGenerate(query.getResultList(),criterias.getStart());
	}
	
	private List<DevicePool> idGenerate(List<DevicePool> DevicePools, int start){
		
		for(int i=0;i<DevicePools.size();i++){
			DevicePools.get(i).setNumberLocalized(start+i+1);
		}
		return DevicePools;
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
		StringBuilder queryBuilder = new StringBuilder("SELECT devPool FROM DevicePool devPool");
		queryBuilder.append(DevicePoolServiceUtils.getFilterQuery(criterias));
		Query query = entityManager.createQuery(queryBuilder.toString());
		return Long.parseLong(String.valueOf(query.getResultList().size()));
	}
	/**
	* @return the total count of persons.
	*/
	public Long getTotalCount() {
		Query query = entityManager.createQuery("SELECT COUNT(devPool) FROM DevicePool devPool");
		return (Long) query.getSingleResult();
	}
}