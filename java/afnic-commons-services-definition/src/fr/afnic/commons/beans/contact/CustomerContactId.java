/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/customer/CustomerId.java#9 $
 * $Revision: #9 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.beans.contact;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.validatable.NumberId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Identifiant d'un customer
 * 
 * @author ginguene
 * 
 */
public class CustomerContactId extends NumberId<CustomerContact> {

    private static final long serialVersionUID = 1L;

    public CustomerContactId() {
    }

    public CustomerContactId(int id) {
        super(id);
    }

    public CustomerContactId(String id) {
        super(id);
    }

    @Override
    public CustomerContact getObjectOwner(UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getCustomerContactService().getCustomerContact(this, userId, tld);
    }

}
