package com.force.aus.test.resthelper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Properties;

import org.apache.http.auth.AuthenticationException;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.force.aus.GetSObjectDescribeResponse;
import com.force.aus.GetSObjectsResponse;
import com.force.aus.GrantType;
import com.force.aus.OAuthProperties;
import com.force.aus.OAuthResult;
import com.force.aus.SFRestHelper;
import com.force.aus.SObject;

@RunWith(JUnit4.class)
public class TestQueries {
	
	private Logger logger;
	private OAuthProperties oauthProps;
	private SFRestHelper helper;
	private OAuthResult oAuth;
	@Before
	public void setup() throws IOException, AuthenticationException, JSONException{
		
		logger = LoggerFactory.getLogger(this.getClass());
		Properties props = new Properties();
		props.load(TestAuthFlows.class.getClassLoader().getResourceAsStream("test.properties"));

		oauthProps = new OAuthProperties();
		oauthProps.setUsername(props.getProperty("sforce.user"));
		oauthProps.setPassword(props.getProperty("sforce.pass"));
		oauthProps.setSecurityToken(props.getProperty("sforce.security.token"));
		oauthProps.setClientId(props.getProperty("sforce.oauth.id"));
		oauthProps.setClientSecret(props.getProperty("sforce.oauth.secret"));
		oauthProps.setHost(props.getProperty("sforce.host"));
		oauthProps.setApiVersion(props.getProperty("sforce.api.version"));
		oauthProps.setGrantType(GrantType.PASSWORD);
		
		helper = new SFRestHelper();
		oAuth = helper.login(oauthProps, true);
	}
		
	@Test
	public void testGetSObjects() throws JSONException {
			
		GetSObjectsResponse response = helper.getSObjects(oAuth, oauthProps.getApiVersion());
		assertNotNull(response.getsObjects());
		assertTrue(response.getsObjects().size() > 0);
	}
	
	@Test
	public void testGetSObject() throws JSONException, IOException {
		GetSObjectsResponse response = helper.getSObjects(oAuth, oauthProps.getApiVersion());
		for(SObject obj : response.getsObjects()) {
			logger.info("Calling object describe {}", obj.getURLS().get("describe"));
			GetSObjectDescribeResponse objResponse = helper.getSObjectDescribe(oAuth, obj);
			assertNotNull(objResponse.getSObject());
			assertNotNull(objResponse.getFields());
			break;
		}
	}
}
