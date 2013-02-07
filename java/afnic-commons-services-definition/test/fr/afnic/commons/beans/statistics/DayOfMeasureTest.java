/*
 * $Id: //depot/main/java/afnic-commons-services-definition/test/fr/afnic/commons/beans/statistics/DayOfMeasureTest.java#2 $
 * $Revision: #2 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.beans.statistics;

import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.statistics.DayOfMeasure;

/**
 * Teste les méthodes de la classe DayOfMeasure.
 * 
 * @author ginguene
 * 
 */
public class DayOfMeasureTest {

    /**
     * Teste le constructeur par défaut.<br/>
     * Ce dernier doit utiliser la date courante.
     */
    @Test
    public void testDefaultConstructor() {
        TestCase.assertEquals("Bad default constuctor", new DayOfMeasure(new Date()), new DayOfMeasure());
    }

    /**
     * Teste la fonction equals.
     * 
     */
    @Test
    public void testEquals() {

        DayOfMeasure day = new DayOfMeasure(new Date());
        DayOfMeasure sameDay = new DayOfMeasure(new Date());

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(GregorianCalendar.DATE, 5);
        DayOfMeasure otherDay = new DayOfMeasure(calendar.getTime());

        String testComment = "Bad equals() result";

        TestCase.assertTrue(testComment, day.equals(sameDay));

        TestCase.assertFalse(testComment, day.equals(otherDay));
        TestCase.assertFalse(testComment, day.equals(null));
    }
}
