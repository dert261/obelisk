package ru.obelisk.monitor.database.models.services;

import java.util.List;

import javax.transaction.Transactional;

import ru.obelisk.monitor.database.models.entity.PbxStation;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

public interface PbxStationService {

	@Transactional
	PbxStation addPbxStation(PbxStation pbxStation);
	@Transactional
	PbxStation editPbxStation(PbxStation pbxStation);
	@Transactional
    void deletePbxStation(int id);
	
    void updateConfig(int id);
    void reinitBase(int id);
    
    PbxStation getPbxStationByName(String name);
    PbxStation getPbxStationById(int id);
    List<PbxStation> getAllPbxStations();
            
    List<Select2Result> findPbxStationByTerm(String term);
    List<PbxStation> findPbxStationWithDatatablesCriterias(DatatablesCriterias criterias);
	Long getFilteredCount(DatatablesCriterias criterias);
	Long getTotalCount();
}