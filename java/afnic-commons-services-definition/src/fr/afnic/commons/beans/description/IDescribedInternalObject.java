/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.description;

import java.util.Locale;

import fr.afnic.commons.services.exception.ServiceException;

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
public interface IDescribedInternalObject extends IDescribedObject {

    /**
     * Retourne la description de l'objet
     * 
     * @return
     * @throws ServiceException
     */
    public String getDescription() throws ServiceException;

    /**
     * Retourne la description de l'objet dans la langue définie par la locale passée en parametre
     * 
     * @return
     * @throws ServiceException
     */
    public String getDescription(Locale locale) throws ServiceException;

}
