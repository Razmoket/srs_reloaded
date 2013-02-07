/**
 * 
 */
package fr.afnic.commons.services.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fr.afnic.commons.beans.OperationForm;
import fr.afnic.commons.beans.OperationFormVersion;
import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.beans.TicketOperation;
import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.search.Pagination;
import fr.afnic.commons.beans.search.customer.CustomerSearchCriteria;
import fr.afnic.commons.beans.search.form.FormSearchCriteria;
import fr.afnic.commons.beans.validatable.OperationFormId;
import fr.afnic.commons.checkers.NichandleChecker;
import fr.afnic.commons.services.IOperationFormService;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.DomainGenerator;
import fr.afnic.commons.test.generator.exception.GeneratorException;
import fr.afnic.commons.utils.Preconditions;

/**
 * Implémentation mock de IOperationFormService
 * 
 * @author alaphilippe
 */
public class MockOperationFormService implements IOperationFormService {

    private int operationFormSequence = 1;

    private HashMap<OperationFormId, OperationForm> idMap = new HashMap<OperationFormId, OperationForm>();

    public MockOperationFormService() {
    }

    public MockOperationFormService(MockOperationFormService mockOperationFormService) {
        this.idMap = mockOperationFormService.idMap;
        this.operationFormSequence = mockOperationFormService.operationFormSequence;
    }

    /**
     * Ajoute un operationForm au service mock
     * 
     * @param operationForm
     */
    public void storeOperationForm(OperationForm operationForm) {
        Preconditions.checkNotNull(operationForm, "operationForm");
        Preconditions.checkNotNull(operationForm.getOperationFormId(), "operationForm.id");

        this.idMap.put(operationForm.getOperationFormId(), operationForm);
    }

    private OperationFormId createOperationFormId() {
        return new OperationFormId(this.operationFormSequence++);
    }

    /**
     * @see fr.afnic.commons.services.IOperationFormService#getOperationFormInitialContent(java.lang.String)
     */
    @Override
    public String getOperationFormInitialContent(OperationFormId id, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see fr.afnic.commons.services.IOperationFormService#getOperationFormWithId(java.lang.String)
     */
    @Override
    public OperationForm getOperationFormWithId(OperationFormId id, UserId userId, TldServiceFacade tld) throws ServiceException {
        OperationForm operationForm = this.idMap.get(id);
        if (operationForm == null) {
            throw new NotFoundException("no operationForm found with id " + id, OperationForm.class);
        }
        return operationForm;
    }

    /**
     * @see fr.afnic.commons.services.IOperationFormService#getOperationFormWithTicket(java.lang.String)
     */
    @Override
    public OperationForm getOperationFormWithTicket(String ticket, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (AppServiceFacade.getTicketService() instanceof MockTicketService) {
            MockTicketService mockTicketService = (MockTicketService) AppServiceFacade.getTicketService();
            Ticket ticketWithId = mockTicketService.getTicketWithId(ticket, userId, tld);
            OperationFormId formId = ticketWithId.getOperationFormId();
            return this.idMap.get(formId);
        }
        return null;
    }

    /**
     * @see fr.afnic.commons.services.IOperationFormService#getOperationFormsWithDomain(java.lang.String)
     */
    @Override
    public List<OperationForm> getOperationFormsWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (AppServiceFacade.getDomainService() instanceof MockDomainService) {
            MockDomainService mockDomainService = (MockDomainService) AppServiceFacade.getDomainService();
            Domain domainWithName = mockDomainService.getDomainWithName(domain, userId, tld);
            List<OperationForm> retour = new ArrayList<OperationForm>();
            for (Ticket tick : domainWithName.getTickets()) {
                retour.add(this.idMap.get(tick.getOperationFormId()));
            }
            return retour;
        }
        return null;
    }

    public OperationForm createCreateDomainOperationForm(Domain domain, String registrarCode, UserId userId, TldServiceFacade tld) throws ServiceException {
        OperationForm of = new OperationForm(userId, tld);
        of.setOperationFormId(this.createOperationFormId());
        of.setCreateDate(new Date());
        of.setOperation(TicketOperation.CreateDomain);

        of.setDomain(domain);
        of.setDomainName(domain.getName());
        of.setAdminHandle(domain.getAdminContactHandle());
        of.setHolderHandle(domain.getHolderHandle());
        of.setRegistrarCode(registrarCode);

        of.setRegistrarName(AppServiceFacade.getCustomerService().getCustomerWithCode(registrarCode, userId, tld).getName());

        of.setVersion(OperationFormVersion.LAST);

        this.storeOperationForm(of);
        return of;
    }

    /**
     * @see fr.afnic.commons.services.IOperationFormService#searchOperationForms(fr.afnic.commons.beans.OperationForm)
     */
    @Override
    public List<OperationForm> searchOperationForms(FormSearchCriteria informationToFind, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.checkSearchCriteria(informationToFind);

        return this.generateSearchResults(informationToFind, userId, tld);
    }

