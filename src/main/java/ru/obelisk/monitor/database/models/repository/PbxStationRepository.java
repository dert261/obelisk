package ru.obelisk.monitor.database.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.obelisk.monitor.database.models.entity.PbxStation;

public interface PbxStationRepository extends JpaRepository<PbxStation, Integer> {

	@Query("select b from PbxStation b where b.name = :name")
	PbxStation findByName(@Param("name") String name);
}
