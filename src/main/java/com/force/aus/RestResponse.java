package com.force.aus;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RestResponse {

	protected String responseContent;
	protected Logger logger;
	
	protected JSONObject json;
	
	public RestResponse() {
		logger = LoggerFactory.getLogger(this.getClass());
	};
	/**
	 * Calls the processResponseContent() method immediately.
	 * If this is not the behaviour you want then call the no arg constructor.
	 * 
	 * @param responseContent
	 */
	public RestResponse(String responseContent) throws JSONException {
		logger = LoggerFactory.getLogger(this.getClass());
		setResponseContent(responseContent);
	}
	
	public String getResponseContent() {
		return responseContent;
	}
	/**
	 * Calls processResponseContent() immediately upon setting of String content.
	 * 
	 * @param responseContent
	 */
	public void setResponseContent(String responseContent) throws JSONException{
		logger.debug("Setting response content ----- \n{}\n-----------",responseContent);
		this.responseContent = responseContent;
		processResponseContent();
	}
	/**
	 * Method processes rest response into useable data inside object.
	 * Concrete implementation to be provided by implementing class.
	 */
	protected abstract void processResponseContent() throws JSONException;
	
	/**
	 * Get the JSONObject to call parameters from.
	 * 
	 * @return
	 */
	public JSONObject getJSONObject() {
		return json;
	}
}
