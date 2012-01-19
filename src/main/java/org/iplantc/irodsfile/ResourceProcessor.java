package org.iplantc.irodsfile;

import org.iplantc.irodsfile.model.Directory;
import org.iplantc.irodsfile.model.File;

public interface ResourceProcessor {

	void processFile(File file, Object parent, Object context);

	Object processDirectory(Directory dir, Object parent, Object context);

}