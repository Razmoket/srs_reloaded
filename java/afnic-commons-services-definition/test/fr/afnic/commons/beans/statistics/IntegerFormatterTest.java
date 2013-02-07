/*
 * $Id: //depot/main/java/afnic-commons-services-definition/test/fr/afnic/commons/beans/statistics/IntegerFormatterTest.java#2 $
 * $Revision: #2 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.beans.statistics;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.statistics.IntegerFormatter;

/**
 * Teste les méthodes de la classe IntegerFormatter.
 * 
 * @author ginguene
 * 
 */
public class IntegerFormatterTest {

    /**
     * Teste la méthode formatValue()
     */
    @Test
    public void testFormatValue() {
        IntegerFormatter formatter = new IntegerFormatter();

        String comment = "Bad formatValue() result";
        TestCase.assertEquals(comment, "1", formatter.format(1.0f));
        TestCase.assertEquals(comment, "1", formatter.format(1.1f));
        TestCase.assertEquals(comment, "1", formatter.format(0.9f));
    }
}
