/*
 * $Id: PhoneNumberGenerator.java,v 1.2 2010/07/15 14:55:44 ginguene Exp $
 * $Revision: 1.2 $
 * $Author: ginguene $
 */

package fr.afnic.commons.test.generator;

import fr.afnic.commons.beans.contactdetails.PhoneNumber;

public final class PhoneNumberGenerator {

    public static PhoneNumber getPhoneNumber() {
        return new PhoneNumber("+33 1 02 03 04 05");
    }

    public static PhoneNumber getFaxNumber() {
        return new PhoneNumber("+33 1 02 03 04 66");
    }

    private PhoneNumberGenerator() {

    }
}
