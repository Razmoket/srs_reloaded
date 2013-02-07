/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/statistics/DayOfMeasure.java#1 $
 * $Revision: #1 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.beans.statistics;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * La mesure a été effectuée sur une pèriode d'un jour.
 * 
 * @author ginguene
 * 
 */
public class DayOfMeasure extends DateOfMeasure {

    private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Constucteur initialisant la date de la mesure avec la date courante.
     * 
     */
    public DayOfMeasure() {
        super();
    }

    /**
     * Constucteur faisant appelle à celui de DateOfMeasure
     * 
     * @param date
     *            Date de la mesure
     */
    public DayOfMeasure(Date date) {
        super(date);
    }

    @Override
    protected SimpleDateFormat getDateFormat() {
        return format;
    }
}
