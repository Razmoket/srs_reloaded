package fr.afnic.commons.beans.operations.qualification.operation.email;

import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class FinishedFromComplaintToInitiator extends QualificationEmailTemplate {

    public FinishedFromComplaintToInitiator(UserId userId, TldServiceFacade tld) {
        super(OperationType.NotifyEmailFinishedFromComplaintToInitiator,
              QualificationEmailTemplateDestinary.Initiator, userId, tld);
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "AFNIC - Avis de clôture de la plainte";
    }

    @Override
    protected String buildFrContent() throws ServiceException {
        return new StringBuilder().append("Nous faisons suite à votre plainte concernant le Nic Handle " + this.getHolderNicHandle() + ".\n")
                                  .append("\n")
                                  .append("Après étude de votre dossier, nous vous informons que votre plainte ne peut pas être prise en compte par le service juridique de l'AFNIC.\n")
                                  .append("\n")
                                  .append("Pour plus d'information n'hésitez pas à contacter notre support au 01.39.30.83.00\n")
                                  .toString();
    }

    @Override
    protected String buildEnContent() throws ServiceException {
        return new StringBuilder().append("We are following up on your complaint about the Nic Handle " + this.getHolderNicHandle() + ".\n")
                                  .append("\n")
                                  .append("After studying your case, we inform you that your complaint can't be taken into account by AFNIC's legal service.\n")
                                  .append("\n")
                                  .append("For more information you can contact our hotline on 01.39.30.83.00\n")
                                  .toString();
    }

}
