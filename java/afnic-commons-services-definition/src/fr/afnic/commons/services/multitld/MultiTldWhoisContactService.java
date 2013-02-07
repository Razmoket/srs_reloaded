package fr.afnic.commons.services.multitld;

import java.util.List;

import fr.afnic.commons.beans.ContactSnapshot;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.domain.DomainContactType;
import fr.afnic.commons.beans.operations.qualification.PublicQualificationSnapshot;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.search.whoiscontact.WhoisContactSearchCriteria;
import fr.afnic.commons.services.IWhoisContactService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldWhoisContactService implements IWhoisContactService {

    protected MultiTldWhoisContactService() {
        super();
    }

    @Override
    public WhoisContact getContactWithHandle(String nicHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getWhoisContactService().getContactWithHandle(nicHandle, userId, tld);
    }

    @Override
    public void updateContact(WhoisContact contact, String login, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getWhoisContactService().updateContact(contact, login, userId, tld);
    }

    @Override
    public void deleteContact(String nichandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getWhoisContactService().deleteContact(nichandle, userId, tld);
    }

    @Override
    public ContactSnapshot createSnapshot(WhoisContact contact, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getWhoisContactService().createSnapshot(contact, userId, tld);
    }

    @Override
    public ContactSnapshot getSnapshot(String snapshotId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getWhoisContactService().getSnapshot(snapshotId, userId, tld);
    }

    @Override
    public WhoisContact createContact(WhoisContact whoisContact, String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getWhoisContactService().createContact(whoisContact, userLogin, userId, tld);
    }

    @Override
    public List<WhoisContact> searchContact(WhoisContactSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getWhoisContactService().searchContact(criteria, userId, tld);
    }

    @Override
    public List<String> getNicHandlesToQualify(java.sql.Date date, int nbResults, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getWhoisContactService().getNicHandlesToQualify(date, nbResults, userId, tld);
    }

    @Override
    public List<String> getNicHandlesToSurvey(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getWhoisContactService().getNicHandlesToSurvey(userId, tld);
    }

    @Override
    public boolean isExistingNicHandle(String nicHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getWhoisContactService().isExistingNicHandle(nicHandle, userId, tld);
    }

    @Override
    public PublicQualificationSnapshot getPublicQualificationSnapshot(String snapshotId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getWhoisContactService().getPublicQualificationSnapshot(snapshotId, userId, tld);
    }

    @Override
    public List<String> getDomainContactHandles(String domainName, DomainContactType contactType, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getWhoisContactService().getDomainContactHandles(domainName, contactType, userId, tld);
    }

    @Override
    public List<WhoisContact> getHoldersWithRegistrarCode(String customerId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getWhoisContactService().getHoldersWithRegistrarCode(customerId, userId, tld);
    }
}
