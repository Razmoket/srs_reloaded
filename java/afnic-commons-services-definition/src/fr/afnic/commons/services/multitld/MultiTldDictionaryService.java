package fr.afnic.commons.services.multitld;

import java.util.Locale;
import java.util.Set;

import fr.afnic.commons.beans.contactdetails.Country;
import fr.afnic.commons.beans.contactdetails.Region;
import fr.afnic.commons.beans.customer.benefit.ServiceType;
import fr.afnic.commons.beans.description.IDescribedExternallyObject;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IDictionaryService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldDictionaryService implements IDictionaryService {

    protected MultiTldDictionaryService() {
        super();
    }

    @Override
    public String getDescription(IDescribedExternallyObject describedObject, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDictionaryService().getDescription(describedObject, userId, tld);
    }

    @Override
    public String getDescription(IDescribedExternallyObject describedObject, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDictionaryService().getDescription(describedObject, locale, userId, tld);
    }

    @Override
    public Set<Region> getRegions(String countryCode, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDictionaryService().getRegions(countryCode, userId, tld);
    }

    @Override
    public Set<Region> getRegions(String countryCode, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDictionaryService().getRegions(countryCode, locale, userId, tld);
    }

    @Override
    public Country getCountry(String countryCode, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDictionaryService().getCountry(countryCode, userId, tld);
    }

    @Override
    public Country getCountry(String countryCode, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDictionaryService().getCountry(countryCode, locale, userId, tld);
    }

    @Override
    public Region getRegion(String regionCode, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDictionaryService().getRegion(regionCode, userId, tld);
    }

    @Override
    public Set<Country> getCountries(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDictionaryService().getCountries(userId, tld);
    }

    @Override
    public Set<Country> getCountries(Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDictionaryService().getCountries(locale, userId, tld);
    }

    @Override
    public Set<ServiceType> getServices(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDictionaryService().getServices(userId, tld);
    }

    @Override
    public Region getRegion(String regionCode, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDictionaryService().getRegion(regionCode, locale, userId, tld);
    }
}
