package fr.afnic.commons.beans.request.email;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class AuthorizationRequestSendEmailTemplate extends AuthorizationRequestTemplate {

    public AuthorizationRequestSendEmailTemplate(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
    }

    @Override
    protected String buildFrContent() throws ServiceException {
        return "";
    }

    @Override
    protected String buildEnContent() throws ServiceException {
        return "";
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "Demande de complément d'information pour la génération du code d'autorisation pour " + this.getDomainName();
    }

}
