/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/statistics/Measure.java#3 $
 * $Revision: #3 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.statistics;

import java.io.Serializable;
import java.util.Date;

/**
 * Mesure d'une statistique faite pour une date données et pour un utilisateur. Aucun utilisateur signifie que la mesure est celle faite pour tous les
 * utilisateurs.
 * 
 * @author ginguene
 * 
 */
public class Measure implements Serializable {

    private final DateOfMeasure date;
    private final Statistic statistic;
    private String userLogin;
    private final float value;

    private boolean isMeasureForAllUsers;

    /**
     * Constructeur d'une mesure d'une statistic pour tous les utilisateurs.
     * 
     * @param statistic
     *            Statistique de la mesure.
     * 
     * @param date
     *            Date de la mesure.
     * 
     * @param value
     *            Valeur de la mesure.
     * 
     */
    public Measure(Statistic statistic, DateOfMeasure date, float value) {

        if (statistic == null) {
            throw new IllegalArgumentException("statistic cannot be null");
        }

        if (date == null) {
            throw new IllegalArgumentException("date cannot be null");
        }

        this.statistic = statistic;
        this.date = date;
        this.value = value;
        this.isMeasureForAllUsers = true;
    }

    /**
     * Constructeur d'une mesure d'une statistic pour un utilisateur en particulier.
     * 
     * @param statistic
     *            Statistique de la mesure.
     * 
     * @param date
     *            Date de la mesure.
     * 
     * @param value
     *            Valeur de la mesure.
     * 
     * @param userLogin
     *            Utilisateur pour le quel la mesure a été faite
     * 
     */
    public Measure(Statistic statistic, DateOfMeasure date, float value, String userLogin) {
        this(statistic, date, value);
        this.userLogin = userLogin;

        this.isMeasureForAllUsers = (this.userLogin == null);
    }

    public Measure plus(float addedValue) {
        return this.newValue(this.value + addedValue);
    }

    public Measure newValue(float newValue) {
        return new Measure(this.statistic, this.date, newValue, this.userLogin);
    }

    public DateOfMeasure getDate() {
        return this.date;
    }

    public Statistic getStatistic() {
        return this.statistic;
    }

    public String getUserLogin() {
        return this.userLogin;
    }

    public float getValue() {
        return this.value;
    }

    public boolean isMeasureForAllUsers() {
        return this.isMeasureForAllUsers;
    }

    /**
     * 
     * 2 mesures sont égales si elles concernent le meme user, le meme statistic et le meme jour et la meme valeur.
     * 
     * @param obj
     *            objet à comparer
     * 
     * @return true si les 2 mesures sont identiques.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Measure)) {
            return false;
        }

        Measure measure = (Measure) obj;

        if (this.statistic != null) {
            if (!this.statistic.equals(measure.getStatistic())) {
                return false;
            }
        } else {
            if (measure.getStatistic() != null) {
                return false;
            }
        }

        if (this.date != null) {
            if (!this.date.equals(measure.getDate())) {
                return false;
            }
        } else {
            if (measure.getDate() != null) {
                return false;
            }
        }

        if (this.userLogin != null) {
            if (!this.userLogin.equals(measure.getUserLogin())) {
                return false;
            }
        } else {
            if (measure.getUserLogin() != null) {
                return false;
            }
        }
        return this.value == measure.getValue();
    }

    public Project getProject() {
        return this.statistic.getProject();
    }

    public String getLabel() {
        return this.statistic.getLabel();
    }

    public Date getStartingDate() {
        return this.date.getStartingDate();
    }

    public String getStartingDateStr() {
        return this.date.toString();
    }

    public String getStringValue() {
        return this.statistic.formatValue(this.value);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return this.date.hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Measure: project[" + this.getProject() + "]"
               + " label[" + this.getLabel() + "]"
               + " startingDate[" + this.date.toString() + "]"
               + " value[" + this.value + "]"
               + " userLogin[" + this.userLogin + "]";
    }

}
