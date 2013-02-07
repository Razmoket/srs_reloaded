/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql;

import java.util.Locale;
import java.util.Set;

import fr.afnic.commons.beans.TicketOperation;
import fr.afnic.commons.beans.TicketStatus;
import fr.afnic.commons.beans.contactdetails.Country;
import fr.afnic.commons.beans.contactdetails.Region;
import fr.afnic.commons.beans.customer.benefit.ServiceType;
import fr.afnic.commons.beans.customer.status.CustomerStatus;
import fr.afnic.commons.beans.description.DescriberMap;
import fr.afnic.commons.beans.description.IDescribedExternallyObject;
import fr.afnic.commons.beans.description.describer.CustomerStatusDescriber;
import fr.afnic.commons.beans.description.describer.UserRightDescriber;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.profiling.users.UserRight;
import fr.afnic.commons.services.IDictionaryService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.sql.describer.SqlCountryDescriber;
import fr.afnic.commons.services.sql.describer.SqlOperationDescriber;
import fr.afnic.commons.services.sql.describer.SqlRegionDescriber;
import fr.afnic.commons.services.sql.describer.SqlTicketStatusDescriber;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.LocaleUtils;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

/**
 * 
 * 
 * 
 * @author ginguene
 * 
 */
public class SqlDictionnaryService implements IDictionaryService {

    protected static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(SqlDictionnaryService.class);

    private final DescriberMap map = new DescriberMap();

    private final SqlCountryDescriber countryDescriber;
    private final SqlRegionDescriber regionDescriber;

    public SqlDictionnaryService(final ISqlConnectionFactory sqlConnectionFactory) throws ServiceException {
        //TODO multi-registre
        //this.map.put(OldCustomerContractType.class, new CustomerContractTypeDescriber());
        //this.map.put(CustomerContactRole.class, CustomerContactTypeDescriber.getInstance());
        this.map.put(CustomerStatus.class, new CustomerStatusDescriber());
        //this.map.put(BenefitType.class, BenefitTypeDescriber.getInstance());
        this.map.put(TicketStatus.class, new SqlTicketStatusDescriber(sqlConnectionFactory));
        this.map.put(TicketOperation.class, new SqlOperationDescriber(sqlConnectionFactory));
        this.map.put(UserRight.class, new UserRightDescriber());

        this.countryDescriber = new SqlCountryDescriber(sqlConnectionFactory);
        this.map.put(Country.class, this.countryDescriber);
        this.regionDescriber = new SqlRegionDescriber(sqlConnectionFactory);
        this.map.put(Region.class, this.regionDescriber);
    }

    public SqlDictionnaryService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this(PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld));
    }

    @Override
    public Set<Country> getCountries(Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        Set<Country> countries = this.getCountries(userId, tld);
        for (Country country : countries) {
            country.setLocale(locale);
        }
        return countries;
    }

    @Override
    public Set<Country> getCountries(UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.countryDescriber.getObjects(userId, tld);
    }

    @Override
    public Country getCountry(String countryCode, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (countryCode == null) {
            return null;
        }
        return this.countryDescriber.getObject(countryCode, userId, tld);
    }

    @Override
    public Country getCountry(String countryCode, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        Country country = this.countryDescriber.getObject(countryCode, userId, tld);
        if (country != null) {
            country.setLocale(locale);
        }
        return country;
    }

    @Override
    public String getDescription(final IDescribedExternallyObject describedObject, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDescription(describedObject, LocaleUtils.getDefaultLanguageLocale(), userId, tld);
    }

    @Override
    public String getDescription(IDescribedExternallyObject describedObject, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.map.getDescription(describedObject, locale, userId, tld);
    }

    @Override
    public Region getRegion(String regionCode, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.regionDescriber.getRegion(regionCode, userId, tld);
    }

    @Override
    public Region getRegion(String regionCode, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        Region region = this.regionDescriber.getRegion(regionCode, userId, tld);
        if (region != null) {
            region.setLocale(locale);
        }
        return region;
    }

    @Override
    public Set<Region> getRegions(String countryCode, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        Set<Region> regions = this.getRegions(countryCode, userId, tld);
        for (Region region : regions) {
            region.setLocale(locale);
        }
        return regions;
    }

    @Override
    public Set<Region> getRegions(String countryCode, UserId userId, TldServiceFacade tld) throws ServiceException {
        Country country = this.getCountry(countryCode, userId, tld);
        return this.regionDescriber.getRegions(country, userId, tld);
    }

    @Override
    public Set<ServiceType> getServices(UserId userId, TldServiceFacade tld) throws ServiceException {

        //TODO multiregistre
        /*Set<Benefit> benefits = new HashSet<Benefit>();
        for (BenefitType types : BenefitTypeDescriber.getInstance().getObjects(locale)) {
            Benefit benefit = new Benefit(types);
            benefit.setDescription(types.getDescription());
            benefits.add(benefit);
        }

        return benefits;*/
        return null;
    }
}
