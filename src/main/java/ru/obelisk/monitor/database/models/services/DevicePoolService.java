package ru.obelisk.monitor.database.models.services;

import java.util.List;

import ru.obelisk.monitor.database.models.entity.DevicePool;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

public interface DevicePoolService {

	DevicePool addDevicePool(DevicePool devicePool);
    void deleteDevicePool(int id);
    DevicePool getDevicePoolByName(String devicePoolName);
    DevicePool getDevicePoolById(int id);
    DevicePool editDevicePool(DevicePool DevicePool);
    
    List<DevicePool> getAllDevicePools();
    List<Select2Result> findDevicePoolByTerm(String term);
    
   	List<DevicePool> findDevicePoolWithDatatablesCriterias(DatatablesCriterias criterias);
	Long getFilteredCount(DatatablesCriterias criterias);
	Long getTotalCount();
}