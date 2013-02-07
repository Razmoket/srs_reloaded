package fr.afnic.commons.services.jooq.converter.jooq.tocommons;

import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.beans.TicketOperation;
import fr.afnic.commons.beans.TicketStatus;
import fr.afnic.commons.beans.customer.CustomerId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.validatable.OperationFormId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.converter.JooqConverterFacade;
import fr.afnic.commons.services.jooq.stub.boa.tables.records.VTicketViewRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class VTicketViewRecordToTicketConverter extends AbstractConverter<VTicketViewRecord, Ticket> {

    public VTicketViewRecordToTicketConverter() {
        super(VTicketViewRecord.class, Ticket.class);
    }

    @Override
    public Ticket convert(VTicketViewRecord toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {

        Ticket ret = new Ticket(userId, tld);
        ret.setCreateDate(toConvert.getCreationDate());

        if (toConvert.getIdOperationform() != null) {
            ret.setOperationFormId(new OperationFormId(toConvert.getIdOperationform().intValue()));
        }

        ret.setComment(toConvert.getComments());
        ret.setDomainNicopeId(toConvert.getIdDomainNicope());
        ret.setDomainName(toConvert.getDomainName());

        ret.setRegistrarId(new CustomerId(toConvert.getIdCustomer().intValue()));
        ret.setRegistrarCode(toConvert.getCustomerCode());
        ret.setRegistrarName(toConvert.getCustomerName());

        ret.setId(toConvert.getIdTicket());

        ret.setOperation(JooqConverterFacade.convert(toConvert.getOperation(), TicketOperation.class, userId, tld));
        ret.setStatus(JooqConverterFacade.convert(toConvert.getStatus(), TicketStatus.class, userId, tld));

        return ret;

    }
}
