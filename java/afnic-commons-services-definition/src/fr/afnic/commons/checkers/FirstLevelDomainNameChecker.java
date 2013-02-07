/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/checkers/DomainNameChecker.java#2 $
 * $Revision: #2 $
 * $Author: ginguene $
 */

package fr.afnic.commons.checkers;

import java.util.regex.Pattern;

import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;

/***
 * Classe permettant de valider un nom de domaine de premier niveau (ex: afnic.fr).<br/>
 * 
 * 
 * 
 * @author ginguene  
 * 
 */
public final class FirstLevelDomainNameChecker implements IInternalChecker {

    private static final long serialVersionUID = 1L;

    protected static final String PATTERN_DOMAIN_NAME_FORMAT_STR = "^([a-z]|[0-9])([a-z]|[0-9]|-|_){1,}\\.(([a-z])*\\.)*([a-z]{2,})$";
    protected static final Pattern PATTERN_DOMAIN_NAME_FORMAT = Pattern.compile(FirstLevelDomainNameChecker.PATTERN_DOMAIN_NAME_FORMAT_STR);

    @Override
    public String check(String domainName) throws InvalidFormatException {

        if (domainName == null) {
            throw new InvalidFormatException("null is not a valid first level domain name");
        }

        String lowerDomainName = domainName.toLowerCase();
        //TODO se relier sur le service qualité
        /*if (lowerDomainName == null
            || !FirstLevelDomainNameChecker.PATTERN_DOMAIN_NAME_FORMAT.matcher(lowerDomainName).find()) {
            throw new InvalidFormatException(lowerDomainName);
        }*/
        return lowerDomainName;
    }
}
