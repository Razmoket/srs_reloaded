package fr.afnic.commons.beans.operations.qualification.operation.email;

import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class ProblemToInitiator extends QualificationEmailTemplate {

    public ProblemToInitiator(UserId userId, TldServiceFacade tld) {
        super(OperationType.NotifyEmailProblemToInitiator,
              QualificationEmailTemplateDestinary.Initiator, userId, tld);
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "AFNIC - AVIS DE TRANSMISSION DE PIECES / NOTICE OF DOCUMENT TRANSMISSION -  " + this.getHolderNicHandle();
    }

    @Override
    protected String buildFrContent() throws ServiceException {
        return new StringBuilder().append("Nous accusons réception de votre demande de vérification concernant  le titulaire " + this.getHolderName() + " " + this.getHolderNicHandle() + ".\n")
                                  .append("\n")
                                  .append("Nous vous informons que votre demande est recevable et est prise en charge par le service juridique.\n")
                                  .append("\n")
                                  .append("Nous ne manquerons pas de vous tenir informé des suites de cette demande.\n")
                                  .toString();
    }

    @Override
    protected String buildEnContent() throws ServiceException {
        return new StringBuilder().append("We hereby acknowledge receipt of your request to verify the holder " + this.getHolderName() + " " + this.getHolderNicHandle() + ".\n")
                                  .append("\n")
                                  .append("We hereby inform you that your request has been accepted, and is being investigated by Legal Affairs.\n")
                                  .append("\n")
                                  .append("We will keep you informed of any follow-up to this request.\n")
                                  .toString();
    }

}
