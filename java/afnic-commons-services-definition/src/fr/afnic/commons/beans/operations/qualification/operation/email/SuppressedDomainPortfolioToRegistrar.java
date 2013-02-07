package fr.afnic.commons.beans.operations.qualification.operation.email;

import java.util.List;

import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class SuppressedDomainPortfolioToRegistrar extends QualificationEmailTemplate {

    public SuppressedDomainPortfolioToRegistrar(UserId userId, TldServiceFacade tld) {
        super(OperationType.NotifyEmailSuppressedDomainPortfolioToRegistrar,
              QualificationEmailTemplateDestinary.Registrar, userId, tld);
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "AFNIC - Qualification " + this.getHolderNicHandle() + " - Suppression de portefeuille de domaines";
    }

    @Override
    protected String buildFrContent() throws ServiceException {
        return new StringBuilder()
                                  .append("Nous revenons vers vous concernant la demande de vérification que nous vous avons fait parvenir par courriel.\n")
                                  .append("\n")
                                  .append("Aucune pièce n'ayant été transmise pour justifier des informations saisies, nous vous informons que le portefeuille de noms de domaine relatif aux informations ci-dessous est supprimé:\n")
                                  .append("HOLDER=" + this.getHolderNicHandle() + "\n")
                                  .append("STATUS=problem" + "\n")
                                  .append(this.listDomain())
                                  .append("\n")
                                  .append("\n")
                                  .append("Nous restons à votre disposition pour tout autre renseignement.\n")
                                  .toString();
    }

    @Override
    protected String buildEnContent() throws ServiceException {
        return new StringBuilder()
                                  .append("We are contacting you again regarding the request for verification which we sent you by e-mail.\n")
                                  .append("\n")
                                  .append("Since no document was sent as proof of the information provided, we hereby inform you that we are deleting the domain name portfolio attached to the following informations:\n")
                                  .append("HOLDER=" + this.getHolderNicHandle() + "\n")
                                  .append("STATUS=problem" + "\n")
                                  .append(this.listDomain())
                                  .append("\n")
                                  .append("\n")
                                  .append("We remain at your disposal for any further information.\n")
                                  .toString();
    }

    protected String listDomain() throws ServiceException {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> domains = AppServiceFacade.getDomainService().getDomainNamesWithHolderHandle(this.getHolderNicHandle(), this.userIdCaller, this.tldCaller);

        for (String domain : domains) {
            stringBuilder.append("DOMAIN=" + domain + this.separator);
        }

        return stringBuilder.toString();
    }
}
