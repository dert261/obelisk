package ru.obelisk.monitor.database.models.repository;

import java.util.List;

import javax.persistence.QueryHint;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import ru.obelisk.monitor.database.models.entity.PbxStation;

public interface PbxStationRepository extends JpaRepository<PbxStation, Integer> {

	@Query("SELECT b FROM PbxStation b LEFT JOIN FETCH b.locations WHERE b.name = :name")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	PbxStation findByName(@Param("name") String name);
	
	@Query("SELECT b FROM PbxStation b LEFT JOIN FETCH b.locations WHERE b.id = :id")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	PbxStation findOne(@Param("id") int id);
	
	@Query("SELECT b FROM PbxStation b")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	List<PbxStation> findAll();
	
	@Transactional
	@Modifying
	@Query("UPDATE PbxStation s SET s.updateFlag=1 WHERE id = :id")
	void setUpdateConfig(@Param("id") int id);
	
	@Transactional
	@Modifying
	@Query("UPDATE PbxStation s SET s.reinitFlag=1 WHERE id = :id")
	void setReinitBase(@Param("id") int id);
}
