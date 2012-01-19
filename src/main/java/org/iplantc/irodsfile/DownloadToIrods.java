package org.iplantc.irodsfile;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.iplantc.irodsfile.util.LargeIOUtils;

import edu.sdsc.grid.io.irods.IRODSFile;
import edu.sdsc.grid.io.irods.IRODSFileOutputStream;

/**
 * Downloads files to iRODS.
 */
public class DownloadToIrods {

    /**
     * The iRODS username.
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
     * The iRODS port.
     */
    private String port;

    /**
     * The base URL used to connect to iRODS.
     */
    private String baseUserDir;

    /**
     * Downloads a file with the given file contents to iRODS.
     * 
     * @param username the username of the file owner.
     * @param filename the name of the file.
     * @param fileContents the contents of the file.
     * @return the iRODS file.
     * @throws IOException if an I/O error occurs.
     * @throws URISyntaxException if we attempt to use an invalid iRODS URL.
     */
    public IRODSFile downloadFile(String username, String filename, String fileContents) throws IOException,
        URISyntaxException
    {
        IRODSFile irodsFile = createIrodsFile(baseUserDir, username, filename);
        IRODSFileOutputStream os = new IRODSFileOutputStream(irodsFile);
        PrintWriter pw = new PrintWriter(os);
        pw.print(fileContents);
        pw.close();
        os.close();
        return irodsFile;
    }

    /**
     * Creates an iRODS file.
     * 
     * @param baseUserDir the base URL used to connect to iRODS.
     * @param username the username of the file owner.
     * @param filename the name of the file.
     * @return the iRODS file.
     * @throws IOException if an I/O error occurs.
     * @throws URISyntaxException if we attempt to use an invalid iRODS URL.
     */
    private IRODSFile createIrodsFile(String baseUserDir, String username, String filename) throws IOException,
        URISyntaxException
    {
        username = removeEmailDomain(username);
        IRODSFile irodsUserDir = getIrodsUserDir(username);
        return createUniqueIrodsFile(filename, irodsUserDir);
    }

    /**
     * Creates an iRODS file for which a file of the same name does not exist.
     * 
     * @param filename the name of the file.
     * @param irodsUserDir the directory in iRODS where the user's files are stored.
     * @return the iRODS file.
     */
    private IRODSFile createUniqueIrodsFile(String filename, IRODSFile irodsUserDir) {
        IRODSFile irodsFile = new IRODSFile(irodsUserDir, filename);
        if (irodsFile.exists()) {
            int extensionIndex = filename.indexOf('.');
            String filenameBase, extension;
            if (extensionIndex == -1) {
                filenameBase = filename;
                extension = "";
            }
            else {
                filenameBase = filename.substring(0, extensionIndex);
                extension = filename.substring(extensionIndex);
            }
            int dedupIndex = 0;
            do {
                dedupIndex++;
                filename = filenameBase + "-" + dedupIndex + extension;
                irodsFile = new IRODSFile(irodsUserDir, filename);
            } while (irodsFile.exists());
        }
        return irodsFile;
    }

    /**
     * Removes the e-mail address domain from the given username if the username appears to be an e-mail address.
     * 
     * @param username the username to convert.
     * @return the converted username.
     */
    private String removeEmailDomain(String username) {
        if (username.endsWith("@iplantcollaborative.org")) {
            username = username.substring(0, username.length() - "@iplantcollaborative.org".length());
        }
        return username;
    }

    /**
     * Gets an iRODS file representing the directory in iRODS where the user's files are stored.
     * 
     * @param username the username of the file owner.
     * @return an iRODS file representing the directory in iRODS where the user's files are stored.
     * @throws IOException if an I/O error occurs.
     * @throws URISyntaxException if we attempt to use a malformed iRODS URL.
     */
    private IRODSFile getIrodsUserDir(String username) throws IOException, URISyntaxException {
        IRODSFile irodsUserDir = new IRODSFile(new IRODSFile(getIrodsBaseDir(), username), "de");
        if (!irodsUserDir.exists()) {
            if (!irodsUserDir.mkdirs()) {
                throw new IOException("Could not create user directory: " + irodsUserDir.getAbsolutePath());
            }
        }
        return irodsUserDir;
    }

    /**
     * Gets an IRODSFile instance that refers to the base directory for all user's files.
     * 
     * @return the IRODSFile instance.
     * @throws IOException if an I/O error occurs.
     * @throws URISyntaxException if we try to use invalid values in the URI.
     */
    private IRODSFile getIrodsBaseDir() throws IOException, URISyntaxException {
        URI uri = new URI("irods", user + ":" + password, host, Integer.valueOf(port), baseUserDir, null, null);
        return new IRODSFile(uri);
    }

    /**
     * Writes data in the given stream to iRODS.
     * 
     * @param irodsFile the iRODS file being created.
     * @param is the input stream to get the data from.
     * @throws IOException if an I/O error occurs.
     */
    private void writeToIrods(IRODSFile irodsFile, InputStream is) throws IOException {
        IRODSFileOutputStream os = new IRODSFileOutputStream(irodsFile);
        LargeIOUtils.copyLarge(is, os);
        os.close();
    }

    /**
     * Gets an input stream that can be used to retrieve data from the given URL.
     * 
     * @param url the URL to get the data from.
     * @return the input stream used to get the data.
     * @throws URISyntaxException if the URL is malformed.
     * @throws IOException if an I/O error occurs.
     */
    private InputStream getInputStreamFromUrl(URL url) throws URISyntaxException, IOException {
        return url.openStream();
    }

    /**
     * Sets the user.
     * 
     * @param user the new user.
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Sets the password.
     * 
     * @param password the new password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the host.
     * 
     * @param host the host.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Sets the port.
     * 
     * @param port the port.
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * Sets the iRODS base user directory.
     * 
     * @param baseUserDir the new base directory.
     */
    public void setBaseUserDir(String baseUserDir) {
        this.baseUserDir = baseUserDir;
    }

    /**
     * Gets the iRODS base user directory.
     * 
     * @return the base directory.
     */
    public String getBaseUserDir() {
        return baseUserDir;
    }

}
