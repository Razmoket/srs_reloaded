package fr.afnic.commons.checkers;

import java.util.regex.Pattern;

import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.exception.invalidformat.InvalidSecondLevelDomainNameException;

/***
 * Classe permettant de valider un nom de domaine de second niveau (ex: dupont.nom.fr).<br/>
 * 
 * 
 * 
 * @author ginguene
 * 
 */
public class SecondLevelDomainNameChecker implements IInternalChecker {

    private static final long serialVersionUID = 1L;

    protected static final String PATTERN_SECOND_LEVEL_DOMAIN_NAME_FORMAT_STR = "^([a-z]|[0-9])([a-z]|[0-9]|-|_ ){1,}\\.([a-z]+)\\.([a-z]{2,})$";
    protected static final Pattern PATTERN_SECOND_LEVEL_DOMAIN_NAME_FORMAT = Pattern.compile(SecondLevelDomainNameChecker.PATTERN_SECOND_LEVEL_DOMAIN_NAME_FORMAT_STR);

    @Override
    public String check(String domainName) throws InvalidFormatException {

        if (domainName == null) {
            throw new InvalidSecondLevelDomainNameException(null);
        }

        String lowerDomainName = domainName.toLowerCase();
        if (!SecondLevelDomainNameChecker.PATTERN_SECOND_LEVEL_DOMAIN_NAME_FORMAT.matcher(lowerDomainName).find()) {
            throw new InvalidSecondLevelDomainNameException(lowerDomainName);
        }
        return lowerDomainName;
    }

}
