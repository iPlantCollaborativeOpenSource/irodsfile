package org.iplantc.irodsfile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.iplantc.importfile.model.RemoteFile;

import edu.sdsc.grid.io.irods.IRODSFile;
import edu.sdsc.grid.io.irods.IRODSFileInputStream;

public class ExtractIrodsFile {
    private int headerBytesToRead = 8192;

    /**
     * The iRODS user name.
     */
    private String user;

    /**
     * The iRODS password.
     */
    private String password;

    /**
     * The iRODS host.
     */
    private String host;

    /**
     * The iRODS port number.
     */
    private String port;

    public RemoteFile transform(IRODSFile file) throws IOException, URISyntaxException {
        RemoteFile remoteFile = new RemoteFile();
        remoteFile.setName(file.getName());
        remoteFile.setUrl(irodsFileToUri(file));

        InputStream is = new IRODSFileInputStream(file);
        int totalRead = 0;
        byte contentsBuffer[] = new byte[headerBytesToRead];
        while (totalRead < headerBytesToRead) {
            int bytesRead = is.read(contentsBuffer, totalRead, headerBytesToRead - totalRead);
            if (bytesRead == -1) {
                break;
            }
            totalRead += bytesRead;
        }
        byte contents[] = new byte[totalRead];
        System.arraycopy(contentsBuffer, 0, contents, 0, totalRead);
        remoteFile.setContents(contents);

        return remoteFile;
    }

    /**
     * Builds the URI used to access a given iRODS file. Jargon seems to put empty strings in the query string and
     * fragment fields, causing an extraneous ?# to be tacked on to the end of the URL string. An extra conversion
     * is done because of that. The user information, host and port from the given URL are also discarded and replaced
     * with the information from our configuration file. This is done so that URLs that are sent over the wire don't
     * have to contain this information.
     * 
     * @param file the iRODS file object.
     * @return the URI as a string.
     * @throws URISyntaxException if the URI is formatted incorrectly.
     */
    public String irodsFileToUri(IRODSFile file) throws URISyntaxException {
        URI fileUri = file.toURI();
        String userInfo = buildUserInfo();
        fileUri = new URI(fileUri.getScheme(), userInfo, host, Integer.valueOf(port), fileUri.getPath(), null, null);
        return fileUri.toASCIIString();
    }

    /**
     * Builds the iRODS file object associated with the given URI. The user information, host, and port from the given
     * URI are also discarded and replace with the information from our configuration file.  This is done so that URLs
     * that are sent over the wire don't have to contain this information.
     * 
     * @param uriString the URI as a string.
     * @return the iRODS file object.
     * @throws URISyntaxException if the URI is formatted incorrectly.
     * @throws IOException if an I/O error occurs.
     */
    public IRODSFile uriToIrodsFile(String uriString) throws URISyntaxException, IOException {
        URI originalUri = new URI(uriString);
        String userInfo = buildUserInfo();
        URI newUri = new URI(originalUri.getScheme(), userInfo, host, Integer.valueOf(port), originalUri.getPath(),
            null, null);
        return new IRODSFile(newUri);
    }

    private String buildUserInfo() {
        String userInfo = null;
        if (user != null && password != null) {
            userInfo = user + ":" + password;
        }
        return userInfo;
    }

    public void setHeaderBytesToRead(int headerBytesToRead) {
        this.headerBytesToRead = headerBytesToRead;
    }

    public int getHeaderBytesToRead() {
        return headerBytesToRead;
    }

    /**
     * Sets the iRODS user name.
     * 
     * @param user the new user name.
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Sets the iRODS password.
     * 
     * @param password the new password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the iRODS host.
     * 
     * @param host the new host.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Sets the iRODS port.
     * 
     * @param port the new port.
     */
    public void setPort(String port) {
        this.port = port;
    }
}
