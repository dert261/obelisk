package ru.obelisk.monitor.database.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.obelisk.monitor.database.models.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {

	@Query("SELECT loc FROM Location loc WHERE loc.name = :name")
	Location findByName(@Param("name") String name);
	
	@Query("SELECT loc FROM Location loc LEFT JOIN FETCH loc.pbxStation LEFT JOIN FETCH loc.devicePool LEFT JOIN FETCH loc.devicePool.timezone WHERE loc.id = :id")
	Location findOne(@Param("id") int id);
}