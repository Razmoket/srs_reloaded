/*
 * $Id: //depot/main/java/afnic-commons-services-definition/test/fr/afnic/commons/beans/contactdetails/DetailTest.java#3 $
 * $Revision: #3 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.contactdetails;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * teste de la classe Detail.
 * 
 * @author ginguene
 * 
 */
public class DetailTest {

    /**
     * Teste de la méthode equals qui doit etre utilisable par les classes filles.
     * 
     */
    @Test
    public void testEquals() {
        String value1 = "0102030405";
        String value2 = "0402030405";

        TestCase.assertTrue(new PhoneNumber(value1).equals(new PhoneNumber(value1)));
        TestCase.assertFalse(new PhoneNumber(value1).equals(new PhoneNumber(value2)));
        TestCase.assertFalse(new PhoneNumber(value1).equals(new EmailAddress(value1)));

    }
}
