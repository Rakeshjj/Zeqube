package com.restapi.config;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;

public class KeycloakProvider {
	
	private static final String SERVER_URL="http://localhost:8080";
	private static final String REALM_NAME="spring-boot-realm-dev";
	private static final String REALM_MASTER="master";
	private static final String ADMIN_CLI="admin-cli";
	private static final String USER_CONSOLE="Rakesh";
	private static final String PASSWORD_CONSOLE="rakesh1711";
	private static final String CLIENT_SECRET="Wm8pw0byQc2v66GYNOMEMyXutAIuqB8d";
	
	public static RealmResource getRealmResource() {
		Keycloak keycloak=KeycloakBuilder.builder()
				.serverUrl(SERVER_URL)
				.realm(REALM_MASTER)
				.clientId(ADMIN_CLI)
				.username(USER_CONSOLE)
				.password(PASSWORD_CONSOLE)
				.clientSecret(CLIENT_SECRET)
				.resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build()).build();
		
		return keycloak.realm(REALM_NAME);
	}
	
	public static UsersResource getUsersResource() {
		RealmResource realmResource=getRealmResource();
		return realmResource.users();
		
	}

	
}
