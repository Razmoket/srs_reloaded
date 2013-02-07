/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.services.contracts.authorization;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import fr.afnic.commons.beans.request.AuthorizationPreliminaryExam;
import fr.afnic.commons.beans.request.AuthorizationPreliminaryExamStatus;
import fr.afnic.commons.beans.request.AuthorizationRequest;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.RegistrarGenerator;
import fr.afnic.commons.test.generator.UserGenerator;

public class AuthorizationServicePreliminaryExamContractTest {

    @Before
    public void clean() throws Exception {
        for (AuthorizationRequest request : AppServiceFacade.getAuthorizationRequestService().getAuthorizationRequests(UserGenerator.getRootUserId(), TldServiceFacade.Fr)) {
            if (request.hasNotFinalStatus()) {
                AppServiceFacade.getRequestService().cancelRequest(request, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            }
        }

        for (AuthorizationPreliminaryExam exam : AppServiceFacade.getAuthorizationPreliminaryExamService().getAuthorizationPreliminaryExams(UserGenerator.getRootUserId(), TldServiceFacade.Fr)) {
            AppServiceFacade.getAuthorizationPreliminaryExamService().delete(exam, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        }
    }

    @Test
    public void testCreateAuthorizationPreliminaryExam() throws Exception {

        AuthorizationPreliminaryExam exam = this.createAuthorizationPreliminaryExam("test-insert.fr", AuthorizationPreliminaryExamStatus.Running);

        AuthorizationPreliminaryExam insertedExam = AppServiceFacade.getAuthorizationPreliminaryExamService().createAuthorizationPreliminaryExam(exam, UserGenerator.getRootUserId(),
                                                                                                                                                 TldServiceFacade.Fr);
        Assert.assertNotNull("insertedExam should not be null", insertedExam);

        Assert.assertEquals("Bad domain name", exam.getDomainName(), insertedExam.getDomainName());
        Assert.assertEquals("Bad registrar code", exam.getRegistrarCode(), insertedExam.getRegistrarCode());
        Assert.assertEquals("Bad holder handle", exam.getHolderHandle(), insertedExam.getHolderHandle());
        Assert.assertEquals("Bad motivation", exam.getMotivation(), insertedExam.getMotivation());
        Assert.assertEquals("Bad Status", exam.getMotivation(), insertedExam.getMotivation());
        Assert.assertTrue("Bad createDate", DateUtils.isSameDay(new Date(), exam.getCreateDate()));
        Assert.assertTrue("Id should be greater than 0", insertedExam.getId() > 0);
    }

    @Test
    public void testGetAuthorizationPreliminaryExamWithId() throws Exception {

        AuthorizationPreliminaryExam insertedExam = this.insertAuthorizationPreliminaryExam("test-get.fr", AuthorizationPreliminaryExamStatus.Running);
        Assert.assertNotNull("insertedExam should not be null", insertedExam);

        AuthorizationPreliminaryExam loadedExam = AppServiceFacade.getAuthorizationPreliminaryExamService().getAuthorizationPreliminaryExamWithId(insertedExam.getId(), UserGenerator.getRootUserId(),
                                                                                                                                                  TldServiceFacade.Fr);

        Assert.assertEquals("Bad domain name", insertedExam.getDomainName(), loadedExam.getDomainName());
        Assert.assertEquals("Bad registrar code", insertedExam.getRegistrarCode(), loadedExam.getRegistrarCode());
        Assert.assertEquals("Bad holder handle", insertedExam.getHolderHandle(), loadedExam.getHolderHandle());
        Assert.assertEquals("Bad motivation", insertedExam.getMotivation(), loadedExam.getMotivation());
        Assert.assertEquals("Bad Status", insertedExam.getMotivation(), loadedExam.getMotivation());
        Assert.assertEquals("Bad id", insertedExam.getId(), loadedExam.getId());
        Assert.assertEquals("Bad createDate", insertedExam.getCreateDate(), loadedExam.getCreateDate());

    }

    @Test
    public void testGetAuthorizationPreliminaryExams() throws Exception {
        this.insertAuthorizationPreliminaryExam("test-get-exams-1.fr", AuthorizationPreliminaryExamStatus.Running);
        this.insertAuthorizationPreliminaryExam("test-get-exams-2.fr", AuthorizationPreliminaryExamStatus.Aborted);
        this.insertAuthorizationPreliminaryExam("test-get-exams-3.fr", AuthorizationPreliminaryExamStatus.Used);

        List<AuthorizationPreliminaryExam> exams = AppServiceFacade.getAuthorizationPreliminaryExamService().getAuthorizationPreliminaryExams(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        Assert.assertNotNull("exams should not be null", exams);
        Assert.assertEquals("exams should contains 3 elements", 3, exams.size());

        // Vérifie que les résultats sont triés par id
        int max = -1;
        for (int i = 0; i < 3; i++) {
            Assert.assertTrue("bad exam id: " + exams.get(i).getId() + " <= " + max, exams.get(i).getId() > max);
            max = exams.get(i).getId();
        }

    }

    @Test
    public void testGetPendingAuthorizationPreliminaryExamsToUseForAuthorizationRequestCreationWithStatus() throws Exception {
        this.insertAuthorizationPreliminaryExam("test-get-pending-1.fr", AuthorizationPreliminaryExamStatus.Running);
        this.insertAuthorizationPreliminaryExam("test-get-pending-2.fr", AuthorizationPreliminaryExamStatus.Aborted);
        this.insertAuthorizationPreliminaryExam("test-get-pending-3.fr", AuthorizationPreliminaryExamStatus.Accepted);
        AuthorizationPreliminaryExam pendingExam = this.insertAuthorizationPreliminaryExam("test-get-pending-4.fr", AuthorizationPreliminaryExamStatus.Pending);
        this.insertAuthorizationPreliminaryExam("test-get-pending-5.fr", AuthorizationPreliminaryExamStatus.Rejected);

        List<AuthorizationPreliminaryExam> pendings = AppServiceFacade.getAuthorizationPreliminaryExamService()
                                                                      .getAuthorizationPreliminaryExamsToUseForAuthorizationRequestCreation(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        Assert.assertNotNull("pendings should not be null", pendings);
        Assert.assertEquals("pendings should contains one element", 1, pendings.size());

        AuthorizationPreliminaryExam exam = pendings.get(0);
        Assert.assertEquals("Bad exam in list", pendingExam.getId(), exam.getId());

    }

    @Test
    public void testGetPendingAuthorizationPreliminaryExamsToUseForAuthorizationRequestCreationWithSameDomainNameInPending() throws Exception {
        String domainName = "test-with-same-name.fr";
        AuthorizationPreliminaryExam firstPendingExam = this.insertAuthorizationPreliminaryExam(domainName, AuthorizationPreliminaryExamStatus.Pending);
        this.insertAuthorizationPreliminaryExam(domainName, AuthorizationPreliminaryExamStatus.Pending);

        List<AuthorizationPreliminaryExam> pendings = AppServiceFacade.getAuthorizationPreliminaryExamService()
                                                                      .getAuthorizationPreliminaryExamsToUseForAuthorizationRequestCreation(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        Assert.assertNotNull("pendings should not be null", pendings);
        Assert.assertEquals("pendings should contains one element", 1, pendings.size());

        AuthorizationPreliminaryExam exam = pendings.get(0);
        Assert.assertEquals("Bad exam in list", firstPendingExam.getId(), exam.getId());

    }

    @Test
    public void testGetPendingAuthorizationPreliminaryExamsToUseForAuthorizationRequestCreationWithSameDomainNameInPendingAndRunning() throws Exception {
        String domainName = "test-with-same-name.fr";
        this.insertAuthorizationPreliminaryExam(domainName, AuthorizationPreliminaryExamStatus.Pending);
        this.insertAuthorizationPreliminaryExam(domainName, AuthorizationPreliminaryExamStatus.Running);

        List<AuthorizationPreliminaryExam> pendings = AppServiceFacade.getAuthorizationPreliminaryExamService()
                                                                      .getAuthorizationPreliminaryExamsToUseForAuthorizationRequestCreation(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        Assert.assertNotNull("pendings should not be null", pendings);
        Assert.assertEquals("pendings should be empty", 0, pendings.size());
    }

    @Test
    public void testGetPendingAuthorizationPreliminaryExamsToUseForAuthorizationRequestCreationWithSameDomainNameInPendingAndNonRunning() throws Exception {
        String domainName = "test-with-same-name.fr";
        AuthorizationPreliminaryExam firstPendingExam = this.insertAuthorizationPreliminaryExam(domainName, AuthorizationPreliminaryExamStatus.Pending);
        this.insertAuthorizationPreliminaryExam(domainName, AuthorizationPreliminaryExamStatus.Rejected);
        this.insertAuthorizationPreliminaryExam(domainName, AuthorizationPreliminaryExamStatus.Aborted);
        this.insertAuthorizationPreliminaryExam(domainName, AuthorizationPreliminaryExamStatus.Accepted);

        List<AuthorizationPreliminaryExam> pendings = AppServiceFacade.getAuthorizationPreliminaryExamService()
                                                                      .getAuthorizationPreliminaryExamsToUseForAuthorizationRequestCreation(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        Assert.assertNotNull("pendings should not be null", pendings);
        Assert.assertEquals("pendings should contains one element", 1, pendings.size());

        AuthorizationPreliminaryExam exam = pendings.get(0);
        Assert.assertEquals("Bad exam in list", firstPendingExam.getId(), exam.getId());

    }

    private AuthorizationPreliminaryExam insertAuthorizationPreliminaryExam(String domainName, AuthorizationPreliminaryExamStatus status) throws ServiceException {
        AuthorizationPreliminaryExam exam = this.createAuthorizationPreliminaryExam(domainName, status);
        AuthorizationPreliminaryExam insertedExam = AppServiceFacade.getAuthorizationPreliminaryExamService().createAuthorizationPreliminaryExam(exam, UserGenerator.getRootUserId(),
                                                                                                                                                 TldServiceFacade.Fr);
        return insertedExam;
    }

    @Test
    public void testGetPendingAuthorizationPreliminaryExamsToUseForAuthorizationRequestCreationWithPendingExamAndRunningAuthorizationRequest() throws Exception {
        String domainName = "test-with-authorization-request.fr";
        AuthorizationPreliminaryExam pendingExam = this.insertAuthorizationPreliminaryExam(domainName, AuthorizationPreliminaryExamStatus.Pending);

        AppServiceFacade.getAuthorizationRequestService().createAuthorizationRequest(pendingExam.createAuthorizationRequest(UserGenerator.getRootUserId(), TldServiceFacade.Fr),
                                                                                     UserGenerator.getRootUserId(),
                                                                                     TldServiceFacade.Fr);

        List<AuthorizationPreliminaryExam> pendings = AppServiceFacade.getAuthorizationPreliminaryExamService()
                                                                      .getAuthorizationPreliminaryExamsToUseForAuthorizationRequestCreation(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        Assert.assertNotNull("pendings should not be null", pendings);
        Assert.assertEquals("pendings should be empty", 0, pendings.size());
    }

    @Test
    public void testGetPendingAuthorizationPreliminaryExamWithDomainName() throws Exception {
        String domainName = "test-get-pending-with-domainName.fr";
        AuthorizationPreliminaryExam exam1 = this.insertAuthorizationPreliminaryExam(domainName, AuthorizationPreliminaryExamStatus.Pending);
        this.insertAuthorizationPreliminaryExam(domainName, AuthorizationPreliminaryExamStatus.Running);
        AuthorizationPreliminaryExam exam2 = this.insertAuthorizationPreliminaryExam(domainName, AuthorizationPreliminaryExamStatus.Pending);
        this.insertAuthorizationPreliminaryExam("another-" + domainName, AuthorizationPreliminaryExamStatus.Pending);

        List<AuthorizationPreliminaryExam> results = AppServiceFacade.getAuthorizationPreliminaryExamService().getPendingAuthorizationPreliminaryExamWithDomainName(domainName,
                                                                                                                                                                    UserGenerator.getRootUserId(),
                                                                                                                                                                    TldServiceFacade.Fr);
        Assert.assertNotNull("results cannot be null", results);
        Assert.assertEquals("results should be contains 2 elements", 2, results.size());

        Assert.assertEquals("Bad element 1", exam1.getId(), results.get(0).getId());
        Assert.assertEquals("Bad element 2", exam2.getId(), results.get(1).getId());
    }

    @Test
    public void testGetPendingAuthorizationPreliminaryExamWithDomainNameWithNoResult() throws Exception {
        String domainName = "test-get-pending-with-domainName-no-result.fr";
        List<AuthorizationPreliminaryExam> results = AppServiceFacade.getAuthorizationPreliminaryExamService().getPendingAuthorizationPreliminaryExamWithDomainName(domainName,
                                                                                                                                                                    UserGenerator.getRootUserId(),
                                                                                                                                                                    TldServiceFacade.Fr);
        Assert.assertNotNull("results cannot be null", results);
        Assert.assertTrue("results should be empty", results.isEmpty());
    }

    @Test
    public void updateAuthorizationPreliminaryExam() throws Exception {
        AuthorizationPreliminaryExam exam = this.insertAuthorizationPreliminaryExam("toto.fr", AuthorizationPreliminaryExamStatus.Pending);

        exam.setStatus(AuthorizationPreliminaryExamStatus.Accepted);
        AppServiceFacade.getAuthorizationPreliminaryExamService().updateAuthorizationPreliminaryExam(exam, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        AuthorizationPreliminaryExam examAfterUpdate = AppServiceFacade.getAuthorizationPreliminaryExamService().getAuthorizationPreliminaryExamWithId(exam.getId(), UserGenerator.getRootUserId(),
                                                                                                                                                       TldServiceFacade.Fr);
        Assert.assertEquals("Status not updated", AuthorizationPreliminaryExamStatus.Accepted, examAfterUpdate.getStatus());
    }

    private AuthorizationPreliminaryExam createAuthorizationPreliminaryExam(String domainName, AuthorizationPreliminaryExamStatus status) {

        AuthorizationPreliminaryExam exam = new AuthorizationPreliminaryExam();
        exam.setDomainName(domainName);
        exam.setRegistrarCode(RegistrarGenerator.CODE_TEST);
        exam.setMotivation("my motivation");
        exam.setHolderHandle("ABC123-FRNIC");
        exam.setStatus(status);
        return exam;

    }
}
