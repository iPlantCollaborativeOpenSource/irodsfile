package org.iplantc.irodsfile;

import org.iplantc.irodsfile.model.Directory;
import org.iplantc.irodsfile.model.File;

import junit.framework.TestCase;

public class TestMirrorDirectoryStructure extends TestCase {
	public void testSingleFile() {
		File file = new File("foo", null);
		MirrorDirectoryStructure mds = new MirrorDirectoryStructure();
		LoggingProcessor lp = new LoggingProcessor();
		mds.setResourceProcessor(lp);
		mds.mirror(file, null);
		assertEquals(1, lp.getOperations().size());
		assertEquals(LoggingProcessor.FILE, lp.getOperations().get(0).getOp());
		assertEquals(file, lp.getOperations().get(0).getResource());
		assertEquals(null, lp.getOperations().get(0).getParent());
	}
	
	public void testSingleEmptyDirectory() {
		Directory dir = new Directory("foo", null);
		MirrorDirectoryStructure mds = new MirrorDirectoryStructure();
		LoggingProcessor lp = new LoggingProcessor();
		mds.setResourceProcessor(lp);
		mds.mirror(dir, null);
		assertEquals(1, lp.getOperations().size());
		assertEquals(LoggingProcessor.DIRECTORY, lp.getOperations().get(0).getOp());
		assertEquals(dir, lp.getOperations().get(0).getResource());
		assertEquals(null, lp.getOperations().get(0).getParent());		
	}
	
	public void testDirectoryWithFiles() {
		Directory dir = new Directory("foo", null);
		File file1 = new File("foo1", null);
		File file2 = new File("foo2", null);
		File file3 = new File("foo3", null);
		dir.addFile(file1);
		dir.addFile(file2);
		dir.addFile(file3);
		MirrorDirectoryStructure mds = new MirrorDirectoryStructure();
		LoggingProcessor lp = new LoggingProcessor();
		mds.setResourceProcessor(lp);
		mds.mirror(dir, null);
		assertEquals(4, lp.getOperations().size());
		assertEquals(LoggingProcessor.DIRECTORY, lp.getOperations().get(0).getOp());
		assertEquals(dir, lp.getOperations().get(0).getResource());
		assertEquals(null, lp.getOperations().get(0).getParent());		
		assertEquals(LoggingProcessor.FILE, lp.getOperations().get(1).getOp());
		assertEquals(file1, lp.getOperations().get(1).getResource());
		assertEquals(dir, lp.getOperations().get(1).getParent());		
		assertEquals(LoggingProcessor.FILE, lp.getOperations().get(2).getOp());
		assertEquals(file2, lp.getOperations().get(2).getResource());
		assertEquals(dir, lp.getOperations().get(2).getParent());		
		assertEquals(LoggingProcessor.FILE, lp.getOperations().get(3).getOp());
		assertEquals(file3, lp.getOperations().get(3).getResource());
		assertEquals(dir, lp.getOperations().get(3).getParent());		
	}
	
	public void testNestedDirectory() {
		Directory dir = new Directory("foo", null);
		Directory dir2 = new Directory("bar", null);
		Directory dir3 = new Directory("baz", null);
		dir.addDirectory(dir2);
		dir2.addDirectory(dir3);
		MirrorDirectoryStructure mds = new MirrorDirectoryStructure();
		LoggingProcessor lp = new LoggingProcessor();
		mds.setResourceProcessor(lp);
		mds.mirror(dir, null);
		assertEquals(3, lp.getOperations().size());
		assertEquals(LoggingProcessor.DIRECTORY, lp.getOperations().get(0).getOp());
		assertEquals(dir, lp.getOperations().get(0).getResource());
		assertEquals(null, lp.getOperations().get(0).getParent());		
		assertEquals(LoggingProcessor.DIRECTORY, lp.getOperations().get(1).getOp());
		assertEquals(dir2, lp.getOperations().get(1).getResource());
		assertEquals(dir, lp.getOperations().get(1).getParent());		
		assertEquals(LoggingProcessor.DIRECTORY, lp.getOperations().get(2).getOp());
		assertEquals(dir3, lp.getOperations().get(2).getResource());
		assertEquals(dir2, lp.getOperations().get(2).getParent());		
	}
	
	public void testNestedDirectoriesAndFiles() {
		Directory dir = new Directory("foo", null);
		Directory dir2 = new Directory("bar", null);
		Directory dir3 = new Directory("baz", null);
		File file1 = new File("file1", null);
		File file2 = new File("file2", null);
		File file3 = new File("file3", null);		
		dir.addDirectory(dir2);
		dir.addFile(file1);
		dir2.addDirectory(dir3);
		dir2.addFile(file2);
		dir3.addFile(file3);
		
		MirrorDirectoryStructure mds = new MirrorDirectoryStructure();
		LoggingProcessor lp = new LoggingProcessor();
		mds.setResourceProcessor(lp);
		mds.mirror(dir, null);
		assertEquals(6, lp.getOperations().size());
		assertEquals(LoggingProcessor.DIRECTORY, lp.getOperations().get(0).getOp());
		assertEquals(dir, lp.getOperations().get(0).getResource());
		assertEquals(null, lp.getOperations().get(0).getParent());		
		assertEquals(LoggingProcessor.DIRECTORY, lp.getOperations().get(1).getOp());
		assertEquals(dir2, lp.getOperations().get(1).getResource());
		assertEquals(dir, lp.getOperations().get(1).getParent());		
		assertEquals(LoggingProcessor.DIRECTORY, lp.getOperations().get(2).getOp());
		assertEquals(dir3, lp.getOperations().get(2).getResource());
		assertEquals(dir2, lp.getOperations().get(2).getParent());		
		assertEquals(LoggingProcessor.FILE, lp.getOperations().get(3).getOp());
		assertEquals(file3, lp.getOperations().get(3).getResource());
		assertEquals(dir3, lp.getOperations().get(3).getParent());		
		assertEquals(LoggingProcessor.FILE, lp.getOperations().get(4).getOp());
		assertEquals(file2, lp.getOperations().get(4).getResource());
		assertEquals(dir2, lp.getOperations().get(4).getParent());		
		assertEquals(LoggingProcessor.FILE, lp.getOperations().get(5).getOp());
		assertEquals(file1, lp.getOperations().get(5).getResource());
		assertEquals(dir, lp.getOperations().get(5).getParent());		
	}
	
	public void testContextPassing() {
		Directory dir = new Directory("foo", null);
		File file1 = new File("foo1", null);
		dir.addFile(file1);
		MirrorDirectoryStructure mds = new MirrorDirectoryStructure();
		LoggingProcessor lp = new LoggingProcessor();
		mds.setResourceProcessor(lp);
		Object context = new Object();
		mds.mirror(dir, null, context);
		assertEquals(2, lp.getOperations().size());
		assertEquals(LoggingProcessor.DIRECTORY, lp.getOperations().get(0).getOp());
		assertEquals(dir, lp.getOperations().get(0).getResource());
		assertEquals(null, lp.getOperations().get(0).getParent());
		assertEquals(context, lp.getOperations().get(0).getContext());
		assertEquals(LoggingProcessor.FILE, lp.getOperations().get(1).getOp());
		assertEquals(file1, lp.getOperations().get(1).getResource());
		assertEquals(dir, lp.getOperations().get(1).getParent());		
		assertEquals(context, lp.getOperations().get(1).getContext());
	}
}
