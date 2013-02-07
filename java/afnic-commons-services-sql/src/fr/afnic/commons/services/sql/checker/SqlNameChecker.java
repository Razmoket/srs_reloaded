/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.checker;

import java.util.regex.Pattern;

import fr.afnic.commons.checkers.IInternalChecker;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.exception.invalidformat.InvalidSecondLevelDomainNameException;

/**
 * Valide des identifiant sql tel que des noms de table ou de colonne dans uen base de donné
 * 
 * @author ginguene
 * 
 */
public class SqlNameChecker implements IInternalChecker {

    private static final long serialVersionUID = 1L;

    protected static final String PATTERN_NAME_FORMAT_STR = "^([a-zA-Z])([a-zA-Z_0-9$#]){0,29}$";
    protected static final Pattern PATTERN_NAME_FORMAT = Pattern.compile(SqlNameChecker.PATTERN_NAME_FORMAT_STR);

    @Override
    public String check(final String name) throws InvalidFormatException {
        if (name == null || !SqlNameChecker.PATTERN_NAME_FORMAT.matcher(name).find()) {
            throw new InvalidSecondLevelDomainNameException(name);
        }
        return name;
    }

}
