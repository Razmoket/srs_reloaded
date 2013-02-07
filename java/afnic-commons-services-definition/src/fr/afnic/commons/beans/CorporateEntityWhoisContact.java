/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/CorporateEntityWhoisContact.java#13 $
 * $Revision: #13 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans;

import java.util.List;

import fr.afnic.commons.beans.contact.identity.CorporateEntityIdentity;
import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.contactdetails.PhoneNumber;
import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.contactdetails.Url;
import fr.afnic.commons.beans.corporateentity.CorporateEntity;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.checkers.CheckerFacade;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.exception.invalidformat.InvalidEmailAddressException;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;

/**
 * 
 * Représente un contact désignant une personne morale. <br/>
 * Toutes les méthodes héritée de WhoisContact sont réécrite en<br/>
 * les délégants à l'objet legalStructure.
 * 
 * @author ginguene
 * 
 */
public class CorporateEntityWhoisContact extends WhoisContact {

    private static final long serialVersionUID = 1L;

    protected CorporateEntityIdentity identity;

    @Override
    public List<Url> getUrls() {
        return this.corporateEntity.getUrls();
    }

    @Override
    public void setEmailAddresses(EmailAddress... emailAddresses) throws InvalidEmailAddressException {
        this.corporateEntity.setEmailAddresses(emailAddresses);
    }

    @Override
    public void setPhoneNumbers(PhoneNumber... phoneNumberArr) {
        this.corporateEntity.setPhoneNumbers(phoneNumberArr);
    }

    @Override
    public void setFaxNumbers(PhoneNumber... faxNumbers) {
        this.corporateEntity.setFaxNumbers(faxNumbers);
    }

    @Override
    public void setUrls(Url... urls) {
        this.corporateEntity.setUrls(urls);
    }

    @Override
    public void setUrls(List<Url> urls) {
        this.corporateEntity.setUrls(urls);
    }

    @Override
    public void setUrlsFromStrList(List<String> urlList) {
        this.corporateEntity.setUrlsFromStrList(urlList);
    }

    @Override
    public Url getFirstUrl() {
        return this.corporateEntity.getFirstUrl();
    }

    @Override
    public boolean hasUrl() {
        return this.corporateEntity.hasUrl();
    }

    private CorporateEntity corporateEntity;

    /**
     * Constucteur prenant en parametre une LegalStructure.
     * 
     * @param corporateEntity
     *            LegalStructure associé au contact
     */
    public CorporateEntityWhoisContact(CorporateEntity corporateEntity, UserId userIdCaller, TldServiceFacade tldCaller) {
        super(new CorporateEntityIdentity(), userIdCaller, tldCaller);
        this.corporateEntity = Preconditions.checkNotNull(corporateEntity, "corporateEntity");
    }

    public void setName(String name) {
        this.corporateEntity.setOrgnaizationName(name);
    }

    @Override
    public String getName() {
        return this.corporateEntity.getName();
    }

    public CorporateEntity getLegalStructure() {
        return this.corporateEntity;
    }

    /**
     * Initialise l'attribut legalStructure. <br/>
     * Comme Toutes les méthodes héritée de WhoisContact sont réécrite en<br/>
     * les délégants à l'objet legalStructure, il est important que l'objet legalStructure<br/>
     * soit différent de null.<br/>
     * En cas de parameter null, retourne une IllegalArgumentException.
     * 
     * @param legalStructure
     *            structure légale du CorporateEntityContact
     */
    public void setLegalStructure(CorporateEntity corporateEntity) {
        this.corporateEntity = Preconditions.checkNotNull(corporateEntity, "corporateEntity");
    }

    /** {@inheritDoc} 
     * @throws ServiceException */
    @Override
    public PostalAddress getPostalAddress() throws ServiceException {
        return this.corporateEntity.getPostalAddress();
    }

    /** {@inheritDoc} */
    @Override
    public void setPostalAddress(PostalAddress postalAddress) {
        this.corporateEntity.setPostalAddress(postalAddress);
    }

    /** {@inheritDoc} */
    @Override
    public void addEmailAddress(EmailAddress emailAddress) {
        this.corporateEntity.addEmailAddress(emailAddress);
    }

    /** {@inheritDoc} */
    @Override
    public void addFaxNumber(PhoneNumber faxNumber) {
        this.corporateEntity.addFaxNumber(faxNumber);
    }

