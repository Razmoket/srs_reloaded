package fr.afnic.commons.beans.operations.qualification.operation.email;

import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class UnblockedDomainPortfolioToHolder extends QualificationEmailTemplate {

    public UnblockedDomainPortfolioToHolder(UserId userId, TldServiceFacade tld) {
        super(OperationType.NotifyEmailUnblockedDomainPortfolioToHolder,
              QualificationEmailTemplateDestinary.Holder, userId, tld);
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "AFNIC - Information sur les suites de la procédure de vérification vous concernant ";
    }

    @Override
    protected String buildFrContent() throws ServiceException {
        return new StringBuilder().append("Nous revenons vers vous au sujet de la procédure de vérification vous concernant.\n")
                                  .append("Pour rappel, ces données sont référencées sous le NIC HANDLE AFNIC " + this.getHolderNicHandle() + ".\n")
                                  .append("\n")
                                  .append("Nous vous informons qu'une erreur est survenue dans le traitement du dossier et a généré accidentellement le blocage du portefeuille de noms de domaine. \n")
                                  .append("\n")
                                  .append("Nous nous excusons de la gêne occasionnée et procédons ce jour au déblocage du portefeuille de noms de domaine.\n")
                                  .append("Notre support est à votre disposition si nécessaire à l'adresse suivante : support@afnic.fr ou par téléphone au 01 39 30 83 00.\n")
                                  .toString();
    }

    @Override
    protected String buildEnContent() throws ServiceException {
        return new StringBuilder().append("We are contacting you again regarding the verification procedure which was notified to you by e-mail.\n")
                                  .append("We remind you that the data are referenced under the AFNIC NIC HANDLE " + this.getHolderNicHandle() + ".\n")
                                  .append(" \n")
                                  .append("We hereby inform you that an anomaly during the handling of the case accidentally blocked the domain name portfolio. \n")
                                  .append(" \n")
                                  .append("We apologise for the inconvenience and are today unblocking the domain name portfolio.\n")
                                  .append("\n")
                                  .append("If required, our support service is available at the following address: support@afnic.fr or by telephone at +33 (0)1 39 30 83 00.\n")
                                  .toString();
    }

}
