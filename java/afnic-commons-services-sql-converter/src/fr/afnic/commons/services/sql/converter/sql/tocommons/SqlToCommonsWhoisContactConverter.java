package fr.afnic.commons.services.sql.converter.sql.tocommons;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.afnic.commons.beans.IndividualWhoisContact;
import fr.afnic.commons.beans.NicHandle;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.sql.converter.mapping.SqlColumnWhoisContactMapping;
import fr.afnic.commons.services.tld.TldServiceFacade;

@SuppressWarnings("rawtypes")
public class SqlToCommonsWhoisContactConverter extends AbstractConverter<ResultSet, WhoisContact> {

    public SqlToCommonsWhoisContactConverter() {
        super(ResultSet.class, WhoisContact.class);
    }

    @Override
    public WhoisContact convert(ResultSet toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        try {
            String vNom = toConvert.getString(SqlColumnWhoisContactMapping.nom.toString());
            String vPrenom = toConvert.getString(SqlColumnWhoisContactMapping.prenom.toString());
            String vData = toConvert.getString(SqlColumnWhoisContactMapping.data.toString());
            String vCommune = toConvert.getString(SqlColumnWhoisContactMapping.commune.toString());
            String vCedex = toConvert.getString(SqlColumnWhoisContactMapping.cedex.toString());
            String vZip = toConvert.getString(SqlColumnWhoisContactMapping.zip.toString());
            String vMediaData = toConvert.getString(SqlColumnWhoisContactMapping.mediaData.toString());
            String vPrefix = toConvert.getString(SqlColumnWhoisContactMapping.prefix.toString());
            int vNum = toConvert.getInt(SqlColumnWhoisContactMapping.num.toString());
            String vSuffix = toConvert.getString(SqlColumnWhoisContactMapping.suffix.toString());
            String registrarCode = toConvert.getString(SqlColumnWhoisContactMapping.registrarCode.toString());

            IndividualWhoisContact retour;
            retour = new IndividualWhoisContact(userId, tld);
            retour.setFirstName(vPrenom);
            retour.setLastName(vNom);
            retour.setRegistrarCode(registrarCode);
            List<String> vListMail = new ArrayList<String>();
            vListMail.add(vMediaData);
            retour.setEmailAddressesFromStrList(vListMail);

            retour.setHandle(new NicHandle(vPrefix, Integer.toString(vNum), vSuffix).toString());
            PostalAddress adress = new PostalAddress(userId, tld);
            adress.setCity(vCommune);
            adress.setCityCedex(vCedex);
            adress.setStreetStr(vData);
            adress.setPostCode(vZip);
            retour.setPostalAddress(adress);

            return retour;
        } catch (SQLException e) {
            throw new ServiceException("Convert() failed", e);
        }
    }
}
