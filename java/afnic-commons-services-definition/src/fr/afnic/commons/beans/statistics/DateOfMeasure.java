/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/statistics/DateOfMeasure.java#4 $
 * $Revision: #4 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.statistics;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.afnic.utils.DateUtils;

/**
 * Représente la date de'une mesure de statistique.
 * 
 * @author ginguene
 * 
 */
public abstract class DateOfMeasure implements Serializable {

    /**
     * Date de la mesure.
     * 
     */
    protected Date date;

    private String dateStr;

    /**
     * Constucteur initialisant la date de la mesure avec la date courante.
     * 
     */
    public DateOfMeasure() {
        this(new Date());
    }

    /**
     * Constucteur permettant d'initialiser la Date.<br/>
     * 
     * @param date
     *            date de la mesure.
     */
    public DateOfMeasure(Date date) {
        if (date == null) throw new IllegalArgumentException("date cannot be null");

        SimpleDateFormat format = this.getDateFormat();

        if (format == null) {
            throw new RuntimeException(this.getClass().getSimpleName() + ".getDateFormat() cannot return null");
        } else {
            this.dateStr = format.format(date);
            try {
                this.date = format.parse(this.dateStr);
            } catch (ParseException e) {
                // Exception impossible
                throw new RuntimeException("Date cannot be reduced to the starting period", e);
            }
        }
    }

    /**
     * Retourne la première date de la pèriode de mesure. Ex:<br/>
     * Pour un DayOfMeasure, c'est la date du jour à 00:00 qui sera retournée.<br/>
     * Pour un MonthOfMeasure, c'est le premier jour du mois à 00:00 qui sera retournée. <br/>
     * 
     * 
     * @return La date de début de pèriode.
     */
    public Date getStartingDate() {
        return DateUtils.clone(this.date);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.dateStr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        try {
            this.getClass().cast(obj);
        } catch (ClassCastException e) {
            return false;
        }

        return this.toString().equals(obj.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.date.hashCode();
    }

    /**
     * Retourne le format de date à utiliser dans la méthode toString()
     * 
     * 
     * @return un format de date
     */
    protected abstract SimpleDateFormat getDateFormat();

}
