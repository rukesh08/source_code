package com.rukesh.service;

import com.rukesh.model.User;

public interface UserService {
	
	public User findUserByJwtToken(String jwt) throws Exception;
	
	
	public User findByUserEmail(String email)throws Exception;
		
}
