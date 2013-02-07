package fr.afnic.commons.beans.operations.qualification.operation.email;

import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class ProblemBisToRegistrar extends QualificationEmailTemplate {

    public ProblemBisToRegistrar(UserId userId, TldServiceFacade tld) {
        super(OperationType.NotifyEmailProblemBisToRegistrar,
              QualificationEmailTemplateDestinary.Registrar, userId, tld);
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "AFNIC - Qualification " + this.getHolderNicHandle() + " – STATUS=problem";
    }

    @Override
    protected String buildFrContent() throws ServiceException {
        return new StringBuilder()
                                  .append("Conformément aux dispositions de la charte de nommage, dès lors où elle se trouve en possession d'éléments contradictoires, l'AFNIC est habilitée à demander la communication de pièces établissant le respect par le titulaire des critères :\n")
                                  .append("\n")
                                  .append("   -  d'identité, \n")
                                  .append("   -  d'éligibilité (adresse postale en Europe  pour le lieu de résidence, raison sociale et/ou identifiant dans un pays /territoire éligible) et, \n")
                                  .append("   -  de joignabilité (courriel et/ou téléphone)\n")
                                  .append("\n")
                                  .append("\n")
                                  .append("Nous vous prions de bien vouloir nous communiquer toutes les pièces pouvant justifier de ces éléments. \n")
                                  .append("\n")
                                  .append("Dans l'attente des documents, nous vous informons que le portefeuille de noms de domaine relatif aux informations suivantes est gelé pour une période de 30 jours:\n")
                                  .append("HOLDER=" + this.getHolderNicHandle() + "\n")
                                  .append("STATUS=problem" + "\n")
                                  .append("\n")
                                  .append("Sans nouvelle de votre part dans ce délai, le portefeuille de noms de domaine sera bloqué pour une nouvelle période de 30 jours, avant sa suppression. \n")
                                  .append("\n")
                                  .append("Nous restons à votre disposition pour de plus amples renseignements.\n")
                                  .toString();
    }

    @Override
    protected String buildEnContent() throws ServiceException {
        return new StringBuilder()
                                  .append("In accordance with the stipulations of the Naming Policy, if AFNIC holds contradictory documents it is authorised to request that further documents be provided to determine that the holder respects the criteria of:\n")
                                  .append("\n")
                                  .append("   - identity, \n")
                                  .append("   - eligibility (i.e. postal address in Europe, postal address for the place of residence, business and/or login in an eligible country or territory), and \n")
                                  .append("   - reachability (i.e. e-mail and/or telephone).\n")
                                  .append("\n")
                                  .append("\n")
                                  .append("Please send us all documents you have in support of these items. \n")
                                  .append("\n")
                                  .append("Pending receipt of these documents, please note that the domain name portfolio attached to the following informations is suspended for a period of 30 days:\n")
                                  .append("HOLDER=" + this.getHolderNicHandle() + "\n")
                                  .append("STATUS=problem" + "\n")
                                  .append("\n")
                                  .append("If we do not receive a reply within this period of time, the domain name portfolio will be blocked for a new period of 30 days, before being deleted. \n")
                                  .append("\n")
                                  .append("We remain at your disposal for any further information.\n")
                                  .toString();
    }
}
