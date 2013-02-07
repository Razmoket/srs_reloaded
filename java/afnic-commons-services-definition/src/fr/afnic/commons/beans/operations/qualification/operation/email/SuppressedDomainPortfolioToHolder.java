package fr.afnic.commons.beans.operations.qualification.operation.email;

import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class SuppressedDomainPortfolioToHolder extends QualificationEmailTemplate {

    public SuppressedDomainPortfolioToHolder(UserId userId, TldServiceFacade tld) {
        super(OperationType.NotifyEmailSuppressedDomainPortfolioToHolder,
              QualificationEmailTemplateDestinary.Holder, userId, tld);
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "AFNIC - Information sur les suites de la procédure de vérification vous concernant";
    }

    @Override
    protected String buildFrContent() throws ServiceException {
        return new StringBuilder()
                                  .append("Ce courriel vous est adressé à titre d'information. \n")
                                  .append(" \n")
                                  .append("Nous revenons vers vous au sujet de la procédure de vérification vous concernant.\n")
                                  .append(" \n")
                                  .append("Pour rappel, ces données sont référencées sous le NIC HANDLE AFNIC " + this.getHolderNicHandle() + ".\n")
                                  .append(" \n")
                                  .append("Nous n'avons reçu aucune pièce justificative de la part du  bureau d'enregistrement " + this.getCustomerName()
                                          + " en charge des noms de domaine dans le délai imparti. \n")
                                  .append(" \n")
                                  .append("En conséquence, les noms de domaine dont la liste vous a été fournie dans le courriel cité en référence ont fait l'objet d'une suppression par nos services. \n")
                                  .append(" \n")
                                  .append("Ces noms de domaine sont donc disponibles à l'enregistrement par des tiers selon la règle du premier arrivé, premier servi.\n")
                                  .append(" \n")
                                  .append("Pour de plus amples renseignements nous vous invitons à prendre contact avec le bureau d'enregistrement ou avec la société auprès de laquelle vous avez souscrit ces enregistrements.\n")
                                  .append("Nous  procédons à la clôture du dossier.\n")
                                  .append("\n")
                                  .append("Notre support est à votre disposition si nécessaire à l'adresse suivante : support@afnic.fr ou par téléphone au 01 39 30 83 00.\n")
                                  .toString();
    }

    @Override
    protected String buildEnContent() throws ServiceException {
        return new StringBuilder().append("You are being sent this e-mail for information purposes. \n")
                                  .append("\n")
                                  .append("We are contacting you again regarding the verification procedure which was notified to you by e-mail.\n")
                                  .append("\n")
                                  .append("We remind you that the data are referenced under the AFNIC NIC HANDLE " + this.getHolderNicHandle() + ".\n")
                                  .append("\n")
                                  .append("We have not received any supporting evidence from the registrar " + this.getCustomerName()
                                          + " in charge of the domain name(s) within the given time limit. \n")
                                  .append("\n")
                                  .append("As a result, the domain name(s) listed in the email referenced above has/have been deleted by our services. \n")
                                  .append("\n")
                                  .append("This/these domain name(s) is/are available for registration by third parties on a \"first come, first served\".\n")
                                  .append("\n")
                                  .append("For further information, please contact the registrar or company through which the registration of the domain name(s) in question was performed.\n")
                                  .append("The file has now been closed.\n")
                                  .append(" \n")
                                  .append("If required, our support service is available at the following address: support@afnic.fr or by telephone at +33 (0)1 39 30 83 00.\n")
                                  .toString();
    }

}
