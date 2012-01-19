package org.iplantc.irodsfile;

import org.iplantc.irodsfile.model.Directory;
import org.iplantc.irodsfile.model.File;
import org.iplantc.irodsfile.model.Resource;

public class MirrorDirectoryStructure {
	
	private ResourceProcessor resourceProcessor;

	public void mirror(Resource resource, Object parent) {
		mirror(resource, parent, null);
	}
	
	public void mirror(Resource resource, Object parent, Object context) {
		if (resource instanceof File) {
			resourceProcessor.processFile((File)resource, parent, context);			
		} else if (resource instanceof Directory) {
			Directory dir = (Directory)resource;
			Object newParent = resourceProcessor.processDirectory(dir, parent, context);
			for (Resource subResource : dir.getFolders()) {
				mirror(subResource, newParent, context);
			}
			for (Resource subResource : dir.getFiles()) {
				mirror(subResource, newParent, context);
			}
		}
	}

	public void setResourceProcessor(ResourceProcessor rp) {
		this.resourceProcessor = rp;
	}

}
