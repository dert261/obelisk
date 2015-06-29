package ru.obelisk.monitor.database.models.services;

import java.util.List;

import ru.obelisk.monitor.database.models.entity.PbxStation;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

public interface PbxStationService {

	PbxStation addPbxStation(PbxStation pbxStation);
    void deletePbxStation(int id);
    PbxStation getPbxStationByName(String name);
    PbxStation getPbxStationById(int id);
    PbxStation editPbxStation(PbxStation pbxStation);
    
    void updateConfig(int id);
    void reinitBase(int id);
    
    List<PbxStation> getAllPbxStations();
    List<Select2Result> findPbxStationByTerm(String term);
    
   	List<PbxStation> findPbxStationWithDatatablesCriterias(DatatablesCriterias criterias);
	Long getFilteredCount(DatatablesCriterias criterias);
	Long getTotalCount();
}