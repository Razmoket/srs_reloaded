package fr.afnic.commons.services.jooq.converter.jooq.tocommons;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.stub.grc.tables.records.TldTableRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class TldTableRecordToTldServiceFacade extends AbstractConverter<TldTableRecord, TldServiceFacade> {

    public TldTableRecordToTldServiceFacade() {
        super(TldTableRecord.class, TldServiceFacade.class);
    }

    @Override
    public TldServiceFacade convert(TldTableRecord toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        return TldServiceFacade.findTldById(toConvert.getIdTld());
    }
}
