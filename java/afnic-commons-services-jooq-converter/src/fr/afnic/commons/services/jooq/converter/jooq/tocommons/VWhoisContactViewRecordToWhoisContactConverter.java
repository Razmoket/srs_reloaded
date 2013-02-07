package fr.afnic.commons.services.jooq.converter.jooq.tocommons;

import org.jooq.tools.StringUtils;

import fr.afnic.commons.beans.CorporateEntityWhoisContact;
import fr.afnic.commons.beans.IndividualWhoisContact;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.corporateentity.CorporateEntity;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.stub.boa.tables.records.VWhoisContactViewRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class VWhoisContactViewRecordToWhoisContactConverter extends AbstractConverter<VWhoisContactViewRecord, WhoisContact> {

    public VWhoisContactViewRecordToWhoisContactConverter() {
        super(VWhoisContactViewRecord.class, WhoisContact.class);
    }

    @Override
    public WhoisContact convert(VWhoisContactViewRecord toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        WhoisContact ret;

        if (StringUtils.equals(toConvert.getContactType(), "PERSON")) {
            ret = this.getIndividualWhoisContact(toConvert, userId, tld);
        } else {
            ret = this.getCorporateEntityWhoisContact(toConvert, userId, tld);
        }

        /*    ret.setEmailAddresses(new EmailAddress(toConvert.getEmail()));
            ret.setPhoneNumbers(new PhoneNumber(toConvert.getPhone()));
            ret.setFaxNumbers(new PhoneNumber(toConvert.getFax()));

            ret.setHandle(toConvert.getHandle());*/

        ret.setPostalAddress(this.getPostalAddress(toConvert, userId, tld));

        return ret;
    }

    private PostalAddress getPostalAddress(VWhoisContactViewRecord toConvert, UserId userId, TldServiceFacade tld) {
        PostalAddress postalAddress = new PostalAddress(userId, tld);
        /*postalAddress.setCity(toConvert.getCity());
        postalAddress.setCountryCode(toConvert.getCountrycode());
        postalAddress.setStreetStr(toConvert.getStreet());
        postalAddress.setCityCedex(toConvert.getCedex());
        postalAddress.setOrganization(toConvert.getAddrOrganisation());*/
        return postalAddress;
    }

    private IndividualWhoisContact getIndividualWhoisContact(VWhoisContactViewRecord toConvert, UserId userId, TldServiceFacade tld) {
        IndividualWhoisContact ret = new IndividualWhoisContact(userId, tld);
        //ret.setFirstName(toConvert.getFirstname());
        //ret.setLastName(toConvert.getLastname());

        return ret;
    }

    private CorporateEntityWhoisContact getCorporateEntityWhoisContact(VWhoisContactViewRecord toConvert, UserId userId, TldServiceFacade tld) {

        CorporateEntity stuct = CorporateEntity.createCompany(userId, tld);
        /*stuct.setOrgnaizationName(toConvert.getLastname());
        CorporateEntityWhoisContact ret = new CorporateEntityWhoisContact(stuct, userId, tld);*/

        //return ret;

        return null;
    }

}
