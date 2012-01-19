package org.iplantc.irodsfile.model;

public class MockFilesystemFolder extends MockFilesystemResource {

	public MockFilesystemFolder(String name) {
		super(name);
	}

	@Override
	public boolean isDirectory() {
		return true;
	}

}
