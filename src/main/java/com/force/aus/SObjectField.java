package com.force.aus;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Describe information for an sObjectField
 * 
 * TODO - implement ReferenceTo fields
 * 
 * @author tsellers
 * 
 */
public class SObjectField extends RestResponse {

	private List<PicklistValue> picklistValues;
	private String name;
	private String label;
	private String type;
	public SObjectField(String content) throws JSONException {
		super(content);
	}

	@Override
	protected void processResponseContent() {
		try {

			json = new JSONObject(responseContent);

			JSONArray jsonPicklistValues = json.getJSONArray("picklistValues");
			picklistValues = new ArrayList<PicklistValue>();
			for (int i = 0; i < jsonPicklistValues.length(); i++) {
				JSONObject picklistValue = jsonPicklistValues.getJSONObject(i);
				picklistValues.add(new PicklistValue(picklistValue.toString()));
			}
			
			name = json.getString("name");
			type = json.getString("type");
			label = json.getString("label");
			
		} catch (JSONException je) {
			je.printStackTrace();
			throw new RuntimeException();
		}

	}

	public List<PicklistValue> getPicklistValues() {
		return picklistValues;
	}

	public void setPicklistValues(List<PicklistValue> picklistValues) {
		this.picklistValues = picklistValues;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
}
