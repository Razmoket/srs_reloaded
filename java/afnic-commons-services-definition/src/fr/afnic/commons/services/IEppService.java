/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/IEppService.java#8 $
 * $Revision: #8 $
 * $Author: barriere $
 */

package fr.afnic.commons.services;

import java.util.List;

import fr.afnic.commons.beans.epp.EppMessage;
import fr.afnic.commons.beans.notifications.Notification;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.QualificationStep;
import fr.afnic.commons.beans.operations.qualification.operation.DomainPortfolioOperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Permet d'effectuer des opérations tel qu'envoyer des notification
 * 
 * 
 * @author ginguene
 * 
 */
public interface IEppService {

    /**
     * Notifie via Epp une opération sur le portefeuille de domaine d'un titulaire dont on passe le nichandle.
     */
    public List<Notification> notifyOfDomainPortfolioOperation(DomainPortfolioOperationType operation, String holderNicHandle, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<Notification> notifyOfQualificationStep(QualificationStep step, Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<EppMessage> getEppMessages(String contactSnapshotId, UserId userId, TldServiceFacade tld) throws ServiceException;

}
