/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/contact/GenericContact.java#12 $ $Revision: #12 $ $Author:
 * ginguene $
 */

package fr.afnic.commons.beans.contact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import fr.afnic.commons.beans.contact.identity.ContactIdentity;
import fr.afnic.commons.beans.contact.identity.CorporateEntityIdentity;
import fr.afnic.commons.beans.contact.identity.IndividualIdentity;
import fr.afnic.commons.beans.contactdetails.ContactDetail;
import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.contactdetails.FaxNumber;
import fr.afnic.commons.beans.contactdetails.PhoneNumber;
import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.contactdetails.Url;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.exception.invalidformat.InvalidEmailAddressException;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.DateUtils;

/**
 * Objet representant un Contact de manière generique. Toutes les classes qui represente des contact heriteront de cette classe. Un GenericContact se
 * caracterise par un PostalAddress et une List de ContactDetail.
 * 
 * 
 * @author ginguene
 * @apiviz.composedOf fr.afnic.commons.beans.contactdetails.PostalAddress
 * @apiviz.composedOf fr.afnic.commons.beans.contactdetails.EmailAddress
 * 
 * @see fr.afnic.commons.beans.contactdetails.PostalAddress
 * @see fr.afnic.commons.beans.contactdetails.EmailAddress
 */
public class GenericContact implements Serializable {

    private static final long serialVersionUID = 1L;

    protected List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
    protected List<PhoneNumber> faxNumbers = new ArrayList<PhoneNumber>();
    protected List<EmailAddress> emailAddresses = new ArrayList<EmailAddress>();
    protected List<Url> urls = new ArrayList<Url>();

    protected List<ContactDetail> contactDetails = new ArrayList<ContactDetail>();

    protected PostalAddress postalAddress;

    protected Date updateDate;
    protected Date createDate;

    protected ContactIdentity identity;

    private boolean isIndividual;

    protected GenericContact(ContactIdentity identity) {
        this.setIdentity(identity);
    }

    public IndividualIdentity getIndividualIdentity() {
        if (this.isIndividual) {
            return (IndividualIdentity) this.identity;
        } else {
            return null;
        }
    }

    public void setIdentity(ContactIdentity identity) {
        this.identity = Preconditions.checkNotNull(identity, "identity");
        this.isIndividual = (identity instanceof IndividualIdentity);
    }

    public CorporateEntityIdentity getCorporateEntityIdentity() {
        if (!this.isIndividual) {
            return (CorporateEntityIdentity) this.identity;
        } else {
            return null;
        }
    }

    public boolean isIndividual() {
        return this.isIndividual;
    }

    public boolean isCorporateEntity() {
        return !this.isIndividual;
    }

    public ContactIdentity getIdentity() {
        return this.identity;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return this.phoneNumbers;
    }

    public List<PhoneNumber> getFaxNumbers() {
        return this.faxNumbers;
    }

    public List<Url> getUrls() {
        return this.urls;
    }

    public void addUrl(Url url) {
        this.urls.add(url);
    }

    public void addEmailAddress(EmailAddress emailAddress) {
        this.emailAddresses.add(emailAddress);
    }

    public void addFaxNumber(PhoneNumber faxNumber) {
        this.faxNumbers.add(faxNumber);
    }

    public void setEmailAddressesFromStrList(List<String> emailAddresses) throws InvalidEmailAddressException {
        this.emailAddresses = new ArrayList<EmailAddress>();
        if (emailAddresses != null) {
            for (String emailAddress : emailAddresses) {
                this.addEmailAddress(new EmailAddress(emailAddress));
            }
        }
    }

    public void setEmailAddresses(EmailAddress... emailAddresses) throws InvalidEmailAddressException {
        this.emailAddresses = Arrays.asList(emailAddresses);
    }

    public void addEmailAddresses(List<String> emailAddresses) throws InvalidEmailAddressException {
        for (String emailAddress : emailAddresses) {
            this.addEmailAddress(new EmailAddress(emailAddress));
        }
    }

    public void setPhoneNumbersFromStrList(List<String> phoneNumbers) {
        this.phoneNumbers = new ArrayList<PhoneNumber>();
        if (phoneNumbers != null) {
            for (String phoneNumber : phoneNumbers) {
                this.addPhoneNumber((new PhoneNumber(phoneNumber)));
            }
        }
    }