    /** {@inheritDoc} */
    @Override
    public void addPhoneNumber(PhoneNumber phoneNumber) {
        this.corporateEntity.addPhoneNumber(phoneNumber);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws InvalidEmailAddressException
     */
    @Override
    public void addEmailAddresses(List<String> emailAddresses) throws InvalidEmailAddressException {
        this.corporateEntity.addEmailAddresses(emailAddresses);
    }

    /** {@inheritDoc} */
    @Override
    public void addFaxNumbers(List<String> faxNumbers) {
        this.corporateEntity.addFaxNumbers(faxNumbers);
    }

    /** {@inheritDoc} */
    @Override
    public void addPhoneNumbers(List<String> phoneNumbers) {
        this.corporateEntity.addPhoneNumbers(phoneNumbers);
    }

    /** {@inheritDoc} */
    @Override
    public List<EmailAddress> getEmailAddresses() {
        return this.corporateEntity.getEmailAddresses();
    }

    /** {@inheritDoc} */
    @Override
    public List<PhoneNumber> getFaxNumbers() {
        return this.corporateEntity.getFaxNumbers();
    }

    /** {@inheritDoc} */
    @Override
    public EmailAddress getFirstEmailAddress() {
        return this.corporateEntity.getFirstEmailAddress();
    }

    /** {@inheritDoc} */
    @Override
    public PhoneNumber getFirstFaxNumber() {
        return this.corporateEntity.getFirstFaxNumber();
    }

    /** {@inheritDoc} */
    @Override
    public PhoneNumber getFirstPhoneNumber() {
        return this.corporateEntity.getFirstPhoneNumber();
    }

    /** {@inheritDoc} */
    @Override
    public List<PhoneNumber> getPhoneNumbers() {
        return this.corporateEntity.getPhoneNumbers();
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasEmailAddress() {
        return this.corporateEntity.hasEmailAddress();
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasFaxNumber() {
        return this.corporateEntity.hasFaxNumber();
    }

    /** {@inheritDoc} */
    @Override
    public void setEmailAddresses(List<EmailAddress> emailAddresses) {
        this.corporateEntity.setEmailAddresses(emailAddresses);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws InvalidEmailAddressException
     */
    @Override
    public void setEmailAddressesFromStrList(List<String> emailAddresses) throws InvalidEmailAddressException {
        this.corporateEntity.setEmailAddressesFromStrList(emailAddresses);
    }

    /** {@inheritDoc} */
    @Override
    public void setFaxNumbers(List<PhoneNumber> faxNumbers) {
        this.corporateEntity.setFaxNumbers(faxNumbers);
    }

    /** {@inheritDoc} */
    @Override
    public void setFaxNumbersFromStrList(List<String> faxNumbers) {
        this.corporateEntity.setFaxNumbersFromStrList(faxNumbers);
    }

    /** {@inheritDoc} */
    @Override
    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.corporateEntity.setPhoneNumbers(phoneNumbers);
    }

    /** {@inheritDoc} */
    @Override
    public void setPhoneNumbersFromStrList(List<String> phoneNumbers) {
        this.corporateEntity.setPhoneNumbersFromStrList(phoneNumbers);
    }

    /**
     * Verifie que la legalstructure est bien initialisé.
     * 
     * @return true si la legalstructure n'est pas null
     */
    public boolean hasLegalStructure() {
        return this.corporateEntity != null;
    }

    /** {@inheritDoc} 
     * @throws ServiceException */
    @Override
    public boolean hasPostalAddress() throws ServiceException {
        return this.corporateEntity.hasPostalAddress();
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasPhoneNumber() {
        return this.corporateEntity.hasPhoneNumber();
    }

    /**
     * Fonction retournant la description d'un Objet CorporateEntityContact. <br/>
     * Cette méthode est utilisée notamment pour remplir le champs afnicHistory des requetes d'identification
     * 
     * 
     * @return une description complète de l'objet en français
     * @throws ServiceException
     *             Si l'accès aux serives échoue
     */
    public String getDescription() throws ServiceException {
        String desc = "";

        if (this.hasLegalStructure() && this.getLegalStructure().hasIdentifier()) {
            desc += "Identifiant: " + this.getLegalStructure().getIdentifier().getValue();
            desc += "\nType d'organisation: " + this.getLegalStructure().getClass().getSimpleName();
            desc += "\nRaison sociale: " + this.getLegalStructure().getName();

            if (this.getLegalStructure().hasPostalAddress()
                && this.getLegalStructure().getPostalAddress().hasOrganization()) {
                desc += "\nNom organisation: " + this.getLegalStructure().getPostalAddress().getOrganization();
            }
        }

        List<String> domainsPortfolio = AppServiceFacade.getDomainService().getDomainNamesWithHolderHandle(this.getHandle(), this.userIdCaller, this.tldCaller);

        boolean hasSecondLevelDomains = false;
        boolean hasReDomains = false;

        for (String domainName : domainsPortfolio) {

            try {
                CheckerFacade.checkSecondLevelDomainName(domainName);
                hasSecondLevelDomains = true;
            } catch (InvalidFormatException e) {
                // Domaine qui n'est pas de second niveau
            }

            if (domainName.endsWith(".re")) {
                hasReDomains = true;
            }
        }

        desc += "\nPrésence de 2nd niveau dans le portefeuille: ";
        if (hasSecondLevelDomains) {
            desc += "oui";
        } else {
            desc += "non";
        }

        desc += "\nPrésence de .re dans le portefeuille: ";
        if (hasReDomains) {
            desc += "oui";
        } else {
            desc += "non";
        }

        desc += "\nDernière identification:" + this.getLastIdentificationStr();
        desc += "\nStatut du titulaire:" + this.getIdentificationStatus();

        return desc;
    }

    @Override
    public CorporateEntityIdentity getIdentity() {
        return this.corporateEntity.getIdentity();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    public void setIdentity(CorporateEntityIdentity identity) {
        this.identity = identity;
    }

}
