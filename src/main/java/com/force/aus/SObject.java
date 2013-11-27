package com.force.aus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Returned as a list from either a query result or from a describe.
 * If from a describe call it will contain a URLs attribute 
 * 
 * @author tsellers
 *
 */
public class SObject extends RestResponse {


	private JSONObject urls;	
	private List<SObjectField> fields;
	private String sObjectName;
	
	public SObject() {}
	
	public SObject(String content) throws JSONException{
		super(content);
	}
	
	@Override
	protected void processResponseContent() {
		
		try {
			json = new JSONObject(responseContent);
			if(json.has("urls"))
				urls = json.getJSONObject("urls");
		} catch (JSONException je) {
			je.printStackTrace();
			throw new RuntimeException();
		}
	}
	/**
	 * Will return the JSONObject representation of data or metadata.
	 * 
	 * @return
	 */
	public JSONObject getJSONObject() {
		return json;
	}
	/**
	 * Will need to be tested for null.
	 * 
	 * @return
	 */
	public JSONObject getURLS() {
		return urls;
	}

	/**
	 * Returns list of Fields for this SObject
	 * @return
	 */
	public List<SObjectField> getFields() {
		return fields;
	}

	/**
	 * Sets list of fields for this SObject
	 * @param fields
	 */
	public void setFields(List<SObjectField> fields) {
		this.fields = fields;
	}
	/**
	 * Get the API Name of this SObject
	 * @return
	 */
	public String getsObjectName() {
		return sObjectName;
	}
	/**
	 * Set the API Name of this SObject
	 * 
	 * @param sObjectName
	 */
	public void setsObjectName(String sObjectName) {
		this.sObjectName = sObjectName;
	}

	/**
	 * Writes object definition to a file.
	 * File will be named as <ObjectName>.csv
	 * Will contain FieldName, FieldDescription, FieldType columns
	 * 
	 * @param directoryPath
	 */
	public void writeToFile(String directoryPath) throws IOException{
		File f = new File(directoryPath + FileSystems.getDefault().getSeparator() + sObjectName + ".csv");
		BufferedWriter writer = new BufferedWriter(new FileWriter(f));
		writer.write("Name,Label,Type\n");
		for(SObjectField field : getFields()) {
			writer.write(field.getName());
			writer.write(",");
			writer.write(field.getLabel());
			writer.write(",");
			writer.write(field.getType());
			writer.write("\n");
		}
		writer.close();
	}
	
	public void writeToWorkbook(HSSFWorkbook workbook) {
		
		Map<Integer, String[]> data = new TreeMap<Integer,String[]>();
		data.put(new Integer(0), new String[] {"Name","Label","Type"});
		Integer rowNumber = new Integer(1);
		
		for(SObjectField f : getFields()) {
			data.put(rowNumber++, new String[] {f.getName(),f.getLabel(),f.getType()});
		}
		
		HSSFSheet sheet = workbook.createSheet(getsObjectName());
		Set<Integer> keySet = data.keySet();
		
		for(Integer key : keySet) {
			HSSFRow row = sheet.createRow(key);
			String[] rowData = data.get(key);
			int cellNumber = 0;
			for(String s : rowData) {
				HSSFCell cell = row.createCell(cellNumber++);
				cell.setCellValue(s);
			}
		}
	}
}
