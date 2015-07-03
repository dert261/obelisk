package ru.obelisk.monitor.database.models.services;

import java.util.List;
import javax.transaction.Transactional;
import ru.obelisk.monitor.database.models.entity.Location;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

public interface LocationService {
	
	@Transactional
	Location addLocation(Location location);
	@Transactional
	Location editLocation(Location Location);
	@Transactional
    void deleteLocation(int id);
		
    Location getLocationByName(String locationName);
	Location getLocationById(int id);
    List<Location> getAllLocations();
        
    List<Select2Result> findLocationByTerm(String term);
    List<Location> findLocationWithDatatablesCriterias(DatatablesCriterias criterias);
    Long getFilteredCount(DatatablesCriterias criterias);
    Long getTotalCount();
}