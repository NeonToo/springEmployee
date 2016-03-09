package com.sap.training.ems.web.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;

import com.sap.ecm.api.EcmService;
import com.sap.ecm.api.RepositoryOptions;
import com.sap.ecm.api.RepositoryOptions.Visibility;

/**
 * Servlet implementation class DocController
 */
public class DocController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected String method;
	private Session openCmisSession = null;
       
    /**
     * @throws NamingException 
     * @see HttpServlet#HttpServlet()
     */
    public DocController() throws NamingException {
//        super();
        // TODO Auto-generated constructor stub
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		method = request.getParameter("method");
		PrintWriter out = response.getWriter();

		switch (method) {
		case "getAll":
			out.println("<html><body>");
			out.println("<h3>You are now connected to the Repository with Id "
					+ openCmisSession.getRepositoryInfo().getId() + "</h3>");
			// access the root folder of the repository
			Folder root = openCmisSession.getRootFolder();
			// Display the root folder's children objects
			ItemIterable<CmisObject> children = root.getChildren();
			out.println("The root folder of the repository with id "
					+ root.getId() + " contains the following objects:<ul>");
			for (CmisObject o : children) {
				out.print("<li>");
				if (o instanceof Folder) {
					out.println(o.getName() + "</br> createdBy: "
							+ o.getCreatedBy() + "</br> folderID: " + o.getId()
							+ "</li></br>");
				} else {
					Document doc = (Document) o;
					out.println("<a href='DocController?id=" + doc.getId()
							+ "&method=show'>" + o.getName() + "</a>"
							+ "</br> createdBy: " + o.getCreatedBy()
							+ "</br> filesize: " + doc.getContentStreamLength()
							+ " bytes</br> documentID: " + " " + doc.getId()
							+ "</li>" + "<div>"
							+ "<a href='DocController?id=" + doc.getId()
							+ "&method=delete'>delete</a>&nbsp;&nbsp;"
							+ "</div></br>");
				}
			}
			break;
		case "delete":
			try {
				String id = request.getParameter("id");
				Document document = (Document) openCmisSession.getObject(id);
				document.delete(true);
				response.sendRedirect("DocController?method=getAll");
			} catch (Exception e) {

			}
			break;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
