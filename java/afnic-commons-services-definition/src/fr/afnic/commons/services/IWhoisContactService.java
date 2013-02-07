/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/IWhoisContactService.java#6 $
 * $Revision: 
 * $Author: barriere $
 */

package fr.afnic.commons.services;

import java.sql.Date;
import java.util.List;

import fr.afnic.commons.beans.ContactSnapshot;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.domain.DomainContactType;
import fr.afnic.commons.beans.operations.qualification.PublicQualificationSnapshot;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.search.whoiscontact.WhoisContactSearchCriteria;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Dao permettant d'accéder à des méthode CRUD sur les contacts
 * 
 * @author ginguene
 * 
 */
public interface IWhoisContactService {

    public WhoisContact getContactWithHandle(String nicHandle, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void updateContact(WhoisContact contact, String login, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void deleteContact(String nichandle, UserId userId, TldServiceFacade tld) throws ServiceException;

    public ContactSnapshot createSnapshot(WhoisContact contact, UserId userId, TldServiceFacade tld) throws ServiceException;

    public ContactSnapshot getSnapshot(String snapshotId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public WhoisContact createContact(WhoisContact whoisContact, String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<WhoisContact> searchContact(WhoisContactSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<String> getNicHandlesToQualify(Date minDate, int nbResults, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<String> getNicHandlesToSurvey(UserId userId, TldServiceFacade tld) throws ServiceException;

    public boolean isExistingNicHandle(String nicHandle, UserId userId, TldServiceFacade tld) throws ServiceException;

    public PublicQualificationSnapshot getPublicQualificationSnapshot(String snapshotId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<String> getDomainContactHandles(String domainName, DomainContactType contactType, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<WhoisContact> getHoldersWithRegistrarCode(final String customerId, UserId userId, TldServiceFacade tld) throws ServiceException;

}
