package fr.afnic.commons.beans.request.email;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import fr.afnic.commons.beans.application.env.Environnement;
import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.operations.qualification.operation.email.ParameterizedEmailTemplate;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.TradeRequest;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public abstract class TradeRequestTemplate extends ParameterizedEmailTemplate<TradeRequest> {

    private final String fromEmailAddress;

    protected TradeRequestTemplate(UserId userId, TldServiceFacade tld) {
        super(TradeRequest.class, userId, tld);

        String fromEmailAddressTmp = "auto-gdd";
        if (!AppServiceFacade.getApplicationService().isEnv(Environnement.Prod)) {
            fromEmailAddressTmp += "-" + AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement().getName().toLowerCase();
        }
        fromEmailAddressTmp += "@afnic.fr";
        this.fromEmailAddress = fromEmailAddressTmp;
    }

    public void setAuthorizationRequest(TradeRequest parameter) {
        this.parameter = parameter;
    }

    @Override
    public EmailAddress buildFromEmailAddress() throws ServiceException {
        return new EmailAddress(this.fromEmailAddress);
    }

    protected String getFrStartEmail() {
        return "Bonjour,\n\n";
    }

    protected String getEnStartEmail() {
        return "Hello,\n\n";
    }

    protected String getFrEndEmail() {
        return "\n\nBien cordialement,\n\nService Client\nAFNIC\n";
    }

    protected String getEnEndEmail() {
        return "\n\nBest regards,\n\nAFNIC Customer Service\n\n";
    }

    protected String getSeparatorEmailContent() {
        return "\n\n\nENGLISH VERSION\n\n";
    }

    @Override
    public List<EmailAddress> buildCcEmailAddress() throws ServiceException {
        List<EmailAddress> list = new ArrayList<EmailAddress>();
        return list;
    }

    @Override
    public List<EmailAddress> buildBccEmailAddress() throws ServiceException {
        return Collections.emptyList();
    }

    @Override
    public List<EmailAddress> buildToEmailAddress() throws ServiceException {

        if (!AppServiceFacade.getApplicationService().isEnv(Environnement.Prod)) {
            String toEmailAddress = "testing+" + AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement().getName().toLowerCase() + "@afnic.fr";
            return Arrays.asList(new EmailAddress(toEmailAddress));
        } else {
            //TODO multi-registre            
            //return Arrays.asList(this.parameter.getTradeTicket().getIncomingRegistrar().getNoc().getFirstEmailAddress());

            String toEmailAddress = "testing+" + AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement().getName().toLowerCase() + "@afnic.fr";
            return Arrays.asList(new EmailAddress(toEmailAddress));
        }
    }

    @Override
    public final String buildContent() throws ServiceException {
        return new StringBuilder().append(this.getFrStartEmail())
                                  .append(this.buildFrContent())
                                  .append(this.getFrEndEmail())
                                  .append(this.getSeparatorEmailContent())
                                  .append(this.getEnStartEmail())
                                  .append(this.buildEnContent())
                                  .append(this.getEnEndEmail())
                                  .append(this.buildEndEmail())
                                  .toString();
    }

    protected abstract String buildFrContent() throws ServiceException;

    protected abstract String buildEnContent() throws ServiceException;

    protected String buildEndEmail() throws ServiceException {
        return "";
    }

    @Override
    public final String buildSubject() throws ServiceException {
        return this.buildEndSubject();
    }

    protected String getDomainName() throws ServiceException {
        return this.parameter.getTicket().getDomainName();
    }

    protected abstract String buildEndSubject() throws ServiceException;

}
