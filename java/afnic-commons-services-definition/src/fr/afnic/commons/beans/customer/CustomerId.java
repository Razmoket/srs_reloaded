/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/customer/CustomerId.java#13 $
 * $Revision: #13 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.customer;

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
public class CustomerId extends NumberId<Customer> {

    private static final long serialVersionUID = 1L;

    public CustomerId() {
    }

    public CustomerId(int id) {
        super(id);
    }

    public CustomerId(String id) {
        super(id);
    }

    @Override
    public Customer getObjectOwner(UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getCustomerService().getCustomerWithId(this, userId, tld);
    }

}
