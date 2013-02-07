package fr.afnic.commons.services.jooq.converter.jooq.tocommons;

import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.domain.DomainNameDetail;
import fr.afnic.commons.beans.domain.DomainStatus;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.converter.JooqConverterFacade;
import fr.afnic.commons.services.jooq.stub.boa.tables.records.VDomainViewRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class VDomainViewRecordToDomainConverter extends AbstractConverter<VDomainViewRecord, Domain> {

    public VDomainViewRecordToDomainConverter() {
        super(VDomainViewRecord.class, Domain.class);
    }

    @Override
    public Domain convert(VDomainViewRecord toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {

        Domain domain = new Domain(userId, tld);
        domain.setHolderHandle(toConvert.getHolderHandle());
        domain.setName(toConvert.getDomainName());
        domain.setCreateDate(toConvert.getCreationDate());
        domain.setAnniversaryDate(toConvert.getAnniversaryDate());
        domain.setDeleteDate(toConvert.getDeletionDate());
        domain.setHolderHandle(toConvert.getHolderHandle());
        domain.setStatus(JooqConverterFacade.convert(toConvert.getDomainStatus(), DomainStatus.class, userId, tld));
        domain.setRegistrarCode(toConvert.getCustomerCode());
        domain.setRegistrarName(toConvert.getCustomerName());
        domain.setAuthInfo(toConvert.getAuthinfo());

        DomainNameDetail detail = new DomainNameDetail();

        if (toConvert.getI18nName() != null) {
            detail.setUtf8(toConvert.getI18nName());
        } else {
            detail.setUtf8(toConvert.getDomainName());
        }

        if (toConvert.getBundleName() != null) {
            detail.setAsciiEquivalent(toConvert.getBundleName());
        } else {
            detail.setLdh(toConvert.getDomainName());
        }

        detail.setLdh(toConvert.getDomainName());

        domain.setNameDetail(detail);

        return domain;
    }
}
