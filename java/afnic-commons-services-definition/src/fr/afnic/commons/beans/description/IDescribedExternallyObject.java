/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.description;

import java.util.Locale;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Objet contenant une description.<br/>
 * utilisé pour tagguer les enumération du packages beans.
 * 
 * 
 * TODO unification avec le modèle de ObjectValue en renommant la méthode getCode() en getValue(), <br/>
 * mais pour le moment trop d'impact notamment sur les RequestStatus
 * 
 * @author ginguene
 * 
 */
public interface IDescribedExternallyObject extends IDescribedObject {

    /**
     * Retourne la description de l'objet
     * 
     * @return
     * @throws ServiceException
     */
    public String getDescription(UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la description de l'objet dans la langue définie par la locale passée en parametre
     * 
     * @return
     * @throws ServiceException
     */
    public String getDescription(Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException;

}
