/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/statistics/PercentFormatter.java#1 $
 * $Revision: #1 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.beans.statistics;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Statistique représentant des pourcentages.
 * 
 * 
 */
public class PercentFormatter implements IStatisticValueFormatter {

    /** Définit le formattage de la valeur en text */
    private static final NumberFormat FORMAT = new DecimalFormat("###.##");

    /**
     * {@inheritDoc}
     */
    @Override
    public String format(float value) {
        return FORMAT.format(value * 100) + "%";
    }
}
