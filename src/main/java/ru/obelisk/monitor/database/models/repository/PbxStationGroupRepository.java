package ru.obelisk.monitor.database.models.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import ru.obelisk.monitor.database.models.entity.PbxStationGroup;

public interface PbxStationGroupRepository extends JpaRepository<PbxStationGroup, Integer> {

	@Query("SELECT tstationgroup FROM PbxStationGroup tstationgroup LEFT JOIN FETCH tstationgroup.pbxStations WHERE tstationgroup.name = :name")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	PbxStationGroup findByName(@Param("name") String name);
	
	@Query("SELECT tstationgroup FROM PbxStationGroup tstationgroup LEFT JOIN FETCH tstationgroup.pbxStations WHERE tstationgroup.id = :id")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	PbxStationGroup findOne(@Param("id") int id);
	
	@Query("SELECT tstationgroup FROM PbxStationGroup tstationgroup")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	List<PbxStationGroup> findAll();
	
	
}
