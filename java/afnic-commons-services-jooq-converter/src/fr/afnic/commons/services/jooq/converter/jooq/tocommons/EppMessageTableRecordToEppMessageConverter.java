package fr.afnic.commons.services.jooq.converter.jooq.tocommons;

import fr.afnic.commons.beans.epp.EppMessage;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.stub.nicope.tables.records.EppMessageTableRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class EppMessageTableRecordToEppMessageConverter extends AbstractConverter<EppMessageTableRecord, EppMessage> {

    public EppMessageTableRecordToEppMessageConverter() {
        super(EppMessageTableRecord.class, EppMessage.class);
    }

    @Override
    public EppMessage convert(EppMessageTableRecord toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        EppMessage message = new EppMessage();
        message.setContactHandle(toConvert.getContactHandle());
        message.setContactSnapshotId(toConvert.getContactSnapshotId());
        //message.setDomainName(toConvert.getDomainName());
        message.setDomainSnapshotId(toConvert.getDomainSnapshotId());
        message.setType(toConvert.getType());
        message.setEnqueueTime((toConvert.getEnqueueTime() != null ? toConvert.getEnqueueTime().toString() : ""));
        message.setIdAdherent(toConvert.getIdadher());
        message.setId(toConvert.getId().intValue());
        message.setMessage(toConvert.getMessage());
        //message.setNumFo(toConvert.getNumfo());

        /* if (toConvert.getSuccess() == 0) {
             message.setStatus(EppMessageStatus.Failed);
         } else {
             message.setStatus(EppMessageStatus.Succed);
         }*/

        return message;
    }
}
