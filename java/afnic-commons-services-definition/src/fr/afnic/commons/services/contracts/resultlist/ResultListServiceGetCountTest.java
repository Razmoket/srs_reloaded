package fr.afnic.commons.services.contracts.resultlist;

import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.operations.OperationView;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.QualificationGenerator;
import fr.afnic.commons.test.generator.UserGenerator;
import fr.afnic.utils.Delay;

public class ResultListServiceGetCountTest {

    int delayInSeconds = 2;
    Delay delay = new Delay(this.delayInSeconds * 5, TimeUnit.SECONDS);

    @Test
    public void getCountForAllValidViews() throws ServiceException {
        Qualification qualification = QualificationGenerator.createQualification();

        this.assertTestResultListCount(qualification, PortfolioStatus.PendingFreeze, OperationView.ValorizationInPendingFreeze);
        this.assertTestResultListCount(qualification, PortfolioStatus.PendingBlock, OperationView.JustificationInPendingBlock);
        this.assertTestResultListCount(qualification, PortfolioStatus.PendingSuppress, OperationView.JustificationInPendingSuppress);
    }

    private void assertTestResultListCount(Qualification qualification, PortfolioStatus portfolioStatus, OperationView view) throws ServiceException {
        int resultListCountBefore = AppServiceFacade.getResultListService().getResultListCount(view, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        qualification.setPortfolioStatus(portfolioStatus);
        AppServiceFacade.getQualificationService().updatePortfolioStatus(qualification, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        this.delay.sleep();
        int resultListCountAfter = AppServiceFacade.getResultListService().getResultListCount(view, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertEquals(resultListCountBefore + 1, resultListCountAfter);
    }

    @Test
    public void getCountWithInvalidView() throws ServiceException {
        OperationView view = OperationView.QualificationsToUpdateInBlock;
        try {
            AppServiceFacade.getResultListService().getResultListCount(view, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("no identifierColumnName for view " + view, e.getMessage());
        }
    }

}
