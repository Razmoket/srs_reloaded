package fr.afnic.commons.beans.operations.qualification.operation.email;

import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class ProblemToHolder extends QualificationEmailTemplate {

    public ProblemToHolder(UserId userId, TldServiceFacade tld) {
        super(OperationType.NotifyEmailProblemToHolder,
              QualificationEmailTemplateDestinary.Holder, userId, tld);
    }

    @Override
    protected String buildEndSubject() throws ServiceException {
        return "AFNIC - AVIS DE TRANSMISSION DE PIECES / NOTICE OF DOCUMENT TRANSMISSION -  " + this.getHolderNicHandle();
    }

    @Override
    protected String buildFrContent() throws ServiceException {
        return new StringBuilder()
                                  .append("Ce courriel vous est adressé à titre d'information. \n")
                                  .append("\n")
                                  .append("L'AFNIC est l'office d'enregistrement qui est en charge de l'attribution et de la gestion des noms de domaine internet sous les extensions .fr, .re, .tf, .wf, .pm, .yt.\n")
                                  .append("\n")
                                  .append("Vous faites l'objet d'une vérification des données d'identification que vous avez fourni lors de l'enregistrement de noms de domaine sous une de ces extensions.\n")
                                  .append("\n")
                                  .append("Ces données sont référencées sous le NIC HANDLE AFNIC " + this.getHolderNicHandle() + ".\n")
                                  .append("\n")
                                  .append("Vous trouverez en fin de courriel la liste des noms de domaine impactés par cette vérification qui ont d'ores et déjà fait l'objet d'un gel par nos services. (Les noms de domaines sont opérationnels mais aucune opérations techniques ne peut être traitées, et notamment changement de bureau d'enregistrement, transmission etc..).\n")
                                  .append("\n")
                                  .append("Nous avons pris contact avec le bureau d'enregistrement " + this.getCustomerName()
                                          + " en charge de ces noms de domaine dont vous trouverez les coordonnées sur le site web de l'AFNIC www.afnic.fr.\n")
                                  .append("\n")
                                  .append("Ce dernier doit nous communiquer sous 30 jours,  les pièces justifiant des éléments transmis lors des enregistrements (existence, adresse, courriel et téléphone).\n")
                                  .append("\n")
                                  .append("Pour de plus amples renseignements concernant cette vérification, nous vous invitons à prendre contact avec ce dernier ou à défaut avec la société auprès de laquelle vous avez souscrit ces enregistrements.\n")
                                  .append("\n")
                                  .append("Notre support est à votre disposition si nécessaire à l'adresse suivante : support@afnic.fr ou par téléphone au 01 39 30 83 00.\n")
                                  .toString();
    }

    @Override
    protected String buildEnContent() throws ServiceException {
        return new StringBuilder().append("You are being sent this e-mail for information purposes. \n")
                                  .append("\n")
                                  .append("AFNIC is the registry responsible for the allocation and management of Internet domain names under the .fr, .re, .tf, .wf, .pm, and .yt extensions.\n")
                                  .append("\n")
                                  .append("The identification data you provided when registering one or more domain names under one of these extensions are being checked.\n")
                                  .append("\n")
                                  .append("The data are referenced under the AFNIC NIC HANDLE " + this.getHolderNicHandle() + ".\n")
                                  .append("\n")
                                  .append("At the end of this e-mail you will find the list of domain names affected by this verification. Please note that they have already been suspended (i.e. the domain names are operational but no technical operations can be processed, including a change of registrar, transfer etc.).\n")
                                  .append("\n")
                                  .append("We have contacted the registrar " + this.getCustomerName()
                                          + " in charge of the domain name(s); their contact details can be found on the AFNIC website www.afnic.fr.\n")
                                  .append("\n")
                                  .append("The registrar must provide us within 30 calendar days with supporting evidence of the items transmitted during the registration of the domain name (i.e. proof of identity, address, e-mail and telephone).\n")
                                  .append("\n")
                                  .append("For further information about this verification, please contact the registrar or company through which the registration of the domain name(s) in question was performed.\n")
                                  .append("\n")
                                  .append("If required, our support service is available at the following address: support@afnic.fr or by telephone at +33 (0)1 39 30 83 00.\n")
                                  .toString();
    }

    @Override
    protected String buildEndEmail() throws ServiceException {
        return new StringBuilder().append(this.getSeparatorEmailContent())
                                  .append(this.getDomainPortfolioContentList()).toString();
    }

}
