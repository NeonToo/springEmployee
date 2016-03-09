package com.sap.training.ems.document.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.sap.training.ems.document.repository.DocumentRepository;

public class DocumentService {
	private DocumentRepository documentRepository = null;

	public DocumentService() throws NamingException {
		documentRepository = new DocumentRepository();
	}

	public void createImg(HttpServletResponse response,
			HttpServletRequest request) {
		String tempPath = "C:\\Dev";
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		factory.setRepository(new File(tempPath));
		try {
			List<FileItem> fileItems = upload.parseRequest(request);
			Iterator<FileItem> iterator = fileItems.iterator();
			while (iterator.hasNext()) {
				FileItem item = iterator.next();
				if (item.isFormField()) { // if normal data
					System.out.println("Normal Data" + item.getFieldName());
					System.out.println(item.getString("utf-8"));
				} else { // if file
					// get file's name and content
					String filename = item.getName();
					InputStream is = item.getInputStream();

					String imgId = documentRepository.createImg(filename, is);
					if (!imgId.equals("Image Already Exist!")) {
						response.addCookie(new Cookie("imgId", imgId));
					} else {
						response.addCookie(new Cookie("imgId", ""));
					}
				}
			}
		} catch (FileUploadException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteImg(String imgId) {
		documentRepository.deleteImg(imgId);
	}

	public void getImg(HttpServletResponse response,
			HttpServletRequest request) throws IOException {

		String imgId = request.getParameter("imgId");

		response.setContentType("image/jpeg");
		IOUtils.copy(documentRepository.getImg(imgId),
				response.getOutputStream());
	}
}
