package ru.obelisk.monitor.database.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.obelisk.monitor.database.models.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select b from User b where b.name = :fname")
    User findByName(@Param("fname") String fname);
	
}
