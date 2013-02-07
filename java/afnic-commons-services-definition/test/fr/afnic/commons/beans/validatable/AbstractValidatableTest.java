/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.validatable;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.utils.CallingCount;
import fr.afnic.utils.CallingFlag;

public class AbstractValidatableTest {

    @Test
    public void testValidateCallsValidateData() throws Exception {
        final CallingFlag checkInvalidData = new CallingFlag();
        AbstractValidatable validatable = new AbstractValidatable() {

            private static final long serialVersionUID = 1L;

            @Override
            public InvalidDataDescription checkInvalidData() {
                checkInvalidData.call();
                return null;
            }
        };

        validatable.validate();
        TestCase.assertTrue("validateData() has not been called", checkInvalidData.hasBeenCalled());

    }

    @Test
    public void testValidateWithFallingValidateData() throws Exception {
        final String desc = "test";
        AbstractValidatable validatable = new AbstractValidatable() {

            private static final long serialVersionUID = 1L;

            @Override
            public InvalidDataDescription checkInvalidData() {
                return new InvalidDataDescription(desc);
            }
        };

        try {
            validatable.validate();
            TestCase.fail("validate() should return an InvalidDataException");
        } catch (InvalidDataException e) {
            TestCase.assertEquals("Bad throwed Exception", desc, e.getInvalidDataDescription().getDescription());
        }
    }

    @Test
    public void testValidateCalledTwiceWithNormalValidateData() throws Exception {
        final CallingCount checkInvalidData = new CallingCount();
        AbstractValidatable validatable = new AbstractValidatable() {

            private static final long serialVersionUID = 1L;

            @Override
            public InvalidDataDescription checkInvalidData() {
                checkInvalidData.call();
                return null;
            }
        };

        validatable.validate();
        TestCase.assertEquals("Invalid validateData() call count", 1, checkInvalidData.getCallCount());
        validatable.validate();
        TestCase.assertEquals("Invalid validateData() call count", 1, checkInvalidData.getCallCount());

    }

    @Test
    public void testValidateCalledTwiceWithInvalidData() throws Exception {
        final CallingCount checkInvalidData = new CallingCount();

        AbstractValidatable validatable = new AbstractValidatable() {
            @Override
            public InvalidDataDescription checkInvalidData() {
                checkInvalidData.call();
                return new InvalidDataDescription("test");
            }
        };

        for (int i = 0; i < 2; i++) {
            try {
                validatable.validate();
                TestCase.fail("validate() should return an InvalidDataException (" + i + ")");
            } catch (InvalidDataException e) {
                TestCase.assertEquals("Invalid validateData() call count (" + i + ")", 1, checkInvalidData.getCallCount());
            }
        }
    }

    @Test
    public void testValidateCalledTwiceWithResetData() throws Exception {
        final CallingCount checkInvalidData = new CallingCount();
        AbstractValidatable validatable = new AbstractValidatable() {

            @Override
            public InvalidDataDescription checkInvalidData() {
                checkInvalidData.call();
                return null;
            }

        };

        validatable.validate();
        TestCase.assertEquals("Invalid validateData() call count", 1, checkInvalidData.getCallCount());
        validatable.resetValidate();
        validatable.validate();
        TestCase.assertEquals("Invalid validateData() call count", 2, checkInvalidData.getCallCount());

    }

}
