package ru.obelisk.monitor.database.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.obelisk.monitor.database.models.entity.LdapAuthenticationServers;

public interface LdapAuthenticationServersRepository extends JpaRepository<LdapAuthenticationServers, Integer>{

}
