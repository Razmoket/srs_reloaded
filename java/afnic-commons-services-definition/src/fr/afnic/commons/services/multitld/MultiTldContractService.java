package fr.afnic.commons.services.multitld;

import java.util.List;

import fr.afnic.commons.beans.contact.CustomerContactId;
import fr.afnic.commons.beans.contract.Contract;
import fr.afnic.commons.beans.contract.ContractId;
import fr.afnic.commons.beans.contract.ContractOffre;
import fr.afnic.commons.beans.contract.ContractTypeOnTld;
import fr.afnic.commons.beans.customer.CustomerId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IContractService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldContractService implements IContractService {

    protected MultiTldContractService() {
        super();
    }

    @Override
    public ContractId createContract(Contract contract, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getContractService().createContract(contract, userId, tld);
    }

    @Override
    public Contract updateContract(Contract contract, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getContractService().updateContract(contract, userId, tld);
    }

    @Override
    public List<Contract> getContracts(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getContractService().getContracts(userId, tld);
    }

    @Override
    public List<ContractTypeOnTld> getContractTypes(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getContractService().getContractTypes(userId, tld);
    }

    @Override
    public Contract getContractWithId(ContractId customerId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getContractService().getContractWithId(customerId, userId, tld);
    }

    @Override
    public List<Contract> getContractWithCustomerId(CustomerId customerId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getContractService().getContractWithCustomerId(customerId, userId, tld);
    }

    @Override
    public ContractTypeOnTld getContractType(int idContracttypetld, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getContractService().getContractType(idContracttypetld, userId, tld);
    }

    @Override
    public List<ContractOffre> getContractOffres(String typeOffre, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getContractService().getContractOffres(typeOffre, userId, tld);
    }

    @Override
    public List<TldServiceFacade> getTlds(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getContractService().getTlds(userId, tld);
    }

    @Override
    public ContractTypeOnTld getContractType(int idTld, String idContractType, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getContractService().getContractType(idTld, idContractType, userId, tld);
    }

    @Override
    public boolean addContactRole(Contract contract, int id_role, CustomerContactId customerContactId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getContractService().addContactRole(contract, id_role, customerContactId, userId, tld);
    }

}
