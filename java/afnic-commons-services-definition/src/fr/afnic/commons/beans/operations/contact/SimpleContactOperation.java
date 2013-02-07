package fr.afnic.commons.beans.operations.contact;

import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.SimpleOperation;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Opération nécéssitant d'étre sous un IContactOperation pour manipuler un contact à partir de son nichandle * 
 * @author ginguene
 *
 */
public abstract class SimpleContactOperation extends SimpleOperation {

    protected SimpleContactOperation(OperationConfiguration conf, UserId userId, TldServiceFacade tld) {
        super(conf, userId, tld);
    }

    public String getContactNicHandle() throws ServiceException {
        return this.getParentOrThrowException(IContactOperation.class).getNicHandle();
    }

}
