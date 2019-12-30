package com.mainmicroservice.mainmicroservice.Repositories;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mainmicroservice.mainmicroservice.Entities.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User,Long> {

	User findByActivateCode(String activationCode);
	User findByEmail(String email);
	Optional<User> findById(Long i);
}
