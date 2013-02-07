/*
 * $Id: //depot/main/java/afnic-commons-services-definition/test/fr/afnic/commons/beans/statistics/MonthOfMeasureTest.java#2 $
 * $Revision: #2 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.beans.statistics;

import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.statistics.MonthOfMeasure;

/**
 * Teste les méthodes de la classe MonthOfMeasure.
 * 
 * @author ginguene
 * 
 */
public class MonthOfMeasureTest {

    /**
     * Teste le constructeur par défaut.<br/>
     * Ce dernier doit utiliser la date courante.
     */
    @Test
    public void testDefaultConstructor() {
        TestCase.assertEquals("Bad default constuctor", new MonthOfMeasure(new Date()), new MonthOfMeasure());
    }

    /**
     * Teste la fonction equals.
     * 
     */
    @Test
    public void testEquals() {

        MonthOfMeasure month = new MonthOfMeasure(new Date());
        MonthOfMeasure sameMonth = new MonthOfMeasure(new Date());

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(GregorianCalendar.MONTH, 5);
        MonthOfMeasure otherMonth = new MonthOfMeasure(calendar.getTime());

        String testComment = "Bad equals() result";

        TestCase.assertTrue(testComment, month.equals(sameMonth));

        TestCase.assertFalse(testComment, month.equals(otherMonth));
        TestCase.assertFalse(testComment, month.equals(null));
    }

}
