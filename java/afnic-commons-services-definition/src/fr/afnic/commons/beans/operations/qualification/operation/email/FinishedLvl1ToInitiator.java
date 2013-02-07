package fr.afnic.commons.beans.operations.qualification.operation.email;

import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class FinishedLvl1ToInitiator extends ValorizationEmailTemplate {

    public FinishedLvl1ToInitiator(UserId userId, TldServiceFacade tld) {
        super(OperationType.NotifyEmailFinishedLvl1ToInitiator,
              QualificationEmailTemplateDestinary.Initiator, userId, tld);
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "[AFNIC Qualification] Votre demande de vérification / Your verification request";
    }

    @Override
    protected String buildFrContent() throws ServiceException {

        return new StringBuilder().append("[English version below]\n")
                                  .append("\n")
                                  .append("Bonjour,\n")
                                  .append("\n")
                                  .append("L'AFNIC vous informe que les vérifications effectuées sur votre demande ont permis de confirmer l'éligibilité et la joignabilité du titulaire de nom de domaine, "
                                                  + this.getHolderNicHandle() + " (référence AFNIC).\n")
                                  .append("\n")
                                  .append("Nous avons mis à jour la base Whois, que vous pouvez consulter depuis notre site www.afnic.fr, et avons clos le dossier.\n")
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
                                  .append("AFNIC informs you that the verification performed upon your request has confirmed the eligibility and the reachability of the registrant, "
                                          + this.getHolderNicHandle() + " (AFNIC reference).\n")
                                  .append("\n")
                                  .append("We have updated the Whois database accordingly as you can check on our web site, www.afnic.fr, and have therefore closed your request file.\n")
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
