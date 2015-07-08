package ru.obelisk.monitor.database.models.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import ru.obelisk.monitor.database.models.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("SELECT b FROM User b LEFT JOIN FETCH b.roles WHERE b.login = :login")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    User findByUsername(@Param("login") String login);
	
	@Query("SELECT b FROM User b LEFT JOIN FETCH b.roles WHERE b.name = :fname")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    User findByName(@Param("fname") String fname);
	
	@Query("SELECT b FROM User b LEFT JOIN FETCH b.roles WHERE b.id = :id")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    User findOne(@Param("id") int id);
	
	@Query("SELECT b FROM User b")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<User> findAll();
	
}
