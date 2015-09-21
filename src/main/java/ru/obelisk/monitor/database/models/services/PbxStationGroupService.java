package ru.obelisk.monitor.database.models.services;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ru.obelisk.monitor.database.models.entity.PbxStationGroup;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

public interface PbxStationGroupService {

	@Transactional
	PbxStationGroup addPbxStationGroup(PbxStationGroup pbxgroup);
	@Transactional
	PbxStationGroup editPbxStationGroup(PbxStationGroup pbxgroup);
	@Transactional
    void deletePbxStationGroup(int id);
    
	PbxStationGroup getPbxStationGroupByName(String pbxgroupName);
	PbxStationGroup getPbxStationGroupById(int id);
    List<PbxStationGroup> getAllPbxStationGroups();
    
    List<Select2Result> findPbxStationGroupByTerm(String term);
    List<PbxStationGroup> findPbxStationGroupWithDatatablesCriterias(DatatablesCriterias criterias);
	Long getFilteredCount(DatatablesCriterias criterias);
	Long getTotalCount();
}