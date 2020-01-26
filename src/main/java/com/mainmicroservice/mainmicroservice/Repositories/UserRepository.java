package com.mainmicroservice.mainmicroservice.Repositories;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.mainmicroservice.mainmicroservice.Entities.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User,Long> {

	User findByActivateCode(String activationCode);
	
	User findByEmail(String email);
	
	List<User> findAll();
	
	User findByUserId(Long id);
}
