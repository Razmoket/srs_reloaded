/*
 * $Id: ContactGenerator.java,v 1.9 2010/09/28 12:25:17 ginguene Exp $
 * $Revision: 1.9 $
 * $Author: ginguene $
 */

package fr.afnic.commons.test.generator;

import java.util.Calendar;
import java.util.GregorianCalendar;

import fr.afnic.commons.beans.ContactIdentificationStatus;
import fr.afnic.commons.beans.CorporateEntityWhoisContact;
import fr.afnic.commons.beans.IndividualWhoisContact;
import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.contactdetails.PhoneNumber;
import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.corporateentity.CorporateEntity;
import fr.afnic.commons.beans.corporateentity.Siren;
import fr.afnic.commons.beans.corporateentity.TradeMark;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.exception.GeneratorException;
import fr.afnic.utils.StringUtils;

/**
 * Permet de créer des contacts pour des tests.<br/>
 * Tous les contacts créés sont liés au bureau d'enregistrement TEST
 * 
 * 
 * @author ginguene
 * 
 */
public final class ContactGenerator {

    /** Code du bureau d'enregistrement chez qui sont créés tous les contacts */
    public static final String CUSTOMER_CODE = "TEST";

    /**
     * Empeche l'instanciation de cette classe utilitaire.
     * 
     */
    private ContactGenerator() {
    }

