package fr.afnic.commons.beans.operations.qualification.operation.email;

import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class StartValorizationToInitiator extends ValorizationEmailTemplate {

    public StartValorizationToInitiator(UserId userId, TldServiceFacade tld) {
        super(OperationType.NotifyEmailStartValorizationToInitiator,
              QualificationEmailTemplateDestinary.Initiator, userId, tld);
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "[AFNIC Qualification] Votre demande de vérification / Your verification request";
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
    protected String buildFrContent() throws ServiceException {
        return new StringBuilder().append("[English version below]\n")
                                  .append("\n")
                                  .append("Bonjour,\n")
                                  .append("\n")
                                  .append("Nous avons reçu une demande de votre part concernant le titulaire de nom de domaine " + this.getHolderNicHandle() + " (référence AFNIC) pour "
                                          + this.getDomainName()
                                          + ".\n")
                                  .append("L'AFNIC vous informe que suite à cette demande une procédure de vérification de l'éligibilité et de la joignabilité du titulaire a été ouverte.\n")
                                  .append("\n")
                                  .append("Nous vous tiendrons informé de l'issue de la vérification dans les meilleurs délais.\n")
                                  .append("\n")
                                  .append("Notre support se tient à votre disposition pour tout complément d'information, par téléphone au +33 1 39 30 83 00 ou par email à support@afnic.fr.\n")
                                  .toString();
    }

    @Override
    protected String getFrEndEmail() {
        return new StringBuilder()
                                  .append("\nBien cordialement,\n")
                                  .append("Le Service Client\n")
                                  .append("AFNIC\n")
                                  .toString();
    }

    @Override
    protected String buildEnContent() throws ServiceException {
        return new StringBuilder().append("Dear requester,\n")
                                  .append("\n")
                                  .append("We have received your request regarding the registrant " + this.getHolderNicHandle() + " (AFNIC reference) for " + this.getDomainName()
                                          + ".\n")
                                  .append("AFNIC informs you that the verification procedure has been initiated regarding the eligibility and the reachability of the registrant.\n")
                                  .append("\n")
                                  .append("We will keep you informed of the outcome as soon as possible.\n")
                                  .append("\n")
                                  .append("May you require any additional information regarding the ongoing procedure, please contact our customer support by phone at +33 1 39 30 83 00 or by email to support@afnic.fr.\n")
                                  .toString();
    }

    @Override
    protected String getEnEndEmail() {
        return new StringBuilder()
                                  .append("\nBest regards,\n")
                                  .append("AFNIC Customer Service")
                                  .toString();
    }
}
