/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/statistics/FloatFormatter.java#1 $
 * $Revision: #1 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.beans.statistics;

/**
 * Formatter qui ne change pas le format du float.
 * 
 * @author ginguene
 * 
 */
public class FloatFormatter implements IStatisticValueFormatter {

    /** {@inheritDoc} */
    @Override
    public String format(float value) {
        return Float.toString(value);
    }

}
