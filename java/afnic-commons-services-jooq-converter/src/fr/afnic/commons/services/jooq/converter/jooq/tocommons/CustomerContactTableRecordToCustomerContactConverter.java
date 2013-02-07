package fr.afnic.commons.services.jooq.converter.jooq.tocommons;

import fr.afnic.commons.beans.contact.CustomerContact;
import fr.afnic.commons.beans.contact.CustomerContactId;
import fr.afnic.commons.beans.contact.CustomerContactStatus;
import fr.afnic.commons.beans.contact.identity.ContactIdentity;
import fr.afnic.commons.beans.contactdetails.PostalAddressId;
import fr.afnic.commons.beans.customer.CustomerId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.jooq.converter.JooqConverterFacade;
import fr.afnic.commons.services.jooq.stub.grc.tables.records.CustomerContactTableRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class CustomerContactTableRecordToCustomerContactConverter extends AbstractConverter<CustomerContactTableRecord, CustomerContact> {

    public CustomerContactTableRecordToCustomerContactConverter() {
        super(CustomerContactTableRecord.class, CustomerContact.class);

    }

    @Override
    public CustomerContact convert(CustomerContactTableRecord toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {

        ContactIdentity identity = null;
        if (toConvert.getIdIndividualentity() != null) {
            identity = AppServiceFacade.getIdentityService().getIndividualIdentity(toConvert.getIdIndividualentity(), userId, tld);
        }

        if (toConvert.getIdCorporateentity() != null) {
            identity = AppServiceFacade.getIdentityService().getCorporateEntityIdentity(toConvert.getIdCorporateentity(), userId, tld);
        }

        CustomerContact ret = new CustomerContact(identity, userId, tld);

        ret.setPostalAddressId(new PostalAddressId(toConvert.getIdPostaladdress().intValue()));
        ret.setCreateDate(toConvert.getCreateDate());
        ret.setUpdateDate(toConvert.getUpdateDate());

        if (toConvert.getObjectVersion() != null) {
            ret.setObjectVersion(toConvert.getObjectVersion());
        }

        if (toConvert.getIdCreateUser() != null) {
            ret.setCreateUserId(new UserId(toConvert.getIdCreateUser().intValue()));
        }

        if (toConvert.getIdUpdateUser() != null) {
            ret.setUpdateUserId(new UserId(toConvert.getIdUpdateUser().intValue()));
        }

        if (toConvert.getIdContact() != null) {
            ret.setContactId(new CustomerContactId(toConvert.getIdContact().intValue()));
        }

        if (toConvert.getIdCorporateentity() != null) {
            ret.setCorporateId(toConvert.getIdCorporateentity().intValue());
        }

        if (toConvert.getIdCustomer() != null) {
            ret.setCustomerId(new CustomerId(toConvert.getIdCustomer()));
        }
        ret.setCompanyRepresentative(toConvert.getIsCompanyRepresentative() != null && toConvert.getIsCompanyRepresentative() == 1);

        ret.setContactDetails(AppServiceFacade.getContactDetailsService().getCustomerContactDetails(toConvert.getIdContact(), userId, tld));

        ret.setStatus(JooqConverterFacade.convert(toConvert.getIdContactstatus(), CustomerContactStatus.class, userId, tld));

        ret.setRemark(toConvert.getRemark());
        return ret;
    }

}
