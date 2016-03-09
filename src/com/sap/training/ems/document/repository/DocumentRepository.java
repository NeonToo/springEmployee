package com.sap.training.ems.document.repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServlet;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisNameConstraintViolationException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import com.sap.ecm.api.EcmService;
import com.sap.ecm.api.RepositoryOptions;
import com.sap.ecm.api.RepositoryOptions.Visibility;

public class DocumentRepository {
	private Session openCmisSession = null;

	/**
	 * @throws NamingException
	 * @see HttpServlet#HttpServlet()
	 */
	public DocumentRepository() throws NamingException {
		String uniqueName = "com.sap.training.ems";
		// Use a secret key only known to your application (min. 10 chars)
		String secretKey = "kay12345678";

		InitialContext ctx = new InitialContext();
		String lookupName = "java:comp/env/" + "EcmService";
		EcmService ecmSvc = (EcmService) ctx.lookup(lookupName);
		try {
			// connect to my repository
			openCmisSession = ecmSvc.connect(uniqueName, secretKey);
		} catch (CmisObjectNotFoundException e) {
			// repository does not exist, so try to create it
			RepositoryOptions options = new RepositoryOptions();
			options.setUniqueName(uniqueName);
			options.setRepositoryKey(secretKey);
			options.setVisibility(Visibility.PROTECTED);
			ecmSvc.createRepository(options);
			// should be created now, so connect to it
			openCmisSession = ecmSvc.connect(uniqueName, secretKey);
		}
	}

	public String createImg(String name, InputStream inputStream) {
		String result;
		Folder root = openCmisSession.getRootFolder();

		// create a new folder
		Map<String, String> newFolderProps = new HashMap<String, String>();
		newFolderProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
		newFolderProps.put(PropertyIds.NAME, "Images");
		try {
			root.createFolder(newFolderProps);
		} catch (CmisNameConstraintViolationException e) {
			// Folder exists already, nothing to do
		}

		// create a new file in the root folder
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
		properties.put(PropertyIds.NAME, name);
		ContentStream contentStream = openCmisSession.getObjectFactory()
				.createContentStream(name, Integer.MAX_VALUE, "image/jpeg",
						inputStream);
		Document image = null;
		try {
			image = root.createDocument(properties, contentStream, VersioningState.NONE);
			result = image.getId();
		} catch (CmisNameConstraintViolationException e) {
			// Document exists already, nothing to do
			result = "Image Already Exist!";
		}
		return result;
	}

//	public void createInitialComment() throws UnsupportedEncodingException {
//		Folder root = openCmisSession.getRootFolder();
//
//		// create a new folder
//		Map<String, String> newFolderProps = new HashMap<String, String>();
//		newFolderProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
//		newFolderProps.put(PropertyIds.NAME, "SapHANANeo");
//		try {
//			root.createFolder(newFolderProps);
//		} catch (CmisNameConstraintViolationException e) {
//			// Folder exists already, nothing to do
//		}
//
//		// create a new file in the root folder
//		Map<String, Object> properties = new HashMap<String, Object>();
//		properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
//		properties.put(PropertyIds.NAME, "HelloWorld.txt");
//		byte[] helloContent = "Hello World!".getBytes("UTF-8");
//		InputStream stream = new ByteArrayInputStream(helloContent);
//		ContentStream contentStream = openCmisSession.getObjectFactory()
//				.createContentStream("HelloWorld.txt", helloContent.length,
//						"text/plain; charset=UTF-8", stream);
//		try {
//			root.createDocument(properties, contentStream, VersioningState.NONE);
//		} catch (CmisNameConstraintViolationException e) {
//			// Document exists already, nothing to do
//		}
//	}
	
	public void deleteAll() {
		Folder root = openCmisSession.getRootFolder();
		ItemIterable<CmisObject> children = root.getChildren();
		for (CmisObject o : children) {
			if (o instanceof Folder) {
				System.out.println(" createdBy: " + o.getCreatedBy() + "</li>");
			} else {
				Document doc = (Document) o;
				System.out.println(" createdBy: " + o.getCreatedBy()
						+ " filesize: " + doc.getContentStreamLength()
						+ " bytes"
						+ " ID: " + doc.getId() + "</li>");
				doc.delete(true);
			}
		}
	}
	
	public void deleteImg(String imgId){
		Document document = (Document) openCmisSession.getObject(imgId);
		document.delete(true);
	}
	
	public InputStream getImg(String imgId) throws IOException{
		Document document = (Document) openCmisSession.getObject(imgId);
		InputStream stream = document.getContentStream().getStream();
		
		return stream;
	}
}
