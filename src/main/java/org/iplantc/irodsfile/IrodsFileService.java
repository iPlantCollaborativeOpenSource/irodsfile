package org.iplantc.irodsfile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import edu.sdsc.grid.io.irods.IRODSFile;
import edu.sdsc.grid.io.irods.IRODSFileInputStream;

public class IrodsFileService {
	private String adminUser;
	private String adminPassword;
	
	public void deleteFile(String irodsUrl) throws IOException, URISyntaxException {
		URI irodsUriWithAuth = getAuthenticatedUri(irodsUrl);
		IRODSFile irodsFile = new IRODSFile(irodsUriWithAuth);
		
		try {
			// We don't want to delete data in the test data area
			// TODO - remove this hack once proper workspace sharing has been implemented
			// and we can share the test data workspace with all users
			if (irodsFile.getAbsolutePath().contains("/de_testdata/")) {
				return;
			}
			
			if (!irodsFile.delete()) {
				throw new IOException("Could not delete iRODS file at " + irodsUrl);
			}
		} finally {
			if (irodsFile != null) {
				irodsFile.close();
			}
		}
	}

	public void createDirectory(String irodsUrl) throws URISyntaxException, IOException {
		URI irodsUriWithAuth = getAuthenticatedUri(irodsUrl);
		IRODSFile irodsFile = new IRODSFile(irodsUriWithAuth);

		try {
			if (irodsFile.exists()) {
				throw new IOException("Could not create iRODS directory at " + irodsUrl + ": file already exists at path");
			}
			if (!irodsFile.mkdirs()) {
				throw new IOException("Could not create iRODS directory at " + irodsUrl);
			}
		} finally {
			if (irodsFile != null) {
				irodsFile.close();
			}
		}
	}
	
	
	public List<String> getIRodsFolderContents(String folderUrl) throws IOException, URISyntaxException {
		URI irodsUriWithAuth = getAuthenticatedUri(folderUrl);
		IRODSFile irodsFile = new IRODSFile(irodsUriWithAuth);
		
		List<String> contents = new LinkedList<String>();
		String[] contents_s =irodsFile.list();
		
		for(int i=0; i < contents_s.length;i++){
			contents.add(contents_s[i]);
		}
		
		return contents;
	}
	
	public InputStream retrieveFileInputStream(String irodsUrl) throws IOException, URISyntaxException {
		URI irodsUriWithAuth = getAuthenticatedUri(irodsUrl);
		IRODSFile irodsFile = new IRODSFile(irodsUriWithAuth);
		IRODSFileInputStream inputStream = new IRODSFileInputStream(irodsFile);
		return inputStream;
	}
	
	public void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
	}

	public String getAdminUser() {
		return adminUser;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public URI getAuthenticatedUri(String irodsUrl) throws URISyntaxException {
		URI irodsUri = new URI(irodsUrl);
		URI irodsUriWithAuth = new URI(irodsUri.getScheme(), adminUser + ":" + adminPassword, irodsUri.getHost(), irodsUri.getPort(), irodsUri.getPath(), irodsUri.getQuery(), irodsUri.getFragment());
		return irodsUriWithAuth;
	}
}
