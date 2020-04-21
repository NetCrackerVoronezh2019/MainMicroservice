package com.mainmicroservice.mainmicroservice.Repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mainmicroservice.mainmicroservice.Entities.Role;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role,Long> {
	
	Role findByRoleName(String rolename);
}
