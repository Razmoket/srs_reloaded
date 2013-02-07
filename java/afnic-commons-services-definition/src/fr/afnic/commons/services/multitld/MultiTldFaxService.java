package fr.afnic.commons.services.multitld;

import java.util.List;

import fr.afnic.commons.beans.documents.Fax;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IFaxService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldFaxService implements IFaxService {

    protected MultiTldFaxService() {
        super();
    }

    @Override
    public List<Fax> getReceivedFaxes(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getFaxService().getReceivedFaxes(userId, tld);
    }
}
