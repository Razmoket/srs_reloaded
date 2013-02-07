package fr.afnic.commons.beans.operations.qualification.operation.email;

import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class UnblockedDomainPortfolioToRegistrar extends QualificationEmailTemplate {

    public UnblockedDomainPortfolioToRegistrar(UserId userId, TldServiceFacade tld) {
        super(OperationType.NotifyEmailUnblockedDomainPortfolioToRegistrar,
              QualificationEmailTemplateDestinary.Registrar, userId, tld);
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "AFNIC - AVIS DE DEBLOCAGE / NOTICE OF UNBLOCKING – " + this.getHolderNicHandle();
    }

    @Override
    protected String buildFrContent() throws ServiceException {
        return new StringBuilder().append("Nous revenons vers vous concernant la procédure de vérification que nous vous avons fait parvenir par courriel.\n")
                                  .append("\n")
                                  .append("Nous vous informons qu'une erreur est survenue dans le traitement du dossier et a généré accidentellement le blocage du portefeuille de noms de domaine. \n")
                                  .append("\n")
                                  .append("Nous nous excusons de la gêne occasionnée et procédons ce jour au déblocage du portefeuille de noms de domaine.\n")
                                  .append(" \n")
                                  .append("Nous restons à votre disposition pour tout autre renseignement.\n")
                                  .toString();
    }

    @Override
    protected String buildEnContent() throws ServiceException {
        return new StringBuilder().append("We are contacting you again regarding the verification procedure which was notified to you by e-mail.\n")
                                  .append("\n")
                                  .append("We hereby inform you that an anomaly during handling of the case accidentally blocked the domain name portfolio. \n")
                                  .append("\n")
                                  .append("We apologise for the inconvenience and are today unblocking the domain name portfolio.\n")
                                  .append("\n")
                                  .append("We remain at your disposal for any further information.\n")
                                  .toString();
    }

}
