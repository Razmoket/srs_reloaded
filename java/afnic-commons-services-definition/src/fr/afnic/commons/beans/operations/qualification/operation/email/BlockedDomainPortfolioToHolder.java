package fr.afnic.commons.beans.operations.qualification.operation.email;

import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class BlockedDomainPortfolioToHolder extends QualificationEmailTemplate {

    public BlockedDomainPortfolioToHolder(UserId userId, TldServiceFacade tld) {
        super(OperationType.NotifyEmailBlockedDomainPortfolioToHolder,
              QualificationEmailTemplateDestinary.Holder, userId, tld);
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "AFNIC - AVIS DE BLOCAGE / NOTICE OF BLOCKING - " + this.getHolderName() + " " + this.getHolderNicHandle();
    }

    @Override
    protected String buildFrContent() throws ServiceException {
        return new StringBuilder()
                                  .append("Ce courriel vous est adressé à titre d'information. \n")
                                  .append("\n")
                                  .append("Nous revenons vers vous au sujet de la procédure de vérification vous concernant.\n")
                                  .append("\n")
                                  .append("Pour rappel, ces données sont référencées sous le NIC HANDLE AFNIC " + this.getHolderNicHandle() + ".\n")
                                  .append("\n")
                                  .append("Nous n'avons reçu aucune pièce justificative de la part du  bureau d'enregistrement " + this.getCustomerName()
                                          + " en charge des noms de domaine dans le délai imparti. \n")
                                  .append("\n")
                                  .append("En conséquence, les noms de domaine (liste en fin de courriel) font l'objet d'un blocage par nos services pour un délai de 30 jours (les noms de domaine ne sont plus opérationnels, le site web et les courriels correspondants sont bloqués).\n")
                                  .append("\n")
                                  .append("Pour de plus amples renseignements concernant cette vérification, nous vous invitons à prendre contact avec ce dernier ou à défaut avec la société auprès de laquelle vous avez souscrit ces enregistrements.\n")
                                  .append("\n")
                                  .append("Nous attirons votre attention sur le fait que si aucune pièce justificative ne nous parvenait avant la fin de ce nouveau délai, l'ensemble des noms de domaine  listé fera l'objet d'une suppression. \n")
                                  .append("\n")
                                  .append("Tous les noms de domaines retomberont dans le domaine public et seront disponibles  à l'enregistrement.\n")
                                  .append("\n")
                                  .append("Notre support est à votre disposition si nécessaire à l'adresse suivante : support@afnic.fr ou par téléphone au 01 39 30 83 00.\n")
                                  .toString();
    }

    @Override
    protected String buildEnContent() throws ServiceException {
        return new StringBuilder()
                                  .append("You are being sent this e-mail for information purposes.\n")
                                  .append("\n")
                                  .append("We are contacting you again regarding the verification procedure which was notified to you by e-mail.\n")
                                  .append("\n")
                                  .append("We remind you that the data are referenced under the AFNIC NIC HANDLE " + this.getHolderNicHandle() + ".\n")
                                  .append("\n")
                                  .append("We have not received any supporting evidence from the registrar " + this.getCustomerName()
                                          + " in charge of the domain name(s) within the given time limit.\n")
                                  .append("\n")
                                  .append("As a result, the domain names (listed at the end of this email) are now blocked by our services for a period of 30 days (the domain names are no longer operational, and the corresponding website and e-mail addresses have been blocked).\n")
                                  .append("\n")
                                  .append("For more information about the verification, please contact the registrar or company through which the registration of the domain name(s) in question was performed.\n")
                                  .append("\n")
                                  .append("Your attention is drawn to the fact that if no supporting evidence is received before the end of this further period, the domain name(s) listed below will be deleted.\n")
                                  .append("\n")
                                  .append("The domain name(s) will fall into the public domain and will be available for registration.\n")
                                  .append("\n")
                                  .append("If required, our support service is available at the following address: support@afnic.fr or by telephone at +33 (0)1 39 30 83 00.\n")
                                  .toString();
    }

    @Override
    protected String buildEndEmail() throws ServiceException {
        return new StringBuilder().append(this.getSeparatorEmailContent())
                                  .append(this.getDomainPortfolioContentList()).toString();
    }
}
