package com.rukesh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rukesh.config.JwtProvider;
import com.rukesh.model.User;
import com.rukesh.repository.UserRepository;

@Service
public class UserServiceImp implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Override
	public User findUserByJwtToken(String jwt) throws Exception {
		String email=jwtProvider.getEmailFromJwtToken(jwt);
		User user=findByUserEmail(email);
		return user;
	}

	@Override
	public User findByUserEmail(String email) throws Exception {
		User user=userRepository.findByEmail(email);
		
		if(user==null) {
			throw new Exception("user not found");
		}
		return user;
	}
	

}
