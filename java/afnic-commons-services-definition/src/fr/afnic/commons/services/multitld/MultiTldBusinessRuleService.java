package fr.afnic.commons.services.multitld;

import java.util.List;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IBusinessRuleService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldBusinessRuleService implements IBusinessRuleService {

    protected MultiTldBusinessRuleService() {
        super();
    }

    @Override
    public String getCustomerForExtension(String extension, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getBusinessRuleService().getCustomerForExtension(extension, userId, tld);
    }

    @Override
    public String getHolderForExtension(String extension, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getBusinessRuleService().getHolderForExtension(extension, userId, tld);
    }

    @Override
    public String getTechCForExtension(String extension, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getBusinessRuleService().getTechCForExtension(extension, userId, tld);
    }

    @Override
    public String getAdminCForExtension(String extension, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getBusinessRuleService().getAdminCForExtension(extension, userId, tld);
    }

    @Override
    public List<String> getListHolder(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getBusinessRuleService().getListHolder(userId, tld);
    }

}
