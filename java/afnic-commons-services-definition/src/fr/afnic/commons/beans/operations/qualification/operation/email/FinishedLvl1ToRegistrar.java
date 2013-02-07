package fr.afnic.commons.beans.operations.qualification.operation.email;

import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class FinishedLvl1ToRegistrar extends ValorizationEmailTemplate {

    public FinishedLvl1ToRegistrar(UserId userId, TldServiceFacade tld) {
        super(OperationType.NotifyEmailFinishedLvl1ToRegistrar,
              QualificationEmailTemplateDestinary.Registrar, userId, tld);
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "AFNIC - Qualification " + this.getHolderNicHandle() + " – STATUS=finished";
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
                                  .append("L'AFNIC vous informe que la procédure de qualification s'est terminée positivement pour :\n")
                                  .append("HOLDER=" + this.getHolderNicHandle() + "\n")
                                  .append("STATUS=finished\n")
                                  .append("ELIG=" + this.getQualification().computePublicEligibilityStatus() + "\n")
                                  .append("REACH=" + this.getQualification().computePublicReachStatus() + "\n")
                                  .append("\n")
                                  .append("La base Whois a été mise à jour.\n")
                                  .append("\n")
                                  .append("Pour information le portefeuille du titulaire comprend les noms de domaine listés à la fin de ce message.\n")
                                  .append("\n")
                                  .append("Votre chargé de clientèle se tient à votre disposition pour tout complément d'information.\n")
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
        return new StringBuilder().append("Dear registrar,\n")
                                  .append("\n")
                                  .append("AFNIC informs you that the qualification procedure has successfully ended for :\n")
                                  .append("HOLDER=" + this.getHolderNicHandle() + "\n")
                                  .append("STATUS=finished\n")
                                  .append("ELIG=" + this.getQualification().computePublicEligibilityStatus() + "\n")
                                  .append("REACH=" + this.getQualification().computePublicReachStatus() + "\n")
                                  .append("\n")
                                  .append("The Whois database has been updated accordingly.\n")
                                  .append("\n")
                                  .append("For your information, the domains involved are listed at the bottom of this message.\n")
                                  .append("\n")
                                  .append("May you require any additional information regarding the qualification procedure, please contact your customer service representative.\n")
                                  .toString();

    }

    @Override
    protected String getEnEndEmail() {
        return new StringBuilder()
                                  .append("\nBest regards,\n")
                                  .append("AFNIC Customer Service")
                                  .append("\n")
                                  .append("\n")
                                  .append("Liste des noms de domaine concernés / List of concerned domain names :\n")
                                  .toString();
    }

    @Override
    protected String buildEndEmail() throws ServiceException {
        return this.getDomainPortfolioContentList();
    }
}
