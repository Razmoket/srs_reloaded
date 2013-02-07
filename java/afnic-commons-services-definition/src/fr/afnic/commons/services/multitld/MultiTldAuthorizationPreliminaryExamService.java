package fr.afnic.commons.services.multitld;

import java.util.List;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.AuthorizationPreliminaryExam;
import fr.afnic.commons.services.IAuthorizationPreliminaryExamService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldAuthorizationPreliminaryExamService implements IAuthorizationPreliminaryExamService {

    protected MultiTldAuthorizationPreliminaryExamService() {
        super();
    }

    @Override
    public List<AuthorizationPreliminaryExam> getAuthorizationPreliminaryExamsToUseForAuthorizationRequestCreation(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationPreliminaryExamService().getAuthorizationPreliminaryExamsToUseForAuthorizationRequestCreation(userId, tld);
    }

    @Override
    public AuthorizationPreliminaryExam getAuthorizationPreliminaryExamWithId(int id, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationPreliminaryExamService().getAuthorizationPreliminaryExamWithId(id, userId, tld);
    }

    @Override
    public AuthorizationPreliminaryExam createAuthorizationPreliminaryExam(AuthorizationPreliminaryExam authorizationPreliminaryExam, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationPreliminaryExamService().createAuthorizationPreliminaryExam(authorizationPreliminaryExam, userId, tld);
    }

    @Override
    public AuthorizationPreliminaryExam updateAuthorizationPreliminaryExam(AuthorizationPreliminaryExam authorizationPreliminaryExam, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationPreliminaryExamService().updateAuthorizationPreliminaryExam(authorizationPreliminaryExam, userId, tld);
    }

    @Override
    public List<AuthorizationPreliminaryExam> getPendingAuthorizationPreliminaryExamWithDomainName(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationPreliminaryExamService().getPendingAuthorizationPreliminaryExamWithDomainName(domainName, userId, tld);
    }

    @Override
    public List<AuthorizationPreliminaryExam> getAuthorizationPreliminaryExams(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationPreliminaryExamService().getAuthorizationPreliminaryExams(userId, tld);
    }

    @Override
    public void delete(AuthorizationPreliminaryExam exam, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getAuthorizationPreliminaryExamService().delete(exam, userId, tld);
    }

}
