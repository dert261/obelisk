package ru.obelisk.monitor.database.models.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import ru.obelisk.monitor.database.models.entity.TimeScheduleGroup;

public interface TimeScheduleGroupRepository extends JpaRepository<TimeScheduleGroup, Integer> {

	@Query("SELECT tschedgroup FROM TimeScheduleGroup tschedgroup LEFT JOIN FETCH tschedgroup.timePeriods WHERE tschedgroup.name = :name")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	TimeScheduleGroup findByName(@Param("name") String name);
	
	@Query("SELECT tschedgroup FROM TimeScheduleGroup tschedgroup LEFT JOIN FETCH tschedgroup.timePeriods WHERE tschedgroup.id = :id")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	TimeScheduleGroup findOne(@Param("id") int id);
	
	@Query("SELECT tschedgroup FROM TimeScheduleGroup tschedgroup")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	List<TimeScheduleGroup> findAll();
	
	
}
