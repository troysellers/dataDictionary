package com.force.aus;

import org.json.JSONException;
import org.json.JSONObject;

public class RecordTypeInfo extends RestResponse {

	private String layoutURL;
	private String recordTypeId;
	private boolean defaultRecordTypeMapping;
	private String name;
	private boolean available;
	
	public RecordTypeInfo(String content) throws JSONException{
		super(content);
	}
	@Override
	protected void processResponseContent() {
		
		try {
			
			json = new JSONObject(responseContent);
			layoutURL = json.getJSONObject("urls").getString("layout");
			recordTypeId = json.getString("recordTypeId");
			defaultRecordTypeMapping = json.getBoolean("defaultRecordTypeMapping");
			name = json.getString("name");
			available = json.getBoolean("available");
			
		} catch (JSONException je) {
			je.printStackTrace();
			throw new RuntimeException();
		}
		
	}

	public String getLayoutURL() {
		return layoutURL;
	}

	public String getRecordTypeId() {
		return recordTypeId;
	}

	public boolean isDefaultRecordTypeMapping() {
		return defaultRecordTypeMapping;
	}

	public String getName() {
		return name;
	}

	public boolean isAvailable() {
		return available;
	}	
}
