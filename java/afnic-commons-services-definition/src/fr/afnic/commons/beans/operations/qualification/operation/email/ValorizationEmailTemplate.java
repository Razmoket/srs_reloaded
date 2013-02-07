package fr.afnic.commons.beans.operations.qualification.operation.email;

import fr.afnic.commons.beans.application.env.Environnement;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public abstract class ValorizationEmailTemplate extends QualificationEmailTemplate {

    private String domainName = null;

    protected ValorizationEmailTemplate(OperationType type, QualificationEmailTemplateDestinary destinary, UserId userId, TldServiceFacade tld) {
        this(type, destinary, "\n", userId, tld);
    }

    protected ValorizationEmailTemplate(OperationType type, QualificationEmailTemplateDestinary destinary, String separator, UserId userId, TldServiceFacade tld) {
        super(type, destinary, separator, userId, tld);

        String fromEmailAddressTmp = "valorisation";
        if (!AppServiceFacade.getApplicationService().isEnv(Environnement.Prod)) {
            fromEmailAddressTmp += "-" + AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement().getName().toLowerCase();
        }
        fromEmailAddressTmp += "@afnic.fr";

        this.fromEmailAddressToUse = fromEmailAddressTmp;
    }

    protected String getDomainName() throws ServiceException {
        if (this.getDomainNameInitializedFrom() != null) {
            this.domainName = this.getDomainNameInitializedFrom();
        } else {
            this.domainName = this.parameter.getHolder().getDomainsPortfolio().get(0);
        }

        return this.domainName;
    }

}
