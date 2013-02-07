/*
 * $Id: EmailAddressGenerator.java,v 1.1 2010/07/08 09:39:28 alaphil Exp $
 * $Revision: 1.1 $
 * $Author: alaphil $
 */

package fr.afnic.commons.test.generator;

import fr.afnic.commons.beans.IndividualWhoisContact;
import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.services.exception.invalidformat.InvalidEmailAddressException;
import fr.afnic.utils.StringUtils;

public final class EmailAddressGenerator {

    private static final String EMAIL_OPERATOR = "@test-afnic.fr";

    public static EmailAddress getEmailAddress() throws InvalidEmailAddressException {
        return new EmailAddress("test-addresse" + EmailAddressGenerator.EMAIL_OPERATOR);
    }

    public static EmailAddress getEmailAddress(IndividualWhoisContact contact) throws InvalidEmailAddressException {
        return new EmailAddress(contact.getFirstName().toLowerCase() + "." + contact.getLastName().toLowerCase() + EmailAddressGenerator.EMAIL_OPERATOR);
    }

    public static EmailAddress getRandomEmailAddress(IndividualWhoisContact contact) throws InvalidEmailAddressException {
        return new EmailAddress(StringUtils.generateWord(5).toLowerCase() + "." + StringUtils.generateWord(5).toLowerCase() + EmailAddressGenerator.EMAIL_OPERATOR);
    }

    private EmailAddressGenerator() {

    }
}
