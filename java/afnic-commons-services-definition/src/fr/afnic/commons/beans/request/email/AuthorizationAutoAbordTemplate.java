package fr.afnic.commons.beans.request.email;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class AuthorizationAutoAbordTemplate extends AuthorizationRequestTemplate {

    public AuthorizationAutoAbordTemplate(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
    }

    @Override
    protected String buildFrContent() throws ServiceException {
        StringBuilder builder = new StringBuilder();

        builder.append("Nous vous informons de l'abandon de votre demande de code d'autorisation pour " + this.getDomainName() + " pour " + this.getHolderNicHandle()
                       + ", le dernier message que nous vous avons adressé à son sujet étant resté sans réponse depuis 15 jours.");
        builder.append("\n");
        builder.append("\nNous restons à votre disposition pour tout autre renseignement.");
        return builder.toString();
    }

    @Override
    protected String buildEnContent() throws ServiceException {
        StringBuilder builder = new StringBuilder();

        builder.append("We inform you that your request for an authorization code for " + this.getDomainName() + " with " + this.getHolderNicHandle()
                       + " has been abandonned, as we did not get any answer to our last message for 15 days.");
        builder.append("\n");
        builder.append("\nPlease feel free to contact your customer service representative for further information.");
        return builder.toString();

    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "AFNIC - Notification d'abandon de la demande de génération de code pour " + this.getDomainName() + " avec " + this.getHolderNicHandle();
    }

}
