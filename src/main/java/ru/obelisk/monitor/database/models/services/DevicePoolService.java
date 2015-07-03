package ru.obelisk.monitor.database.models.services;

import java.util.List;

import javax.transaction.Transactional;

import ru.obelisk.monitor.database.models.entity.DevicePool;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

public interface DevicePoolService {

	@Transactional
	DevicePool addDevicePool(DevicePool devicePool);
	@Transactional
    void deleteDevicePool(int id);
	@Transactional
	DevicePool editDevicePool(DevicePool DevicePool);
	
    DevicePool getDevicePoolByName(String devicePoolName);
    DevicePool getDevicePoolById(int id);
    List<DevicePool> getAllDevicePools();
    
    List<Select2Result> findDevicePoolByTerm(String term);
    List<DevicePool> findDevicePoolWithDatatablesCriterias(DatatablesCriterias criterias);
	Long getFilteredCount(DatatablesCriterias criterias);
	Long getTotalCount();
}