package com.restapi.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restapi.Dto.UserDto;

public interface UserRepo extends JpaRepository<UserDto, Long>{

}
