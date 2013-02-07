/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.checkers;

import java.util.regex.Pattern;

import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;

public abstract class PatternChecker implements IInternalChecker {

    private static final long serialVersionUID = 1L;

    private Pattern pattern = null;

    public PatternChecker(String patternString) {
        pattern = Pattern.compile(patternString);
    }

    @Override
    public String check(String data) throws InvalidFormatException {
        if (data == null || !pattern.matcher(data).find()) {
            throw createInvalidFormatException(data);
        } else {
            return data;
        }
    }

    public abstract InvalidFormatException createInvalidFormatException(String data);

}
