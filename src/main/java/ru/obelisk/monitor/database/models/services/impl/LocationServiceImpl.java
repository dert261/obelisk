package ru.obelisk.monitor.database.models.services.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.obelisk.monitor.database.models.entity.Location;
import ru.obelisk.monitor.database.models.repository.LocationRepository;
import ru.obelisk.monitor.database.models.services.LocationService;
import ru.obelisk.monitor.database.models.services.utils.LocationServiceUtils;
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
public class LocationServiceImpl implements LocationService {
 
    @Autowired
    private LocationRepository locationRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    @Transactional
    public Location addLocation(Location location) {
    	Location savedLocation = locationRepository.saveAndFlush(location);
        return savedLocation;
    }
 
    @Override
    @Transactional
    public void deleteLocation(int id) {
    	locationRepository.delete(id);
    }
    
    @Override
    public Location getLocationByName(String name) {
        return locationRepository.findByName(name);
    }
 
    @Override
    @Transactional
    public Location editLocation(Location formLocation) {
    	Location location = new Location();
    	BeanUtils.copyProperties(formLocation, location);
    	return locationRepository.saveAndFlush(location);
    }
 
    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

	@Override
	public Location getLocationById(int id) {
		return locationRepository.findOne(id);
	}
	
	public List<Select2Result> findLocationByTerm(String term) {
		
		List<Select2Result> resultList = entityManager.createQuery(
                "SELECT NEW ru.obelisk.monitor.web.ui.select2.Select2Result(loc.id, loc.name) FROM Location loc" 
                		+ " WHERE "
                        + " loc.name LIKE :term", Select2Result.class)
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
	@Transactional
	public List<Location> findLocationWithDatatablesCriterias(DatatablesCriterias criterias) {
		StringBuilder queryBuilder = new StringBuilder("SELECT loc FROM Location loc LEFT JOIN FETCH loc.pbxStation LEFT JOIN FETCH loc.devicePool");
		
		/**
		* Step 1: global and individual column filtering
		*/
		queryBuilder.append(LocationServiceUtils.getFilterQuery(criterias));
				
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
				orderParams.add("loc." + columnName + " " + columnDef.getSortDirection());
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
		
		TypedQuery<Location> query = entityManager.createQuery(queryBuilder.toString(), Location.class);
		
		/**
		* Step 3: paging
		*/
		query.setFirstResult(criterias.getStart());
		if(criterias.getLength()>0)
			query.setMaxResults(criterias.getLength());
				
		return idGenerate(query.getResultList(),criterias.getStart());
	}
	
	private List<Location> idGenerate(List<Location> Locations, int start){
		
		for(int i=0;i<Locations.size();i++){
			Locations.get(i).setNumberLocalized(start+i+1);
		}
		return Locations;
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
		StringBuilder queryBuilder = new StringBuilder("SELECT loc FROM Location loc");
		queryBuilder.append(LocationServiceUtils.getFilterQuery(criterias));
		Query query = entityManager.createQuery(queryBuilder.toString());
		return Long.parseLong(String.valueOf(query.getResultList().size()));
	}
	/**
	* @return the total count of persons.
	*/
	public Long getTotalCount() {
		Query query = entityManager.createQuery("SELECT COUNT(loc) FROM Location loc");
		return (Long) query.getSingleResult();
	}
}