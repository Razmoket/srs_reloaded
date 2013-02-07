package fr.afnic.commons.services.contracts.resultlist;

import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.junit.Ignore;
import org.junit.Test;

import fr.afnic.commons.beans.list.Line;
import fr.afnic.commons.beans.list.ResultList;
import fr.afnic.commons.beans.operations.OperationView;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.QualificationGenerator;
import fr.afnic.commons.test.generator.UserGenerator;
import fr.afnic.utils.Delay;

public class ResultListServiceGetTest {

    int delayInSeconds = 2;
    Delay delay = new Delay(this.delayInSeconds * 5, TimeUnit.SECONDS);

    @Test
    public void getListForAllValidViews() throws ServiceException {
        Qualification qualification = QualificationGenerator.createQualification();

        this.assertTestResultList(qualification, PortfolioStatus.PendingFreeze, OperationView.ValorizationInPendingFreeze);
        this.assertTestResultList(qualification, PortfolioStatus.PendingBlock, OperationView.JustificationInPendingBlock);
        this.assertTestResultList(qualification, PortfolioStatus.PendingSuppress, OperationView.JustificationInPendingSuppress);
    }

    private void assertTestResultList(Qualification qualification, PortfolioStatus portfolioStatus, OperationView view) throws ServiceException {
        ResultList<?> resultListBefore = AppServiceFacade.getResultListService().getResultList(view, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        qualification.setPortfolioStatus(portfolioStatus);
        AppServiceFacade.getQualificationService().updatePortfolioStatus(qualification, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        //this.delay.sleep();
        ResultList<?> resultListAfter = AppServiceFacade.getResultListService().getResultList(view, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        System.out.println("qualifId:" + qualification.getIdAsString() + " view:" + view);

        TestCase.assertNotNull(resultListBefore);
        TestCase.assertNotNull(resultListAfter);
        TestCase.assertEquals(resultListBefore.getIdentifierColumnName(), resultListAfter.getIdentifierColumnName());
        TestCase.assertEquals(resultListBefore.getIdentifierColumn(), resultListAfter.getIdentifierColumn());
        TestCase.assertEquals(resultListBefore.getView(), resultListAfter.getView());

        TestCase.assertNotNull(resultListBefore.getLines());
        TestCase.assertNotNull(resultListAfter.getLines());
        TestCase.assertFalse(resultListBefore.getLines().isEmpty());
        TestCase.assertFalse(resultListAfter.getLines().isEmpty());
        TestCase.assertEquals(resultListBefore.getLines().size() + 1, resultListAfter.getLines().size());

        TestCase.assertNotNull(resultListBefore.getColumns());
        TestCase.assertNotNull(resultListAfter.getColumns());
        TestCase.assertFalse(resultListBefore.getColumns().isEmpty());
        TestCase.assertFalse(resultListAfter.getColumns().isEmpty());
        TestCase.assertEquals(resultListBefore.getColumns().size(), resultListAfter.getColumns().size());

        boolean isQualifFound = false;
        for (Line line : resultListAfter.getLines()) {
            String value = line.getValue(resultListAfter.getIdentifierColumn());
            isQualifFound = qualification.getIdAsString().equals(value);
        }
        TestCase.assertTrue(isQualifFound);

    }

    @Test
    @Ignore
    public void getListWithInvalidView() throws ServiceException {
        OperationView view = OperationView.QualificationsToUpdateInBlock;
        try {
            AppServiceFacade.getResultListService().getResultList(view, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("no identifierColumnName for view " + view, e.getMessage());
        }

    }
}
