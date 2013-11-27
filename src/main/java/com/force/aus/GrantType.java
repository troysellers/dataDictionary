package com.force.aus;
/**
 * Enum to represent GrantTypes that are passed to the OAuth login
 * 
 * @author tsellers
 *
 */
public enum GrantType {
	AUTH_CODE ("authorization_code"),
	PASSWORD ("password"),
	REFRESH_TOKEN ("refresh_token");
	
	String grantType;
	
	GrantType(String grantType) {
		this.grantType = grantType;
	}
	
	String getValue() {
		return grantType;
	}
}
