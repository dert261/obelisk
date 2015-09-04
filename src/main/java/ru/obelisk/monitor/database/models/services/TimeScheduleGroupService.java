package ru.obelisk.monitor.database.models.services;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ru.obelisk.monitor.database.models.entity.TimeScheduleGroup;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

public interface TimeScheduleGroupService {

	@Transactional
	TimeScheduleGroup addTimeScheduleGroup(TimeScheduleGroup timesched);
	@Transactional
	TimeScheduleGroup editTimeScheduleGroup(TimeScheduleGroup timesched);
	@Transactional
    void deleteTimeScheduleGroup(int id);
    
	TimeScheduleGroup getTimeScheduleGroupByName(String timeschedName);
	TimeScheduleGroup getTimeScheduleGroupById(int id);
    List<TimeScheduleGroup> getAllTimeScheduleGroups();
    
    List<Select2Result> findTimeScheduleGroupByTerm(String term);
    List<TimeScheduleGroup> findTimeScheduleGroupWithDatatablesCriterias(DatatablesCriterias criterias);
	Long getFilteredCount(DatatablesCriterias criterias);
	Long getTotalCount();
}