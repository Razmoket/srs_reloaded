/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/request/TradeRequestStatus.java#8 $
 * $Revision: #8 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;

/**
 * Statut d'une requete de Trade
 * 
 * 
 * @author ginguene
 * 
 */
public enum TradeRequestStatus implements RequestStatus {

    Running("À traiter", false),
    Answered("Répondu", false),
    Waiting("Attente réponse", false),
    Expired("Expirée", false),
    Problem("Problème", false),
    Finished("Fini", true),
    Aborded("Abandonnée", true),
    Suppressed("Supprimée", true);

    private final String description;
    private final boolean isFinalStatus;

    private static List<TradeRequestStatus> FINAL_STATUS_LIST = new ArrayList<TradeRequestStatus>();
    private static List<TradeRequestStatus> NON_FINAL_STATUS_LIST = new ArrayList<TradeRequestStatus>();

    private TradeRequestStatus(String description, boolean isFinalStatus) {
        this.description = description;
        this.isFinalStatus = isFinalStatus;
    }

    @Override
    public String getDescription(Locale locale) throws ServiceException {
        throw new NotImplementedException();
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
    public String getDictionaryKey() {
        return this.toString();
    }

    /**
     * Permet de savoir si le status est un status final ou non.
     * 
     * @param status
     * @return
     */
    @Override
    public boolean isFinalStatus() {
        return this.isFinalStatus;
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
        throw new NotImplementedException();
    }

    public static List<TradeRequestStatus> getNonFinalStatus() {
        populateNonFinalStatusList();
        return NON_FINAL_STATUS_LIST;
    }

    public static List<TradeRequestStatus> getFinalStatus() {
        populateFinalStatusList();
        return FINAL_STATUS_LIST;
    }

    private static void populateFinalStatusList() {
        if (FINAL_STATUS_LIST.isEmpty()) {
            for (TradeRequestStatus status : TradeRequestStatus.values()) {
                if (status.isFinalStatus) {
                    FINAL_STATUS_LIST.add(status);
                }
            }
        }
    }

    private static void populateNonFinalStatusList() {
        if (NON_FINAL_STATUS_LIST.isEmpty()) {
            for (TradeRequestStatus status : TradeRequestStatus.values()) {
                if (!status.isFinalStatus) {
                    NON_FINAL_STATUS_LIST.add(status);
                }
            }
        }
    }

}
