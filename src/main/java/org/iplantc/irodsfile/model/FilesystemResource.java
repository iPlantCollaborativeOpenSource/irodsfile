package org.iplantc.irodsfile.model;

import java.util.List;

public interface FilesystemResource {

	boolean isDirectory();
	String getName();
	List<FilesystemResource> getResources();
	boolean exists();
}
