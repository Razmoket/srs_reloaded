/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.validatable;

import fr.afnic.commons.beans.OperationForm;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class OperationFormId extends NumberId<OperationForm> {

    private static final long serialVersionUID = 1L;

    public OperationFormId(int value) {
        super(value);
    }

    @Override
    public OperationForm getObjectOwner(UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getOperationFormService().getOperationFormWithId(this, userId, tld);
    }

}
