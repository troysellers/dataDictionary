package com.force.aus;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetSObjectsResponse extends RestResponse {

	private List<SObject> sObjects;
	
	@Override
	protected void processResponseContent() throws JSONException {
		
		json = new JSONObject(responseContent);
		JSONArray sObjectArray = json.getJSONArray("sobjects");
		sObjects = new ArrayList<SObject>();
		
		for(int i=0 ; i<sObjectArray.length() ; i++) {
			JSONObject obj = sObjectArray.getJSONObject(i);
			sObjects.add(new SObject(obj.toString()));
		}
	}

	/**
	 * Returns the list of SObjects that are returned as part of the 
	 * getSObjtects response. 
	 * @return
	 */
	public List<SObject> getsObjects() {
		return sObjects;
	}
	
}
