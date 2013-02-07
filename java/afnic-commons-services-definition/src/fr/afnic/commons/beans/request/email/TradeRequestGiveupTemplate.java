package fr.afnic.commons.beans.request.email;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class TradeRequestGiveupTemplate extends TradeRequestTemplate {

    public TradeRequestGiveupTemplate(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
    }

    @Override
    protected String buildFrContent() throws ServiceException {
        StringBuilder builder = new StringBuilder();
        builder.append("L'AFNIC vous informe que le ticket " + this.parameter.getTicket().getId() + " concernant le trade du domaine " + this.getDomainName()
                       + " a été abandonné pour la raison suivante :");
        builder.append("\n\nxxx");
        builder.append("\n\n\nNous restons à votre disposition pour tout autre renseignement.");
        return builder.toString();
    }

    @Override
    protected String buildEnContent() throws ServiceException {
        StringBuilder builder = new StringBuilder();
        builder.append("The trade ticket " + this.parameter.getTicket().getId() + " for domain " + this.getDomainName() + " has been abandonned because :");
        builder.append("\n\nxxx");
        builder.append("\n\n\nWe remain at your disposal should you require any further information.");
        return builder.toString();
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "AFNIC - Notification d'abandon de la demande de trade sur " + this.getDomainName();
    }

}
