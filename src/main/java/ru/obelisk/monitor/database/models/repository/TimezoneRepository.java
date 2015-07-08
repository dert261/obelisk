package ru.obelisk.monitor.database.models.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import ru.obelisk.monitor.database.models.entity.Timezone;

public interface TimezoneRepository extends JpaRepository<Timezone, Integer> {

	@Query("SELECT tzone FROM Timezone tzone LEFT JOIN FETCH tzone.devicePools WHERE tzone.name = :name")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	Timezone findByName(@Param("name") String name);
	
	@Query("SELECT tzone FROM Timezone tzone LEFT JOIN FETCH tzone.devicePools WHERE tzone.id = :id")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	Timezone findOne(@Param("id") int id);
	
	@Query("SELECT tzone FROM Timezone tzone")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	List<Timezone> findAll();
	
	
}
