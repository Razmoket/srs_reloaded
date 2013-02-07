package fr.afnic.commons.services.sql.converter.mapping;

import fr.afnic.commons.beans.profiling.users.UserRight;
import fr.afnic.commons.services.converter.ConstantTypeMapping;

public class UserRightMapping extends ConstantTypeMapping<Integer, UserRight> {

    public UserRightMapping() {
        super(Integer.class, UserRight.class);
    }

    @Override
    protected void populateMap() {
        this.addMapping(1, UserRight.Viewer);
        this.addMapping(2, UserRight.AuthorizationRead);
        this.addMapping(3, UserRight.AuthorizationWrite);
        this.addMapping(4, UserRight.AuthorizationUnlock);
        this.addMapping(5, UserRight.AuthorizationSuppression);
        this.addMapping(6, UserRight.TradeRead);
        this.addMapping(7, UserRight.TradeWrite);
        this.addMapping(8, UserRight.TradeUnlock);
        this.addMapping(9, UserRight.Admin);
        this.addMapping(10, UserRight.StatistictRead);
        this.addMapping(11, UserRight.CustomerCreate);
        this.addMapping(12, UserRight.CustomerUpdate);
        this.addMapping(13, UserRight.CustomerRead);
        this.addMapping(14, UserRight.AccountManager);
        this.addMapping(15, UserRight.TicketRead);
        this.addMapping(16, UserRight.TicketWrite);
        this.addMapping(17, UserRight.EmailView);
        this.addMapping(18, UserRight.EmailSend);
        this.addMapping(19, UserRight.QualificationRead);
        this.addMapping(20, UserRight.QualificationWrite);
        this.addMapping(21, UserRight.QualificationCreate);
        this.addMapping(22, UserRight.QualificationStat);
        this.addMapping(23, UserRight.OperationRelaunch);
        this.addMapping(24, UserRight.WhoisContactRead);
        this.addMapping(25, UserRight.WhoisContactBlockportfolio);
        this.addMapping(26, UserRight.WhoisContactWrite);
        this.addMapping(27, UserRight.DomainRead);
        this.addMapping(28, UserRight.DomainBlock);
        this.addMapping(29, UserRight.StagedDelete);
        this.addMapping(30, UserRight.AgtfManager);
        this.addMapping(31, UserRight.ContractCreate);
        this.addMapping(32, UserRight.ContractUpdate);
    }

}
