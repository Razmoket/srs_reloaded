package fr.afnic.commons.services.mock;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import fr.afnic.commons.beans.contactdetails.Country;
import fr.afnic.commons.beans.contactdetails.Region;
import fr.afnic.commons.beans.customer.benefit.ServiceType;
import fr.afnic.commons.beans.customer.status.CustomerStatus;
import fr.afnic.commons.beans.description.DescriberMap;
import fr.afnic.commons.beans.description.IDescribedExternallyObject;
import fr.afnic.commons.beans.description.IDescriber;
import fr.afnic.commons.beans.description.describer.CustomerStatusDescriber;
import fr.afnic.commons.beans.description.describer.UserRightDescriber;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.profiling.users.UserRight;
import fr.afnic.commons.services.IDictionaryService;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Data Access Object permettant de fournir un service de dictionnaire pour les tests.
 * 
 * 
 * 
 * @author ginguene
 * 
 */
public class MockDictionaryService implements IDictionaryService {

    private static final DescriberMap MAP = MockDictionaryService.createMap();

    public static DescriberMap createMap() {
        DescriberMap map = new DescriberMap();

        map.put(CustomerStatus.class, new CustomerStatusDescriber());
        map.put(UserRight.class, new UserRightDescriber());

        /* Utilisation d'une implémentation de test */
        map.put(Country.class, new IDescriber<Country>() {

            @Override
            public String getDescription(Country country, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
                if (locale == Locale.FRANCE || locale == Locale.FRENCH) {
                    if (country.getDictionaryKey().equals("FR")) return "France";
                    if (country.getDictionaryKey().equals("GB")) return "Royaume Uni";
                    if (country.getDictionaryKey().equals("DE")) return "Allemagne";
                    if (country.getDictionaryKey().equals("US")) return "Etats Unis";
                } else {
                    if (country.getDictionaryKey().equals("FR")) return "France";
                    if (country.getDictionaryKey().equals("GB")) return "United kingdom";
                    if (country.getDictionaryKey().equals("DE")) return "Deutch";
                    if (country.getDictionaryKey().equals("US")) return "United States of America";
                }

                return country.getValue() + locale.getISO3Language() + " description";
            }

        });
        return map;
    }

    @Override
    public Set<Country> getCountries(UserId userId, TldServiceFacade tld) throws ServiceException {
        HashSet<Country> countryCodes = new HashSet<Country>();

        countryCodes.add(new Country(1, "FR", userId, tld));
        countryCodes.add(new Country(2, "GB", userId, tld));
        countryCodes.add(new Country(3, "DE", userId, tld));
        countryCodes.add(new Country(4, "US", userId, tld));
        countryCodes.add(new Country(5, "SE", userId, tld));
        return countryCodes;
    }

    @Override
    public Set<Region> getRegions(String countryCode, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public Country getCountry(String countryCode, UserId userId, TldServiceFacade tld) throws ServiceException {
        for (Country country : this.getCountries(userId, tld)) {
            if (country.getDictionaryKey().equals(countryCode)) {
                return country;
            }
        }

        return new Country(1, countryCode, userId, tld);
        //throw new NotFoundException("No country found with code " + countryCode);
    }

    @Override
    public String getDescription(IDescribedExternallyObject describedObject, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDescription(describedObject, Locale.getDefault(), userId, tld);
    }

    @Override
    public String getDescription(IDescribedExternallyObject describedObject, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        return MockDictionaryService.MAP.getDescription(describedObject, locale, userId, tld);
    }

    @Override
    public Region getRegion(String regionCode, UserId userId, TldServiceFacade tld) throws ServiceException {
        return new Region(Integer.parseInt(regionCode), new Country(1, "FR", userId, tld));
    }

    @Override
    public Set<Region> getRegions(String countryCode, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getRegions(countryCode, userId, tld);
    }

    @Override
    public Set<Country> getCountries(Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getCountries(userId, tld);
    }

    @Override
    public Set<ServiceType> getServices(UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public Country getCountry(String countryCode, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public Region getRegion(String regionCode, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

}
