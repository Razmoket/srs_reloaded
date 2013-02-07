package fr.afnic.commons.services;

import java.util.Date;
import java.util.List;

import fr.afnic.commons.beans.export.ExportView;
import fr.afnic.commons.beans.list.IView;
import fr.afnic.commons.beans.list.QualificationStatResultList;
import fr.afnic.commons.beans.list.ResultList;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.statistics.StatisticsView;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public interface IResultListService {

    public ResultList<?> getResultList(IView view, UserId userId, TldServiceFacade tld) throws ServiceException;

    public int getResultListCount(IView view, UserId userId, TldServiceFacade tld) throws ServiceException;

    public QualificationStatResultList getResultList(StatisticsView view, Date date, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<Date> getListDate(StatisticsView view, UserId userId, TldServiceFacade tld) throws ServiceException;

    public Date getLastDate(StatisticsView view, UserId userId, TldServiceFacade tld) throws ServiceException;

    public String getPeriodicity(StatisticsView view, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void createValoTag(Date date, Date dateEnd, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void createJustifBlock(Date date, Date dateEnd, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void createJustifDelete(Date date, Date dateEnd, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void createBeWhois(Date date, Date dateEnd, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<ExportView> getExportViews(UserId userId, TldServiceFacade tld) throws ServiceException;;

}
