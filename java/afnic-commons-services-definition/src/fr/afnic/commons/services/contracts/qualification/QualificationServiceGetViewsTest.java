package fr.afnic.commons.services.contracts.qualification;

import java.util.List;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.operations.OperationView;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.QualificationGenerator;
import fr.afnic.commons.test.generator.UserGenerator;
import fr.afnic.utils.Delay;

public class QualificationServiceGetViewsTest {

    public void init() {

    }

    @Test
    public void getQualificationsToUpdateWithDelay() throws Exception {
        this.assertGetQualificationsToUpdate(PortfolioStatus.Active, OperationView.QualificationsToUpdateInPendingFreeze);
        this.assertGetQualificationsToUpdate(PortfolioStatus.PendingFreeze, OperationView.QualificationsToUpdateInFreeze);
        this.assertGetQualificationsToUpdate(PortfolioStatus.Frozen, OperationView.QualificationsToUpdateInPendingBlock);
        this.assertGetQualificationsToUpdate(PortfolioStatus.PendingBlock, OperationView.QualificationsToUpdateInBlock);
        this.assertGetQualificationsToUpdate(PortfolioStatus.Blocked, OperationView.QualificationsToUpdateInPendingSuppress);
        this.assertGetQualificationsToUpdate(PortfolioStatus.PendingSuppress, OperationView.QualificationsToUpdateInSuppress);

    }

    private void assertGetQualificationsToUpdate(PortfolioStatus portfolioStatus, OperationView view) throws ServiceException {
        Qualification qualification = QualificationGenerator.createQualification();
        qualification.setPortfolioStatus(portfolioStatus);
        AppServiceFacade.getQualificationService().updatePortfolioStatus(qualification, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        UserId userId = UserGenerator.getRootUserId();

        int delayInSeconds = 2;
        new Delay(delayInSeconds * 5, TimeUnit.SECONDS).sleep();

        List<Qualification> qualifList = AppServiceFacade.getQualificationService().getQualificationsToUpdate(view, new Delay(delayInSeconds, TimeUnit.SECONDS), userId, TldServiceFacade.Fr);

        String test = "portfolioStatus: " + portfolioStatus + "; view: " + view;

        TestCase.assertNotNull(test, qualifList);
        TestCase.assertFalse(test, qualifList.isEmpty());
        TestCase.assertTrue(test, this.isQualificationInList(qualification, qualifList));
    }

    private boolean isQualificationInList(Qualification qualification, List<Qualification> qualifList) {
        for (Qualification qualif : qualifList) {
            if (qualif.getIdAsInt() == qualification.getIdAsInt()) {
                return true;
            }
        }
        return false;
    }
}
