/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.statistics.DateOfMeasure;
import fr.afnic.commons.beans.statistics.Measure;
import fr.afnic.commons.beans.statistics.Project;
import fr.afnic.commons.beans.statistics.Statistic;
import fr.afnic.commons.services.IStatisticService;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MockStatisticService implements IStatisticService {

    private final List<Measure> measures = new ArrayList<Measure>();

    @Override
    public Measure createOrIncrementMeasure(Measure measure, UserId userId, TldServiceFacade tld) throws ServiceException {
        Measure lastMeasure = this.getLastMeasure(measure);
        Measure newMeasure = null;

        if (lastMeasure == null) {
            newMeasure = measure;
        } else {
            this.measures.remove(lastMeasure);
            newMeasure = lastMeasure.plus(1);
        }

        this.measures.add(newMeasure);

        return newMeasure;
    }

    @Override
    public Measure createOrUpdateMeasure(Measure measure, UserId userId, TldServiceFacade tld) throws ServiceException {
        Measure lastMeasure = this.getLastMeasure(measure);

        if (lastMeasure != null) {
            this.measures.remove(lastMeasure);
        }

        this.measures.add(measure);
        return measure;
    }

    @Override
    public boolean createMeasureIfNotExist(Measure measure, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (this.getLastMeasure(measure) == null) {
            this.measures.add(measure);
            return true;
        }
        return false;
    }

    private Measure getLastMeasure(Measure measure) {
        for (Measure lastMeasure : this.measures) {
            if (lastMeasure.getProject().equals(measure.getProject())
                && lastMeasure.getLabel().equals(measure.getLabel())
                && lastMeasure.getDate().equals(measure.getDate())) {
                return lastMeasure;
            }
        }
        return null;
    }

    @Override
    public Measure getMeasure(DateOfMeasure date, Statistic statistic, UserId userId, TldServiceFacade tld) throws ServiceException {
        for (Measure measure : this.measures) {
            if (measure.getDate().equals(date)
                && measure.getStatistic().equals(statistic)) {
                return measure;
            }
        }
        return null;
    }

    @Override
    public Measure getMeasure(DateOfMeasure date, Statistic statistic, String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        for (Measure measure : this.measures) {
            if (measure.getDate().equals(date)
                && measure.getStatistic().equals(statistic)
                && measure.getUserLogin().equals(userLogin)) {
                return measure;
            }
        }
        return null;
    }

    @Override
    public List<Measure> getMeasuresWithProject(Project project, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public List<Measure> getMeasuresWithProjectAndUser(Project project, String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public List<Measure> getMeasuresWithProjectBetweenTwoDates(Project project, Date startDate, Date endDate, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

}
