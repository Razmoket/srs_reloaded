package fr.afnic.commons.services;

import java.util.List;

import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.OperationView;
import fr.afnic.commons.beans.operations.qualification.AutoMailReachability;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.QualificationId;
import fr.afnic.commons.beans.operations.qualification.QualificationSnapshot;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.Delay;

public interface IQualificationService {

    public Qualification createAndGet(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException;

    public OperationId create(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void updateTopLevelStatus(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void updateReachStatus(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void updateEligibilityStatus(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void updatePortfolioStatus(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void setIncoherent(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void populateQualification(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException;

    public Qualification getQualification(OperationId operationId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public Qualification getQualification(QualificationId qualificationId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public boolean isExistingQualificationId(QualificationId qualificationId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public Qualification getQualificationInProgress(String nicHandle, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<Qualification> getQualifications(OperationView view, UserId userId, TldServiceFacade tld) throws ServiceException;

    public int getQualificationsCount(OperationView view, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<Qualification> getQualificationsToUpdate(OperationView view, Delay delay, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<Qualification> getQualifications(String nicHanlde, UserId userId, TldServiceFacade tld) throws ServiceException;

    public int getQualificationInProgressCount(UserId userId, TldServiceFacade tld) throws ServiceException;

    public int createSnapshot(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException;

    public QualificationSnapshot getQualificationSnapshot(int snapshotId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public AutoMailReachability checkKey(String key, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void updateAutoMail(QualificationId id, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<QualificationId> getListQualificationWaitingCheckAutoReachability(UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<QualificationId> getListQualificationWaitingCheckAutoReminderReachability(UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<QualificationId> getListQualificationAutoReminderTimeout(UserId userId, TldServiceFacade tld) throws ServiceException;

    public void createQualificationCheckAutoReachability(Qualification qualification, boolean reminder, UserId userId, TldServiceFacade tld) throws ServiceException;

    public String getQualificationCheckAutoReachability(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void updateComment(QualificationId qualificationId, String comment, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void updateEnQualif(UserId userId, TldServiceFacade tld) throws ServiceException;

    public int getNbQualifLaunchedToday(UserId userId, TldServiceFacade tld) throws ServiceException;

}
