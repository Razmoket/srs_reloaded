package fr.afnic.commons.services.proxy;

import java.sql.Date;
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

public class ProxyWhoisContactService extends ProxyService<IWhoisContactService> implements IWhoisContactService {

    protected ProxyWhoisContactService() {
        super();
    }

    protected ProxyWhoisContactService(IWhoisContactService delegationService) {
        super(delegationService);
    }

    @Override
    public WhoisContact getContactWithHandle(String nicHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getContactWithHandle(nicHandle, userId, tld);
    }

    @Override
    public void updateContact(WhoisContact contact, String login, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.getDelegationService().updateContact(contact, login, userId, tld);
    }

    @Override
    public ContactSnapshot createSnapshot(WhoisContact contact, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().createSnapshot(contact, userId, tld);
    }

    @Override
    public ContactSnapshot getSnapshot(String snapshotId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getSnapshot(snapshotId, userId, tld);
    }

    @Override
    public WhoisContact createContact(WhoisContact whoisContact, String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().createContact(whoisContact, userLogin, userId, tld);
    }

    @Override
    public List<WhoisContact> searchContact(WhoisContactSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().searchContact(criteria, userId, tld);
    }

    @Override
    public List<String> getNicHandlesToQualify(Date minDate, int nbResults, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getNicHandlesToQualify(minDate, nbResults, userId, tld);
    }

    @Override
    public List<String> getNicHandlesToSurvey(UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getNicHandlesToSurvey(userId, tld);
    }

    @Override
    public boolean isExistingNicHandle(String nicHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().isExistingNicHandle(nicHandle, userId, tld);
    }

    @Override
    public PublicQualificationSnapshot getPublicQualificationSnapshot(String snapshotId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getPublicQualificationSnapshot(snapshotId, userId, tld);
    }

    @Override
    public List<String> getDomainContactHandles(String domainName, DomainContactType contactType, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getDomainContactHandles(domainName, contactType, userId, tld);
    }

    @Override
    public List<WhoisContact> getHoldersWithRegistrarCode(String customerId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getHoldersWithRegistrarCode(customerId, userId, tld);
    }

    @Override
    public void deleteContact(String nichandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.getDelegationService().deleteContact(nichandle, userId, tld);
    }

}
