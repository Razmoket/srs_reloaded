package fr.afnic.commons.test.generator;

import de.svenjacobs.loremipsum.LoremIpsum;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.customer.CustomerId;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.QualificationOperationType;
import fr.afnic.commons.beans.operations.qualification.QualificationSource;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public final class QualificationGenerator {

    private QualificationGenerator() {

    }

    public static Qualification createQualification(String handle) throws ServiceException {
        Qualification createAndGet = AppServiceFacade.getQualificationService().createAndGet(generateQualification(handle), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        createAndGet.setStatus(OperationStatus.Succed);
        AppServiceFacade.getOperationService().updateStatus(createAndGet, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        return createAndGet;
    }

    public static Qualification createQualification() throws ServiceException {
        Qualification createAndGet = AppServiceFacade.getQualificationService().createAndGet(generateQualification(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        createAndGet.setStatus(OperationStatus.Succed);
        AppServiceFacade.getOperationService().updateStatus(createAndGet, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        return createAndGet;
    }

    public static Qualification createQualificationInJustification() throws ServiceException {
        Qualification createAndGet = AppServiceFacade.getQualificationService().createAndGet(generateQualification(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        createAndGet.setStatus(OperationStatus.Succed);
        AppServiceFacade.getOperationService().updateStatus(createAndGet, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        /*Email email = new Email();
        email.setToEmailAddresses("barriere@nic.fr");
        email.setFromEmailAddress("test@nic.fr");
        email.setSubject("test justif");
        email.setContent("test");*/
        createAndGet.execute(QualificationOperationType.FinishValorization, null);
        return createAndGet;
    }

    public static Qualification generateQualification() throws ServiceException {
        WhoisContact contact = ContactGenerator.createCorporateEntityContact();
        try {
            DomainGenerator.createNewDomainWithHolder("test-qualif", contact);
        } catch (Exception e) {
            throw new ServiceException("generateQualification failed.", e);
        }
        return generateQualification(contact.getHandle());
    }

    public static Qualification generateQualification(String handle) throws ServiceException {
        Qualification qualif = new Qualification(UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        UserId rootUserId = UserGenerator.getRootUserId();
        LoremIpsum li = new LoremIpsum();

        qualif.setCreateUserId(rootUserId);
        qualif.setSource(QualificationSource.Reporting);

        qualif.setHolderNicHandle(handle);

        qualif.setInitiatorEmailAddress(new EmailAddress("boa@afnic.fr"));
        qualif.setComments(li.getWords(50));
        qualif.setDetails(li.getWords(50));

        qualif.setCustomerId(new CustomerId(59));

        return qualif;
    }
}
