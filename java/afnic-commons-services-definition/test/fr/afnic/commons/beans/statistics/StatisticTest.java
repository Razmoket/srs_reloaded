/*
 * $Id: //depot/main/java/afnic-commons-services-definition/test/fr/afnic/commons/beans/statistics/StatisticTest.java#3 $
 * $Revision: #3 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.statistics;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.statistics.IStatisticValueFormatter;
import fr.afnic.commons.beans.statistics.IntegerFormatter;
import fr.afnic.commons.beans.statistics.Project;
import fr.afnic.commons.beans.statistics.ProjectFactory;
import fr.afnic.commons.beans.statistics.Statistic;

/**
 * Teste la classe ProjectStat
 * 
 * @author ginguene
 * 
 */
public class StatisticTest {

    /**
     * Teste le constructeur avec le parametre statistic null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullProject() {
        new Statistic(null, "", new IntegerFormatter());
    }

    /**
     * Teste le constructeur avec le parametre statistic null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullLabel() {
        new Statistic(ProjectFactory.BOA, null, new IntegerFormatter());
    }

    /**
     * Teste le constructeur avec le parametre statistic null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullFormatter() {
        new Statistic(ProjectFactory.BOA, "", null);
    }

    /**
     * Vérifie le fonctionnement de la méthode equals de ProjectStat.
     * 
     */
    @Test
    public void testEquals() {
        Project project1 = new Project("monProjet1" + System.currentTimeMillis());
        String label1 = "monLabel1";

        Project project2 = new Project("monProjet2" + System.currentTimeMillis());
        String label2 = "monLabel2";

        String testComment = "Bad equals evaluation";
        IStatisticValueFormatter formatter = new IntegerFormatter();

        TestCase.assertTrue(testComment, new Statistic(project1, label1, formatter).equals(new Statistic(project1, label1, formatter)));

        TestCase.assertFalse(testComment, new Statistic(project1, label1, formatter).equals(new Statistic(project1, label2, formatter)));
        TestCase.assertFalse(testComment, new Statistic(project1, label1, formatter).equals(new Statistic(project2, label1, formatter)));

        TestCase.assertFalse(testComment, new Statistic(project1, label1, formatter).equals(new Object()));
        TestCase.assertFalse(testComment, new Statistic(project1, label1, formatter).equals(null));

    }
}
