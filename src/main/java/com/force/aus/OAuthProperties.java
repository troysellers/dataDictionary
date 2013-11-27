package com.force.aus;

/**
 * Properties class to be populated and passed to an OAuth login method.
 * 
 * @author tsellers
 *
 */
public class OAuthProperties {

	private String tokenURL = "/services/oauth2/token";
	private String host;
	private String username;
	private String password;
	private String securityToken;
	private String clientId;
	private String clientSecret;
	private String accessToken;
	private String refreshToken;
	private String apiVersion;
	
	private GrantType grantType;
	
	public OAuthProperties () {
	}
	public void setTokenURL (String tokenURL) {
		this.tokenURL = tokenURL;
	}
	public String getTokenURL() {
		return host+tokenURL;
	}
	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}
	public String getSecurityToken() {
		return securityToken;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}
	
	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}
	
	public String getApiVersion() {
		return apiVersion;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public GrantType getGrantType () {
		return grantType;
	}
	
	public void setGrantType(GrantType grantType) {
		this.grantType = grantType;
	}
	/**
	 * returns a formatted String for display in logging output
	 * @return
	 */
	public String getLogString() {
		StringBuilder b = new StringBuilder();
		
		b.append("User - ");
		b.append(getUsername());
		b.append("\n");

		b.append("Pass - ");
		b.append(getPassword());
		b.append("\n");
		
		b.append("Token URL - ");
		b.append(getTokenURL());
		b.append("\n");
		
		b.append("Client ID - ");
		b.append(getClientId());
		b.append("\n");
		
		b.append("Client Secret - ");
		b.append(getClientSecret());
		b.append("\n");
		
		b.append("Grant Type - ");
		b.append(getUsername());
		b.append("\n");
		
		b.append("Access Token - ");
		b.append(getAccessToken());
		b.append("\n");
		
		b.append("Refresh Token - ");
		b.append(getRefreshToken());
		b.append("\n");	
		
		return b.toString();
	}
}
