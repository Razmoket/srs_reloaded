package fr.afnic.commons.beans.operations.qualification.operation.factory;

import java.io.Serializable;
import java.util.Map;

import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Factory pour les opérations applicables directement sur les Qualification
 * 
 */
public interface IQualificationOperationFactory extends Serializable {

    public Operation create(Qualification qualification, Map<OperationType, String> comment, Map<OperationType, Email> mapEmail, UserId userId, TldServiceFacade tld) throws ServiceException;

}
