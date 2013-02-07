package fr.afnic.commons.test.generator;

import java.util.Random;

import fr.afnic.commons.beans.contact.CustomerContact;
import fr.afnic.commons.beans.contact.CustomerContactId;
import fr.afnic.commons.beans.contactdetails.Url;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.exception.GeneratorException;
import fr.afnic.utils.StringUtils;

public final class CustomerContactGenerator {

    /** Definition du Logger de la classe CustomerContactGenerator */
    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(CustomerContactGenerator.class);

    private CustomerContactGenerator() {
    }

    public static CustomerContact createRandomCustomerContact() throws GeneratorException {
        CustomerContact customerContact;
        try {
            customerContact = CustomerContact.createIndividualCustomer(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        } catch (ServiceException e) {
            throw new GeneratorException("createRandomCustomerContact() failed", e);
        }
        Random random = new Random(System.currentTimeMillis());

        int idxContact = random.nextInt(10) + 1;

        customerContact.setContactId(new CustomerContactId(random.nextInt()));

        customerContact.getIndividualIdentity().setLastName(StringUtils.generateWord(11));
        customerContact.getIndividualIdentity().setFirstName(StringUtils.generateWord(6));

        customerContact.addEmailAddress(EmailAddressGenerator.getEmailAddress());
        customerContact.addPhoneNumber(PhoneNumberGenerator.getPhoneNumber());
        customerContact.addFaxNumber(PhoneNumberGenerator.getFaxNumber());

        customerContact.addUrl(new Url("www." + StringUtils.generateWord(10) + ".fr"));
        customerContact.setPostalAddress(PostalAddressGenerator.generateRandomPostalAddressInParis());

        if (CustomerContactGenerator.LOGGER.isDebugEnabled()) {
            CustomerContactGenerator.LOGGER.debug(customerContact.toString());
        }
        return customerContact;
    }
}
