/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/statistics/IStatisticValueFormatter.java#2 $
 * $Revision: #2 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.statistics;

import java.io.Serializable;

/**
 * Classe chargée de transformer un float en String.
 * 
 * @author ginguene
 * 
 */
public interface IStatisticValueFormatter extends Serializable {

    /**
     * Formatte un float en String
     * 
     * @param value
     *            float à formatter
     * @return Une chaine de caractère représentant la valeur.
     */
    public String format(float value);

}
