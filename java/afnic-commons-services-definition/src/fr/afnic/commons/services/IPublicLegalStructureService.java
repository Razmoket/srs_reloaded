/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/IPublicLegalStructureService.java#6 $
 * $Revision: #6 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services;

import fr.afnic.commons.beans.corporateentity.CorporateEntity;
import fr.afnic.commons.beans.corporateentity.CorporateEntityIdentifier;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Data Access Object permettant de consulter les base publiques au sujet des personnes morale.
 * 
 * 
 * 
 * TODO JGI:devrait etre renommé en IPublicCorporateEntityService
 * 
 */
public interface IPublicLegalStructureService {

    /**
     * 
     * Retourne une Structure légale à partir de l'identifiant (siren, siret, waldec) d'une Structure (Entreprise, association, ...)
     * 
     * @param id
     * @return
     * @throws ServiceException
     */
    public CorporateEntity getCorporateEntity(String id, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * 
     * Retourne une Structure légale à partir de l'identifiant (Siren, Siret, Waldec) d'une Structure (Entreprise, association, ...)
     * 
     * @param legalStructureIdentifier
     * @return
     * @throws ServiceException
     */
    public CorporateEntity getCorporateEntity(CorporateEntityIdentifier legalStructureIdentifier, UserId userId, TldServiceFacade tld) throws ServiceException;

}
