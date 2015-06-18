package ru.obelisk.monitor.database.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.obelisk.monitor.database.models.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer>{
	
}
