package ru.obelisk.monitor.database.models.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.obelisk.monitor.database.models.entity.PbxStation;

public interface PbxStationRepository extends JpaRepository<PbxStation, Integer> {

	@Query("select b from PbxStation b where b.name = :name")
	PbxStation findByName(@Param("name") String name);
	
	@Transactional
	@Modifying
	@Query("UPDATE PbxStation s SET s.updateFlag=1 WHERE id = :id")
	void setUpdateConfig(@Param("id") int id);
	
	@Transactional
	@Modifying
	@Query("UPDATE PbxStation s SET s.reinitFlag=1 WHERE id = :id")
	void setReinitBase(@Param("id") int id);
}
