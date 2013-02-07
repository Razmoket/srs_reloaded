/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/statistics/DailyIncrementalMesure.java#1 $
 * $Revision: #1 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.beans.statistics;

/**
 * Mesure prise quotidiennement en l'incrementant à chaque fois que l'opération surveillée se produit.<br/>
 * ex Validation des autorisations.
 * 
 * 
 * @author ginguene
 * 
 */
public class DailyIncrementalMesure extends Measure {

    /**
     * Constructeur créant une statistique ayant comme valeur 1 et une DayOfMeasure comme date.
     * 
     * @param statistic
     *            Statistique concernée par la mesure
     * 
     * @param userLogin
     *            Login de l'utilisateur à l'origine de la mesure.
     */
    public DailyIncrementalMesure(Statistic statistic, String userLogin) {
        super(statistic, new DayOfMeasure(), 1.0f, userLogin);
    }

}
