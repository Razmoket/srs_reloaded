package fr.afnic.commons.services.contracts.operation;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.beans.operations.qualification.operation.PortfolioStatusUpdate;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

public class OperationServiceCreateStatusUpdateTest {

    @Test
    public void createStatusUpdate() throws Exception {
        Operation createOperationContent = this.generatePortfolioStatusUpdate();

        OperationId create = AppServiceFacade.getOperationService().create(createOperationContent, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        System.out.println("create=" + create.getIntValue());
        PortfolioStatusUpdate operation = AppServiceFacade.getOperationService().getOperation(create, PortfolioStatusUpdate.class, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        OperationTestCase.assertOperation(createOperationContent, operation);
        TestCase.assertEquals(((PortfolioStatusUpdate) createOperationContent).getOldValue(), operation.getOldValue());
        TestCase.assertEquals(((PortfolioStatusUpdate) createOperationContent).getNewValue(), operation.getNewValue());

        TestCase.assertEquals(0, operation.getObjectVersion());
    }

    private PortfolioStatusUpdate generatePortfolioStatusUpdate() throws ServiceException {
        PortfolioStatusUpdate update = new PortfolioStatusUpdate(UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        update.setCreateUserId(UserGenerator.getRootUserId());
        update.setNewValue(PortfolioStatus.Frozen);
        update.setOldValue(PortfolioStatus.Active);

        return update;
    }
}
