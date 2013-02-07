/*
 * $Id: //depot/main/java/afnic-commons-services-definition/test/fr/afnic/commons/beans/statistics/PercentFormatterTest.java#2 $
 * $Revision: #2 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.beans.statistics;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.statistics.PercentFormatter;

/**
 * Teste les méthodes de la classe PercentFormatter.
 * 
 * @author ginguene
 * 
 */
public class PercentFormatterTest {

    /**
     * Teste la méthode formatValue()
     */
    @Test
    public void tefFormatValue() {
        PercentFormatter formatter = new PercentFormatter();

        String comment = "Bad formatValue() result";
        TestCase.assertEquals(comment, "100%", formatter.format(1.0f));
        TestCase.assertEquals(comment, "90%", formatter.format(0.9f));
        TestCase.assertEquals(comment, "12,3%", formatter.format(0.123f));
        TestCase.assertEquals(comment, "1,23%", formatter.format(0.0123f));
        TestCase.assertEquals(comment, "1,23%", formatter.format(0.01234f));
    }
}
