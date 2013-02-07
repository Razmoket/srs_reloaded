/*
 * $Id: //depot/main/java/afnic-commons-services-definition/test/fr/afnic/commons/beans/statistics/ProjectTest.java#2 $
 * $Revision: #2 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.beans.statistics;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.statistics.Project;

/**
 * Teste les méthodes de la classe Project.
 * 
 * @author ginguene
 * 
 */
public class ProjectTest {

    /**
     * Teste le constructeur avec le parametre statistic null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullName() {
        new Project(null);
    }

    /**
     * Teste la méthode equals()
     */
    @Test
    public void testEquals() {
        String comment = "Bad equals() return";
        String projectName1 = "project 1";
        String projectName2 = "project 2";

        TestCase.assertTrue(comment, new Project(projectName1).equals(new Project(projectName1)));
        TestCase.assertFalse(comment, new Project(projectName1).equals(new Project(projectName2)));
        TestCase.assertFalse(comment, new Project(projectName1).equals(null));
        TestCase.assertFalse(comment, new Project(projectName1).equals(new Object()));
    }
}
