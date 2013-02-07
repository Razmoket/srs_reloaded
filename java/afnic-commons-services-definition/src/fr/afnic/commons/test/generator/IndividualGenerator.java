package fr.afnic.commons.test.generator;

import java.text.ParseException;

import fr.afnic.commons.beans.contact.GrcContact;
import fr.afnic.commons.beans.contact.identity.IndividualIdentity;
import fr.afnic.commons.beans.contactdetails.Url;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.exception.GeneratorException;
import fr.afnic.utils.DateUtils;
import fr.afnic.utils.StringUtils;

public final class IndividualGenerator {
    /** Definition du Logger de la classe IndividualGenerator */
    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(IndividualGenerator.class);

    private IndividualGenerator() {
    }

    public static GrcContact generateIndividual() throws GeneratorException {
        GrcContact individual;
        try {
            individual = new GrcContact(new IndividualIdentity(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        } catch (ServiceException e) {
            throw new GeneratorException(e);
        }

        individual.getIndividualIdentity().setFirstName(StringUtils.generateWord(6));
        individual.getIndividualIdentity().setLastName(StringUtils.generateWord(11));

        individual.getIndividualIdentity().setBirthCity("Paris");
        try {
            individual.getIndividualIdentity().setBirthDate(DateUtils.parseDayFormat("19/10/2010"));
        } catch (ParseException e) {
            throw new GeneratorException(e);
        }

        individual.addEmailAddress(EmailAddressGenerator.getEmailAddress());
        individual.addPhoneNumber(PhoneNumberGenerator.getPhoneNumber());
        individual.addFaxNumber(PhoneNumberGenerator.getFaxNumber());

        individual.addUrl(new Url("www." + StringUtils.generateWord(10) + ".fr"));
        individual.setPostalAddress(PostalAddressGenerator.generateRandomPostalAddressInParis());
        if (IndividualGenerator.LOGGER.isDebugEnabled()) {
            IndividualGenerator.LOGGER.debug(individual.toString());
        }
        return individual;
    }
}
