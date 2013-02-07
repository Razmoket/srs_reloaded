/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.checkers;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Classe permettant de tester la classe DocumentHandleChecker
 * 
 * @author ginguene
 * 
 */
public class DocumentHandleCheckerTest {

    @Test
    public void testIsValidMailHandleWithValidHandles() {
        String[] validHandles = { "MailMessage-123",
                                  "MailMessage-1",
                                  "MailMessage-123456789" };

        for (String validHandle : validHandles) {
            TestCase.assertTrue("'" + validHandle + "' should be considered as a valid mail handle",
                                DocumentHandleChecker.isValidMailHandle(validHandle));
        }
    }

    @Test
    public void testIsValidMailHandleWithInvalidHandles() {
        String[] invalidHandles = { null,
                                    "MailMessage-",
                                   "MailMessage-abc",
                                   "MailMessage-123a",
                                   "aMailMessage-123a",
                                   "DocumentMessage-123",
                                   "MailDocument-123" };

        for (String invalidHandle : invalidHandles) {
            TestCase.assertFalse("'" + invalidHandle + "' should be considered as an invalid mail handle",
                                 DocumentHandleChecker.isValidMailHandle(invalidHandle));
        }
    }

    @Test
    public void testIsValidSimpleDocumentHandleWithValidHandles() {
        String[] validHandles = { "Document-123",
                                  "Document-1",
                                  "Document-123456789" };

        for (String validHandle : validHandles) {
            TestCase.assertTrue("'" + validHandle + "' should be considered as a valid simple document handle",
                                DocumentHandleChecker.isValidSimpleDocumentHandle(validHandle));
        }
    }

    @Test
    public void testIsValidSimpleDocumentHandleWithInvalidHandles() {
        String[] invalidHandles = { null,
                                    "Document-",
                                   "Document-abc",
                                   "Document-123a",
                                   "aDocument-123a",
                                   "DocumentMessage-123",
                                   "MailMessage-123" };

        for (String invalidHandle : invalidHandles) {
            TestCase.assertFalse("'" + invalidHandle + "' should be considered as an invalid simple document handle",
                                 DocumentHandleChecker.isValidSimpleDocumentHandle(invalidHandle));
        }
    }

    @Test
    public void testIsValidGddDocumentHandleWithValidHandles() {
        String[] validHandles = { "AF_Document_GDD-123",
                                  "AF_Document_GDD-1",
                                  "AF_Document_GDD-123456789" };

        for (String validHandle : validHandles) {
            TestCase.assertTrue("'" + validHandle + "' should be considered as a valid GddDocument handle",
                                DocumentHandleChecker.isValidGddDocumentHandle(validHandle));
        }
    }

    @Test
    public void testIsValidGddDocumentHandleWithInvalidHandles() {
        String[] invalidHandles = { null,
                                    "AF_Document_GDD-",
                                   "AF_Document_GDD-abc",
                                   "AF_Document_GDD-123a",
                                   "aAF_Document_GDD-123a",
                                   "DocumentMessage-123",
                                   "GddDocumentDocument-123",
                                   "MailMessage-123"
                                   };

        for (String invalidHandle : invalidHandles) {
            TestCase.assertFalse("'" + invalidHandle + "' should be considered as an invalid GddDocument handle",
                                 DocumentHandleChecker.isValidGddDocumentHandle(invalidHandle));
        }
    }

    @Test
    public void testIsValidDocumentHandleWithValidHandles() {
        String[] validHandles = { "AF_Document_GDD-123",
                                  "Document-123",
                                  "MailMessage-123" };

        for (String validHandle : validHandles) {
            TestCase.assertTrue("'" + validHandle + "' should be considered as a valid GddDocument handle",
                                DocumentHandleChecker.isValidDocumentHandle(validHandle));
        }
    }

}
