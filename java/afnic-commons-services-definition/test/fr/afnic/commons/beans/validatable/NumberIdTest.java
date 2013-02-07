/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.validatable;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class NumberIdTest {

    @Test
    public void testWithIntConstructor() {
        TestNumberId numberId = new TestNumberId(12);
        numberId.validate();
        TestCase.assertEquals(12, numberId.getIntValue());
        TestCase.assertEquals("12", numberId.getValue());
    }

    @Test
    public void testWithStringConstructorAndValidValue() {
        TestNumberId numberId = new TestNumberId("12");
        numberId.validate();
        TestCase.assertEquals(12, numberId.getIntValue());
        TestCase.assertEquals("12", numberId.getValue());
    }

    @Test
    public void testWithStringConstructorAndNonIntegerValue() {
        TestNumberId numberId = new TestNumberId("12a");
        try {
            numberId.validate();
        } catch (InvalidDataException e) {
            TestCase.assertEquals(e.getInvalidDataDescription().getDescription(), "Id value [12a] must be a number");
        }
    }

    @Test
    public void testWithStringConstructorAndZeroValue() {
        TestNumberId numberId = new TestNumberId("0");
        try {
            numberId.validate();
        } catch (InvalidDataException e) {
            TestCase.assertEquals(e.getInvalidDataDescription().getDescription(), "Id value [0] must be greater than 0");
        }
    }

    @Test
    public void testWithStringConstructorAndNegativeValue() {
        TestNumberId numberId = new TestNumberId("-1");
        try {
            numberId.validate();
        } catch (InvalidDataException e) {
            TestCase.assertEquals(e.getInvalidDataDescription().getDescription(), "Id value [-1] must be greater than 0");
        }
    }

    @Test
    public void testEquals() {
        TestCase.assertEquals(new TestNumberId("2"), new TestNumberId("2"));
        TestCase.assertFalse(new TestNumberId("2").equals(new Date()));
        TestCase.assertFalse(new TestNumberId("2").equals(null));
        TestCase.assertFalse(new TestNumberId("1").equals(new TestNumberId("2")));
    }

    public class TestNumberId extends NumberId<String> {

        private static final long serialVersionUID = 1L;

        public TestNumberId(String id) {
            super(id);
        }

        public TestNumberId(int id) {
            super(id);
        }

        @Override
        public String getObjectOwner(UserId userId, TldServiceFacade tld) throws ServiceException {
            return this.toString();
        }

    }
}
