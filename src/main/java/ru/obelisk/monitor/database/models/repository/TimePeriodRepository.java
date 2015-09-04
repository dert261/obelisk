package ru.obelisk.monitor.database.models.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import ru.obelisk.monitor.database.models.entity.TimePeriod;

public interface TimePeriodRepository extends JpaRepository<TimePeriod, Integer> {

	@Query("SELECT tperiod FROM TimePeriod tperiod LEFT JOIN FETCH tperiod.timeScheduleGroup WHERE tperiod.name = :name")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	TimePeriod findByName(@Param("name") String name);
	
	@Query("SELECT tperiod FROM TimePeriod tperiod LEFT JOIN FETCH tperiod.timeScheduleGroup WHERE tperiod.id = :id")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	TimePeriod findOne(@Param("id") int id);
	
	@Query("SELECT tperiod FROM TimePeriod tperiod")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	List<TimePeriod> findAll();
	
	
}
