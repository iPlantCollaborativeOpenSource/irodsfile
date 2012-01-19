package org.iplantc.irodsfile.model;

public class MockFilesystemFile extends MockFilesystemResource {

	public MockFilesystemFile(String name) {
		super(name);
	}

	@Override
	public boolean isDirectory() {
		return false;
	}

}
