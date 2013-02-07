package fr.afnic.commons.test.generator;

import de.svenjacobs.loremipsum.LoremIpsum;
import fr.afnic.commons.beans.mail.SentEmail;

public class EmailGenerator {

    public SentEmail generateSentEmail() {
        SentEmail sentEmail = new SentEmail();
        sentEmail.setSubject(new LoremIpsum().getWords(20));
        sentEmail.setContent(new LoremIpsum().getWords(200, 5));

        sentEmail.setFromEmailAddress("boaTestUnit@afnic.fr");

        sentEmail.setToEmailAddresses("boa+to1@afnic.fr", "boa+to2@afnic.fr");
        sentEmail.setCcEmailAddresses("boa+cc1@afnic.fr", "boa+cc2@afnic.fr");
        sentEmail.setBccEmailAddresses("boa+bcc1@afnic.fr", "boa+bcc2@afnic.fr");

        return sentEmail;
    }
}
