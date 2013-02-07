/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.describer;

import java.sql.ResultSet;
import java.util.Locale;
import java.util.Set;

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
 * ATTENTION: Si un pays se trouve dans nicope.pays et pas dans nicope.region, il ne sera pas retourné.
 * 
 * 
 * @author ginguene
 * 
 */
public class SqlCountryDescriber extends SqlDescriber<Country> {

    private static final String SQL = new StringBuilder()
                                                         .append(" select nicope.region.id, nicope.pays.CodeIso2,nvl(nicope.pays.Nom_FR, pays.nom) Nom_FR, nvl(nicope.pays.Nom_EN, pays.nom) Nom_EN ")
                                                         .append(" from nicope.pays,nicope.region")
                                                         .append(" where nicope.pays.id = nicope.region.id_pays")
                                                         .append(" and nicope.region.id = nicope.region.id_pays").toString();

    private SqlRegionDescriber regionDescriber;

    public SqlCountryDescriber(final ISqlConnectionFactory sqlConnectionFactory) throws ServiceException {
        super(sqlConnectionFactory);

        Country country = new Country(-5, "RE", new UserId(22), TldServiceFacade.Fr);
        this.addDescription(country, Locale.FRENCH, "Réunion", new UserId(22), TldServiceFacade.Fr);
        this.addDescription(country, Locale.ENGLISH, "Réunion", new UserId(22), TldServiceFacade.Fr);

    }

    @Override
    protected String getSql() {
        return SqlCountryDescriber.SQL;
    }

    @Override
    protected int getIdColumn() {
        return 1;
    }

    @Override
    protected int getCodeColumn() {
        return 2;
    }

    @Override
    protected int getFrDescColumn() {
        return 3;
    }

    @Override
    protected int getEnDescColumn() {
        return 4;
    }

    @Override
    protected Country createNewObject(int id, String code, ResultSet resultSet, UserId userId, TldServiceFacade tld) throws ServiceException {
        Country country = new Country(id, code, userId, tld);
        country.setRegions(this.getRegions(country, userId, tld));

        return new Country(id, code, userId, tld);
    }

    public Set<Region> getRegions(Country country, UserId userId, TldServiceFacade tld) throws ServiceException {
        Set<Region> regions = this.getOrCreateSqlRegionDescriber().getRegions(country, userId, tld);
        regions.add(new AllRegionsOfACountry(country));
        return regions;
    }

    private SqlRegionDescriber getOrCreateSqlRegionDescriber() throws ServiceException {
        if (this.regionDescriber == null) {
            this.regionDescriber = new SqlRegionDescriber(this.sqlConnectionFactory);
        }
        return this.regionDescriber;
    }

}
