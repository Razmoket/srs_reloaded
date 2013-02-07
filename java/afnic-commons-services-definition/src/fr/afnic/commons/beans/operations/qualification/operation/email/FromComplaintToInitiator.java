package fr.afnic.commons.beans.operations.qualification.operation.email;

import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class FromComplaintToInitiator extends QualificationEmailTemplate {

    public FromComplaintToInitiator(UserId userId, TldServiceFacade tld) {
        super(OperationType.NotifyEmailFromComplaintToInitiator,
              QualificationEmailTemplateDestinary.Initiator, userId, tld);
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "AFNIC - Accusé de réception de plainte";
    }

    @Override
    protected String buildFrContent() throws ServiceException {
        return new StringBuilder().append("Nous accusons réception de votre plainte concernant le Nic Handle " + this.getHolderNicHandle() + "\n")
                                  .append("\n")
                                  .append("Nous allons procéder à l'étude de votre plainte et ne manquerons pas de vous tenir informé des suites de cette demande.\n")
                                  .toString();
    }

    @Override
    protected String buildEnContent() throws ServiceException {
        return new StringBuilder().append("We acknowledge receipt of your complaint about the Nic Handle " + this.getHolderNicHandle() + "\n")
                                  .append("\n")
                                  .append("We are going to study your complaint and won't miss to keep you informed on the following up of this demand.\n")
                                  .toString();
    }

}
