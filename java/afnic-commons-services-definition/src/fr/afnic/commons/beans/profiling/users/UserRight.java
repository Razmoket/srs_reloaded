/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/profiling/users/UserRight.java#23 $ $Revision: #23 $ $Author:
 * ginguene $
 */

package fr.afnic.commons.beans.profiling.users;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedExternallyObject;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Liste des droit que peuvent avoir les utilisateurs
 * 
 * @author ginguene
 * 
 */
public enum UserRight implements IDescribedExternallyObject {
    Viewer,
    AuthorizationRead,
    AuthorizationWrite,
    AuthorizationUnlock,
    AuthorizationSuppression,
    TradeRead,
    TradeWrite,
    TradeUnlock,
    TradeSuppression,
    Testing,
    Admin,
    StatistictRead,
    StagedDelete,
    CustomerCreate,
    CustomerUpdate,
    CustomerRead,
    AccountManager,
    AgtfManager,
    DomainRead,
    DomainBlock,
    WhoisContactRead,
    WhoisContactWrite,
    WhoisContactBlockportfolio,
    All,

    QualificationRead,
    QualificationCreate,
    QualificationWrite,

    OperationRelaunch,

    QualificationStat,

    TicketRead,
    TicketWrite,
    EmailView,
    EmailSend,

    FormRead,

    ContractCreate,
    ContractUpdate, ;

    private UserRight() {
    }

    @Override
    public String getDescription(UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getDictionaryService().getDescription(this, userId, tld);
    }

    @Override
    public String getDescription(Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getDictionaryService().getDescription(this, locale, userId, tld);
    }

    /**
     * Indique si le droit se transmet entre profil Parent et fils
     * 
     * @return
     */
    public boolean isInheritedRight() {
        return this != AccountManager;
    }

    @Override
    public String getDictionaryKey() {
        return this.toString();
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
        throw new NotImplementedException();
    }

}
