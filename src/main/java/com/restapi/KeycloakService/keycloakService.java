package com.restapi.KeycloakService;

import java.util.List;

import org.keycloak.representations.idm.UserRepresentation;

import com.restapi.Dto.UserDto;

public interface keycloakService {

	List<UserRepresentation> findAllUsers();

	List<UserRepresentation> searchUserByUsername(String username);

	String createUser(UserDto userDto);

	void deleteUser(String userId);

	void updateUser(String userId, UserDto userDto);
}
