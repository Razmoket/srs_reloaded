/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/customer/CustomerId.java#13 $
 * $Revision: #13 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.contract;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.validatable.NumberId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Identifiant d'un contract
 * 
 * @author ginguene
 * 
 */
public class ContractId extends NumberId<Contract> {

    private static final long serialVersionUID = 1L;

    public ContractId() {
    }

    public ContractId(int id) {
        super(id);
    }

    public ContractId(String id) {
        super(id);
    }

    @Override
    public Contract getObjectOwner(UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getContractService().getContractWithId(this, userId, tld);
    }

}
