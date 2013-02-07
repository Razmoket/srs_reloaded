package fr.afnic.commons.services.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.AuthorizationPreliminaryExam;
import fr.afnic.commons.beans.request.AuthorizationPreliminaryExamIdOrdering;
import fr.afnic.commons.beans.request.AuthorizationPreliminaryExamStatus;
import fr.afnic.commons.beans.request.AuthorizationRequest;
import fr.afnic.commons.services.IAuthorizationPreliminaryExamService;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MockAuthorizationPreliminaryExamService implements IAuthorizationPreliminaryExamService {

    private final static Map<Integer, AuthorizationPreliminaryExam> MAP = new HashMap<Integer, AuthorizationPreliminaryExam>();

    private static int LAST_ID = 1;

    @Override
    public AuthorizationPreliminaryExam createAuthorizationPreliminaryExam(AuthorizationPreliminaryExam authorizationPreliminaryExam, UserId userId, TldServiceFacade tld) throws ServiceException {
        authorizationPreliminaryExam.setCreateDate(new Date());
        authorizationPreliminaryExam.setId(MockAuthorizationPreliminaryExamService.LAST_ID++);
        MAP.put(authorizationPreliminaryExam.getId(), authorizationPreliminaryExam);
        return authorizationPreliminaryExam.copy();
    }

    @Override
    public List<AuthorizationPreliminaryExam> getPendingAuthorizationPreliminaryExamWithDomainName(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException {
        List<AuthorizationPreliminaryExam> results = new ArrayList<AuthorizationPreliminaryExam>();
        for (AuthorizationPreliminaryExam exam : MAP.values()) {
            if (exam.getStatus() == AuthorizationPreliminaryExamStatus.Pending && StringUtils.equals(domainName, exam.getDomainName())) {
                results.add(exam.copy());
            }
        }
        return results;
    }

    @Override
    public AuthorizationPreliminaryExam updateAuthorizationPreliminaryExam(AuthorizationPreliminaryExam authorizationPreliminaryExam, UserId userId, TldServiceFacade tld) throws ServiceException {

        MAP.put(authorizationPreliminaryExam.getId(), authorizationPreliminaryExam.copy());
        return authorizationPreliminaryExam.copy();
    }

    @Override
    public List<AuthorizationPreliminaryExam> getAuthorizationPreliminaryExams(UserId userId, TldServiceFacade tld) throws ServiceException {
        List<AuthorizationPreliminaryExam> results = new ArrayList<AuthorizationPreliminaryExam>();
        for (AuthorizationPreliminaryExam exam : MAP.values()) {
            results.add(exam.copy());
        }
        return results;
    }

    @Override
    public void delete(AuthorizationPreliminaryExam exam, UserId userId, TldServiceFacade tld) throws ServiceException {
        MAP.remove(exam.getId());
    }

    @Override
    public List<AuthorizationPreliminaryExam> getAuthorizationPreliminaryExamsToUseForAuthorizationRequestCreation(UserId userId, TldServiceFacade tld) throws ServiceException {

        List<AuthorizationPreliminaryExam> results = new ArrayList<AuthorizationPreliminaryExam>();
        List<String> domainNames = new ArrayList<String>();

        List<AuthorizationPreliminaryExam> availableExams = new ArrayList<AuthorizationPreliminaryExam>();
        availableExams.addAll(MAP.values());
        List<AuthorizationPreliminaryExam> sortedAvailableExams = AuthorizationPreliminaryExamIdOrdering.sortListById(availableExams);

        for (AuthorizationRequest request : MockAuthorizationRequestService.MAP.values()) {
            if (request.hasNotFinalStatus()) {
                domainNames.add(request.getRequestedDomainName());
            }
        }

        for (AuthorizationPreliminaryExam exam : MAP.values()) {
            if (exam.getStatus() == AuthorizationPreliminaryExamStatus.Running) {
                domainNames.add(exam.getDomainName());
            }
        }

        for (AuthorizationPreliminaryExam exam : sortedAvailableExams) {
            if (exam.getStatus() == AuthorizationPreliminaryExamStatus.Pending
                && !domainNames.contains(exam.getDomainName())) {
                results.add(exam);
                domainNames.add(exam.getDomainName());
            }
        }
        return results;
    }

    @Override
    public AuthorizationPreliminaryExam getAuthorizationPreliminaryExamWithId(int id, UserId userId, TldServiceFacade tld) throws ServiceException {
        AuthorizationPreliminaryExam result = null;

        for (AuthorizationPreliminaryExam exam : MAP.values()) {
            if (exam.getId() == id) {
                result = exam.copy();
            }
        }

        if (result != null) {
            return result;
        } else {
            throw new NotFoundException("No AuthorizationPreliminaryExam found with id " + id);
        }
    }

}
