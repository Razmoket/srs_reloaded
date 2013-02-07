/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/checkers/DomainNameChecker.java#14 $
 * $Revision: #14 $
 * $Author: barriere $
 */

package fr.afnic.commons.checkers;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.exception.invalidformat.InvalidDomainNameException;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/***
 * Classe permettant de savoir si un nom de domaine est valide ou non. <br/>
 * 
 * 
 * TODO Cette classe doit disparaitre a terme au profit d'un service centralisé qui permettra de gérér plus facilement d'autres extensions.
 * 
 * @author ginguene
 * 
 */
public final class DomainNameChecker implements IExternalChecker {

    private static final long serialVersionUID = 1L;

    @Override
    public String check(String domainName, UserId userId, TldServiceFacade tld) throws InvalidFormatException {
        try {
            if (AppServiceFacade.getQualityService().isLegalDomainName(domainName, userId, tld)) {
                return domainName;
            } else {
                throw new InvalidDomainNameException(domainName);
            }
        } catch (ServiceException e) {
            throw new RuntimeException("check('" + domainName + "') failed", e);
        }
    }

}