    /**
     * Crée un contact de type OtherLegalStructure sans aucun champs d'initialisé.
     * 
     * @return
     * @throws DaoException
     * @throws InvalidFormatException
     * @throws GeneratorException
     * @throws InvalidFormatException
     */
    public static CorporateEntityWhoisContact createOtherLegalStructure() throws GeneratorException {
        try {
            CorporateEntity other = CorporateEntity.createOther(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            other.setDescription(StringUtils.generateWord(20));
            other.setTradeMark(new TradeMark(StringUtils.generateWord(10)));
            return ContactGenerator.createCorporateEntityContactForLegalStructure(other);
        } catch (Exception e) {
            throw new GeneratorException("createCorporateEntityContactWithInvalidIdentifier() failed", e);
        }
    }

    /**
     * Crée un contact avec un identifiant incorrect.<br/>
     * Pour cela on crée un siren avec des caractéres [a-z].
     * 
     * @return
     * @throws DaoException
     * @throws InvalidFormatException
     * @throws GeneratorException
     * @throws InvalidFormatException
     */
    public static CorporateEntityWhoisContact createCorporateEntityContactWithInvalidIdentifier() throws GeneratorException {
        try {
            CorporateEntity company = CorporateEntity.createCompany(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            company.setSiren(new Siren("abc"));
            return ContactGenerator.createCorporateEntityContactForLegalStructure(company);
        } catch (Exception e) {
            throw new GeneratorException("createCorporateEntityContactWithInvalidIdentifier() failed", e);
        }
    }

    /**
     * Crée un contact lié à une structure de type Other.
     * 
     * 
     * @return un contact lié à une structure de type Other.
     * 
     * @throws GeneratorException
     *             Si la génération du jeu de test échoue.
     */
    public static CorporateEntityWhoisContact createOtherLegalStructureContact() throws GeneratorException {
        try {
            return ContactGenerator.createCorporateEntityContactWithRegistrarCodeAndLegalStructure(ContactGenerator.CUSTOMER_CODE,
                                                                                                   CorporateEntityGenerator.generateOtherLegalStructureWithRandomDescription());
        } catch (Exception e) {
            throw new GeneratorException("createOtherLegalStructureContact() failed", e);
        }
    }

    /**
     * Crée un contact lié à une structure de type personne morale.
     * 
     * 
     * @return un contact lié à une structure de type personne morale.
     * 
     * @throws GeneratorException
     *             Si la génération du jeu de test échoue.
     */
    public static CorporateEntityWhoisContact createCorporateEntityContactWithCustomerCode(String customerCode) throws GeneratorException {
        try {
            return ContactGenerator.createCorporateEntityContactWithRegistrarCodeAndLegalStructure(customerCode,
                                                                                                   CorporateEntityGenerator.generateCompanyWithRandomTradeMark());
        } catch (Exception e) {
            throw new GeneratorException("createCorporateEntityContactWithCustomerCode() failed", e);
        }
    }

    /**
     * Crée un contact lié à une structure de type personne morale.
     * 
     * 
     * @return un contact lié à une structure de type personne morale.
     * 
     * @throws GeneratorException
     *             Si la génération du jeu de test échoue.
     */
    public static CorporateEntityWhoisContact createCorporateEntityContact() throws ServiceException {
        try {
            return ContactGenerator.createCorporateEntityContactWithRegistrarCodeAndLegalStructure(ContactGenerator.CUSTOMER_CODE,
                                                                                                   CorporateEntityGenerator.generateCompanyWithRandomTradeMark());
        } catch (Exception e) {
            throw new ServiceException("createCorporateEntityContact() failed", e);
        }
    }

    /**
     * Crée un contact lié à une structure que l'on passe en parametre.
     * 
     * 
     * @param legalStrucure
     *            Structure correspondant au contact.
     * 
     * @return un contact lié à la structure passée en parametre.
     * 
     * @throws GeneratorException
     *             Si la génération du jeu de test échoue.
     */
    public static CorporateEntityWhoisContact createCorporateEntityContactForLegalStructure(CorporateEntity legalStrucure) throws GeneratorException {
        try {
            return ContactGenerator.createCorporateEntityContactWithRegistrarCodeAndLegalStructure(ContactGenerator.CUSTOMER_CODE, legalStrucure);
        } catch (Exception e) {
            throw new GeneratorException("createCorporateEntityContact() failed", e);
        }
    }

    /**
     * Crée un contact lié à une structure que l'on passe en parametre chez <br/>
     * un bureau d'enregistrement dont on passe le code en parametre.
     * 
     * @param registrarCode
     *            Code du bureau d'enregistrement chez qui on souhaite créer le contact.
     * 
     * @param legalStructure
     *            Structure correspondant au contact.
     * 
     * @return un contact lié à la structure passée en parametre enregistré chez le Bureau d'enregistrement dont le code est passé en parametre.
     * 
     * 
     * @throws GeneratorException
     *             Si la génération du jeu de test échoue.
     */
    public static CorporateEntityWhoisContact createCorporateEntityContactWithRegistrarCodeAndLegalStructure(String registrarCode, CorporateEntity legalStructure) throws GeneratorException {

        try {
            // Pour chaques test on utilise un nom different
            // Sinon la methode create de la gateway recycle les vieux handle avec les même données
            String name = ContactGenerator.generateContactName();

            PostalAddress postalAddress = legalStructure.getPostalAddress();
            if (postalAddress == null) {
                postalAddress = PostalAddressGenerator.generateRandomPostalAddressInParis();
            }

            CorporateEntityWhoisContact corporateEntityContact = new CorporateEntityWhoisContact(legalStructure, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            corporateEntityContact.setName(name);
            corporateEntityContact.setRegistrarCode(registrarCode);
            corporateEntityContact.setPostalAddress(postalAddress);
            corporateEntityContact.addFaxNumber(PhoneNumberGenerator.getFaxNumber());
            corporateEntityContact.addPhoneNumber(PhoneNumberGenerator.getPhoneNumber());
            corporateEntityContact.addEmailAddress(EmailAddressGenerator.getEmailAddress());

            corporateEntityContact = (CorporateEntityWhoisContact) AppServiceFacade.getWhoisContactService().createContact(corporateEntityContact, UserGenerator.ROOT_LOGIN,
                                                                                                                           UserGenerator.getRootUserId(), TldServiceFacade.Fr);

            corporateEntityContact.setIdentificationStatus(ContactIdentificationStatus.Ok);
            AppServiceFacade.getWhoisContactService().updateContact(corporateEntityContact, UserGenerator.ROOT_LOGIN, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

            return corporateEntityContact;
        } catch (Exception e) {
            throw new GeneratorException("createCorporateEntityContactWithRegistrarCodeAndLegalStructure() failed", e);
        }
    }

    /**
     * Crée un contact lié à une structure que l'on passe en parametre chez <br/>
     * un bureau d'enregistrement dont on passe le code en parametre.
     * 
     * @param registrarCode
     *            Code du bureau d'enregistrement chez qui on souhaite créer le contact.
     * 
     * @param legalStructure
     *            Structure correspondant au contact.
     * 
     * @return un contact lié à la structure passée en parametre enregistré chez le Bureau d'enregistrement dont le code est passé en parametre.
     * 
     * 
     * @throws GeneratorException
     *             Si la génération du jeu de test échoue.
     */
    public static IndividualWhoisContact createIndividualContact() throws GeneratorException {

        try {
            // Pour chaques test on utilise un nom different
            // Sinon la methode create de la gateway recycle les vieux handle avec les même données
            String fistName = StringUtils.generateWord(10);
            String lastName = StringUtils.generateWord(10);

            PostalAddress postalAddress = PostalAddressGenerator.generateRandomPostalAddressInParis();
            PhoneNumber faxNumber = PhoneNumberGenerator.getFaxNumber();
            PhoneNumber phoneNumber = PhoneNumberGenerator.getPhoneNumber();

            IndividualWhoisContact individualContact = new IndividualWhoisContact(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            individualContact.setFirstName(fistName);
            individualContact.setLastName(lastName);
            individualContact.setRegistrarCode(ContactGenerator.CUSTOMER_CODE);
            individualContact.setPostalAddress(postalAddress);
            individualContact.addFaxNumber(faxNumber);
            individualContact.addPhoneNumber(phoneNumber);

            individualContact.setBirthCity("Paris");
            individualContact.setBirthPostCode("75001");
            individualContact.setBirthCountryCode("FR");
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.add(Calendar.YEAR, -20);
            individualContact.setBirthDate(calendar.getTime());

            EmailAddress emailAddress = EmailAddressGenerator.getEmailAddress(individualContact);
            individualContact.addEmailAddress(emailAddress);

            return (IndividualWhoisContact) AppServiceFacade.getWhoisContactService().createContact(individualContact, UserGenerator.ROOT_LOGIN, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        } catch (Exception e) {
            throw new GeneratorException("createIndividualContact() failed", e);
        }
    }

    /**
     * Génère un nom de contact unique. Les contacts utilisant ces noms, auront des handle avec des débuts uniques.<br/>
     * 
     * @return Un nom de contact unique
     */
    private static String generateContactName() {
        return "boa-" + StringUtils.generateWord(4)
               + " " + StringUtils.generateWord(4)
               + " " + StringUtils.generateWord(4)
               + " " + System.currentTimeMillis();

    }

    /**
     * Retrourne une entreprise avec une marque générée aléatoirement
     * 
     * @return
     * @throws InvalidFormatException
     * @throws ServiceException 
     */
    public static CorporateEntity getCompanyWithTradeMark() throws InvalidFormatException, ServiceException {
        CorporateEntity company = CorporateEntity.createCompany(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        company.setTradeMark(new TradeMark(StringUtils.generateWord(10) + " " + StringUtils.generateWord(10)));
        return company;
    }

}
