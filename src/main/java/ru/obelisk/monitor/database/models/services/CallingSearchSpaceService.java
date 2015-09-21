package ru.obelisk.monitor.database.models.services;

import java.util.List;
import javax.transaction.Transactional;
import ru.obelisk.monitor.database.models.entity.CallingSearchSpace;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

public interface CallingSearchSpaceService {
	
	@Transactional
	CallingSearchSpace addCallingSearchSpace(CallingSearchSpace css);
	@Transactional
	CallingSearchSpace editCallingSearchSpace(CallingSearchSpace CallingSearchSpace);
	@Transactional
    void deleteCallingSearchSpace(int id);
		
    CallingSearchSpace getCallingSearchSpaceByName(String cssName);
	CallingSearchSpace getCallingSearchSpaceById(int id);
    List<CallingSearchSpace> getAllCallingSearchSpaces();
        
    List<Select2Result> findCallingSearchSpaceByTerm(String term);
    List<CallingSearchSpace> findCallingSearchSpaceWithDatatablesCriterias(DatatablesCriterias criterias);
    Long getFilteredCount(DatatablesCriterias criterias);
    Long getTotalCount();
}