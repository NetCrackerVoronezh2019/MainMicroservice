package com.mainmicroservice.mainmicroservice.Services;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mainmicroservice.mainmicroservice.Entities.Users;
import com.mainmicroservice.mainmicroservice.Repositories.UsersRepository;

@Service
public class UsersService {
	
	private UsersRepository ur;
	
	@Transactional
	public Users getUserById(Long id)
	{
		return ur.findById(id).get();
	}
	
	@Transactional
	public void addNewUser(Users us)
	{
		ur.save(us);
	}
	@Autowired
	public void setUsersRepository(UsersRepository ur)
	{
		this.ur=ur;
	}
	
}
