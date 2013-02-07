package fr.afnic.commons.beans.operations.qualification.operation.email;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.DateUtils;

public class FinishedToRegistrar extends QualificationEmailTemplate {

    public FinishedToRegistrar(UserId userId, TldServiceFacade tld) {
        super(OperationType.NotifyEmailFinishedToRegistrar,
              QualificationEmailTemplateDestinary.Registrar, userId, tld);
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "AFNIC - Qualification " + this.getHolderNicHandle() + " – STATUS=finished";
    }

    @Override
    protected String buildFrContent() throws ServiceException {

        return new StringBuilder().append("Nous faisons suite à votre courriel " + this.getLastResponseDate() + " dans lequel vous nous communiquez les pièces qui permettent de justifier ")
                                  .append("de la conformité des enregistrements de noms de domaine du Nic handle " + this.getHolderNicHandle() + ".")
                                  .append("La conformité des enregistrements étant justifiée par les pièces transmises,  nous vous informons que nous procédons à  la clôture du dossier ci-dessous référencé:\n")
                                  .append("HOLDER=" + this.getHolderNicHandle() + "\n")
                                  .append("STATUS=finished\n")
                                  .append("ELIG=" + this.getQualification().computePublicEligibilityStatus() + "\n")
                                  .append("REACH=" + this.getQualification().computePublicReachStatus() + "\n")
                                  .append("\n")
                                  .append("Nous restons à votre disposition pour tout autre renseignement.")
                                  .toString();

    }

    @Override
    protected String buildEnContent() throws ServiceException {
        return new StringBuilder().append("We are following up on your e-mail of  " + this.getLastResponseDate() + " in which you communicated to us the documents proving ")
                                  .append("the compliance of the registration domain names of Nic handle " + this.getHolderNicHandle() + ".")
                                  .append("Since the compliance of the registration has been proven by the documents transmitted, we hereby inform you that the case referenced below has been closed:\n")
                                  .append("HOLDER=" + this.getHolderNicHandle() + "\n")
                                  .append("STATUS=finished\n")
                                  .append("ELIG=" + this.getQualification().computePublicEligibilityStatus() + "\n")
                                  .append("REACH=" + this.getQualification().computePublicReachStatus() + "\n")
                                  .append("\n")
                                  .append("We remain at your disposal for any further information.")
                                  .toString();

    }

    private String getLastResponseDate() throws ServiceException {
        List<Operation> operations = this.getQualification().getSubOperations();
        Collections.sort(operations, new Comparator<Operation>() {
            @Override
            public int compare(Operation op1, Operation op2) {
                if (op1 == null) {
                    return 1;
                }

                if (op2 == null) {
                    return -1;
                }

                return op1.getIdAsInt() - op2.getIdAsInt();
            }
        });

        for (Operation operation : this.getQualification().getSubOperations()) {
            if (operation.getType() == OperationType.AttachDocument) {
                return "du " + DateUtils.toDayFormat(operation.getCreateDate());
            }
        }
        return "";
    }
}
