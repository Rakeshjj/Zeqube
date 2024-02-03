package com. restapi.Keycloakcontroller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.Dto.UserDto;
import com.restapi.KeycloakService.keycloakService;
import com.restapi.UserService.ServiceImplementation;

@RestController
@RequestMapping("/keycloak/user")
@PreAuthorize("hasRole('admin_client_role')")
@CrossOrigin("*")
public class KeycloakController {

	@Autowired
	private keycloakService ikeycloakService;

	@Autowired
	private ServiceImplementation serviceImplementation;

	@GetMapping("/search")
	public ResponseEntity<?> findAllUsers() {
		return ResponseEntity.ok(ikeycloakService.findAllUsers());
	}

	@GetMapping("/search/{username}")
	public ResponseEntity<?> findAllUsers(@PathVariable String username) {
		return ResponseEntity.ok(ikeycloakService.searchUserByUsername(username));
	}

	@PostMapping("/create")
	public ResponseEntity<?> createUser(@RequestBody UserDto userDto) throws URISyntaxException {
		UserDto user = serviceImplementation.saveUser(userDto);
		String response = ikeycloakService.createUser(userDto);
		return ResponseEntity.created(new URI("/keycloak/user/create")).body(response);
	}

	@PutMapping("/update/{userId}")
	public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserDto userDto) {
		ikeycloakService.updateUser(userId, userDto);
		return ResponseEntity.ok("user updated successfully!!");

	}

	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable String userId) {
		ikeycloakService.deleteUser(userId);
		return ResponseEntity.noContent().build();
	}

}
