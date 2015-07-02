package ru.obelisk.monitor.database.models.services;

import java.util.List;

import ru.obelisk.monitor.database.models.entity.Timezone;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

public interface TimezoneService {

	Timezone addTimezone(Timezone timezone);
    void deleteTimezone(int id);
    Timezone getTimezoneByName(String timezoneName);
    Timezone getTimezoneById(int id);
    Timezone editTimezone(Timezone Timezone);
    
    List<Timezone> getAllTimezones();
    List<Select2Result> findTimezoneByTerm(String term);
    
   	List<Timezone> findTimezoneWithDatatablesCriterias(DatatablesCriterias criterias);
	Long getFilteredCount(DatatablesCriterias criterias);
	Long getTotalCount();
}