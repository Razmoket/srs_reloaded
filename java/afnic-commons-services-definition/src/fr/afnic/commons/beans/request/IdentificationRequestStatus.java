/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/request/IdentificationRequestStatus.java#10 $
 * $Revision: #10 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.request;

import java.util.Locale;

import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;

/**
 * Liste des statuts que peut prendre une requête d'identification.<br/>
 * Pour plus d'information, consulter le workflow d'identification.
 * 
 * 
 * @version $Revision: #10 $
 * @author $Author: ginguene $
 * 
 */
public enum IdentificationRequestStatus implements RequestStatus {

    /**
     * Statut temporaire utilisé durant la phase ou le robot cherche<br/>
     * à effectuer l'identification.
     * 
     */
    Initialized("Initialisé"),

    /** Un opérateur doit traiter la requete */
    PendingNicUserInput("A traiter"),

    /** Un opérateur a envoyé un mail problème et on n'a pas encore reçu de réponse */
    PendingRegistrarInput("En attente de réponse"),

    /** Un opérateur a envoyé un mail problème et on areçu de réponse */
    PendingRegistrarInputReview("Répondu"),

    /**
     * Statut lorsque l'identification est valide.<br/>
     * Le passage dans ce statut est toujours suivi immédiatement <br/>
     * d'un passage en closed.
     */
    Ok("ok"),

    /** Les domaines du portefeuille du titulaire ont été bloqués */
    DomainsBlocked("Portefeuille bloqué"),

    /**
     * Les domaines du portefeuille du titulaire ont été supprimés.<br/>
     * Le passage dans ce statut est toujours suivi immédiatement <br/>
     * d'un passage en closed.
     */
    DomainsSuppressed("Portefeuille supprimé"),

    /** Statut deprecated, remplacer par Cancelled */
    Deleted("Supprimée"),

    /** Requete non menée à son terme */
    Cancelled("Abandonnée"),

    /** Requete menée à son terme */
    Closed("Fermée"),

    /**
     * Une réponse a été reçue, une fois la requete terminée.<br/>
     * Ce statut est anormal, en théorie, le bureau d'enregistrement n'a pas à répondre<br/>
     * après le traitement de la requete
     */
    PendingRegistrarInputReviewInFinalStatus("Répondu en statut final");

    /** Description française du statut */
    private final String description;

    /**
     * Constructeur privé permettant d'associé une description <br/>
     * en français à chaque statut.
     * 
     * @param description
     *            Description française du statut
     */
    private IdentificationRequestStatus(String description) {
        this.description = description;
    }

    /**
     * Renvoie une description de l'etat en toute lettre
     * 
     * @return description de l'etat
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getDescription(Locale locale) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public String getDictionaryKey() {
        return this.toString();
    }

    /**
     * Permet de savoir si le statut est un statut final ou non. <br/>
     * Un statut est final, si une fois ce statut atteint, <br/>
     * la requete ne changera plus jamais de statut.
     * 
     * @param status
     * @return true si le status est un statut final.
     */
    @Override
    public boolean isFinalStatus() {
        return this == IdentificationRequestStatus.Closed
               || this == IdentificationRequestStatus.Cancelled;
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
        throw new NotImplementedException();
    }

}
