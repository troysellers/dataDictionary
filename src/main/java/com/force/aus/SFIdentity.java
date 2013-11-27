package com.force.aus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Holds result of call to the SF Identity service.
 * Use this for URLs and user details during an API Session.
 * 
 * @author tsellers
 *
 */
public class SFIdentity extends RestResponse{

	private JSONObject urls;
	private JSONObject photos;
	
	@Override
	protected void processResponseContent() throws JSONException {
		
		json = new JSONObject(responseContent);
		photos = json.getJSONObject("photos");
		urls = json.getJSONObject("urls");
	}
	
	public Date getDate(String key) throws ParseException, JSONException {
		if(json != null && json.has(key)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			return sdf.parse(json.getString(key));
		}
		return null;
	}
	
	public String getURL(String key, String apiVersion) throws JSONException {

		if(apiVersion == null)
			throw new NullPointerException("API Version must be set before attempting to populate URLs");

		return urls.getString(key).replaceAll("\\{version\\}", apiVersion);

	}
	
	public String getPhotoURL(String key) throws JSONException {
		return photos.getString(key);
	}
}
