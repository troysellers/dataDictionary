package com.force.aus;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Captures a picklist value object
 * 
 * @author tsellers
 *
 */
public class PicklistValue extends RestResponse {

	private String validFor;
	private boolean defaultValue;
	private String value;
	private boolean active;
	private String label;
	
	public PicklistValue(String content) throws JSONException {
		super(content);
	}
	
	@Override
	protected void processResponseContent() throws JSONException {

		json = new JSONObject(responseContent);
		validFor = json.getString("validFor");
		defaultValue = json.getBoolean("defaultValue");
		value = json.getString("value");
		active = json.getBoolean("active");
		label = json.getString("label");

	}

	public String getValidFor() {
		return validFor;
	}
	public boolean isDefaultValue() {
		return defaultValue;
	}
	public String getValue() {
		return value;
	}
	public boolean isActive() {
		return active;
	}
	public String getLabel() {
		return label;
	}	
}
