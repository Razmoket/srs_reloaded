/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/contactdetails/EmailAddress.java#10 $
 * $Revision: #10 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.contactdetails;

import fr.afnic.commons.checkers.EmailAddressChecker;
import fr.afnic.commons.checkers.IInternalChecker;
import fr.afnic.commons.services.exception.invalidformat.InvalidEmailAddressException;

/**
 * Représente une addresse mail
 * 
 * @author ginguene
 * 
 */
public class EmailAddress extends ContactDetail {

    private static final long serialVersionUID = 1L;

    public EmailAddress() {
        super();
    }

    public EmailAddress(String value) throws InvalidEmailAddressException {
        super(value);
    }

    @Override
    protected IInternalChecker createChecker() {
        return new EmailAddressChecker();
    }

}
