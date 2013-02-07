/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.checkers;

import java.util.regex.Pattern;

import fr.afnic.commons.services.exception.invalidformat.InvalidEmailAddressException;

/**
 * Classe permettant de valider le format des addresses Email.
 * 
 * @author ginguene
 * 
 */
public class EmailAddressChecker implements IInternalChecker {

    private static final long serialVersionUID = 1L;

    /** Expression régulière basée sur la RFC 2822 */
    protected static final String PATTERN_EMAIL_ADDRESS_FORMAT_STR = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

    //niclog-dev+preprod+domainmanager+capmg.fr@nic.fr

    //ginguene+toto+@afnic.fr

    protected static final Pattern PATTERN_EMAIL_ADDRESS_FORMAT = Pattern.compile(PATTERN_EMAIL_ADDRESS_FORMAT_STR);

    @Override
    public String check(String emailAddress) throws InvalidEmailAddressException {
        if (emailAddress == null || !PATTERN_EMAIL_ADDRESS_FORMAT.matcher(emailAddress.toLowerCase()).find()) {
            throw new InvalidEmailAddressException(emailAddress);
        }
        return emailAddress;
    }
}
