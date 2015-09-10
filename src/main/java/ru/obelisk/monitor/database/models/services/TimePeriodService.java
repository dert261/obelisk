package ru.obelisk.monitor.database.models.services;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ru.obelisk.monitor.database.models.entity.TimePeriod;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

public interface TimePeriodService {

	@Transactional
	TimePeriod addTimePeriod(TimePeriod timeperiod);
	@Transactional
	TimePeriod editTimePeriod(TimePeriod timeperiod);
	@Transactional
    void deleteTimePeriod(int id);
    
	TimePeriod getTimePeriodByName(String timeperiodName);
	TimePeriod getTimePeriodById(int id);
	List<TimePeriod> getTimePeriodByIds(int[] ids);
    List<TimePeriod> getAllTimePeriods();
    
    List<Select2Result> findTimePeriodByTerm(String term);
    List<TimePeriod> findTimePeriodWithDatatablesCriterias(DatatablesCriterias criterias);
	Long getFilteredCount(DatatablesCriterias criterias);
	Long getTotalCount();
}