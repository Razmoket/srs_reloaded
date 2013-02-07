package fr.afnic.commons.services.jooq.converter.jooq.tocommons;

import fr.afnic.commons.beans.contact.identity.IndividualIdentity;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.stub.grc.tables.records.IndividualEntityTableRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class IndividualTableRecordToIndividualIdentityConverter extends AbstractConverter<IndividualEntityTableRecord, IndividualIdentity> {

    public IndividualTableRecordToIndividualIdentityConverter() {
        super(IndividualEntityTableRecord.class, IndividualIdentity.class);

    }

    @Override
    public IndividualIdentity convert(IndividualEntityTableRecord toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {

        IndividualIdentity ret = new IndividualIdentity();
        ret.setFirstName(toConvert.getFirstName());
        ret.setLastName(toConvert.getLastName());
        ret.setBirthCity(toConvert.getBirthPlace());
        ret.setBirthDate(toConvert.getBirthDate());
        ret.setId(toConvert.getIdIndividualentity());

        return ret;
    }
}
