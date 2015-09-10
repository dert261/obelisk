package ru.obelisk.monitor.database.models.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import ru.obelisk.monitor.database.models.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {

	@Query("SELECT loc FROM Location loc LEFT JOIN FETCH loc.pbxStationGroup LEFT JOIN FETCH loc.devicePool LEFT JOIN FETCH loc.devicePool.timezone WHERE loc.name = :name")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	Location findByName(@Param("name") String name);
	
	@Query("SELECT loc FROM Location loc LEFT JOIN FETCH loc.pbxStationGroup LEFT JOIN FETCH loc.devicePool LEFT JOIN FETCH loc.devicePool.timezone WHERE loc.id = :id")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	Location findOne(@Param("id") int id);
	
	@Query("SELECT loc FROM Location loc")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	List<Location> findAll();
}
