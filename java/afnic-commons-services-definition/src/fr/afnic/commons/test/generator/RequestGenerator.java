/*
 * $Id: RequestGenerator.java,v 1.1 2010/07/21 06:16:07 ginguene Exp $
 * $Revision: 1.1 $
 * $Author: ginguene $
 */

package fr.afnic.commons.test.generator;

import java.util.Date;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.Request;
import fr.afnic.commons.beans.request.RequestHistoryEvent;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Permet de manipuler les requetes.
 * 
 * 
 * @author ginguene
 * 
 */
public final class RequestGenerator {

    /**
     * Constructeur par défaut pour empecher l'instanciation.
     */
    private RequestGenerator() {

    }

    /**
     * Permet de changer la date de dernier changement de statut d'une requete.<br/>
     * Cela permet de simuler le passage du temps.
     * 
     * 
     * @param request
     *            Requete à modifier
     * @param newDate
     *            Nouvelle date du dernier changement de statut
     * @throws ServiceException
     *             Si l'opération échoue
     * 
     * @deprecated("On devrait utiliser des serviceMock à la place de ce genre de magouille")
     */
    public static void changeLastStatusChangeDate(Request<?> request, Date newDate, UserId userId) throws ServiceException {
        AppServiceFacade.getRequestService().changeLastStatusChange(request, newDate, userId, TldServiceFacade.Fr);

        RequestHistoryEvent event = request.getHistory().get(0);
        event.setDate(newDate);
        AppServiceFacade.getRequestService().updateHistory(event, userId, TldServiceFacade.Fr);

    }

}
