package fr.afnic.commons.beans.operations.qualification.operation;

import java.util.Date;

import fr.afnic.commons.beans.QualificationUpdateSource;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class UpdateContactWhoisQualification extends SimpleQualificationOperation {

    protected static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(UpdateContactWhoisQualification.class);

    public UpdateContactWhoisQualification(UserId userId, TldServiceFacade tld) {
        this(OperationConfiguration.create(), userId, tld);
    }

    public UpdateContactWhoisQualification(OperationConfiguration conf, UserId userId, TldServiceFacade tld) {
        super(conf.setType(OperationType.UpdateContactWhoisQualification), userId, tld);

    }

    @Override
    protected OperationStatus executeSimpleImpl() throws Exception {
        WhoisContact holder = this.getQualification().getHolder();

        holder.setEligibilityStatus(this.getQualification().computePublicEligibilityStatus());
        holder.setReachStatus(this.getQualification().computePublicReachStatus());
        holder.setReachMedia(this.getQualification().computePublicReachMedia());

        holder.setReachDate(new Date());
        holder.setEligibilityDate(new Date());
        holder.setReachSource(QualificationUpdateSource.Registry);
        holder.setEligibilitySource(QualificationUpdateSource.Registry);

        AppServiceFacade.getWhoisContactService().updateContact(holder, this.userIdCaller.getObjectOwner(this.userIdCaller, this.tldCaller).getLogin(), this.userIdCaller, this.tldCaller);

        return OperationStatus.Succed;
    }
}
