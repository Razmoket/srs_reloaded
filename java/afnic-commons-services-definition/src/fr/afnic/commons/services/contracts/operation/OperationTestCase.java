package fr.afnic.commons.services.contracts.operation;

import junit.framework.TestCase;
import fr.afnic.commons.beans.operations.CompositeOperation;
import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.utils.DateUtils;

public final class OperationTestCase {

    public static void assertOperation(Operation expected, Operation actual) {
        TestCase.assertEquals(expected.getCreateUserId(), actual.getCreateUserId());
        TestCase.assertEquals(expected.getComments(), actual.getComments());
        TestCase.assertEquals(expected.getDetails(), actual.getDetails());
        TestCase.assertEquals(expected.isBlocking(), actual.isBlocking());
        TestCase.assertEquals(OperationStatus.Pending, actual.getStatus());
        TestCase.assertEquals(expected.getType(), actual.getType());
        TestCase.assertTrue(DateUtils.isToday(actual.getCreateDate()));
        TestCase.assertEquals(actual.getCreateDate(), actual.getUpdateDate());
        TestCase.assertEquals(actual.getCreateUserId(), actual.getUpdateUserId());
    }

    public static void assertSimpleOperation(Operation expected, Operation actual) {
        assertOperation(expected, actual);
        TestCase.assertFalse(actual.hasParent());
    }

    public static void assertCompositeOperation(CompositeOperation expected, CompositeOperation actual) throws ServiceException {
        assertOperation(expected, actual);
        TestCase.assertEquals(expected.getSubOperations().size(), actual.getSubOperations().size());
    }

    private OperationTestCase() {

    }
}
