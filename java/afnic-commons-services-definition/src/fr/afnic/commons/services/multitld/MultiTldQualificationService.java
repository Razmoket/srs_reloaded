package fr.afnic.commons.services.multitld;

import java.util.List;

import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.OperationView;
import fr.afnic.commons.beans.operations.qualification.AutoMailReachability;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.QualificationId;
import fr.afnic.commons.beans.operations.qualification.QualificationSnapshot;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IQualificationService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.Delay;

public class MultiTldQualificationService implements IQualificationService {

    protected MultiTldQualificationService() {
        super();
    }

    @Override
    public Qualification createAndGet(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getQualificationService().createAndGet(qualification, userId, tld);
    }

    @Override
    public OperationId create(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getQualificationService().create(qualification, userId, tld);
    }

    @Override
    public void updateTopLevelStatus(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getQualificationService().updateTopLevelStatus(qualification, userId, tld);
    }

    @Override
    public void updateReachStatus(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getQualificationService().updateReachStatus(qualification, userId, tld);
    }

    @Override
    public void updateEligibilityStatus(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getQualificationService().updateEligibilityStatus(qualification, userId, tld);
    }

    @Override
    public void updatePortfolioStatus(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getQualificationService().updatePortfolioStatus(qualification, userId, tld);
    }

    @Override
    public void setIncoherent(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getQualificationService().setIncoherent(qualification, userId, tld);
    }

    @Override
    public void populateQualification(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getQualificationService().populateQualification(qualification, userId, tld);
    }

    @Override
    public Qualification getQualification(OperationId operationId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getQualificationService().getQualification(operationId, userId, tld);
    }

    @Override
    public Qualification getQualification(QualificationId qualificationId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getQualificationService().getQualification(qualificationId, userId, tld);
    }

    @Override
    public boolean isExistingQualificationId(QualificationId qualificationId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getQualificationService().isExistingQualificationId(qualificationId, userId, tld);
    }

    @Override
    public Qualification getQualificationInProgress(String nicHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getQualificationService().getQualificationInProgress(nicHandle, userId, tld);
    }

    @Override
    public List<Qualification> getQualifications(OperationView view, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getQualificationService().getQualifications(view, userId, tld);
    }

    @Override
    public int getQualificationsCount(OperationView view, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getQualificationService().getQualificationsCount(view, userId, tld);
    }

    @Override
    public List<Qualification> getQualificationsToUpdate(OperationView view, Delay delay, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getQualificationService().getQualificationsToUpdate(view, delay, userId, tld);
    }

    @Override
    public List<Qualification> getQualifications(String nicHanlde, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getQualificationService().getQualifications(nicHanlde, userId, tld);
    }

    @Override
    public int getQualificationInProgressCount(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getQualificationService().getQualificationInProgressCount(userId, tld);
    }

    @Override
    public int createSnapshot(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getQualificationService().createSnapshot(qualification, userId, tld);
    }

    @Override
    public QualificationSnapshot getQualificationSnapshot(int snapshotId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getQualificationService().getQualificationSnapshot(snapshotId, userId, tld);
    }

    @Override
    public AutoMailReachability checkKey(String key, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getQualificationService().checkKey(key, userId, tld);
    }

    @Override
    public void updateAutoMail(QualificationId id, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getQualificationService().updateAutoMail(id, userId, tld);
    }

    @Override
    public List<QualificationId> getListQualificationWaitingCheckAutoReachability(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getQualificationService().getListQualificationWaitingCheckAutoReachability(userId, tld);
    }

    @Override
    public List<QualificationId> getListQualificationWaitingCheckAutoReminderReachability(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getQualificationService().getListQualificationWaitingCheckAutoReminderReachability(userId, tld);
    }

    @Override
    public List<QualificationId> getListQualificationAutoReminderTimeout(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getQualificationService().getListQualificationAutoReminderTimeout(userId, tld);
    }

    @Override
    public void createQualificationCheckAutoReachability(Qualification qualification, boolean reminder, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getQualificationService().createQualificationCheckAutoReachability(qualification, reminder, userId, tld);
    }

    @Override
    public String getQualificationCheckAutoReachability(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getQualificationService().getQualificationCheckAutoReachability(qualification, userId, tld);
    }

    @Override
    public void updateComment(QualificationId qualificationId, String comment, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getQualificationService().updateComment(qualificationId, comment, userId, tld);
    }

    @Override
    public void updateEnQualif(UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getQualificationService().updateEnQualif(userId, tld);
    }

    @Override
    public int getNbQualifLaunchedToday(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getQualificationService().getNbQualifLaunchedToday(userId, tld);
    }
}
