package com.force.aus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * The SFRestHelper is designed to allow simple access to the 
 * SF Rest APIs that are available as of time of writing (V29.0)
 * 
 * TODO - Exception handling. Currently throwing RuntimeExceptions
 * 
 * @author tsellers@salesforce.com
 *
 */
public class SFRestHelper {
	
	private Logger logger;

	public SFRestHelper() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

	/**
	 * Logs into the Salesforce service using OAuthProperties configured in the constructor.
	 * 
	 * You should use the Identity service once as it can take a while to return. 
	 * It provides the information about logged in user as well as all other URLs that 
	 * the SF api provides.
	 * 
	 * 
	 * 
	 * @param getIdentity
	 * @return 
	 * @throws AuthenticationException
	 */
	public OAuthResult login(OAuthProperties props, boolean getIdentity) throws JSONException, IOException {

		OAuthResult oauthResult = new OAuthResult();
		logger.debug("Login to Salesforce ----\n{}",props.getLogString());
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("grant_type", props.getGrantType().getValue());
		params.put("client_id", props.getClientId());
		params.put("client_secret", props.getClientSecret());
		params.put("username", props.getUsername());
		params.put("password", props.getPassword());
		
		CloseableHttpClient client = HttpClients.createDefault();
		doPost(props.getTokenURL(), client, params, oauthResult, null);
		if(getIdentity) {
			SFIdentity identity = new SFIdentity();
			params = new HashMap<String, String>();
			doGet(oauthResult.getIdURL(), client, params, identity, oauthResult);
			oauthResult.setIdentity(identity);
		}

		client.close();
		return oauthResult;
	}
	/**
	 * 
	 * @param oAuth
	 * @return
	 */
	public GetSObjectsResponse getSObjects(OAuthResult oAuth, String apiVersion) throws JSONException {
		GetSObjectsResponse result = new GetSObjectsResponse();
		
		CloseableHttpClient client = HttpClients.createDefault();
		Map<String,String> params = new HashMap<String, String>();
		doGet(oAuth.getIdentity().getURL("sobjects", apiVersion), client, params, result, oAuth);
		
		return result;
	}
	/**
	 * Describes an Object.
	 * 
	 * @param objectName
	 * @return
	 */
	public GetSObjectDescribeResponse getSObjectDescribe(OAuthResult oAuth, SObject sObject) throws JSONException, IOException {
		
		GetSObjectDescribeResponse result = new GetSObjectDescribeResponse();
		
		result.setSObject(sObject);
		
		CloseableHttpClient client = HttpClients.createDefault();
		Map<String, String> params = new HashMap<String, String>();			
		

		doGet(oAuth.getInstanceURL() + sObject.getURLS().getString("describe"), client, params, result, oAuth);
		client.close();

		return result;
	}
	
	private void doGet(String url, CloseableHttpClient client, Map<String, String> params, RestResponse requiredResponse, OAuthResult oAuth) throws JSONException {
		
		String result = "";
		try {
			URI uri = buildUri(url, params);
			
			logger.debug("GET - {}", uri.toASCIIString());
			
			HttpGet httpget = new HttpGet(uri);
			httpget.setHeader("content-type","application/x-www-form-urlencoded");
			if(oAuth != null)
				httpget.setHeader("Authorization: ", "OAuth " + oAuth.getAccessToken());
			
			CloseableHttpResponse response = client.execute(httpget);
			if(response.getStatusLine().getStatusCode() == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				result = reader.readLine();
			} else {
				logger.warn("HTTP Get has returned {} - {}", response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
				result = response.getStatusLine().getReasonPhrase();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		requiredResponse.setResponseContent(result);
	}	
	
	private void doPost(String url, CloseableHttpClient client, Map<String,String> params, RestResponse requiredResponse, OAuthResult oAuth) throws JSONException{
		
		String result = "";
		try {
			URI uri = buildUri(url, params);
			logger.debug("POST - {}", uri.toURL());

			HttpPost httpPost = new HttpPost(uri);
			
			httpPost.setHeader("content-type","application/x-www-form-urlencoded");
			if(oAuth != null)
				httpPost.setHeader("Authorization: ", "OAuth " + oAuth.getAccessToken());
			
			CloseableHttpResponse response = client.execute(httpPost);
			if(response.getStatusLine().getStatusCode() == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				result = reader.readLine();
			} else {
				logger.warn("HTTP Get has returned {} - {}", response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
				result = response.getStatusLine().getReasonPhrase();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		requiredResponse.setResponseContent(result);
	}	
	
	private URI buildUri(String url, Map<String, String> parameters) throws URISyntaxException {

		URIBuilder builder = new URIBuilder(url);

		for (String key : parameters.keySet()) {
			builder.setParameter(key, parameters.get(key));
		}

		return builder.build();
	}	
}
