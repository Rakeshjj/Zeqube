package com.restapi.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restapi.Dto.UserDto;
import com.restapi.Repo.UserRepo;

@Service
public class ServiceImplementation {

	@Autowired
	private UserRepo userRepo;

	public UserDto saveUser(UserDto userDto) {
		return userRepo.save(userDto);
	}

}
