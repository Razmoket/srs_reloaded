/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/statistics/MonthOfMeasure.java#1 $
 * $Revision: #1 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.beans.statistics;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * La mesure a été effectuée sur une pèriode d'un mois.
 * 
 * @author ginguene
 * 
 */
public class MonthOfMeasure extends DateOfMeasure {

    private static SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");

    /**
     * Constucteur initialisant la date de la mesure avec la date courante.
     * 
     */
    public MonthOfMeasure() {
        super();
    }

    /**
     * Constucteur faisant appelle à celui de DateOfMeasure
     * 
     * @param date
     *            Date de la mesure
     */
    public MonthOfMeasure(Date date) {
        super(date);
    }

    @Override
    protected SimpleDateFormat getDateFormat() {
        return format;
    }
}
