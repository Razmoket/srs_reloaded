package fr.afnic.commons.beans.customer.benefit;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedExternallyObject;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * 
 * 
 * @author alaphilippe
 * 
 */
public enum ServiceType implements IDescribedExternallyObject {

    WebHosting,
    WebSiteCreation,
    NetworkOperator,
    ProfessionalServices,
    IndividualServices,
    Others,
    DomainRegistrarWithoutServices,
    Bundle,
    NationalCoverage;

    @Override
    public String getDescription(UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.toString();
    }

    @Override
    public String getDescription(Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.toString();
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {

    }

    @Override
    public String getDictionaryKey() {
        return this.toString();
    }
}
