/*
 * $Id: //depot/main/java/afnic-commons-services-definition/test/fr/afnic/commons/beans/statistics/MeasureTest.java#2 $
 * $Revision: #2 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.beans.statistics;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.statistics.DateOfMeasure;
import fr.afnic.commons.beans.statistics.DayOfMeasure;
import fr.afnic.commons.beans.statistics.Measure;
import fr.afnic.commons.beans.statistics.MonthOfMeasure;
import fr.afnic.commons.beans.statistics.StatisticsFactory;
import fr.afnic.commons.beans.statistics.Statistic;

/**
 * Teste les méthodes de la classe Measure.
 * 
 * @author ginguene
 * 
 */
public class MeasureTest {

    /**
     * Teste le constructeur avec le parametre statistic null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullStatistic() {
        new Measure(null, new DayOfMeasure(), 1);
    }

    /**
     * Teste le constructeur avec le parametre date null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullDate() {
        new Measure(StatisticsFactory.AUTHORIZATION_ABORD, null, 1);
    }

    /**
     * Teste la méthode isMeasureForAllUsers
     */
    @Test
    public void testIsMeasureForAllUsers() {

        String comment = "Bad isMeasureForAllUsers value";
        TestCase.assertTrue(comment, new Measure(StatisticsFactory.AUTHORIZATION_ABORD,
                                                 new DayOfMeasure(), 1,
                                                 null).isMeasureForAllUsers());

        TestCase.assertTrue(comment, new Measure(StatisticsFactory.AUTHORIZATION_ABORD,
                                                 new DayOfMeasure(),
                                                 1).isMeasureForAllUsers());

        TestCase.assertFalse(comment, new Measure(StatisticsFactory.AUTHORIZATION_ABORD,
                                                  new DayOfMeasure(),
                                                  1,
                                                  "user1").isMeasureForAllUsers());
    }

    /**
     * Teste la méthode equals
     */
    @Test()
    public void testEquals() {

        Statistic statistic1 = StatisticsFactory.AUTHORIZATION_ABORD;
        Statistic statistic2 = StatisticsFactory.AUTHORIZATION_GENERATED;

        DateOfMeasure date1 = new DayOfMeasure();
        DateOfMeasure date2 = new MonthOfMeasure();

        String comment = "Bad equals() result";
        TestCase.assertTrue(comment, new Measure(statistic1, date1, 1).equals(new Measure(statistic1, date1, 1)));

        TestCase.assertFalse(comment, new Measure(statistic1, date1, 1).equals(new Measure(statistic2, date1, 1)));
        TestCase.assertFalse(comment, new Measure(statistic1, date1, 1).equals(new Measure(statistic1, date2, 1)));
        TestCase.assertFalse(comment, new Measure(statistic1, date1, 1).equals(new Measure(statistic1, date1, 2)));

    }
}
