package org.iplantc.irodsfile.model;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.irodsfile.model.FilesystemResource;

public abstract class MockFilesystemResource implements FilesystemResource {

	private String name;
	private List<FilesystemResource> resources = new ArrayList<FilesystemResource>();

	public MockFilesystemResource(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract boolean isDirectory();

	public void addResource(MockFilesystemResource resource) {
		resources.add(resource);
	}

	public List<FilesystemResource> getResources() {
		return resources;
	}

	public boolean exists() {
	    return true;
	}
}
