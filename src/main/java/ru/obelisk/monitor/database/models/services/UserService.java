package ru.obelisk.monitor.database.models.services;

import java.util.List;

import ru.obelisk.monitor.database.models.entity.User;

public interface UserService {

	User addUser(User user);
    void deleteUser(int id);
    User getUserByName(String name);
    User editUser(User user);
    List<User> getAllUsers();
}