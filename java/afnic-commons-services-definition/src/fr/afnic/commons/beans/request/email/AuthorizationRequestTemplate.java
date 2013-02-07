package fr.afnic.commons.beans.request.email;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import fr.afnic.commons.beans.IndividualWhoisContact;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.application.env.Environnement;
import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.operations.qualification.operation.email.ParameterizedEmailTemplate;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.AuthorizationRequest;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public abstract class AuthorizationRequestTemplate extends ParameterizedEmailTemplate<AuthorizationRequest> {

    private final String fromEmailAddress;

    protected AuthorizationRequestTemplate(UserId userId, TldServiceFacade tld) {
        super(AuthorizationRequest.class, userId, tld);

        String fromEmailAddressTmp = "auto-gdd";
        if (!AppServiceFacade.getApplicationService().isEnv(Environnement.Prod)) {
            fromEmailAddressTmp += "-" + AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement().getName().toLowerCase();
        }
        fromEmailAddressTmp += "@afnic.fr";
        this.fromEmailAddress = fromEmailAddressTmp;
    }

    public void setAuthorizationRequest(AuthorizationRequest parameter) {
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
        return "Dear Sir or Madam,\n\n";
    }

    protected String getFrEndEmail() {
        return "\n\nBien cordialement,\nService Client\nAFNIC\n\n";
    }

    protected String getEnEndEmail() {
        return "\n\nBest regards,\nAFNIC Customer Service\n\n";
    }

    protected String getSeparatorEmailContent() {
        return "\n======================================\n\n";
    }

    @Override
    public List<EmailAddress> buildCcEmailAddress() throws ServiceException {
        List<EmailAddress> list = new ArrayList<EmailAddress>();
        list.add(new EmailAddress("gdd@afnic.fr"));
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
            //TODO multiregistre
            //return Arrays.asList(this.parameter.getRegistrar().getNoc().getFirstEmailAddress());
            throw new NotImplementedException();
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
        return "[A_" + this.parameter.getId() + "] " + this.buildEndSubject();
    }

    protected String getHolderNicHandle() {
        return this.parameter.getRequestedHolderHandle();
    }

    protected String getHolderName() throws ServiceException {
        try {
            WhoisContact holder = this.parameter.getHolder();
            if (holder instanceof IndividualWhoisContact) {
                final IndividualWhoisContact individual = (IndividualWhoisContact) holder;
                return individual.getLastName() + " " + individual.getFirstName();
            } else {
                return holder.getName();
            }
        } catch (NotFoundException e) {
            return "";
        }
    }

    protected String getDomainName() throws ServiceException {
        Domain domain = this.parameter.getDomain();
        String domainName = domain.getNameDetail().getUtf8();
        if (domain.getNameDetail().isNotAsciiName()) {
            domainName += " (" + domain.getNameDetail().getLdh() + ")";
        }
        return domainName;
    }

    protected String getAuthorizationCode() throws ServiceException {
        return this.parameter.getAuthorization().getCode();
    }

    protected abstract String buildEndSubject() throws ServiceException;

}
