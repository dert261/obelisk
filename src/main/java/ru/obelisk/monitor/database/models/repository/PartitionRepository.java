package ru.obelisk.monitor.database.models.repository;

import java.util.List;

import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import ru.obelisk.monitor.database.models.entity.Partition;

public interface PartitionRepository extends JpaRepository<Partition, Integer> {

	@Query("SELECT partition FROM Partition partition LEFT JOIN FETCH partition.css LEFT JOIN FETCH partition.timezone LEFT JOIN FETCH partition.timeScheduleGroup WHERE partition.name = :name")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	Partition findByName(@Param("name") String name);
	
	@Query("SELECT partition FROM Partition partition LEFT JOIN FETCH partition.css LEFT JOIN FETCH partition.timezone LEFT JOIN FETCH partition.timeScheduleGroup WHERE partition.id = :id")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	Partition findOne(@Param("id") int id);
	
	@Query("SELECT partition FROM Partition partition")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	List<Partition> findAll();
}
