package ru.obelisk.monitor.database.models.services;

import java.util.List;
import ru.obelisk.monitor.database.models.entity.UserRole;

public interface UserRoleService {
    List<UserRole> getAllUserRoles();
}
