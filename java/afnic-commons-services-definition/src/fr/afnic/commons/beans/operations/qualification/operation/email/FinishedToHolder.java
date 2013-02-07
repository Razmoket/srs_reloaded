package fr.afnic.commons.beans.operations.qualification.operation.email;

import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class FinishedToHolder extends QualificationEmailTemplate {

    public FinishedToHolder(UserId userId, TldServiceFacade tld) {
        super(OperationType.NotifyEmailFinishedToHolder,
              QualificationEmailTemplateDestinary.Holder, userId, tld);
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "AFNIC - AVIS DE CLOTURE / NOTICE OF CLOSURE - " + this.getHolderName() + " " + this.getHolderNicHandle();
    }

    @Override
    protected String buildFrContent() throws ServiceException {
        return new StringBuilder().append("Ce courriel vous est adressé à titre d'information.\n")
                                  .append("\n")
                                  .append("Nous revenons vers vous au sujet de la procédure de vérification vous concernant (cf courriel du " + this.getQualificationCreateDate() + ")\n")
                                  .append("Pour rappel, ces données étaient référencées sous le NIC HANDLE AFNIC=" + this.getHolderNicHandle() + " \n")
                                  .append("\n")
                                  .append("Ayant reçu de la part du  bureau d'enregistrement " + this.getCustomerName() + " en charge des noms de domaine, les pièces justifiant des éléments saisis, ")
                                  .append("nous vous informons que nous procédons à  la clôture du dossier.\n")
                                  .append("\n")
                                  .append("Les  noms de domaine dont la liste a été jointe au courriel visé en référence sont de nouveau opérationnels.\n")
                                  .append("\n")
                                  .append("Notre support est à votre disposition si nécessaire à l'adresse suivante : support@afnic.fr ou par téléphone au 01 39 30 83 00.")
                                  .toString();
    }

    @Override
    protected String buildEnContent() throws ServiceException {
        return new StringBuilder().append("You are being sent this e-mail for information purposes..\n")
                                  .append("\n")
                                  .append("We are contacting you again regarding the verification procedure which was notified to you by e-mail on " + this.getQualificationCreateDate() + ")\n")
                                  .append("We remind you that the data are referenced under the AFNIC NIC HANDLE" + this.getHolderNicHandle() + " \n")
                                  .append("\n")
                                  .append(" Since we have received from your Registrar " + this.getCustomerName()
                                          + ", the documents proving the information provided, we hereby inform you that this case has been closed.")
                                  .append("\n")
                                  .append("The domain name(s) listed in the email referenced above is/are once again operational.\n")
                                  .append("\n")
                                  .append("If required, our support service is available at the following address: support@afnic.fr or by telephone at +33 (0)1 39 30 83 00.")
                                  .toString();

    }

}
