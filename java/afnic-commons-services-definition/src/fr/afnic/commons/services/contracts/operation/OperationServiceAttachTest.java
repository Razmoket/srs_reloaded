package fr.afnic.commons.services.contracts.operation;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.DocumentGenerator;
import fr.afnic.commons.test.generator.OperationGenerator;
import fr.afnic.commons.test.generator.UserGenerator;

public class OperationServiceAttachTest {

    @Test
    public void createOneDocumentAttachmentWithNullOperation() throws ServiceException {
        UserId user = UserGenerator.getRootUserId();
        Document doc = DocumentGenerator.createSimpleDocument();
        try {
            AppServiceFacade.getOperationService().attach(null, user, doc, TldServiceFacade.Fr);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("operation cannot be null.", e.getMessage());
        }
    }

    @Test
    public void createOneDocumentAttachmentWithNullOperationId() throws ServiceException {
        Operation operation = OperationGenerator.generateSimpleOperationContent();
        UserId user = UserGenerator.getRootUserId();
        Document doc = DocumentGenerator.createSimpleDocument();
        operation.setId(null);
        try {
            AppServiceFacade.getOperationService().attach(operation, user, doc, TldServiceFacade.Fr);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("operation.id cannot be null.", e.getMessage());
        }
    }

    @Test
    public void createOneDocumentAttachmentWithUnknownOperationId() throws ServiceException {
        Operation operation = OperationGenerator.generateSimpleOperationContent();
        UserId user = UserGenerator.getRootUserId();
        Document doc = DocumentGenerator.createSimpleDocument();
        operation.setId(new OperationId(OperationGenerator.UNKNOWN_OPERATION_ID));
        try {
            AppServiceFacade.getOperationService().attach(operation, user, doc, TldServiceFacade.Fr);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("operation.id: OperationId[" + OperationGenerator.UNKNOWN_OPERATION_ID + "] does not exist.", e.getMessage());
        }
    }

    @Test
    public void createOneDocumentAttachmentWithNullUserId() throws ServiceException {
        Operation operation = OperationGenerator.createOperation();
        UserId user = null;
        Document doc = DocumentGenerator.createSimpleDocument();
        try {
            AppServiceFacade.getOperationService().attach(operation, user, doc, TldServiceFacade.Fr);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("updateUserId cannot be null.", e.getMessage());
        }
    }

    @Test
    public void createOneDocumentAttachmentWithUnknownUserId() throws ServiceException {
        Operation operation = OperationGenerator.createOperation();
        UserId user = new UserId(UserGenerator.UNKNOWN_USER_ID);
        Document doc = DocumentGenerator.createSimpleDocument();
        try {
            AppServiceFacade.getOperationService().attach(operation, user, doc, TldServiceFacade.Fr);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("updateUserId: UserId[" + UserGenerator.UNKNOWN_USER_ID + "] does not exist.", e.getMessage());
        }
    }

    @Test
    public void createOneDocumentAttachmentWithNullDoc() throws ServiceException {
        Operation operation = OperationGenerator.createOperation();
        UserId user = UserGenerator.getRootUserId();
        Document doc = null;
        try {
            AppServiceFacade.getOperationService().attach(operation, user, doc, TldServiceFacade.Fr);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("document cannot be null.", e.getMessage());
        }
    }

    @Test
    public void createOneDocumentAttachmentWithDocWithouHandle() throws ServiceException {
        Operation operation = OperationGenerator.createOperation();
        UserId user = UserGenerator.getRootUserId();
        Document doc = DocumentGenerator.createSimpleDocument();
        doc.setHandle(null);
        try {
            AppServiceFacade.getOperationService().attach(operation, user, doc, TldServiceFacade.Fr);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("document.handle cannot be null.", e.getMessage());
        }
    }

    @Test
    public void createOneDocumentAttachment() throws ServiceException {
        Operation operation = OperationGenerator.createOperation();
        UserId user = UserGenerator.getRootUserId();
        Document doc = DocumentGenerator.createSimpleDocument();

        AppServiceFacade.getOperationService().attach(operation, user, doc, TldServiceFacade.Fr);

        List<Document> documents = AppServiceFacade.getOperationService().getDocuments(operation.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertNotNull(documents);
        TestCase.assertEquals(1, documents.size());
        this.documentTestCase(doc, documents.get(0));

    }

    @Test
    public void createTwoDocumentAttachment() throws ServiceException {
        Operation operation = OperationGenerator.createOperation();
        UserId user = UserGenerator.getRootUserId();
        Document doc1 = DocumentGenerator.createSimpleDocument();
        AppServiceFacade.getOperationService().attach(operation, user, doc1, TldServiceFacade.Fr);
        Document doc2 = DocumentGenerator.createSimpleDocument();
        AppServiceFacade.getOperationService().attach(operation, user, doc2, TldServiceFacade.Fr);

        List<Document> documents = AppServiceFacade.getOperationService().getDocuments(operation.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        TestCase.assertNotNull(documents);
        TestCase.assertEquals(2, documents.size());

        this.documentTestCase(doc1, documents.get(0));
        this.documentTestCase(doc2, documents.get(1));
    }

    private void documentTestCase(Document expected, Document actual) {
        TestCase.assertEquals(expected.getComment(), actual.getComment());
        TestCase.assertEquals(expected.getLockedBy(), actual.getLockedBy());
        TestCase.assertEquals(expected.getHandle(), actual.getHandle());
        TestCase.assertEquals(expected.getReceptionDateStr(), actual.getReceptionDateStr());
        TestCase.assertEquals(expected.getTitle(), actual.getTitle());
        TestCase.assertEquals(expected.getSource(), actual.getSource());
    }

}
