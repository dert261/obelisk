package ru.obelisk.monitor.database.models.services;

import java.util.List;
import javax.transaction.Transactional;
import ru.obelisk.monitor.database.models.entity.Partition;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

public interface PartitionService {
	
	@Transactional
	Partition addPartition(Partition partition);
	@Transactional
	Partition editPartition(Partition Partition);
	@Transactional
    void deletePartition(int id);
		
    Partition getPartitionByName(String partitionName);
	Partition getPartitionById(int id);
    List<Partition> getAllPartitions();
        
    List<Select2Result> findPartitionByTerm(String term);
    List<Partition> findPartitionWithDatatablesCriterias(DatatablesCriterias criterias);
    Long getFilteredCount(DatatablesCriterias criterias);
    Long getTotalCount();
}