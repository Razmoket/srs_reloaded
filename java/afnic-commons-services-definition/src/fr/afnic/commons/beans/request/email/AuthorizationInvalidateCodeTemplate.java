package fr.afnic.commons.beans.request.email;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class AuthorizationInvalidateCodeTemplate extends AuthorizationRequestTemplate {

    public AuthorizationInvalidateCodeTemplate(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
    }

    @Override
    protected String buildFrContent() throws ServiceException {
        StringBuilder builder = new StringBuilder();
        builder.append("L'AFNIC vous informe que le code d'autorisation " + this.getAuthorizationCode() + " qui a été généré");
        builder.append(" pour le titulaire " + this.getHolderNicHandle() + "," + this.getHolderName() + " pour le nom de domaine " + this.getDomainName() + " a été");
        builder.append(" invalidé pour la raison suivante :");
        builder.append("\n");
        builder.append("\nIl est donc inutilisable.");
        builder.append("\n");
        builder.append("\nNous vous remercions de bien vouloir contacter votre Chargé de clientèle");
        builder.append(" pour tout renseignement à ce sujet.");
        return builder.toString();
    }

    @Override
    protected String buildEnContent() throws ServiceException {
        StringBuilder builder = new StringBuilder();
        builder.append("\nAFNIC informs you that the authorization code " + this.getAuthorizationCode());
        builder.append(" delivered for " + this.getHolderNicHandle() + " and for the domain name " + this.getDomainName() + " has been");
        builder.append(" invalidated for the following reason :");
        builder.append("\n");
        builder.append("\nYou can therefore not use it any more.");
        builder.append("\n");
        builder.append("\nPlease contact your customer service representative may you require");
        builder.append(" further information.");
        return builder.toString();
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "Annulation du code d'autorisation pour / Cancellation of the authorization code for " + this.getHolderNicHandle() + " et " + this.getDomainName();
    }

}
