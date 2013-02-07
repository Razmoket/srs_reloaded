/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services;

import java.util.List;

import fr.afnic.commons.beans.contact.CustomerContactId;
import fr.afnic.commons.beans.contract.Contract;
import fr.afnic.commons.beans.contract.ContractId;
import fr.afnic.commons.beans.contract.ContractOffre;
import fr.afnic.commons.beans.contract.ContractTypeOnTld;
import fr.afnic.commons.beans.customer.CustomerId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Service permettant d'accéder à des méthodes CRUD sur les clients
 * 
 * @author ginguene
 * 
 */
public interface IContractService {

    public ContractId createContract(Contract contract, UserId userId, TldServiceFacade tld) throws ServiceException;

    public Contract updateContract(Contract contract, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<Contract> getContracts(UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<ContractTypeOnTld> getContractTypes(UserId userId, TldServiceFacade tld) throws ServiceException;

    public ContractTypeOnTld getContractType(int idTld, String idContractType, UserId userId, TldServiceFacade tld) throws ServiceException;

    public Contract getContractWithId(final ContractId customerId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<Contract> getContractWithCustomerId(final CustomerId customerId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public ContractTypeOnTld getContractType(int idContracttypetld, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<ContractOffre> getContractOffres(String typeOffre, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<TldServiceFacade> getTlds(UserId userId, TldServiceFacade tld) throws ServiceException;

    public boolean addContactRole(Contract contract, int id_role, CustomerContactId customerContactId, UserId userId, TldServiceFacade tld) throws ServiceException;

}
