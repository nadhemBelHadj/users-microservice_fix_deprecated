package com.nadhem.users.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nadhem.users.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByRole(String role);
	
}
