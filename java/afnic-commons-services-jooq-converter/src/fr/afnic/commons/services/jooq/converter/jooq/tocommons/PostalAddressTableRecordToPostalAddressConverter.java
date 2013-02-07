package fr.afnic.commons.services.jooq.converter.jooq.tocommons;

import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.contactdetails.PostalAddressId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.stub.grc.tables.records.PostalAddressTableRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class PostalAddressTableRecordToPostalAddressConverter extends AbstractConverter<PostalAddressTableRecord, PostalAddress> {

    public PostalAddressTableRecordToPostalAddressConverter() {
        super(PostalAddressTableRecord.class, PostalAddress.class);

    }

    @Override
    public PostalAddress convert(PostalAddressTableRecord toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {

        PostalAddress ret = new PostalAddress(userId, tld);
        ret.setCity(toConvert.getCity());
        ret.setCityCedex(toConvert.getCityCedex());
        ret.setCountryCode(toConvert.getCountryCode());
        ret.setPostCode(toConvert.getPostCode());
        ret.setOrganization(toConvert.getOrganization());
        ret.setCreateDate(toConvert.getCreateDate());
        ret.setObjectVersion(toConvert.getObjectVersion());

        ret.setUpdateDate(toConvert.getUpdateDate());

        if (toConvert.getIdCreateUser() != null) {
            ret.setCreateUserId(new UserId(toConvert.getIdCreateUser()));
        }

        if (toConvert.getIdUpdateUser() != null) {
            ret.setUpdateUserId(new UserId(toConvert.getIdUpdateUser()));
        }

        ret.setStreet(toConvert.getStreetLine_1(),
                      toConvert.getStreetLine_2(),
                      toConvert.getStreetLine_3());

        ret.setId(new PostalAddressId(toConvert.getIdPostaladdress()));
        return ret;
    }
}
