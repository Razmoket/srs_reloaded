/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/request/AuthorizationRequestStatus.java#22 $ $Revision: #22 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;

/**
 * Status d'une requete d'identification
 * 
 * @author ginguene
 * 
 */
public enum AuthorizationRequestStatus implements RequestStatus {

    Running("À traiter", false),
    Failed("Échec", false),
    Waiting("En attente de réponse", false),
    PendingAbort("À abandonner", false),
    Aborded("Abandonnée", false),
    Answered("Répondu", false),
    Generated("Code attribué", false),
    InvalidatedCode("Code invalidé", false),
    UsedCode("Code utilisé", true),
    ExpiredCode("Code perimé", true),
    Rejected("Rejetée", true),
    Suppressed("Supprimée", true),
    Problem("Problème", true);

    private final String description;
    private final boolean isFinalStatus;

    private static List<AuthorizationRequestStatus> FINAL_STATUS_LIST = new ArrayList<AuthorizationRequestStatus>();
    private static List<AuthorizationRequestStatus> NON_FINAL_STATUS_LIST = new ArrayList<AuthorizationRequestStatus>();

    private AuthorizationRequestStatus(String description, boolean isFinalStatus) {
        this.description = description;
        this.isFinalStatus = isFinalStatus;
    }

    /**
     * Renvoi une description de l'etat en toute lettre
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
     * Permet de savoir si le statut est un statut final ou non.
     */
    @Override
    public boolean isFinalStatus() {
        return this.isFinalStatus;
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
        throw new NotImplementedException();
    }

    public static List<AuthorizationRequestStatus> getNonFinalStatus() {
        populateNonFinalStatusList();
        return NON_FINAL_STATUS_LIST;
    }

    public static List<AuthorizationRequestStatus> getFinalStatus() {
        populateFinalStatusList();
        return FINAL_STATUS_LIST;
    }

    private static void populateFinalStatusList() {
        if (FINAL_STATUS_LIST.isEmpty()) {
            for (AuthorizationRequestStatus status : AuthorizationRequestStatus.values()) {
                if (status.isFinalStatus) {
                    FINAL_STATUS_LIST.add(status);
                }
            }
        }
    }

    private static void populateNonFinalStatusList() {
        if (NON_FINAL_STATUS_LIST.isEmpty()) {
            for (AuthorizationRequestStatus status : AuthorizationRequestStatus.values()) {
                if (!status.isFinalStatus) {
                    NON_FINAL_STATUS_LIST.add(status);
                }
            }
        }
    }
}
