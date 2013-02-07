package fr.afnic.commons.beans.request.email;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class AuthorizationManualAbordTemplate extends AuthorizationRequestTemplate {

    public AuthorizationManualAbordTemplate(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
    }

    @Override
    protected String buildFrContent() throws ServiceException {
        StringBuilder builder = new StringBuilder();
        builder.append("L'AFNIC vous informe que la demande de generation de code pour le titulaire " + this.getHolderNicHandle() + "," + this.getHolderName());
        builder.append("\npour le nom de domaine " + this.getDomainName() + " a été abandonné à votre demande.");
        builder.append("\n");
        builder.append("\nNous restons à votre disposition pour tout autre renseignement.");
        return builder.toString();
    }

    @Override
    protected String buildEnContent() throws ServiceException {
        StringBuilder builder = new StringBuilder();
        builder.append("AFNIC informs you that the request for an authorization code for " + this.getHolderNicHandle() + "," + this.getHolderName());
        builder.append("\nand for " + this.getDomainName() + " has been abandonned upon your request");
        builder.append("\n");
        builder.append("\nPlease feel free to contact your customer service representative for further information.");
        return builder.toString();
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "AFNIC - Notification d'abandon de la demande de génération de code pour " + this.getDomainName() + " avec " + this.getHolderNicHandle();
    }

}
