package ru.obelisk.monitor.database.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.obelisk.monitor.database.models.entity.BankAccount;

public interface BankAccountRepositroy extends JpaRepository<BankAccount, Long>{
}