/*
 * $Id: //depot/main/java/afnic-commons-services-definition/test/fr/afnic/commons/beans/VersionTest.java#8 $
 * $Revision: #8 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.application.Application;
import fr.afnic.commons.beans.application.Version;
import fr.afnic.commons.beans.application.env.Environnement;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;

/**
 * Verification dus comparaison de version
 * 
 * 
 * @author ginguene
 * 
 */
public class VersionTest {

    /**
     * Verification de la méthode compareTo() de la classe Version.
     * 
     */
    @Test
    public void testVersionCompareTo() {
        Version v1 = new Version(1, 2, 3);
        Version v2 = new Version(1, 2, 3);
        Version v3 = new Version(1, 2, 4);
        Version v4 = new Version(1, 3, 3);
        Version v5 = new Version(2, 2, 3);
        Version v6 = new Version(1, 2, 4);

        TestCase.assertEquals(0, v1.compareTo(v2));

        TestCase.assertEquals(-1, v1.compareTo(v3));
        TestCase.assertEquals(-1, v1.compareTo(v4));
        TestCase.assertEquals(-1, v1.compareTo(v5));
        TestCase.assertEquals(-1, v1.compareTo(v6));

        TestCase.assertEquals(1, v3.compareTo(v1));
        TestCase.assertEquals(1, v4.compareTo(v1));
        TestCase.assertEquals(1, v5.compareTo(v1));
        TestCase.assertEquals(1, v6.compareTo(v1));

    }

    /**
     * Verification de la méthode equals() de la classe Version.
     * 
     */
    @Test
    public void testVersionEquals() {
        Version v1 = new Version(1, 2, 3);
        Version v2 = new Version(1, 2, 3);
        Version v3 = new Version(1, 4, 3);

        TestCase.assertTrue(v1.equals(v2));
        TestCase.assertTrue(v2.equals(v1));

        TestCase.assertFalse(v1.equals(v3));
        TestCase.assertFalse(v3.equals(v1));

    }

    /**
     * Verification du constructeur de la classe Version.
     * 
     * @throws InvalidFormatException
     *             Si un test de création échoue
     * 
     * 
     */
    @Test
    public void testVersionNew() throws InvalidFormatException {
        Version v1 = new Version("1.2.3");
        TestCase.assertEquals(1, v1.getMajor());
        TestCase.assertEquals(2, v1.getMinor());
        TestCase.assertEquals(3, v1.getPatch());

        Version v2 = new Version("10.20.30");
        TestCase.assertEquals(10, v2.getMajor());
        TestCase.assertEquals(20, v2.getMinor());
        TestCase.assertEquals(30, v2.getPatch());

        try {
            String strVersion = "1.2";
            new Version(strVersion);
            TestCase.fail("Version " + strVersion + " should thrown InvalidFormatException");
        } catch (InvalidFormatException e) {
            // Doit passer par ici
        }

        try {
            String strVersion = "1.A.4";
            new Version(strVersion);
            TestCase.fail("Version " + strVersion + " should thrown InvalidFormatException");
        } catch (InvalidFormatException e) {
            // Doit passer par ici
        }
    }

    /**
     * Verification du constructeur de la classe Version avec une version au format String ayant un mauvais format.
     * 
     * @throws InvalidFormatException
     * 
     * 
     * 
     * 
     */
    @Test(expected = InvalidFormatException.class)
    public void testVersionNewWithBadFormat() throws InvalidFormatException {
        new Version("1.2-3");
    }

    @Test
    public void testDisplayNameForEachEnv() {
        String applicationName = "my App";

        TestCase.assertEquals("Bad version display name", "my App 1.2.3.4 [dev]", new Version(1, 2, 3, 4, new Application(applicationName), Environnement.Dev).getDisplayName());
        TestCase.assertEquals("Bad version display name", "my App 1.2.3.4 [preprod]", new Version(1, 2, 3, 4, new Application(applicationName), Environnement.Preprod).getDisplayName());
        TestCase.assertEquals("Bad version display name", "my App 1.2.3.4 [sandbox]", new Version(1, 2, 3, 4, new Application(applicationName), Environnement.Sandbox).getDisplayName());
        TestCase.assertEquals("Bad version display name", "my App 1.2.3", new Version(1, 2, 3, 4, new Application(applicationName), Environnement.Prod).getDisplayName());

    }

}
