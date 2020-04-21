package com.mainmicroservice.mainmicroservice.Services;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import com.mainmicroservice.mainmicroservice.Entities.User;
import com.mainmicroservice.mainmicroservice.Repositories.UserRepository;


@Service

public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	

	public List<User> getAllUsers()
	{
		return this.userRepository.findAll();
	}
	public User getUserById(Long id)
	{
		return userRepository.findByUserId(id);
	}
	
	public void saveChanges(User user) {
		userRepository.save(user);
	}
	
	public User findByEmail(String email) {
        User result = userRepository.findByEmail(email);
       
        return result;
    }
	
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
	
	public User addNewUser(User us)
	{
		return userRepository.save(us);
	}

	
}
