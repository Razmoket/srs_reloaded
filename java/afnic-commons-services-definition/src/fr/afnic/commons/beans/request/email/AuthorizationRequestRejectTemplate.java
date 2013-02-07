package fr.afnic.commons.beans.request.email;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class AuthorizationRequestRejectTemplate extends AuthorizationRequestTemplate {

    public AuthorizationRequestRejectTemplate(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
    }

    @Override
    protected String buildFrContent() throws ServiceException {
        StringBuilder builder = new StringBuilder();
        builder.append("L'AFNIC vous informe que la demande de generation de code pour le titulaire " + this.getHolderNicHandle() + "," + this.getHolderName());
        builder.append("\npour le nom de domaine " + this.getDomainName() + " a été rejeté pour la raison suivante:");
        builder.append("\n[...]");
        builder.append("\n");
        builder.append("\nNous restons à votre disposition pour tout autre renseignement.");
        return builder.toString();
    }

    @Override
    protected String buildEnContent() throws ServiceException {
        StringBuilder builder = new StringBuilder();
        builder.append("AFNIC informs you that the request for an authorization code for " + this.getHolderNicHandle() + "," + this.getHolderName());
        builder.append("\nfor the domain name " + this.getDomainName() + " has been denied for the following reason:");
        builder.append("\n[...]");
        builder.append("\n");
        builder.append("\nPlease feel free to contact your customer service representative for further information.");
        return builder.toString();
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "AFNIC - Notification de rejet de la demande de génération de code pour " + this.getDomainName() + " avec " + this.getHolderNicHandle();
    }

}
