package fr.afnic.commons.services.proxy;

import java.util.List;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.AuthorizationPreliminaryExam;
import fr.afnic.commons.services.IAuthorizationPreliminaryExamService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class ProxyAuthorizationPreliminaryExamService extends ProxyService<IAuthorizationPreliminaryExamService> implements IAuthorizationPreliminaryExamService {

    protected ProxyAuthorizationPreliminaryExamService() {
        super();
    }

    protected ProxyAuthorizationPreliminaryExamService(IAuthorizationPreliminaryExamService delegationService) {
        super(delegationService);
    }

    @Override
    public List<AuthorizationPreliminaryExam> getAuthorizationPreliminaryExamsToUseForAuthorizationRequestCreation(UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getAuthorizationPreliminaryExamsToUseForAuthorizationRequestCreation(userId, tld);
    }

    @Override
    public AuthorizationPreliminaryExam getAuthorizationPreliminaryExamWithId(int id, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getAuthorizationPreliminaryExamWithId(id, userId, tld);
    }

    @Override
    public AuthorizationPreliminaryExam createAuthorizationPreliminaryExam(AuthorizationPreliminaryExam authorizationPreliminaryExam, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().createAuthorizationPreliminaryExam(authorizationPreliminaryExam, userId, tld);
    }

    @Override
    public AuthorizationPreliminaryExam updateAuthorizationPreliminaryExam(AuthorizationPreliminaryExam authorizationPreliminaryExam, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().updateAuthorizationPreliminaryExam(authorizationPreliminaryExam, userId, tld);
    }

    @Override
    public List<AuthorizationPreliminaryExam> getPendingAuthorizationPreliminaryExamWithDomainName(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getPendingAuthorizationPreliminaryExamWithDomainName(domainName, userId, tld);
    }

    @Override
    public List<AuthorizationPreliminaryExam> getAuthorizationPreliminaryExams(UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getAuthorizationPreliminaryExams(userId, tld);
    }

    @Override
    public void delete(AuthorizationPreliminaryExam exam, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.getDelegationService().delete(exam, userId, tld);
    }
}
