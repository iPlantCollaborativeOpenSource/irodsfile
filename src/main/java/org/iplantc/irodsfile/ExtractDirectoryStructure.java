package org.iplantc.irodsfile;

import org.iplantc.irodsfile.model.Directory;
import org.iplantc.irodsfile.model.File;
import org.iplantc.irodsfile.model.FilesystemResource;
import org.iplantc.irodsfile.model.Resource;

public class ExtractDirectoryStructure {
	
	public Resource extract(FilesystemResource resource) {
		if (!resource.isDirectory()) {
			return new File(resource.getName(), resource);
		}

		Directory folder = new Directory(resource.getName(), resource);

		for (FilesystemResource subResource : resource.getResources()) {
			if (subResource.isDirectory()) {
				folder.addDirectory((Directory)extract(subResource));
			} else {
				folder.addFile(new File(subResource.getName(), subResource));
			}
		}
		
		return folder;
	}
	
}
