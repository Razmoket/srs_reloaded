/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/IQualityService.java#3 $
 * $Revision: #3 $
 * $Author: barriere $
 */

package fr.afnic.commons.services;

import fr.afnic.commons.beans.domain.DomainNameDetail;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public interface IQualityService {

    public DomainNameDetail normalizeDomainName(String domain, UserId userId, TldServiceFacade tld) throws ServiceException;

    public boolean isLegalDomainName(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException;

}
