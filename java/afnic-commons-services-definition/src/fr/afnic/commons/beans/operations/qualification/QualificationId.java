package fr.afnic.commons.beans.operations.qualification;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.validatable.NumberId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationId extends NumberId<Qualification> {

    private static final long serialVersionUID = 1L;

    public QualificationId(int id) {
        super(id);
    }

    @Override
    public Qualification getObjectOwner(UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getQualificationService().getQualification(this, userId, tld);
    }

    @Override
    public boolean isExisting(UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getQualificationService().isExistingQualificationId(this, userId, tld);
    }

}
