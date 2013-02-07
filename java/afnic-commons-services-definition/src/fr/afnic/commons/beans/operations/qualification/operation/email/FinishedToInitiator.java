package fr.afnic.commons.beans.operations.qualification.operation.email;

import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class FinishedToInitiator extends QualificationEmailTemplate {

    public FinishedToInitiator(UserId userId, TldServiceFacade tld) {
        super(OperationType.NotifyEmailFinishedToInitiator,
              QualificationEmailTemplateDestinary.Initiator, userId, tld);
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "AFNIC - AVIS DE CLOTURE / NOTICE OF CLOSURE - " + this.getHolderNicHandle();
    }

    @Override
    protected String buildFrContent() throws ServiceException {

        return new StringBuilder().append("Nous faisons suite à la demande de vérification concernant le Nic Handle " + this.getHolderNicHandle() + ".\n")
                                  .append("La conformité des enregistrements ayant été justifiée par les pièces communiquées, nous vous informons que nous clôturons ce dossier, ce jour.\n")
                                  .append("Nous restons à votre disposition pour tout autre renseignement.\n")
                                  .toString();
    }

    @Override
    protected String buildEnContent() throws ServiceException {
        return new StringBuilder().append("We are following up on the verification request for Nic Handle " + this.getHolderNicHandle() + ".\n")
                                  .append("Since the compliance of the registration has been proven by the documents transmitted, we hereby inform you that this case has been closed.\n")
                                  .append("We remain at your disposal for any further information.\n")
                                  .toString();
    }

}
