package fr.afnic.commons.services.sql.converter.sql.tocommons;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import fr.afnic.commons.beans.contact.IndividualAndLegalStructure;
import fr.afnic.commons.beans.contact.identity.IndividualIdentity;
import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.contactdetails.PhoneNumber;
import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.legal.AskedAction;
import fr.afnic.commons.legal.SyreliLegalData;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.sql.converter.SqlConverterFacade;
import fr.afnic.commons.services.sql.converter.mapping.SqlColumnSyreliLegalDataMapping;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class SqlToSyreliLegalDataConverter extends AbstractConverter<ResultSet, SyreliLegalData> {

    public SqlToSyreliLegalDataConverter() {
        super(ResultSet.class, SyreliLegalData.class);
    }

    @Override
    public SyreliLegalData convert(ResultSet toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        try {

            String vReqCivility = toConvert.getString(SqlColumnSyreliLegalDataMapping.reqCivility.toString());
            String vReqSurname = toConvert.getString(SqlColumnSyreliLegalDataMapping.reqSurname.toString());
            String vReqFirstName = toConvert.getString(SqlColumnSyreliLegalDataMapping.reqFirstName.toString());
            String vReqOrganization = toConvert.getString(SqlColumnSyreliLegalDataMapping.reqOrganization.toString());
            String vReqAddress1 = toConvert.getString(SqlColumnSyreliLegalDataMapping.reqAdress1.toString());
            String vReqAddress2 = toConvert.getString(SqlColumnSyreliLegalDataMapping.reqAdress2.toString());
            String vReqAddress3 = toConvert.getString(SqlColumnSyreliLegalDataMapping.reqAdress3.toString());
            String vReqZipCode = toConvert.getString(SqlColumnSyreliLegalDataMapping.reqZipCode.toString());
            String vReqTown = toConvert.getString(SqlColumnSyreliLegalDataMapping.reqTown.toString());
            String vReqCountry = toConvert.getString(SqlColumnSyreliLegalDataMapping.reqCountry.toString());
            String vReqPhone = toConvert.getString(SqlColumnSyreliLegalDataMapping.reqPhone.toString());
            String vReqFax = toConvert.getString(SqlColumnSyreliLegalDataMapping.reqFax.toString());
            String vReqEMail = toConvert.getString(SqlColumnSyreliLegalDataMapping.reqMail.toString());
            String vReqVatNumber = toConvert.getString(SqlColumnSyreliLegalDataMapping.reqVatNumber.toString());

            String vRepCivility = toConvert.getString(SqlColumnSyreliLegalDataMapping.repCivility.toString());
            String vRepSurname = toConvert.getString(SqlColumnSyreliLegalDataMapping.repSurname.toString());
            String vRepFirstName = toConvert.getString(SqlColumnSyreliLegalDataMapping.repFirstName.toString());
            String vRepOrganization = toConvert.getString(SqlColumnSyreliLegalDataMapping.repOrganization.toString());
            String vRepAddress1 = toConvert.getString(SqlColumnSyreliLegalDataMapping.repAdress1.toString());
            String vRepAddress2 = toConvert.getString(SqlColumnSyreliLegalDataMapping.repAdress2.toString());
            String vRepAddress3 = toConvert.getString(SqlColumnSyreliLegalDataMapping.repAdress3.toString());
            String vRepZipCode = toConvert.getString(SqlColumnSyreliLegalDataMapping.repZipCode.toString());
            String vRepTown = toConvert.getString(SqlColumnSyreliLegalDataMapping.repTown.toString());
            String vRepCountry = toConvert.getString(SqlColumnSyreliLegalDataMapping.repCountry.toString());
            String vRepPhone = toConvert.getString(SqlColumnSyreliLegalDataMapping.repPhone.toString());
            String vRepFax = toConvert.getString(SqlColumnSyreliLegalDataMapping.repFax.toString());
            String vRepEMail = toConvert.getString(SqlColumnSyreliLegalDataMapping.repMail.toString());
            String vRepVatNumber = toConvert.getString(SqlColumnSyreliLegalDataMapping.repVatNumber.toString());

            String vAskedAction = toConvert.getString(SqlColumnSyreliLegalDataMapping.askedAction.toString());
            String vRefAjpr = toConvert.getString(SqlColumnSyreliLegalDataMapping.refAjpr.toString());
            String vDomainName = toConvert.getString(SqlColumnSyreliLegalDataMapping.domainName.toString());
            Date vCreatedAt = toConvert.getTimestamp(SqlColumnSyreliLegalDataMapping.createdAt.toString());
            Date vAnsweredAt = toConvert.getTimestamp(SqlColumnSyreliLegalDataMapping.answeredAt.toString());
            String vTituRights = toConvert.getString(SqlColumnSyreliLegalDataMapping.tituRights.toString());
            String vTituPosition = toConvert.getString(SqlColumnSyreliLegalDataMapping.tituPosition.toString());
            String vTituLegalProcedures = toConvert.getString(SqlColumnSyreliLegalDataMapping.tituLegalProcedures.toString());
            String vViolationReasonsPart1 = toConvert.getString(SqlColumnSyreliLegalDataMapping.violationReasonsPart1.toString());
            String vViolationReasonsPart2 = toConvert.getString(SqlColumnSyreliLegalDataMapping.violationReasonsPart2.toString());
            String vViolationReasonsPart3 = toConvert.getString(SqlColumnSyreliLegalDataMapping.violationReasonsPart3.toString());

            SyreliLegalData vSyreliLegalData = new SyreliLegalData();
            IndividualIdentity vId = new IndividualIdentity();
            vId.setFirstName(vReqFirstName);
            vId.setLastName(vReqSurname);

            PostalAddress vPostalAddr = new PostalAddress(userId, tld);
            vPostalAddr.setCity(vReqTown);
            vPostalAddr.setPostCode(vReqZipCode);
            vPostalAddr.setCountryCode(vReqCountry);
            vPostalAddr.setStreet(vReqAddress1, vReqAddress2, vReqAddress3);

            IndividualAndLegalStructure vIndividu = new IndividualAndLegalStructure(vId, userId, tld);
            vIndividu.setOrganization(vReqOrganization);
            vIndividu.setVatnumber(vReqVatNumber);
            vIndividu.setCivility(vReqCivility);
            vIndividu.getPhoneNumbers().add(new PhoneNumber(vReqPhone));
            vIndividu.getFaxNumbers().add(new PhoneNumber(vReqFax));
            vIndividu.addEmailAddress(new EmailAddress(vReqEMail));
            vIndividu.setPostalAddress(vPostalAddr);

            vSyreliLegalData.setRequerant(vIndividu);

            vId = new IndividualIdentity();
            vId.setFirstName(vRepFirstName);
            vId.setLastName(vRepSurname);

            vPostalAddr = new PostalAddress(userId, tld);
            vPostalAddr.setCity(vRepTown);
            vPostalAddr.setPostCode(vRepZipCode);
            vPostalAddr.setCountryCode(vRepCountry);
            vPostalAddr.setStreet(vRepAddress1, vRepAddress2, vRepAddress3);

            vIndividu = new IndividualAndLegalStructure(vId, userId, tld);
            vIndividu.setOrganization(vRepOrganization);
            vIndividu.setVatnumber(vRepVatNumber);
            vIndividu.setCivility(vRepCivility);
            vIndividu.getPhoneNumbers().add(new PhoneNumber(vRepPhone));
            vIndividu.getFaxNumbers().add(new PhoneNumber(vRepFax));
            vIndividu.addEmailAddress(new EmailAddress(vRepEMail));
            vIndividu.setPostalAddress(vPostalAddr);

            vSyreliLegalData.setRepresentant(vIndividu);
            vSyreliLegalData.setAskedAction(SqlConverterFacade.convert(vAskedAction, AskedAction.class, userId, tld));
            vSyreliLegalData.setDateCreation(vCreatedAt);
            vSyreliLegalData.setNumDossier(vRefAjpr);
            vSyreliLegalData.setDomainName(vDomainName);
            vSyreliLegalData.setAnswerDate(vAnsweredAt);

            vSyreliLegalData.setTituLegalProcedures(vTituLegalProcedures);
            vSyreliLegalData.setTituPosition(vTituPosition);
            vSyreliLegalData.setTituRights(vTituRights);

            vSyreliLegalData.setViolationReasonsPart1(vViolationReasonsPart1);
            vSyreliLegalData.setViolationReasonsPart2(vViolationReasonsPart2);
            vSyreliLegalData.setViolationReasonsPart3(vViolationReasonsPart3);

            return vSyreliLegalData;
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
