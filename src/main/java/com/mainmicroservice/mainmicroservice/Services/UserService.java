package com.mainmicroservice.mainmicroservice.Services;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mainmicroservice.mainmicroservice.Entities.User;
import com.mainmicroservice.mainmicroservice.Repositories.UserRepository;


@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	public User getUserById(Long id)
	{
		return userRepository.findById(id).get();
	}
	
	public void saveChanges(User user) {
		userRepository.save(user);
	}
	
	public User findByEmail(String email) {
        User result = userRepository.findByEmail(email);
       
        return result;
    }
	
	@Transactional
	public User getUserByActivateCode(String code) 
	{
		try
		{
			return userRepository.findByActivateCode(code);
		}
		catch(Exception ex)
		{
			return null;
		}
		
	}
	
	@Transactional
	public void addNewUser(User us)
	{
		userRepository.save(us);
	}

	
}
