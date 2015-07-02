package ru.obelisk.monitor.database.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.obelisk.monitor.database.models.entity.DevicePool;

public interface DevicePoolRepository extends JpaRepository<DevicePool, Integer> {

	@Query("SELECT devPool FROM DevicePool devPool WHERE devPool.name = :name")
	DevicePool findByName(@Param("name") String name);
}
