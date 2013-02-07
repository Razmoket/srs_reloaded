/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.checkers;

import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.exception.invalidformat.InvalidPhoneNumberException;

public class PhoneNumberChecker extends PatternChecker {

    private static final long serialVersionUID = 1L;

    private static final String PATTERN_STR = "^( *[0-9()+] *)+$";

    public PhoneNumberChecker() {
        super(PATTERN_STR);
    }

    @Override
    public InvalidFormatException createInvalidFormatException(String data) {
        return new InvalidPhoneNumberException(data);
    }
}
