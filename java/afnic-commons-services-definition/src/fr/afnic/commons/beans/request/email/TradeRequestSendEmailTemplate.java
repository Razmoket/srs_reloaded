package fr.afnic.commons.beans.request.email;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class TradeRequestSendEmailTemplate extends TradeRequestTemplate {

    public TradeRequestSendEmailTemplate(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
    }

    @Override
    protected String buildFrContent() throws ServiceException {
        StringBuilder builder = new StringBuilder();
        builder.append("Demande de complément d'information pour la validation du trade de " + this.getDomainName());
        return builder.toString();
    }

    @Override
    protected String buildEnContent() throws ServiceException {
        return "";
    }

    @Override
    protected String getFrEndEmail() {
        return "";
    }

    @Override
    protected String getFrStartEmail() {
        return "";
    }

    @Override
    protected String getEnStartEmail() {
        return "";
    }

    @Override
    protected String getEnEndEmail() {
        return "";
    }

    @Override
    protected String getSeparatorEmailContent() {
        return "";
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "[" + this.parameter.getIdAsString() + "] Demande de complément d'information pour la validation du trade de " + this.getDomainName();
    }

}
