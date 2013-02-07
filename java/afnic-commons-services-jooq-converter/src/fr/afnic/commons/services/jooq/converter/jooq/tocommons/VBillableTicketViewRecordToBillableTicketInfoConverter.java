package fr.afnic.commons.services.jooq.converter.jooq.tocommons;

import fr.afnic.commons.beans.billing.BillableTicketInfo;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.stub.boa.tables.records.VBillableTicketViewRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class VBillableTicketViewRecordToBillableTicketInfoConverter extends AbstractConverter<VBillableTicketViewRecord, BillableTicketInfo> {

    public VBillableTicketViewRecordToBillableTicketInfoConverter() {
        super(VBillableTicketViewRecord.class, BillableTicketInfo.class);
    }

    @Override
    public BillableTicketInfo convert(VBillableTicketViewRecord toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {

        BillableTicketInfo ret = new BillableTicketInfo();
        ret.setCommandDate(toConvert.getCommandDate());
        ret.setDomainName(toConvert.getDomaineName());
        ret.setArticle(toConvert.getArticle());
        ret.setPayersCustomer(toConvert.getPayersCustomer());
        ret.setBilledCustomer(toConvert.getBilledCustomer());
        ret.setTicketId(toConvert.getIdTicket());
        ret.setTld(toConvert.getTld());
        return ret;

    }
}
