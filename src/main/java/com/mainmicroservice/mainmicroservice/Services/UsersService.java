package com.mainmicroservice.mainmicroservice.Services;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mainmicroservice.mainmicroservice.Entities.User;
import com.mainmicroservice.mainmicroservice.Repositories.UsersRepository;

@Service
public class UsersService {
	
	private UsersRepository ur;
	
	@Transactional
	public User getUserById(Long id)
	{
		return ur.findById(id).get();
	}
	
	public void saveChanges(User user) {
		ur.save(user);
	}
	
	@Transactional
	public User getUserByActivateCode(String code) 
	{
		try
		{
			return ur.findByActivateCode(code);
		}
		catch(Exception ex)
		{
			return null;
		}
		
	}
	
	@Transactional
	public void addNewUser(User us)
	{
		ur.save(us);
	}
	@Autowired
	public void setUsersRepository(UsersRepository ur)
	{
		this.ur=ur;
	}
	
}
