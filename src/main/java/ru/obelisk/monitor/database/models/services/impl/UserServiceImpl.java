package ru.obelisk.monitor.database.models.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.obelisk.monitor.database.models.entity.User;
import ru.obelisk.monitor.database.models.repository.UserRepository;
import ru.obelisk.monitor.database.models.services.UserService;

import java.util.List;
 
@Service
public class UserServiceImpl implements UserService {
 
    @Autowired
    private UserRepository userRepository;
 
    @Override
    public User addUser(User user) {
        User savedUser = userRepository.saveAndFlush(user);
        return savedUser;
    }
 
    @Override
    public void deleteUser(int id) {
        userRepository.delete(id);
    }
 
    @Override
    public User getUserByName(String name) {
        return userRepository.findByName(name);
    }
 
    @Override
    public User editUser(User user) {
        return userRepository.saveAndFlush(user);
    }
 
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

	@Override
	public User getUserById(int id) {
		return userRepository.findOne(id);
	}
}