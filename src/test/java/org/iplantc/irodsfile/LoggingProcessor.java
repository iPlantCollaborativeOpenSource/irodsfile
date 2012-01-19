package org.iplantc.irodsfile;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.irodsfile.model.Directory;
import org.iplantc.irodsfile.model.File;
import org.iplantc.irodsfile.model.Resource;

public class LoggingProcessor implements ResourceProcessor {

	class ProcessingOperation {

		private String op;
		private Resource resource;
		private Object parent;
		private Object context;
		
		public ProcessingOperation(String op, Resource resource, Object parent, Object context) {
			this.op = op;
			this.resource = resource;
			this.parent = parent;
			this.context = context;
		}
		
		public String getOp() {
			return op;
		}

		public Resource getResource() {
			return resource;
		}

		public Object getParent() {
			return parent;
		}
		
		public Object getContext() {
			return context;
		}
	}

	public static final String FILE = "file";
	public static final String DIRECTORY = "directory";

	private List<ProcessingOperation> operations = new ArrayList<ProcessingOperation>();
	
	public List<ProcessingOperation> getOperations() {
		return operations;
	}

	/* (non-Javadoc)
	 * @see org.iplantc.irodsfile.ResourceProcessor#processFile(org.iplantc.irodsfile.model.File, java.lang.Object)
	 */
	public void processFile(File file, Object parent, Object context) {
		operations.add(new ProcessingOperation(FILE, file, parent, context));
	}

	/* (non-Javadoc)
	 * @see org.iplantc.irodsfile.ResourceProcessor#processDirectory(org.iplantc.irodsfile.model.Directory, java.lang.Object)
	 */
	public Object processDirectory(Directory dir, Object parent, Object context) {
		operations.add(new ProcessingOperation(DIRECTORY, dir, parent, context));
		return dir;
	}

}
