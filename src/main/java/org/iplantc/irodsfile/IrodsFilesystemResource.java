package org.iplantc.irodsfile;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.irodsfile.model.FilesystemResource;

import edu.sdsc.grid.io.irods.IRODSFile;

public class IrodsFilesystemResource implements FilesystemResource {
	private IRODSFile irodsFile;

	public IrodsFilesystemResource(IRODSFile irodsFile) {
		this.irodsFile = irodsFile;
	}

	@Override
	public String getName() {
		return irodsFile.getName();
	}

	@Override
	public List<FilesystemResource> getResources() {
		List<FilesystemResource> ret = new ArrayList<FilesystemResource>();
		
		for (String subName : irodsFile.list()) {
			ret.add(new IrodsFilesystemResource(new IRODSFile(irodsFile, subName)));
		}
		
		return ret;
	}

	@Override
	public boolean isDirectory() {
		return irodsFile.isDirectory();
	}

	public IRODSFile getIrodsFile() {
		return irodsFile;
	}

    @Override
    public boolean exists() {
        return irodsFile.exists();
    }
}
