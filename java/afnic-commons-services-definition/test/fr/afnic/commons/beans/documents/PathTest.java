package fr.afnic.commons.beans.documents;

import junit.framework.TestCase;

import org.junit.Test;

public class PathTest {

    @Test
    public void testNew() {

        TestCase.assertEquals("Bad path value", "/", new Path("/").getValue());
        TestCase.assertEquals("Bad path value", "/path1/", new Path("/path1/").getValue());

        TestCase.assertEquals("Bad path value", "/path1/path2/", new Path("//path1//path2//").getValue());

        TestCase.assertEquals("Bad path value", "/path1/", new Path("/path1").getValue());
        TestCase.assertEquals("Bad path value", "/path1/", new Path("path1").getValue());

        TestCase.assertEquals("Bad path value", "/path1/path2/", new Path("path1/path2/").getValue());
        TestCase.assertEquals("Bad path value", "/path1/path2/", new Path("/path1/path2/").getValue());
        TestCase.assertEquals("Bad path value", "/path1/path2/", new Path("/path1/path2").getValue());

    }

    @Test
    public void testGetParent() {
        TestCase.assertEquals("Bad parent path value", "/", new Path("/path1/").getParent().getValue());
        TestCase.assertEquals("Bad parent path value", "/path1/", new Path("/path1/path2/").getParent().getValue());
        TestCase.assertEquals("Bad parent path value", "/path1/path2/", new Path("/path1/path2/path3").getParent().getValue());
    }

    @Test
    public void testEquals() {
        TestCase.assertEquals("Should be equals", new Path("/path1"), new Path("/path1/"));
        TestCase.assertEquals("Should be equals", new Path("/path1/path2"), new Path("/path1/path2"));

        TestCase.assertFalse("Should not be equals", new Path("/path1/path2").equals(new Path("/path1/")));
        TestCase.assertFalse("Should not be equals", new Path("/path1/path2").equals("/path1/"));
    }

    @Test(expected = RuntimeException.class)
    public void testGetParentWithRoot() {
        TestCase.assertEquals("Bad path value", "/", new Path("/").getParent());
    }

    @Test()
    public void testIsRoot() {
        TestCase.assertTrue("isRoot should be true", new Path("/").isRoot());
        TestCase.assertFalse("isRoot should be false", new Path("/path").isRoot());
    }

    @Test
    public void testGetName() {
        TestCase.assertEquals("Bad path name", "path2", new Path("/path1/path2/").getName());
        TestCase.assertEquals("Bad path name", "path1", new Path("/path1/").getName());
        TestCase.assertEquals("Bad path name", "", new Path("/").getName());
    }

}
