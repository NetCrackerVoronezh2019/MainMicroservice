package com.mainmicroservice.mainmicroservice.Repositories;
import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.mainmicroservice.mainmicroservice.Entities.User;

@Repository
@Transactional
public interface UsersRepository extends CrudRepository<User,Long> {

	User findByActivateCode(String activationCode);
}
