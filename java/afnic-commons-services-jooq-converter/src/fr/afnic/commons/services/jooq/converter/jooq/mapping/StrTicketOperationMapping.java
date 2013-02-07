package fr.afnic.commons.services.jooq.converter.jooq.mapping;

import fr.afnic.commons.beans.TicketOperation;
import fr.afnic.commons.services.converter.ConstantTypeMapping;

public class StrTicketOperationMapping extends ConstantTypeMapping<String, TicketOperation> {

    public StrTicketOperationMapping() {
        super(String.class, TicketOperation.class);
    }

    @Override
    protected void populateMap() {

        this.addMapping("ID", TicketOperation.IdentifyHolder);
        this.addMapping("CO", TicketOperation.CheckExtraLegalSettlement);
        this.addMapping("BK", TicketOperation.BlockDomain);
        this.addMapping("CD", TicketOperation.TransferDomain);
        this.addMapping("CR", TicketOperation.CreateDomain);
        this.addMapping("DB", TicketOperation.UnblockDomain);
        this.addMapping("MN", TicketOperation.LegacyRenameDomain);
        this.addMapping("SU", TicketOperation.DeleteDomain);
        this.addMapping("TR", TicketOperation.BulkTransferDomain);
        this.addMapping("MA", TicketOperation.UpdateDomainContactInfo);
        this.addMapping("MT", TicketOperation.UpdateDomainConfiguration);
        this.addMapping("MG", TicketOperation.LegacyUpdateDomain);
        this.addMapping("VA", TicketOperation.CheckHolder);
        this.addMapping("RE", TicketOperation.RestoreDomain);
        this.addMapping("CX", TicketOperation.UpdateDomainContext);
        this.addMapping("TM", TicketOperation.TradeDomain);
        this.addMapping("GL", TicketOperation.FreezeDomain);
        this.addMapping("DG", TicketOperation.UnfreezeDomain);

        //Peut poser des problèmes, le recover est en fait la même opération que le trade
        //this.addMapping("", TicketOperation.RecoverDomain);

    }

}
