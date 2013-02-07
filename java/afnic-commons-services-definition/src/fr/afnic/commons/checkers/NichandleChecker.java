/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.checkers;

import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.exception.invalidformat.InvalidNichandleException;

/***
 * Classe classe chargée de controlée le format des nichandle.
 * 
 * @author ginguene
 * 
 */
public class NichandleChecker extends PatternChecker {

    private static final long serialVersionUID = 1L;

    private static final String PATTERN_STR = "^([A-Z]+)([1-9][0-9]*)?-(FRNIC|AFNIC|FR[0-9]*)$";

    public NichandleChecker() {
        super(PATTERN_STR);
    }

    @Override
    public InvalidFormatException createInvalidFormatException(String data) {
        return new InvalidNichandleException(data);
    }
}
