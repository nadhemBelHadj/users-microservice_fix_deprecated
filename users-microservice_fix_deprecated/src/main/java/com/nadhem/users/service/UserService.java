package com.nadhem.users.service;

import com.nadhem.users.entities.Role;
import com.nadhem.users.entities.User;

public interface UserService {
	User saveUser(User user);
	User findUserByUsername (String username);
	Role addRole(Role role);
	User addRoleToUser(String username, String rolename);
}