    private List<OperationForm> generateSearchResults(FormSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {

        List<OperationForm> retour = new ArrayList<OperationForm>();

        String domainName = "operationformsearch" + this.operationFormSequence + "";
        if ((criteria.getDomainName() != null) && (!criteria.getDomainName().equals(""))) {
            domainName = criteria.getDomainName();
        }
        if (AppServiceFacade.getDomainService() instanceof MockDomainService) {
            if ((criteria.getDomainName() != null) && (!criteria.getDomainName().equals(""))) {
                try {
                    domainName = DomainGenerator.createNewDomain(domainName);
                } catch (GeneratorException e) {
                    throw new ServiceException("domain generation failed", e);
                }
            }
            MockDomainService mockDomainService = (MockDomainService) AppServiceFacade.getDomainService();

            if ((criteria.getFormId() != null) && (!criteria.getFormId().equals(""))) {
                OperationFormId operationFormId = new OperationFormId(Integer.parseInt(criteria.getFormId()));
                OperationForm operationForm = this.idMap.get(operationFormId);
                if (operationForm == null) {
                    throw new NotFoundException("no operationForm found with id " + criteria.getFormId(), OperationForm.class);
                }
                retour.add(operationForm);
                return retour;
            }
            if ((criteria.getTicketId() != null) && (!criteria.getTicketId().equals(""))) {
                Ticket ticketWithId = AppServiceFacade.getTicketService().getTicketWithId(criteria.getTicketId(), userId, tld);
                OperationForm operationForm = this.idMap.get(ticketWithId.getOperationFormId());
                if (operationForm == null) {
                    throw new NotFoundException("no operationForm found with ticket id " + criteria.getTicketId(), OperationForm.class);
                }
                retour.add(operationForm);
                return retour;
            }
            if ((criteria.getHolderHandle() != null) && (!criteria.getHolderHandle().equals(""))) {
                List<String> domainList = AppServiceFacade.getDomainService().getDomainNamesWithHolderHandle(criteria.getHolderHandle(), userId, tld);
                for (String domainDesc : domainList) {
                    retour.addAll(this.populateList(mockDomainService, domainDesc, userId, tld));
                }
                return retour;
            }

            if ((criteria.getRegistrar() != null) && (!criteria.getRegistrar().equals(""))) {
                String name = criteria.getRegistrar();
                if (criteria.getRegistrar().contains("*")) {
                    name = criteria.getRegistrar().substring(0, criteria.getRegistrar().indexOf("*"));
                }

                CustomerSearchCriteria searchCriteria = new CustomerSearchCriteria();
                searchCriteria.setName(name);
                Customer customer = AppServiceFacade.getCustomerService()
                                                    .searchCustomer(searchCriteria, Pagination.ONE_ELEMENT_PAGINATION, userId, tld)
                                                    .getPageResults()
                                                    .get(0);

                List<Domain> domainList = AppServiceFacade.getDomainService().getDomainsWithRegistrarCode(customer.getAccountLogin(), userId, tld);
                for (Domain domainDesc : domainList) {
                    retour.addAll(this.populateList(mockDomainService, domainDesc.getName(), userId, tld));
                }
                return retour;
            }
            if (!domainName.contains("*")) {
                retour.addAll(this.populateList(mockDomainService, domainName, userId, tld));
            } else {
                domainName = domainName.substring(0, domainName.indexOf("*"));
                if (domainName.length() < 5) {
                    throw new ServiceException("Not enough caracters.");
                }
                List<Domain> domainList = mockDomainService.getDomainsWithNameContaining(domainName, userId, tld);
                for (Domain domain : domainList) {
                    retour.addAll(this.populateList(mockDomainService, domain.getName(), userId, tld));
                }
            }
        }
        return retour;
    }

    private List<OperationForm> populateList(MockDomainService mockDomainService, String domainName, UserId userId, TldServiceFacade tld) throws ServiceException {
        List<OperationForm> retour = new ArrayList<OperationForm>();
        Domain domain = mockDomainService.getDomainWithName(domainName, userId, tld);
        List<Ticket> ticketsWithDomain = AppServiceFacade.getTicketService().getTicketsWithDomain(domain.getName(), userId, tld);
        for (Ticket tick : ticketsWithDomain) {
            if (tick.getOperationFormId() != null) {
                retour.add(this.idMap.get(tick.getOperationFormId()));
            }
        }
        return retour;
    }

    private void checkSearchCriteria(FormSearchCriteria criteria) throws ServiceException {
        Preconditions.checkNotNull(criteria, "criteria");

        if ((criteria.getDomainName() != null) && (!criteria.getDomainName().equals(""))) {
            if (criteria.getDomainName().length() < 5) {
                throw new IllegalArgumentException("criteria.domainName should contains at least 5 characters");
            }
        }

        if ((criteria.getHolderHandle() != null) && (!criteria.getHolderHandle().equals(""))) {
            new NichandleChecker().check(criteria.getHolderHandle());
        }
        if ((criteria.getAdminHandle() != null) && (!criteria.getAdminHandle().equals(""))) {
            new NichandleChecker().check(criteria.getHolderHandle());
        }

        if ((criteria.getRegistrar() != null) && (!criteria.getRegistrar().equals(""))) {
            if (criteria.getRegistrar().length() < 3) {
                throw new IllegalArgumentException("criteria.registrarName should contains at least 5 characters");
            }
        }
    }

    @Override
    public void archiveOperationForm(OperationFormId operationFormId, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkParameter(operationFormId, "operationFormId");
        AppServiceFacade.getOperationFormService().getOperationFormWithId(operationFormId, userId, tld).setArchived(true);
    }

    @Override
    public boolean isArchived(OperationFormId operationFormId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getOperationFormService().getOperationFormWithId(operationFormId, userId, tld).isArchived();

    }

    @Override
    public String getLastTransmissionCommentForDomainName(String pNdd, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }
}
