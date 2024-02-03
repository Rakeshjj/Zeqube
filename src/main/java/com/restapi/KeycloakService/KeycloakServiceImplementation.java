package com. restapi.KeycloakService;

import java. util.List;
import java.util.logging.Logger;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import com.restapi.Dto.UserDto;
import com.restapi.config.KeycloakProvider;

import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KeycloakServiceImplementation implements keycloakService {
	private static final Logger LOGGER = Logger.getLogger(KeycloakServiceImplementation.class.getName());

	@Override
	public List<UserRepresentation> findAllUsers() {
		return KeycloakProvider.getRealmResource().users().list();
	}

	@Override
	public List<UserRepresentation> searchUserByUsername(String username) {
		return KeycloakProvider.getRealmResource().users().searchByUsername(username, true);
	}

	@Override
	public String createUser(@org.springframework.lang.NonNull UserDto userDto) {
		int status = 0;
		UsersResource usersResource = KeycloakProvider.getUsersResource();

		UserRepresentation userRepresentation = new UserRepresentation();
		userRepresentation.setFirstName(userDto.getFirstName());
		userRepresentation.setLastName(userDto.getLastName());
		userRepresentation.setEmail(userDto.getEmail());
		userRepresentation.setUsername(userDto.getUsername());
		userRepresentation.setEmailVerified(true);
		userRepresentation.setEnabled(true);

		Response response = usersResource.create(userRepresentation);
		status = response.getStatus();

		if (status == 201) {
			String path = response.getLocation().getPath();
			String userId = path.substring(path.lastIndexOf("/") + 1);

			CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
			credentialRepresentation.setTemporary(false);
			credentialRepresentation.setType(OAuth2Constants.PASSWORD);
			credentialRepresentation.setValue(userDto.getPassword());

			usersResource.get(userId).resetPassword(credentialRepresentation);

			RealmResource realmResource = KeycloakProvider.getRealmResource();
			List<RoleRepresentation> roleRepresentations = null;

			if (userDto.getRoles() == null || userDto.getRoles().isEmpty()) {
				roleRepresentations = List.of(realmResource.roles().get("user").toRepresentation());
			} else {
				roleRepresentations = realmResource.roles().list().stream().filter(role -> userDto.getRoles().stream()
						.anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName()))).toList();
			}
			realmResource.users().get(userId).roles().realmLevel().add(roleRepresentations);
			return "User Created Successfully!!";
		} else if (status == 409) {
			LOGGER.info("User exist already");
			return "User Exist already!!!";
		} else {
			LOGGER.info("Error creating user,contact with administrator");
			return "Error creating user, contact with administrator";
		}

	}

	@Override
	public void deleteUser(String userId) {
		KeycloakProvider.getUsersResource().get(userId).remove();
	}

	@Override
	public void updateUser(String userId, UserDto userDto) {
		CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
		credentialRepresentation.setTemporary(false);
		credentialRepresentation.setType(OAuth2Constants.PASSWORD);
		credentialRepresentation.setValue(userDto.getPassword());

		UserRepresentation userRepresentation = new UserRepresentation();
		userRepresentation.setFirstName(userDto.getFirstName());
		userRepresentation.setLastName(userDto.getLastName());
		userRepresentation.setEmail(userDto.getEmail());
		userRepresentation.setUsername(userDto.getUsername());
		userRepresentation.setEnabled(true);
		userRepresentation.setEmailVerified(true);
		
		UserResource userResource=KeycloakProvider.getUsersResource().get(userId);
		userResource.update(userRepresentation);
	}

}
