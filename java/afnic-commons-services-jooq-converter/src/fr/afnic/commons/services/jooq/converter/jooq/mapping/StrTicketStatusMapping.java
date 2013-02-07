package fr.afnic.commons.services.jooq.converter.jooq.mapping;

import fr.afnic.commons.beans.TicketStatus;
import fr.afnic.commons.services.converter.ConstantTypeMapping;

public class StrTicketStatusMapping extends ConstantTypeMapping<String, TicketStatus> {

    public StrTicketStatusMapping() {
        super(String.class, TicketStatus.class);
    }

    @Override
    protected void populateMap() {

        this.addMapping("AB", TicketStatus.Cancelled);
        this.addMapping("AC", TicketStatus.PendingRegistrarInput);
        this.addMapping("AR", TicketStatus.PendingRegistrarCancel);
        this.addMapping("AV", TicketStatus.PendingCheckover);
        this.addMapping("BL", TicketStatus.DomainBlocked);
        this.addMapping("DN", TicketStatus.DNSNotReady);
        this.addMapping("F6", TicketStatus.InvoiceTwoMonthsOrMore);
        this.addMapping("FI", TicketStatus.Closed);
        this.addMapping("OU", TicketStatus.Open);
        this.addMapping("PB", TicketStatus.PendingSolveProblem);
        this.addMapping("SU", TicketStatus.DomainDeleted);
        this.addMapping("AS", TicketStatus.PendingUserInput);
        this.addMapping("FC", TicketStatus.InvalidForm);
        this.addMapping("NA", TicketStatus.DomainNameApproved);
        this.addMapping("DI", TicketStatus.DNSInstalled);
        this.addMapping("AT", TicketStatus.PendingUserProcess);
        this.addMapping("AO", TicketStatus.PendingRegistrarReject);
        this.addMapping("AA", TicketStatus.PendingRegistrarApprove);
        this.addMapping("AD", TicketStatus.PendingDNSConfiguration);
        this.addMapping("WB", TicketStatus.PendingBlockDomain);
        this.addMapping("WD", TicketStatus.PendingUnblockDomain);
        this.addMapping("WS", TicketStatus.PendingDelete);
        this.addMapping("WI", TicketStatus.PendingIdentifyHolder);
        this.addMapping("AJ", TicketStatus.PendingExtraLegalSettlement);
        this.addMapping("AF", TicketStatus.PendingHolderFax);
        this.addMapping("AM", TicketStatus.PendingHolderEMail);

    }

}