    public void addPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumbers.add(phoneNumber);
    }

    public PhoneNumber getFirstPhoneNumber() {
        for (ContactDetail contactDetail : this.contactDetails) {
            if (contactDetail instanceof PhoneNumber) {
                return (PhoneNumber) contactDetail;
            }
        }
        return null;
    }

    public void setPhoneNumbers(PhoneNumber... phoneNumberArr) {
        this.phoneNumbers.clear();
        this.phoneNumbers.addAll(Arrays.asList(phoneNumberArr));
    }

    public boolean hasPhoneNumber() {
        return this.phoneNumbers != null && !this.phoneNumbers.isEmpty();

    }

    public void addPhoneNumbers(List<String> phoneNumbers) {
        for (String phoneNumber : phoneNumbers) {
            this.addPhoneNumber((new PhoneNumber(phoneNumber)));
        }
    }

    public void setFaxNumbersFromStrList(List<String> faxNumbers) {
        this.faxNumbers = new ArrayList<PhoneNumber>();
        if (faxNumbers != null) {
            for (String faxNumber : faxNumbers) {
                this.addFaxNumber(new PhoneNumber(faxNumber));
            }
        }
    }

    public void setFaxNumbers(PhoneNumber... faxNumbers) {
        this.faxNumbers = Arrays.asList(faxNumbers);
    }

    public void setUrls(Url... urls) {
        this.urls = Arrays.asList(urls);
    }

    public void setUrls(List<Url> urls) {
        this.urls = urls;
    }

    public void setUrlsFromStrList(List<String> urlList) {
        this.urls = new ArrayList<Url>();
        if (urlList != null) {
            for (String url : urlList) {
                this.addUrl((new Url(url)));
            }
        }
    }

    public void addFaxNumbers(List<String> faxNumbers) {
        for (String faxNumber : faxNumbers) {
            this.addFaxNumber(new FaxNumber(faxNumber));
        }
    }

    public PostalAddress getPostalAddress() throws ServiceException {
        return this.postalAddress;
    }

    public void setPostalAddress(PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
    }

    public List<EmailAddress> getEmailAddresses() {
        List<EmailAddress> emailAddresses = new ArrayList<EmailAddress>();
        for (ContactDetail contactDetail : this.contactDetails) {
            if (contactDetail instanceof EmailAddress) {
                emailAddresses.add((EmailAddress) contactDetail);
            }
        }
        return emailAddresses;
    }

    public Url getFirstUrl() {
        if (this.hasUrl()) {
            return this.urls.get(0);
        } else {
            return null;
        }

    }

    public boolean hasUrl() {
        return this.urls != null && this.urls.size() > 0;
    }

    public boolean hasFaxNumber() {
        return this.faxNumbers != null && this.faxNumbers.size() > 0;
    }

    public PhoneNumber getFirstFaxNumber() {
        for (ContactDetail contactDetail : this.contactDetails) {
            if (contactDetail instanceof FaxNumber) {
                return (PhoneNumber) contactDetail;
            }
        }
        return null;
    }

    public boolean hasEmailAddress() {
        return this.emailAddresses != null && this.emailAddresses.size() > 0;
    }

    public EmailAddress getFirstEmailAddress() {
        if (this.hasEmailAddress()) {
            return this.emailAddresses.get(0);
        } else {
            return null;
        }
    }

    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        for (PhoneNumber phoneNumber : phoneNumbers) {
            this.contactDetails.add(phoneNumber);
        }
    }

    public void setFaxNumbers(List<PhoneNumber> faxNumbers) {
        this.faxNumbers = faxNumbers;
    }

    public void setEmailAddresses(List<EmailAddress> emailAddresses) {
        for (EmailAddress emailAddress : emailAddresses) {
            this.contactDetails.add(emailAddress);
        }
    }

    public boolean hasPostalAddress() throws ServiceException {
        return this.getPostalAddress() != null;
    }

    public Date getUpdateDate() {
        return DateUtils.clone(this.updateDate);
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = DateUtils.clone(updateDate);
    }

    public Date getCreateDate() {
        return DateUtils.clone(this.createDate);
    }

    public void setCreateDate(Date createDate) {
        this.createDate = DateUtils.clone(createDate);
    }

    public boolean hasCreateDate() {
        return this.createDate != null;
    }

    public boolean hasUpdateDate() {
        return this.updateDate != null;
    }

    public boolean hasName() {
        return this.getName() != null;
    }

    public String getName() {
        if (this.identity == null) {
            return null;
        }
        return this.getIdentity().getName();
    }

    public List<ContactDetail> getContactDetails() {
        return this.contactDetails;
    }

    public void setContactDetails(List<ContactDetail> contactDetails) {
        this.contactDetails = contactDetails;
    }

}
