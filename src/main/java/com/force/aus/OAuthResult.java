package com.force.aus;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Returned as the result of an OAuth login.
 * Can also be populated manually (e.g. from a Canvas Request)
 * 
 * @author tsellers
 *
 */
public class OAuthResult extends RestResponse {


	private String idURL;
	private String instanceURL;
	private Date issuedAt;
	private String signature;
	private String accessToken;
	
	private SFIdentity identity;
	
	@Override
	protected void processResponseContent() {
		try {
			
			json = new JSONObject(responseContent);
			idURL = json.getString("id");
			instanceURL = json.getString("instance_url");
			issuedAt = new Date(Long.valueOf(json.getString("issued_at")));
			signature = json.getString("signature");
			accessToken = json.getString("access_token");
			
		}catch (JSONException je) {
			je.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public String getIdURL() {
		return idURL;
	}

	public String getInstanceURL() {
		return instanceURL;
	}

	public Date getIssuedAt() {
		return issuedAt;
	}

	public String getSignature() {
		return signature;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setIdentity(SFIdentity identity) {
		this.identity = identity;
	}
	
	public SFIdentity getIdentity() {
		return identity;
	}
	
	public String print() {
		StringBuilder b = new StringBuilder();
		
		b.append("ID URL [");
		b.append(getIdURL());
		b.append("]\n");
		
		b.append("Instance URL [");
		b.append(getInstanceURL());
		b.append("]\n");
		
		b.append("Issued At [");
		b.append(getIssuedAt());
		b.append("]\n");
		
		b.append("Signature [");
		b.append(getSignature());
		b.append("]\n");		
		
		b.append("Access Token [");
		b.append(getAccessToken());
		b.append("]\n");		
				
		return b.toString();
	}
}
