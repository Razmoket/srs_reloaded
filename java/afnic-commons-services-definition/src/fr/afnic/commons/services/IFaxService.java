/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/IFaxService.java#3 $
 * $Revision: #3 $
 * $Author: barriere $
 */

package fr.afnic.commons.services;

import java.util.List;

import fr.afnic.commons.beans.documents.Fax;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Service permettant de gérer les Fax
 * 
 * @author ginguene
 * 
 */
public interface IFaxService {

    /**
     * Retourne la liste des fax recus depuis le dernier appel à cette fonction.
     * 
     * @return
     * @throws ServiceException
     */
    public List<Fax> getReceivedFaxes(UserId userId, TldServiceFacade tld) throws ServiceException;

}
