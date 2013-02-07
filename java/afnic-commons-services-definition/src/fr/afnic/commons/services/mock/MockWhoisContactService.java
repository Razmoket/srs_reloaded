package fr.afnic.commons.services.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fr.afnic.commons.beans.ContactSnapshot;
import fr.afnic.commons.beans.IndividualWhoisContact;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.domain.DomainContactType;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.operations.qualification.PublicQualificationItemStatus;
import fr.afnic.commons.beans.operations.qualification.PublicQualificationSnapshot;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.search.whoiscontact.WhoisContactSearchCriteria;
import fr.afnic.commons.services.IWhoisContactService;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;
import fr.afnic.commons.utils.Preconditions;

public class MockWhoisContactService implements IWhoisContactService {

    protected static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(MockWhoisContactService.class);

    /** clé=handle;valeur=contact */
    @SuppressWarnings("rawtypes")
    public HashMap<String, WhoisContact> contacts = new HashMap<String, WhoisContact>();

    /** Sequence des nichandle */
    private int nichandleSequence = 1;

    public MockWhoisContactService() throws ServiceException {
        IndividualWhoisContact contact = new IndividualWhoisContact(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        contact.setHandle("VL123-FRNIC");
        contact.setFirstName("john");
        contact.setLastName("doe");
        this.contacts.put(contact.getHandle(), contact);

    }

    @Override
    public WhoisContact getContactWithHandle(String nicHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (this.contacts.containsKey(nicHandle)) {
            return this.contacts.get(nicHandle).copy();
        } else {
            throw new NotFoundException("not found");
        }
    }

    @Override
    public void updateContact(WhoisContact contact, String login, UserId userId, TldServiceFacade tld) throws ServiceException {

        String handle = contact.getHandle();
        if (this.contacts.containsKey(handle)) {
            WhoisContact copy = null;
            copy = contact.copy();

            if (copy.getEligibilityStatus() == PublicQualificationItemStatus.Ok) {
                copy.setIdentificationDate(new Date());
            }

            if (copy.getReachStatus() == PublicQualificationItemStatus.Ok) {
                copy.setReachabilityQualificationDate(new Date());
            }

            this.contacts.put(handle, copy);
        } else {
            throw new NotFoundException("contact " + handle + " not found");
        }

    }

    @Override
    public ContactSnapshot createSnapshot(WhoisContact contact, UserId userId, TldServiceFacade tld) throws ServiceException {
        return null;
    }

    @Override
    public ContactSnapshot getSnapshot(String snapshotId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return null;
    }

    @Override
    public WhoisContact createContact(WhoisContact whoisContact, String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(whoisContact, "whoisContact");
        Preconditions.checkNotNull(whoisContact.getRegistrarCode(), "whoisContact.registrarCode");
        Preconditions.checkNotNull(userLogin, "userLogin");
        if (AppServiceFacade.getUserService().isNotKnownUser(userLogin, userId, tld)) {
            throw new IllegalArgumentException("'" + userLogin + "' is an unknow user login.");
        }

        whoisContact.setHandle(this.createHandle());

        WhoisContact copy = whoisContact.copy();
        this.contacts.put(copy.getHandle(), copy);

        return whoisContact;

    }

    /**
     * Génère les nichandles utilisés par la méthode createContact.
     */
    private String createHandle() {
        return "HOLDER" + this.nichandleSequence++ + "-FRNIC";
    }

    @Override
    public List<String> getNicHandlesToQualify(java.sql.Date minDate, int nbResults, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public boolean isExistingNicHandle(String nicHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.contacts.containsKey(nicHandle);
    }

    @Override
    public List<String> getNicHandlesToSurvey(UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public PublicQualificationSnapshot getPublicQualificationSnapshot(String snapshotId, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public List<String> getDomainContactHandles(String domainName, DomainContactType contactType, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<WhoisContact> getHoldersWithRegistrarCode(String customerId, UserId userId, TldServiceFacade tld) throws ServiceException {
        List<WhoisContact> list = new ArrayList<WhoisContact>();
        list.add(this.contacts.get("VL123-FRNIC"));
        return list;
    }

    @Override
    public List<WhoisContact> searchContact(WhoisContactSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteContact(String nichandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }
}
