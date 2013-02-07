package fr.afnic.commons.beans.operations.qualification.operation;

import java.util.Map;

import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.operations.CompositeOperation;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.commons.AttachDocument;
import fr.afnic.commons.beans.operations.qualification.operation.factory.QualificationFinishWithAfnicValidationFactory;
import fr.afnic.commons.beans.operations.qualification.operation.factory.QualificationFinishWithoutValidationFactory;
import fr.afnic.commons.beans.operations.qualification.operation.factory.QualificationStartJustificationFactory;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Centralise l'accès aux fat des operations complexes.
 * 
 * @author ginguene
 *
 */
public final class OperationFactoryFacade {

    private static final OperationFactoryFacade INSTANCE = new OperationFactoryFacade();

    private OperationFactoryFacade() {

    }

    private final QualificationFinishWithAfnicValidationFactory qualificationAfnicValidationFactory = new QualificationFinishWithAfnicValidationFactory();
    private final QualificationStartJustificationFactory qualificationStartJustificationFactory = new QualificationStartJustificationFactory();
    private final QualificationFinishWithoutValidationFactory qualificationFinishWithoutValidationFactory = new QualificationFinishWithoutValidationFactory();

    public static CompositeOperation createQualificationFinishWithAfnicValidation(OperationConfiguration conf, Map<OperationType, String> comment, Map<OperationType, Email> mapEmail, UserId userId,
                                                                                  TldServiceFacade tld)
                                                                                                       throws ServiceException {
        return INSTANCE.qualificationAfnicValidationFactory.create(conf, comment, mapEmail, userId, tld);
    }

    public static CompositeOperation createQualificationFinishWithoutValidation(OperationConfiguration conf, Map<OperationType, String> comment, Map<OperationType, Email> mapEmail, UserId userId,
                                                                                TldServiceFacade tld)
                                                                                                     throws ServiceException {
        return INSTANCE.qualificationFinishWithoutValidationFactory.create(conf, comment, mapEmail, userId, tld);
    }

    public static QualificationStartJustification createQualificationStartJustification(OperationConfiguration conf, Map<OperationType, String> comment, Map<OperationType, Email> mapEmail,
                                                                                        UserId userId, TldServiceFacade tld)
                                                                                                                            throws ServiceException {
        return INSTANCE.qualificationStartJustificationFactory.create(conf, comment, mapEmail, userId, tld);
    }

    public static AttachDocument createAttachDocument(OperationConfiguration conf, String documentHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getOperationService().createAndGet(new AttachDocument(conf, documentHandle, userId, tld), userId, tld);
    }

}
