/*
 * $Id: StatisticGenerator.java,v 1.3 2010/08/19 13:49:47 ginguene Exp $
 * $Revision: 1.3 $
 * $Author: ginguene $
 */

package fr.afnic.commons.test.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.afnic.commons.beans.statistics.Measure;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Permet de calculer des statistiques.
 * 
 * 
 * @author ginguene
 * 
 */
public final class StatisticGenerator {

    /**
     * Prend une liste de Mesures et additionne les valeurs de toutes celles qui ont les meme projet/label/date<br/>
     * 
     * 
     * @param measures
     * @return
     * @throws ServiceException
     */
    public static List<Measure> getCalculatedMeasures(List<Measure> measures) throws ServiceException {

        List<Measure> calculatedMeasures = new ArrayList<Measure>();
        HashMap<String, Measure> measureToCalculate = new HashMap<String, Measure>();

        for (Measure measure : measures) {
            if (!measureToCalculate.containsKey(StatisticGenerator.getMeasureHash(measure))) {
                measureToCalculate.put(StatisticGenerator.getMeasureHash(measure), measure);

                Measure globalMeasure = AppServiceFacade.getStatisticService().getMeasure(measure.getDate(), measure.getStatistic(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
                calculatedMeasures.add(globalMeasure);
            }
        }
        return calculatedMeasures;
    }

    /**
     * Génère une clé unique pour toues les mesures de même date/projet/label.
     * 
     * @param measure
     * @return
     */
    private static String getMeasureHash(Measure measure) {
        return measure.getStartingDateStr() + "-" + measure.getProject() + "-" + measure.getLabel();
    }

    private StatisticGenerator() {

    }

}
