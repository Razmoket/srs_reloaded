/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.services.sql.describer;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;

import fr.afnic.commons.beans.contactdetails.AllRegionsOfACountry;
import fr.afnic.commons.beans.contactdetails.Country;
import fr.afnic.commons.beans.contactdetails.Region;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.sql.ISqlConnectionFactory;

/**
 * Cherche la description des country code dans la base de données.
 * 
 * 
 * @author ginguene
 * 
 */
public class SqlRegionDescriber extends SqlDescriber<Region> {

    private static final String SQL = new StringBuilder().append(" Select nicope.region.id, nicope.region.Nom_FR, nicope.region.Nom_EN, nicope.pays.id, nicope.pays.CodeIso2 ")
                                                         .append(" from nicope.Region, nicope.pays ")
                                                         .append(" where nicope.Region.type_region != 'pays'")
                                                         .append(" and nicope.pays.id = nicope.region.id_pays")
                                                         .append(" order by nicope.region.Nom_FR")
                                                         .toString();

    //private static final RegionOrdering REGION_ORDERING = new RegionOrdering();

    public SqlRegionDescriber(final ISqlConnectionFactory sqlConnectionFactory) throws ServiceException {
        super(sqlConnectionFactory);
    }

    @Override
    protected String getSql() {
        return SqlRegionDescriber.SQL;
    }

    @Override
    protected int getIdColumn() {
        return 1;
    }

    @Override
    protected int getCodeColumn() {
        return 1;
    }

    @Override
    protected int getFrDescColumn() {
        return 2;
    }

    @Override
    protected int getEnDescColumn() {
        return 3;
    }

    @Override
    protected Region createNewObject(int id, String code, ResultSet resultSet, UserId userId, TldServiceFacade tld) throws Exception {
        int countryId = resultSet.getInt(4);
        String countryCode = resultSet.getString(5);
        Country country = new Country(countryId, countryCode, userId, tld);

        if (id != countryId) {
            return new Region(id, country);
        } else {
            return new AllRegionsOfACountry(new Country(id, code, userId, tld));
        }
    }

    public Set<Region> getRegions(Country country, UserId userId, TldServiceFacade tld) throws ServiceException {

        HashSet<Region> regions = new HashSet<Region>();
        for (Region region : this.getOrCreateDescriptionMap(userId, tld).getObjects()) {
            if (ObjectUtils.equals(country, region.getCountry())) {
                regions.add(region);
            }
        }
        return regions;
    }

    @Override
    protected DescriptionMap<Region> createNewDescriptionMap() {
        return new DescriptionMap<Region>() {
            @Override
            public String getDefaultDescription(Region object, java.util.Locale locale) {

                String defaultDesc = super.getDefaultDescription(object, locale);
                if (SqlRegionDescriber.this.isAllRegionDesc(defaultDesc)) {
                    return SqlRegionDescriber.this.getDefaultAllRegionDesc(locale);
                } else {
                    return defaultDesc;
                }
            }
        };
    }

    public Region getRegion(String regionCode, UserId userId, TldServiceFacade tld) throws ServiceException {
        for (Region region : this.getOrCreateDescriptionMap(userId, tld).getObjects()) {
            if (region.getValue().equals(regionCode)) {
                return region;
            }
        }

        // A améliorer, ici on part du principe que seul la france possède des sous-divisions
        return new AllRegionsOfACountry(Country.FR);
    }

    private String getDefaultAllRegionDesc(Locale locale) {
        if (Locale.FRENCH.equals(locale)) {
            return "Toutes";
        } else {
            return "All";
        }
    }

    protected boolean isAllRegionDesc(String desc) {
        return desc.contains(AllRegionsOfACountry.class.getSimpleName());
    }

}
