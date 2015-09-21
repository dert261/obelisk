package ru.obelisk.monitor.database.models.services.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.obelisk.monitor.database.models.entity.Partition;
import ru.obelisk.monitor.database.models.repository.PartitionRepository;
import ru.obelisk.monitor.database.models.services.PartitionService;
import ru.obelisk.monitor.database.models.services.utils.PartitionServiceUtils;
import ru.obelisk.monitor.web.ui.datatables.ColumnDef;
import ru.obelisk.monitor.web.ui.datatables.DatatablesCriterias;
import ru.obelisk.monitor.web.ui.select2.Select2Result;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
 
@Service
public class PartitionServiceImpl implements PartitionService {
 
    @Autowired
    private PartitionRepository partitionRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    @Transactional
    public Partition addPartition(Partition partition) {
    	Partition savedPartition = partitionRepository.saveAndFlush(partition);
        return savedPartition;
    }
 
    @Override
    @Transactional
    public void deletePartition(int id) {
    	partitionRepository.delete(id);
    }
    
    @Override
    public Partition getPartitionByName(String name) {
    	return partitionRepository.findByName(name);
    }
 
    @Override
    @Transactional
    public Partition editPartition(Partition formPartition) {
    	Partition partition = new Partition();
    	BeanUtils.copyProperties(formPartition, partition);
    	partition.setCss(partitionRepository.findOne(formPartition.getId()).getCss());
    	return partitionRepository.saveAndFlush(partition);
    }
 
    @Override
    public List<Partition> getAllPartitions() {
    	return partitionRepository.findAll();
    }

	@Override
	public Partition getPartitionById(int id) {
		return partitionRepository.findOne(id);
	}
	
	public List<Select2Result> findPartitionByTerm(String term) {
		
		List<Select2Result> resultList = entityManager.createQuery(
                "SELECT NEW ru.obelisk.monitor.web.ui.select2.Select2Result(partition.id, partition.name) FROM Partition partition" 
                		+ " WHERE "
                        + " partition.name LIKE :term", Select2Result.class)
        .setParameter("term", "%" + term.toLowerCase() + "%")
        .setHint("org.hibernate.cacheable", true)
        .getResultList();
        return resultList;
	}
	
	/**
	* <p>
	* Query used to populate the DataTables that display the list of persons.
	*
	* @param criterias
	* The DataTables criterias used to filter the persons.
	* (maxResult, filtering, paging, ...)
	* @return a filtered list of persons.
	*/
	@Override
	public List<Partition> findPartitionWithDatatablesCriterias(DatatablesCriterias criterias) {
		StringBuilder queryBuilder = new StringBuilder("SELECT partition FROM Partition partition");
		
		/**
		* Step 1: global and individual column filtering
		*/
		queryBuilder.append(PartitionServiceUtils.getFilterQuery(criterias));
				
		/**
		* Step 2: sorting
		*/
		if (criterias.hasOneSortedColumn()) {
			List<String> orderParams = new ArrayList<String>();
			for (ColumnDef columnDef : criterias.getSortingColumnDefs()) {
				String columnName = null;
				if(columnDef.getName().endsWith("Localized")){
					columnName=columnDef.getName().replaceAll("Localized", "");
				}else {
					columnName=columnDef.getName();
				}
				orderParams.add("partition." + columnName + " " + columnDef.getSortDirection());
			}
			if(!orderParams.isEmpty()){
				queryBuilder.append(" ORDER BY ");
				Iterator<String> itr2 = orderParams.iterator();
				while (itr2.hasNext()) {
					queryBuilder.append(itr2.next());
					if (itr2.hasNext()) {
						queryBuilder.append(" , ");
					}
				}
			}
		}
		TypedQuery<Partition> query = entityManager.createQuery(queryBuilder.toString(), Partition.class);
		
		/**
		* Step 3: paging
		*/
		query.setFirstResult(criterias.getStart());
		if(criterias.getLength()>0)
			query.setMaxResults(criterias.getLength());
		query.setHint("org.hibernate.cacheable", true);
		return idGenerate(query.getResultList(),criterias.getStart());
	}
	
	private List<Partition> idGenerate(List<Partition> Partitions, int start){
		
		for(int i=0;i<Partitions.size();i++){
			Partitions.get(i).setNumberLocalized(start+i+1);
		}
		return Partitions;
	}
	/**
	* <p>
	* Query used to return the number of filtered persons.
	*
	* @param criterias
	* The DataTables criterias used to filter the persons.
	* (maxResult, filtering, paging, ...)
	* @return the number of filtered persons.
	*/
	public Long getFilteredCount(DatatablesCriterias criterias) {
		StringBuilder queryBuilder = new StringBuilder("SELECT partition FROM Partition partition");
		queryBuilder.append(PartitionServiceUtils.getFilterQuery(criterias));
		Query query = entityManager.createQuery(queryBuilder.toString());
		query.setHint("org.hibernate.cacheable", true);
		return Long.parseLong(String.valueOf(query.getResultList().size()));
	}
	/**
	* @return the total count of persons.
	*/
	public Long getTotalCount() {
		Query query = entityManager.createQuery("SELECT COUNT(partition) FROM Partition partition");
		query.setHint("org.hibernate.cacheable", true);
		return (Long) query.getSingleResult();
	}
}