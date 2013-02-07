package fr.afnic.commons.beans.operations.qualification.operation.email;

import fr.afnic.commons.beans.application.env.Environnement;
import fr.afnic.commons.beans.mail.EmailFormat;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class AutoMailReachabilityToHolder extends ValorizationEmailTemplate {

    public AutoMailReachabilityToHolder(UserId userId, TldServiceFacade tld) {
        super(OperationType.NotifyAutoMailReachabilityToHolder,
              QualificationEmailTemplateDestinary.Holder, "<br>", userId, tld);
        this.setFormat(EmailFormat.HTML);
    }

    @Override
    protected String buildEndSubject() throws ServiceException {

        return "IMPORTANT Votre nom de domaine " + this.getDomainName() + " / Your domain name " + this.getDomainName();
    }

    @Override
    protected String buildFrContent() throws ServiceException {
        String urlFr = this.getUrlFr() + AppServiceFacade.getQualificationService().getQualificationCheckAutoReachability(this.getQualification(), this.userIdCaller, this.tldCaller);
        return new StringBuilder().append("[English version below]" + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("Cher titulaire du nom de domaine <a href=\"" + this.getDomainName() + "\">" + this.getDomainName() + "</a>,"
                                          + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("Ce message a pour but de vérifier que l'adresse courriel associée à votre nom de domaine est correcte." + this.getSeparator())
                                  .append("La fiabilité de cette donnée est essentielle pour pouvoir vous contacter en cas de problème ou pour certaines opérations sur votre nom de domaine. Maintenir des données exactes est également une obligation légale. Plus de détails sont disponibles dans la <a href=\"http://www.afnic.fr/fr/ressources/documents-de-reference/chartes/charte-de-nommage-applicable-a-partir-du-6-decembre-2011-6.html\">charte de nommage AFNIC</a>."
                                                  + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("Pour répondre à cette vérification et ainsi confirmer à l'AFNIC que l'adresse courriel associée à votre nom de domaine est valide, il vous suffit de cliquer sur le lien ci-dessous :"
                                                  + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("<a href=\"" + urlFr + "\">")
                                  .append(urlFr + "</a>"
                                          + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("Nous vous invitons à effectuer la démarche de validation de votre adresse courriel dès à présent, le lien à cliquer a une durée de validité de 15 jours."
                                          + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("Vous ne connaissez pas l'AFNIC?" + this.getSeparator())
                                  .append("L'AFNIC (Association Française pour le Nommage Internet en Coopération) est l'organisme désigné par l'État français pour la gestion des noms de domaine sous les extensions géographiques françaises suivantes : .fr, .re, .pm, .yt, .tf et .wf. Cette délégation est formalisée par une convention consultable à l'adresse suivante : <a href=\"http://www.afnic.fr/fr/ressources/documents-de-reference/cadre-legal/\">http://www.afnic.fr/fr/ressources/documents-de-reference/cadre-legal/</a>"
                                                  + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("C'est auprès de l'AFNIC que votre bureau d'enregistrement ou le prestataire qui gère votre site web a enregistré le nom de domaine <a href=\""
                                          + this.getDomainName() + "\">"
                                          + this.getDomainName() + "</a>." + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("Vous souhaitez en savoir plus :" + this.getSeparator())
                                  .append("-   sur l'AFNIC : <a href=\"www.afnic.fr\">www.afnic.fr</a>," + this.getSeparator())
                                  .append("-   sur le cadre légal applicable à l'enregistrement des noms de domaine sous les extensions gérées par l'AFNIC : <a href=\"http://www.afnic.fr/fr/ressources/documents-de-reference/cadre-legal/\">http://www.afnic.fr/fr/ressources/documents-de-reference/cadre-legal/</a>"
                                                  + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("Vous souhaitez nous contacter pour vérifier la provenance du présent courriel : notre support est joignable au +33 1 39 30 83 00 ou par courriel à <a href=\"\">support@afnic.fr</a>."
                                                  + this.getSeparator())
                                  .append("Vous pouvez également contacter votre bureau d'enregistrement ou votre prestataire informatique pour tout complément d'information." + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("Bien cordialement," + this.getSeparator())
                                  .append("Le Service Client" + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("<a href=\"support@afnic.fr\">support@afnic.fr</a>" + this.getSeparator())
                                  .append("Tél +33 (0) 1 39 30 83 00" + this.getSeparator())
                                  .append("Fax +33 (0) 1 39 30 83 01" + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("Immeuble International" + this.getSeparator())
                                  .append("78181 Saint Quentin en Yvelines Cedex" + this.getSeparator())
                                  .append("France" + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("<a href=\"\">www.afnic.fr</a>" + this.getSeparator())
                                  .toString();
    }

    @Override
    protected String buildEnContent() throws ServiceException {
        String urlEn = this.getUrlEn()
                       + AppServiceFacade.getQualificationService().getQualificationCheckAutoReachability(this.getQualification(), this.userIdCaller, this.tldCaller);
        return new StringBuilder().append("Dear holder of <a href=\"" + this.getDomainName() + "\">" + this.getDomainName() + "</a>," + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("The aim of this message is to verify that the email address linked to your domain name is valid." + this.getSeparator())
                                  .append("It is indeed crucial that this data is reliable so that you can be contacted in the event of an issue with your domain or for operations on your domain."
                                          + this.getSeparator())
                                  .append("Keeping your data correct and updated is a legal oligation. Get more details in the <a href=\"http://www.afnic.fr/en/ressources/reference/charters/\">AFNIC's Naming Charter</a>."
                                                  + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("To answer to our verification request and confirm the validity of the email address linked to your doamin name, please click on the below link :"
                                          + this.getSeparator())
                                  .append("<a href=\"" + urlEn + "\">")
                                  .append(urlEn + "</a>" + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("We advise that you do it as soon as possible, as the link is only valid for 15 days." + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("You don't know AFNIC?" + this.getSeparator())
                                  .append("AFNIC (Association Française pour le Nommage Internet en Coopération) is the organization designated by the French State for managing the domain names under the following French geographical extensions : .fr, .re, .pm, .yt, .tf and .wf."
                                                  + this.getSeparator())
                                  .append("Details about AFNIC's legal framework : <a href=\"http://www.afnic.fr/en/ressources/reference/legal-framework/\">http://www.afnic.fr/en/ressources/reference/legal-framework/</a>"
                                                  + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("Your registrar or your web site provider has registered <a href=\"" + this.getDomainName() + "\">" + this.getDomainName()
                                          + "</a> with AFNIC." + this.getSeparator())
                                  .append("To learn more about AFNIC : <a href=\"www.afnic.fr\">www.afnic.fr</a>" + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("If you wish to verify that we are the actual sender of the present email, you can contact our customer support by phone at +33 1 39 30 83 00 or by email to <a href=\"support@afnic.fr\">support@afnic.fr</a>."
                                                  + this.getSeparator())
                                  .append("You may also contact your registrar or your web site provider to get additional information regarding AFNIC." + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("Best regards," + this.getSeparator())
                                  .append("Customer Service" + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("<a href=\"support@afnic.fr\">support@afnic.fr</a>" + this.getSeparator())
                                  .append("Tel +33 (0) 1 39 30 83 00" + this.getSeparator())
                                  .append("Fax +33 (0) 1 39 30 83 01" + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("Immeuble International" + this.getSeparator())
                                  .append("78181 Saint Quentin en Yvelines Cedex" + this.getSeparator())
                                  .append("France" + this.getSeparator())
                                  .append(this.getSeparator())
                                  .append("<a href=\"www.afnic.fr\">www.afnic.fr</a>" + this.getSeparator())
                                  .toString();
    }

    private String getUrlFr() {
        if (AppServiceFacade.getApplicationService().isEnv(Environnement.Prod)) {
            return "http://www.afnic.fr/fr/valorisation/";
        }
        if (AppServiceFacade.getApplicationService().isEnv(Environnement.Preprod)) {
            return "http://www-preprod.nic.fr/fr/valorisation/";
        }
        if (AppServiceFacade.getApplicationService().isEnv(Environnement.Sandbox)) {
            return "http://www.sandbox.afnic.fr/fr/valorisation/";
        }
        return "http://projetweb2-front.dev.nic.fr/en/valorisation/";
    }

    private String getUrlEn() {
        if (AppServiceFacade.getApplicationService().isEnv(Environnement.Prod)) {
            return "http://www.afnic.fr/en/valorisation/";
        }
        if (AppServiceFacade.getApplicationService().isEnv(Environnement.Preprod)) {
            return "http://www-preprod.nic.fr/en/valorisation/";
        }
        if (AppServiceFacade.getApplicationService().isEnv(Environnement.Sandbox)) {
            return "http://www.sandbox.afnic.fr/en/valorisation/";
        }
        return "http://projetweb2-front.dev.nic.fr/en/valorisation/";
    }

}
