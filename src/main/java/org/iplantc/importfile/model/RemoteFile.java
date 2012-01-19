package org.iplantc.importfile.model;

public class RemoteFile {
	private String name;
	private byte[] contents;
	private String url;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public byte[] getContents() {
		return contents;
	}
	
	public void setContents(byte[] contents) {
		this.contents = contents;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
