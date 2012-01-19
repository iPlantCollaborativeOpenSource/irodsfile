package org.iplantc.irodsfile.model;

public class File implements Resource {
	private String name;
	private FilesystemResource filesystemResource;
	
	public File(String name, FilesystemResource resource) {
		this.name = name;
		this.filesystemResource = resource;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public FilesystemResource getFilesystemResource() {
		return filesystemResource;
	}

}
