package fr.afnic.commons.services.multitld;

import java.util.Date;
import java.util.List;

import fr.afnic.commons.beans.export.ExportView;
import fr.afnic.commons.beans.list.IView;
import fr.afnic.commons.beans.list.QualificationStatResultList;
import fr.afnic.commons.beans.list.ResultList;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.statistics.StatisticsView;
import fr.afnic.commons.services.IResultListService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldResultListService implements IResultListService {

    protected MultiTldResultListService() {
        super();
    }

    @Override
    public ResultList<?> getResultList(IView view, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getResultListService().getResultList(view, userId, tld);
    }

    @Override
    public int getResultListCount(IView view, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getResultListService().getResultListCount(view, userId, tld);
    }

    @Override
    public QualificationStatResultList getResultList(StatisticsView view, Date date, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getResultListService().getResultList(view, date, userId, tld);
    }

    @Override
    public List<Date> getListDate(StatisticsView view, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getResultListService().getListDate(view, userId, tld);
    }

    @Override
    public Date getLastDate(StatisticsView view, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getResultListService().getLastDate(view, userId, tld);
    }

    @Override
    public String getPeriodicity(StatisticsView view, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getResultListService().getPeriodicity(view, userId, tld);
    }

    @Override
    public void createValoTag(Date date, Date dateEnd, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getResultListService().createValoTag(date, dateEnd, userId, tld);
    }

    @Override
    public void createJustifBlock(Date date, Date dateEnd, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getResultListService().createJustifBlock(date, dateEnd, userId, tld);
    }

    @Override
    public void createJustifDelete(Date date, Date dateEnd, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getResultListService().createJustifDelete(date, dateEnd, userId, tld);
    }

    @Override
    public void createBeWhois(Date date, Date dateEnd, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getResultListService().createBeWhois(date, dateEnd, userId, tld);
    }

    @Override
    public List<ExportView> getExportViews(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getResultListService().getExportViews(userId, tld);
    }
}
