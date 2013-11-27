package com.force.aus.test.resthelper;

import static org.junit.Assert.*;

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

import com.force.aus.GrantType;
import com.force.aus.OAuthProperties;
import com.force.aus.OAuthResult;
import com.force.aus.SFRestHelper;

@RunWith(JUnit4.class)
public class TestAuthFlows {

	private static Logger logger;
	private OAuthProperties oauthProps;
	private SFRestHelper restHelper;

	@Before
	public void setupTests() throws Exception {

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
	}

	@Test
	public void TestPasswordFlow() throws AuthenticationException, JSONException, IOException {

		oauthProps.setGrantType(GrantType.PASSWORD);
		restHelper = new SFRestHelper();
		OAuthResult result = restHelper.login(oauthProps, false);
		assertNotNull(result.getAccessToken());
		assertNull(result.getIdentity());
	}

	@Test
	public void TestLoginGetIdentity() throws AuthenticationException, JSONException, IOException {
		oauthProps.setGrantType(GrantType.PASSWORD);
		restHelper = new SFRestHelper();
		OAuthResult result = restHelper.login(oauthProps, true);
		assertNotNull(result.getIdentity());
		String userId = result.getIdentity().getJSONObject().getString("user_id");
		assertTrue(userId != null && userId.length() == 18);
	}
	@Test
	public void TestWebserverFlow() {
		logger.info("Test WebserverFlow");
	}

	@Test
	public void TestRefreshToken() {
		logger.info("Test Refresh Token");
	}

}
