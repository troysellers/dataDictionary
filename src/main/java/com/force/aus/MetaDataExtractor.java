package com.force.aus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaDataExtractor {

	private Properties appProps;
	private Logger logger;
	private OAuthProperties oauthProps;
	
	public static void main(String[] args) {
		MetaDataExtractor extractor = new MetaDataExtractor();
		extractor.run();
	}
	
	private void run() {
		logger = LoggerFactory.getLogger(this.getClass());
		
		// initialise application properties and files for write
		SFRestHelper restHelper = new SFRestHelper();
		initialise();
		
		// clean file output directory
		cleanFileOutputDirectory();		
		
		// login to the salesforce instance
		OAuthResult oauthResult = login(restHelper);
		
		// call to get list of all SObjects
		writeSObjects(oauthResult, restHelper);
		
		logger.info("Complete...");
	} 
	
	private void initialise() {
		logger.info("Loading application properties...");
		appProps = new Properties();
		try {
			appProps.load(new FileInputStream("app.properties"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new RuntimeException ("Unable to read application properties file");
		}
		logger.info("Creating Salesforce OAuth Properties...");
		oauthProps = new OAuthProperties();
		oauthProps.setUsername(appProps.getProperty("sforce.user"));
		oauthProps.setPassword(appProps.getProperty("sforce.pass"));
		oauthProps.setSecurityToken(appProps.getProperty("sforce.security.token"));
		oauthProps.setClientId(appProps.getProperty("sforce.oauth.id"));
		oauthProps.setClientSecret(appProps.getProperty("sforce.oauth.secret"));
		oauthProps.setHost(appProps.getProperty("sforce.host"));
		oauthProps.setApiVersion(appProps.getProperty("sforce.api.version"));	
	}
	
	private void cleanFileOutputDirectory() {
		logger.info("Cleaning file directory ["+appProps.getProperty("metadata.file.directory")+"]");
		File fileDirectory = new File(appProps.getProperty("metadata.file.directory"));
		if(!fileDirectory.exists()) {
			fileDirectory.mkdirs();
		}
		if(!fileDirectory.isDirectory()) {
			throw new RuntimeException("Directory path specified in app.properties file 'metadata.file.directory' must be a directory!");
		}
		// go through files in directory and delete
		File[] files = fileDirectory.listFiles();
		for(File f : files) {
			if(f.getName().contains(".csv")) {
				f.delete();
			}
		}
	}
	
	private OAuthResult login(SFRestHelper restHelper) {
		logger.info("Logging into Salesforce instance ["+oauthProps.getHost()+"] with User ["+oauthProps.getUsername()+"]");
		oauthProps.setGrantType(GrantType.PASSWORD);
		try {
			return restHelper.login(oauthProps, true);		
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new RuntimeException("Unable to login");
		} catch (JSONException joe) {
			joe.printStackTrace();
			throw new RuntimeException("JSON Exception logging in");
		}
	}
	
	private List<SObject> writeSObjects(OAuthResult oAuth, SFRestHelper helper) {
		logger.info("Getting SObjects in org ... ");
		List<SObject> sObjects = new ArrayList<SObject>();
		int fieldCount = 0;
		int objectCount = 0;
		
		try {
			
			File summary = new File(appProps.getProperty("metadata.file.directory")+ FileSystems.getDefault().getSeparator()+"summary.txt");
			FileWriter summaryWriter = new FileWriter(summary);
			
			File errors = new File(appProps.getProperty("metadata.file.directory")+ FileSystems.getDefault().getSeparator()+"errors.txt");
			FileWriter errorWriter = new FileWriter(errors);
			
			GetSObjectsResponse response = helper.getSObjects(oAuth, oauthProps.getApiVersion());
			logger.info("We have {} objects", response.getsObjects().size());
			objectCount = response.getsObjects().size();
			for(SObject obj : response.getsObjects()) {
				
				File workbookFile = new File(appProps.getProperty("metadata.file.directory")+ FileSystems.getDefault().getSeparator()+"dataDictionary.xls");
				FileInputStream fis = new FileInputStream(workbookFile);
				HSSFWorkbook workbook = new HSSFWorkbook(fis);
				
				GetSObjectDescribeResponse res = helper.getSObjectDescribe(oAuth, obj);
				sObjects.add(res.getSObject());
				
				if(Boolean.valueOf(appProps.getProperty("dictionary.write.objects"))) {
					obj.writeToFile(appProps.getProperty("metadata.file.directory"));
					logger.info("Added ["+obj.getsObjectName()+"] written to Object file...");
				}
				
				if(Boolean.valueOf(appProps.getProperty("dictionary.write.xls"))) {
					logger.info("Writting ["+obj.getsObjectName()+"] to be workbook...");
					if(obj.getsObjectName().length() < 32) {
						obj.writeToWorkbook(workbook);
						logger.info("Added ["+obj.getsObjectName()+"] written to workbook...");
					} else {
						logger.error("We are skipping {} because name is too long", obj.getsObjectName());
						errorWriter.write("Unable to create Workbook sheet for object ["+obj.getsObjectName()+"] as name is too long!\n");
					}
				}
				summaryWriter.write("Object ["+obj.getsObjectName()+"] contains ["+obj.getFields().size()+"] fields\n");
				fieldCount += obj.getFields().size();
				FileOutputStream outputStream = new FileOutputStream(workbookFile);
				workbook.write(outputStream);
				outputStream.close();
			}
			summaryWriter.write("We have written "+objectCount+" objects for a total of "+fieldCount+" fields");
			summaryWriter.close();
			errorWriter.close();
			
		} catch (JSONException joe) {
			joe.printStackTrace();
			throw new RuntimeException("JSON Exception while getting SObject List");
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new RuntimeException("IOException while getting SObject list");
		}
		return sObjects;
	}
}
