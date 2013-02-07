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
 * Classe permettant de récupérer la description d'un objet
 * 
 * @author ginguene
 * 
 * @param <T>
 */
public interface IDescriber<D extends IDescribedExternallyObject> {

    public String getDescription(D object, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException;

}
