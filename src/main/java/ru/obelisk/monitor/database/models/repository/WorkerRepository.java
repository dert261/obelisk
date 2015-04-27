package ru.obelisk.monitor.database.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.obelisk.monitor.database.models.entity.Worker;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
}