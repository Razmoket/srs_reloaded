package fr.afnic.commons.beans.operations.qualification.operation.factory;

import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationWhoisUpdate;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * @deprecated jamais utilisé
 * @author presta
 *
 */
@Deprecated
public class QualificationWhoisUpdateFactory {

    public QualificationWhoisUpdate create(OperationConfiguration conf, UserId userId, TldServiceFacade tld) throws ServiceException {

        QualificationWhoisUpdate qualificationWhoisUpdate = new QualificationWhoisUpdate(conf, userId, tld);
        return qualificationWhoisUpdate;

    }

}
