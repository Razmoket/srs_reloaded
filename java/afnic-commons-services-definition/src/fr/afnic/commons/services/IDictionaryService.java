/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/IDictionaryService.java#21 $ $Revision: #21 $Author: ginguene $
 */

package fr.afnic.commons.services;

import java.util.Locale;
import java.util.Set;

import fr.afnic.commons.beans.contactdetails.Country;
import fr.afnic.commons.beans.contactdetails.Region;
import fr.afnic.commons.beans.customer.benefit.ServiceType;
import fr.afnic.commons.beans.description.IDescribedExternallyObject;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Permet d'obtenir une description d'un certain nombre d'enumeration.
 * 
 * @author ginguene
 * 
 */
public interface IDictionaryService {

    /**
     * Retourne la description correspondant à un objet passée en parametre. <br/>
     * Correspond à un appel getDescription( describedObject, Locale.getDefault());
     * 
     * 
     * 
     * @param enumType
     * @return
     * @throws ServiceException
     */
    public String getDescription(IDescribedExternallyObject describedObject, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la description correspondant à un objet dans la langue passé via la locale.
     * 
     */
    public String getDescription(IDescribedExternallyObject describedObject, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne toutes les régions d'un pays
     * 
     * @param country
     * @return
     * @throws ServiceException
     */
    public Set<Region> getRegions(String countryCode, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne toutes les régions d'un pays avec la description dans la locale fournit
     * 
     * @param country
     * @return
     * @throws ServiceException
     */
    public Set<Region> getRegions(String countryCode, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne le Pays correspondant au code pays passé en parametre.
     * 
     * @param countryCode
     * @return
     * @throws ServiceException
     */
    public Country getCountry(String countryCode, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne le Pays correspondant au code pays passé en parametre.
     * 
     * @param countryCode
     * @return
     * @throws ServiceException
     */
    public Country getCountry(String countryCode, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne le Pays correspondant au code pays passé en parametre.
     * 
     * @param countryCode
     * @return
     * @throws ServiceException
     */
    public Region getRegion(String regionCode, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la liste de tous les codes pays connus.
     * 
     * @return
     * @throws ServiceException
     */
    public Set<Country> getCountries(UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la liste de tous les codes pays connus avec la description dans la locale fournit.
     * 
     * @return
     * @throws ServiceException
     */
    public Set<Country> getCountries(Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la liste de toutes les prestations connues.
     * 
     * @return
     * @throws ServiceException
     */
    public Set<ServiceType> getServices(UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la region dont le code est celui passé en parametre en lui initialisant la locale
     * 
     * @param regionCode
     * @param locale
     * @return
     * @throws ServiceException
     */
    public Region getRegion(String regionCode, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException;

}
