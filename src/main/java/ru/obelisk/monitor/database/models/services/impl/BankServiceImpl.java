package ru.obelisk.monitor.database.models.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 



import ru.obelisk.monitor.database.models.entity.Bank;
import ru.obelisk.monitor.database.models.repository.BankRepository;
import ru.obelisk.monitor.database.models.services.BankService;

import java.util.List;
 
@Service
public class BankServiceImpl implements BankService {
 
    @Autowired
    private BankRepository bankRepository;
 
    @Override
    public Bank addBank(Bank bank) {
        Bank savedBank = bankRepository.saveAndFlush(bank);
 
        return savedBank;
    }
 
    @Override
    public void delete(long id) {
        bankRepository.delete(id);
    }
 
    @Override
    public Bank getByName(String name) {
        return bankRepository.findByName(name);
    }
 
    @Override
    public Bank editBank(Bank bank) {
        return bankRepository.saveAndFlush(bank);
    }
 
    @Override
    public List<Bank> getAll() {
        return bankRepository.findAll();
    }
}