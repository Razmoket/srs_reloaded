/*
 * $Id: StatisticsToCsv.java,v 1.4 2010/08/20 12:30:54 ginguene Exp $
 * $Revision: 1.4 $
 * $Author: ginguene $
 */

package fr.afnic.commons.export.csv;

import java.util.List;

import fr.afnic.commons.beans.statistics.Measure;

/**
 * Classe utilitaire permettant de transformer une liste de statistiques en csv.
 * 
 * @author ginguene
 * 
 */
public final class StatisticsToCsv {

    /**
     * Constucteur privé pour empecher l'instanciation
     * 
     */
    private StatisticsToCsv() {
    }

    /**
     * Converti une liste de statistique au format CSV.
     * 
     * @param measures
     *            Liste de mesures de statistiques à convertir
     * 
     * @return Une chaine de caratère au format CSV.
     */
    public static String format(List<Measure> measures) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("Jour, intitulé, Opérateur, Valeur\n");
        for (Measure measure : measures) {
            buffer.append(measure.getStartingDateStr() + ","
                          + measure.getLabel() + ","
                          + getUserLogin(measure) + ","
                          + measure.getStringValue() + "\n");
        }

        return buffer.toString();
    }

    private static String getUserLogin(Measure measure) {
        if (measure.isMeasureForAllUsers()) {
            return "Tous";
        } else {
            return measure.getUserLogin();
        }
    }
}
