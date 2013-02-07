package fr.afnic.commons.services.jooq.converter.jooq.tocommons;

import fr.afnic.commons.beans.contactdetails.ContactDetail;
import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.contactdetails.FaxNumber;
import fr.afnic.commons.beans.contactdetails.PhoneNumber;
import fr.afnic.commons.beans.contactdetails.Url;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.stub.grc.tables.records.ContactDetailsTableRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class ContactDetailsTableRecordToContactDetailsConverter extends AbstractConverter<ContactDetailsTableRecord, ContactDetail> {

    public ContactDetailsTableRecordToContactDetailsConverter() {
        super(ContactDetailsTableRecord.class, ContactDetail.class);

    }

    @Override
    public ContactDetail convert(ContactDetailsTableRecord toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {

        ContactDetail ret;

        switch (toConvert.getIdContactdetailstype()) {
        case ContactDetail.EMAIL:
            ret = new EmailAddress(toConvert.getValue());
            break;
        case ContactDetail.PHONE:
            ret = new PhoneNumber(toConvert.getValue());
            break;
        case ContactDetail.FAX:
            ret = new FaxNumber(toConvert.getValue());
            break;
        case ContactDetail.URL:
            ret = new Url(toConvert.getValue());
            break;
        default:
            ret = new ContactDetail(toConvert.getValue());
            break;
        }
        return ret;
    }
}
