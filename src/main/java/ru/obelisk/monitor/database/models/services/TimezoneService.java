package ru.obelisk.monitor.database.models.services;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ru.obelisk.monitor.database.models.entity.Timezone;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

public interface TimezoneService {

	@Transactional
	Timezone addTimezone(Timezone timezone);
	@Transactional
	Timezone editTimezone(Timezone timezone);
	@Transactional
    void deleteTimezone(int id);
    
    Timezone getTimezoneByName(String timezoneName);
    Timezone getTimezoneById(int id);
    List<Timezone> getAllTimezones();
    
    List<Select2Result> findTimezoneByTerm(String term);
    List<Timezone> findTimezoneWithDatatablesCriterias(DatatablesCriterias criterias);
	Long getFilteredCount(DatatablesCriterias criterias);
	Long getTotalCount();
}