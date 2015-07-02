package ru.obelisk.monitor.database.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.obelisk.monitor.database.models.entity.Timezone;

public interface TimezoneRepository extends JpaRepository<Timezone, Integer> {

	@Query("SELECT tzone FROM Timezone tzone WHERE tzone.name = :name")
	Timezone findByName(@Param("name") String name);
}
