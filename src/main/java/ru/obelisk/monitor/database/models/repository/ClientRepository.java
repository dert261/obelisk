package ru.obelisk.monitor.database.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.obelisk.monitor.database.models.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
