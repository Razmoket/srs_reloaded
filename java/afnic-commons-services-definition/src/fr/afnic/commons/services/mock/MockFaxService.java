/*
 * $Id: MockFaxDao.java,v 1.1 2010/06/15 15:36:15 ginguene Exp $
 * $Revision: 1.1 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services.mock;

import java.util.ArrayList;
import java.util.List;

import fr.afnic.commons.beans.documents.Fax;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IFaxService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Implémentation de test du service de Fax.
 * 
 * @author ginguene
 * 
 */
public class MockFaxService implements IFaxService {

    List<Fax> receivedFaxes = new ArrayList<Fax>();

    public void setReceivedFaxes(List<Fax> receivedFaxes) {
        this.receivedFaxes = receivedFaxes;
    }

    public void addReceivedFax(Fax receivedFax) {
        this.receivedFaxes.add(receivedFax);
    }

    @Override
    public List<Fax> getReceivedFaxes(UserId userId, TldServiceFacade tld) throws ServiceException {
        List<Fax> receivedFaxesToReturn = this.receivedFaxes;
        this.receivedFaxes = new ArrayList<Fax>();
        return receivedFaxesToReturn;
    }

}
