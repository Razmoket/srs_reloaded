package fr.afnic.commons.services.multitld;

import java.util.List;

import fr.afnic.commons.beans.OperationForm;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.search.form.FormSearchCriteria;
import fr.afnic.commons.beans.validatable.OperationFormId;
import fr.afnic.commons.services.IOperationFormService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldOperationFormService implements IOperationFormService {

    protected MultiTldOperationFormService() {
        super();
    }

    @Override
    public OperationForm getOperationFormWithId(OperationFormId id, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOperationFormService().getOperationFormWithId(id, userId, tld);
    }

    @Override
    public String getLastTransmissionCommentForDomainName(String pNdd, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOperationFormService().getLastTransmissionCommentForDomainName(pNdd, userId, tld);
    }

    @Override
    public String getOperationFormInitialContent(OperationFormId id, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOperationFormService().getOperationFormInitialContent(id, userId, tld);
    }

    @Override
    public OperationForm getOperationFormWithTicket(String ticket, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOperationFormService().getOperationFormWithTicket(ticket, userId, tld);
    }

    @Override
    public List<OperationForm> getOperationFormsWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOperationFormService().getOperationFormsWithDomain(domain, userId, tld);
    }

    @Override
    public List<OperationForm> searchOperationForms(FormSearchCriteria informationToFind, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOperationFormService().searchOperationForms(informationToFind, userId, tld);
    }

    @Override
    public void archiveOperationForm(OperationFormId operationFormId, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getOperationFormService().archiveOperationForm(operationFormId, userId, tld);
    }

    @Override
    public boolean isArchived(OperationFormId operationFormId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOperationFormService().isArchived(operationFormId, userId, tld);
    }
}
