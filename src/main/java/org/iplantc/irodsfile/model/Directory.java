package org.iplantc.irodsfile.model;

import java.util.ArrayList;
import java.util.List;

public class Directory implements Resource {
	
	private List<File> files = new ArrayList<File>();
	private List<Directory> folders = new ArrayList<Directory>();
	private String name;
	private FilesystemResource filesystemResource;
	
	public Directory(String name, FilesystemResource resource) {
		this.name = name;
		this.filesystemResource = resource;
	}
	
	public List<File> getFiles() {
		return files;
	}

	public List<Directory> getFolders() {
		return folders;
	}

	public String getName() {
		return name;
	}

	public void addDirectory(Directory folder) {
		folders.add(folder);
	}

	public void addFile(File file) {
		files.add(file);
	}

	public FilesystemResource getFilesystemResource() {
		return filesystemResource;
	}

}
