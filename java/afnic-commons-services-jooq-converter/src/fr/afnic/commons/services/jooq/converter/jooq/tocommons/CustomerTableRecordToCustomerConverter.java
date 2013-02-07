package fr.afnic.commons.services.jooq.converter.jooq.tocommons;

import fr.afnic.commons.beans.contact.identity.ContactIdentity;
import fr.afnic.commons.beans.contactdetails.PostalAddressId;
import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.customer.CustomerId;
import fr.afnic.commons.beans.customer.status.CustomerStatus;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.jooq.converter.JooqConverterFacade;
import fr.afnic.commons.services.jooq.stub.grc.tables.records.CustomerTableRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class CustomerTableRecordToCustomerConverter extends AbstractConverter<CustomerTableRecord, Customer> {

    public CustomerTableRecordToCustomerConverter() {
        super(CustomerTableRecord.class, Customer.class);

    }

    @Override
    public Customer convert(CustomerTableRecord toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {

        ContactIdentity identity = null;
        if (toConvert.getIdIndividualentity() != null) {
            identity = AppServiceFacade.getIdentityService().getIndividualIdentity(toConvert.getIdIndividualentity(), userId, tld);
        }

        if (toConvert.getIdCorporateentity() != null) {
            identity = AppServiceFacade.getIdentityService().getCorporateEntityIdentity(toConvert.getIdCorporateentity(), userId, tld);
        }

        Customer ret = new Customer(identity, userId, tld);

        if (toConvert.getIdPostaladdress() != null) {
            ret.setPostalAddressId(new PostalAddressId(toConvert.getIdPostaladdress().intValue()));
        }

        ret.setCreateDate(toConvert.getCreateDate());
        ret.setUpdateDate(toConvert.getUpdateDate());

        if (toConvert.getObjectVersion() != null) {
            ret.setObjectVersion(toConvert.getObjectVersion().intValue());
        }

        if (toConvert.getIdCreateUser() != null) {
            ret.setCreateUserId(new UserId(toConvert.getIdCreateUser().intValue()));
        }

        if (toConvert.getIdUpdateUser() != null) {
            ret.setUpdateUserId(new UserId(toConvert.getIdUpdateUser().intValue()));
        }

        if (toConvert.getIdCustomer() != null) {
            ret.setCustomerId(new CustomerId(toConvert.getIdCustomer().intValue()));
        }

        ret.setCode(toConvert.getCode());
        ret.setCustomerNumber(toConvert.getIdCustomerNicope());

        if (toConvert.getIdAccountmanager() != null) {
            ret.setAccountManagerId(new UserId(toConvert.getIdAccountmanager().intValue()));
        }

        //TODO : gerer en fonction de l'évolution du modèle
        //ret.setAccreditationNumber();

        ret.setContactDetails(AppServiceFacade.getContactDetailsService().getCustomerContactDetails(toConvert.getIdCustomer(), userId, tld));
        ret.setNicopeId(toConvert.getIdCustomerNicope());

        ret.setStatus(JooqConverterFacade.convert(toConvert.getIdCustomerstatus(), CustomerStatus.class, userId, tld));

        return ret;
    }
}
