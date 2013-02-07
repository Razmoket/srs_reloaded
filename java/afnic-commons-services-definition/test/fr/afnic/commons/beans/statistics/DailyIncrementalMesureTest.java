/*
 * $Id: //depot/main/java/afnic-commons-services-definition/test/fr/afnic/commons/beans/statistics/DailyIncrementalMesureTest.java#2 $
 * $Revision: #2 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.beans.statistics;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.statistics.DailyIncrementalMesure;
import fr.afnic.commons.beans.statistics.DayOfMeasure;
import fr.afnic.commons.beans.statistics.Measure;
import fr.afnic.commons.beans.statistics.StatisticsFactory;

/**
 * Teste des méthodes de la classe DailyIncrementalMesure.
 * 
 * @author ginguene
 * 
 */
public class DailyIncrementalMesureTest {

    /**
     * Teste du constructeur.
     * 
     */
    @Test
    public void testConstructor() {
        String userLogin = "bob";

        Measure measure = new DailyIncrementalMesure(StatisticsFactory.AUTHORIZATION_ABORD, "bob");

        TestCase.assertEquals(1.0f, measure.getValue());
        TestCase.assertTrue(measure.getDate() instanceof DayOfMeasure);
        TestCase.assertEquals(userLogin, measure.getUserLogin());
        TestCase.assertEquals(StatisticsFactory.AUTHORIZATION_ABORD, measure.getStatistic());

    }
}
