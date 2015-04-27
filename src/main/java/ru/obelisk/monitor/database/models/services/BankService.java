package ru.obelisk.monitor.database.models.services;

import java.util.List;

import ru.obelisk.monitor.database.models.entity.Bank;

public interface BankService {

	Bank addBank(Bank bank);
    void delete(long id);
    Bank getByName(String name);
    Bank editBank(Bank bank);
    List<Bank> getAll();
}