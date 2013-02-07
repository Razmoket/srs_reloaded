/*
 * $Id: //depot/main/java/afnic-commons-services-definition/test/fr/afnic/commons/beans/statistics/FloatFormatterTest.java#2 $
 * $Revision: #2 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.beans.statistics;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.statistics.FloatFormatter;

/**
 * Teste les méthodes de la classe FloatFormatter.
 * 
 * @author ginguene
 * 
 */
public class FloatFormatterTest {

    /**
     * Teste la méthode format()
     */
    @Test
    public void testFormatValue() {
        FloatFormatter formatter = new FloatFormatter();

        String comment = "Bad formatValue() result";
        TestCase.assertEquals(comment, "1.0", formatter.format(1.0f));
        TestCase.assertEquals(comment, "1.12", formatter.format(1.12f));
        TestCase.assertEquals(comment, "0.9", formatter.format(0.9f));
    }
}
