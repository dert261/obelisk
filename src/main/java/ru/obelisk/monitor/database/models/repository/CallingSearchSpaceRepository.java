package ru.obelisk.monitor.database.models.repository;

import java.util.List;

import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import ru.obelisk.monitor.database.models.entity.CallingSearchSpace;

public interface CallingSearchSpaceRepository extends JpaRepository<CallingSearchSpace, Integer> {

	@Query("SELECT css FROM CallingSearchSpace css LEFT JOIN FETCH css.partitions WHERE css.name = :name")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	CallingSearchSpace findByName(@Param("name") String name);
	
	@Query("SELECT css FROM CallingSearchSpace css LEFT JOIN FETCH css.partitions WHERE css.id = :id")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	CallingSearchSpace findOne(@Param("id") int id);
	
	@Query("SELECT css FROM CallingSearchSpace css")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	List<CallingSearchSpace> findAll();
}
