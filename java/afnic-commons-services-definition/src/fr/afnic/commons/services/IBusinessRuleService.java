package fr.afnic.commons.services;

import java.util.List;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public interface IBusinessRuleService {

    public String getCustomerForExtension(String extension, UserId userId, TldServiceFacade tld) throws ServiceException;

    public String getHolderForExtension(String extension, UserId userId, TldServiceFacade tld) throws ServiceException;

    public String getTechCForExtension(String extension, UserId userId, TldServiceFacade tld) throws ServiceException;

    public String getAdminCForExtension(String extension, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<String> getListHolder(UserId userId, TldServiceFacade tld) throws ServiceException;

}
