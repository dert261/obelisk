package ru.obelisk.monitor.database.models.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import ru.obelisk.monitor.database.models.entity.DevicePool;

public interface DevicePoolRepository extends JpaRepository<DevicePool, Integer> {

	@Query("SELECT devPool FROM DevicePool devPool LEFT JOIN FETCH devPool.timezone LEFT JOIN FETCH devPool.locations WHERE devPool.name = :name")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	DevicePool findByName(@Param("name") String name);
	
	@Query("SELECT devPool FROM DevicePool devPool LEFT JOIN FETCH devPool.timezone LEFT JOIN FETCH devPool.locations WHERE devPool.id = :id")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	DevicePool findOne(@Param("id") int id);
	
	@Query("SELECT devPool FROM DevicePool devPool")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	List<DevicePool> findAll();
	
	
}
