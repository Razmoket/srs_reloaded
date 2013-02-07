/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.checkers;

import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.exception.invalidformat.InvalidNichandleException;

public class CountryCodeChecker extends PatternChecker {

    private static final long serialVersionUID = 1L;

    private static final String PATTERN_STR = "^[A-Z][A-Z]$";

    public CountryCodeChecker() {
        super(PATTERN_STR);
    }

    @Override
    public InvalidFormatException createInvalidFormatException(String data) {
        return new InvalidNichandleException(data);
    }
}
