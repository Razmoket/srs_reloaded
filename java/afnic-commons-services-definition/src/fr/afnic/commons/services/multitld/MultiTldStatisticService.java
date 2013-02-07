package fr.afnic.commons.services.multitld;

import java.util.Date;
import java.util.List;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.statistics.DateOfMeasure;
import fr.afnic.commons.beans.statistics.Measure;
import fr.afnic.commons.beans.statistics.Project;
import fr.afnic.commons.beans.statistics.Statistic;
import fr.afnic.commons.services.IStatisticService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldStatisticService implements IStatisticService {

    protected MultiTldStatisticService() {
        super();
    }

    @Override
    public Measure createOrIncrementMeasure(Measure measure, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getStatisticService().createOrIncrementMeasure(measure, userId, tld);
    }

    @Override
    public Measure createOrUpdateMeasure(Measure measure, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getStatisticService().createOrUpdateMeasure(measure, userId, tld);
    }

    @Override
    public boolean createMeasureIfNotExist(Measure measure, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getStatisticService().createMeasureIfNotExist(measure, userId, tld);
    }

    @Override
    public Measure getMeasure(DateOfMeasure date, Statistic statistic, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getStatisticService().getMeasure(date, statistic, userId, tld);
    }

    @Override
    public Measure getMeasure(DateOfMeasure date, Statistic statistic, String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getStatisticService().getMeasure(date, statistic, userLogin, userId, tld);
    }

    @Override
    public List<Measure> getMeasuresWithProject(Project project, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getStatisticService().getMeasuresWithProject(project, userId, tld);
    }

    @Override
    public List<Measure> getMeasuresWithProjectAndUser(Project project, String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getStatisticService().getMeasuresWithProjectAndUser(project, userLogin, userId, tld);
    }

    @Override
    public List<Measure> getMeasuresWithProjectBetweenTwoDates(Project project, Date startDate, Date endDate, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getStatisticService().getMeasuresWithProjectBetweenTwoDates(project, startDate, endDate, userId, tld);
    }
}
