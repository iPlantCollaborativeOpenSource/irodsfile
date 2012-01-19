package org.iplantc.irodsfile;

import junit.framework.TestCase;

import org.iplantc.irodsfile.model.File;
import org.iplantc.irodsfile.model.Directory;
import org.iplantc.irodsfile.model.MockFilesystemFile;
import org.iplantc.irodsfile.model.MockFilesystemFolder;
import org.iplantc.irodsfile.model.MockFilesystemResource;
import org.iplantc.irodsfile.model.Resource;

public class TestExtractDirectoryStructure extends TestCase {
	public void testEmptyDirectory() {
		MockFilesystemResource fsFolder = new MockFilesystemFolder("foo");
		ExtractDirectoryStructure eds = new ExtractDirectoryStructure();
		Resource rsrc = eds.extract(fsFolder);
		assertTrue(rsrc instanceof Directory);
		Directory folder = (Directory)rsrc;
		assertEquals("foo", folder.getName());
		assertEquals(0, folder.getFiles().size());
		assertEquals(0, folder.getFolders().size());
		assertEquals(fsFolder, folder.getFilesystemResource());
	}
	
	public void testSingleFile() {
		MockFilesystemResource fsFolder = new MockFilesystemFile("foo");
		ExtractDirectoryStructure eds = new ExtractDirectoryStructure();
		Resource rsrc = eds.extract(fsFolder);
		assertTrue(rsrc instanceof File);
		assertEquals("foo", rsrc.getName());
		assertEquals(fsFolder, rsrc.getFilesystemResource());
	}
	
	public void testSingleDirectoryWithSingleFile() {
		MockFilesystemResource fsFolder = new MockFilesystemFolder("foo");
		MockFilesystemResource fsFile = new MockFilesystemFile("bar");
		fsFolder.addResource(fsFile);
		ExtractDirectoryStructure eds = new ExtractDirectoryStructure();		
		Resource rsrc = eds.extract(fsFolder);
		assertTrue(rsrc instanceof Directory);
		Directory folder = (Directory)rsrc;
		assertEquals("foo", folder.getName());
		assertEquals(1, folder.getFiles().size());
		assertEquals(0, folder.getFolders().size());
		assertEquals(fsFolder, folder.getFilesystemResource());
		assertEquals("bar", folder.getFiles().get(0).getName());
		assertEquals(fsFile, folder.getFiles().get(0).getFilesystemResource());
	}
	
	public void testSingleDirectoryWithMultipleFiles() {
		MockFilesystemResource fsFolder = new MockFilesystemFolder("foo");
		MockFilesystemResource fsFile1 = new MockFilesystemFile("bar");
		MockFilesystemResource fsFile2 = new MockFilesystemFile("baz");
		MockFilesystemResource fsFile3 = new MockFilesystemFile("blip");
		fsFolder.addResource(fsFile1);
		fsFolder.addResource(fsFile2);
		fsFolder.addResource(fsFile3);
		ExtractDirectoryStructure eds = new ExtractDirectoryStructure();		
		Resource rsrc = eds.extract(fsFolder);
		assertTrue(rsrc instanceof Directory);
		Directory folder = (Directory)rsrc;
		assertEquals("foo", folder.getName());
		assertEquals(3, folder.getFiles().size());
		assertEquals(0, folder.getFolders().size());
		assertEquals(fsFolder, folder.getFilesystemResource());
		assertEquals("bar", folder.getFiles().get(0).getName());		
		assertEquals(fsFile1, folder.getFiles().get(0).getFilesystemResource());		
		assertEquals("baz", folder.getFiles().get(1).getName());		
		assertEquals(fsFile2, folder.getFiles().get(1).getFilesystemResource());		
		assertEquals("blip", folder.getFiles().get(2).getName());		
		assertEquals(fsFile3, folder.getFiles().get(2).getFilesystemResource());		
	}
	
	public void testNestedDirectory() {
		MockFilesystemResource fsFolder = new MockFilesystemFolder("foo");
		MockFilesystemResource fsFolder2 = new MockFilesystemFolder("bar");
		fsFolder.addResource(fsFolder2);
		ExtractDirectoryStructure eds = new ExtractDirectoryStructure();		
		Resource rsrc = eds.extract(fsFolder);
		assertTrue(rsrc instanceof Directory);
		Directory folder = (Directory)rsrc;
		assertEquals("foo", folder.getName());
		assertEquals(0, folder.getFiles().size());
		assertEquals(1, folder.getFolders().size());
		assertEquals(fsFolder, folder.getFilesystemResource());
		assertEquals("bar", folder.getFolders().get(0).getName());		
		assertEquals(fsFolder2, folder.getFolders().get(0).getFilesystemResource());
	}

	public void test3xNestedDirectory() {
		MockFilesystemResource fsFolder = new MockFilesystemFolder("foo");
		MockFilesystemResource fsFolder2 = new MockFilesystemFolder("bar");
		MockFilesystemFolder fsFolder3 = new MockFilesystemFolder("baz");
		fsFolder.addResource(fsFolder2);
		fsFolder2.addResource(fsFolder3);
		ExtractDirectoryStructure eds = new ExtractDirectoryStructure();		
		Resource rsrc = eds.extract(fsFolder);
		assertTrue(rsrc instanceof Directory);
		Directory folder = (Directory)rsrc;
		assertEquals("foo", folder.getName());
		assertEquals(0, folder.getFiles().size());
		assertEquals(1, folder.getFolders().size());
		assertEquals(fsFolder, folder.getFilesystemResource());
		assertEquals("bar", folder.getFolders().get(0).getName());		
		assertEquals(1, folder.getFolders().get(0).getFolders().size());		
		assertEquals(fsFolder2, folder.getFolders().get(0).getFilesystemResource());
		assertEquals("baz", folder.getFolders().get(0).getFolders().get(0).getName());		
		assertEquals(fsFolder3, folder.getFolders().get(0).getFolders().get(0).getFilesystemResource());
	}
}
