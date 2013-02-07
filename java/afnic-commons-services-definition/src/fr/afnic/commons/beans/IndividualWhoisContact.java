/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/IndividualWhoisContact.java#6 $
 * $Revision: #6 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans;

import java.util.Date;

import fr.afnic.commons.beans.contact.identity.IndividualIdentity;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * 
 * Représente un contact désignant une personne morale
 * 
 * @author ginguene
 * 
 */
public class IndividualWhoisContact extends WhoisContact {

    protected IndividualIdentity identity = new IndividualIdentity();

    public IndividualWhoisContact(UserId userId, TldServiceFacade tld) {
        super(new IndividualIdentity(), userId, tld);
    }

    private static final long serialVersionUID = 1L;

    public Date getBirthDate() {
        return this.identity.getBirthDate();
    }

    public void setBirthDate(final Date birthDate) {
        this.identity.setBirthDate(birthDate);
    }

    public String getBirthPostCode() {
        return this.identity.getBirthPostCode();
    }

    public void setBirthPostCode(String birthPostCode) {
        this.identity.setBirthPostCode(birthPostCode);
    }

    public String getBirthCity() {
        return this.identity.getBirthCity();
    }

    public void setBirthCity(String birthCity) {
        this.identity.setBirthCity(birthCity);
    }

    public String getBirthCountryCode() {
        return this.identity.getBirthCountryCode();
    }

    public void setBirthCountryCode(String birthCountryCode) {
        this.identity.setBirthCountryCode(birthCountryCode);
    }

    public String getFirstName() {
        return this.identity.getFirstName();
    }

    public void setFirstName(String firstName) {
        this.identity.setFirstName(firstName);
    }

    public String getLastName() {
        return this.identity.getLastName();
    }

    public void setLastName(String lastName) {
        this.identity.setLastName(lastName);
    }

    public boolean hasBirthDate() {
        return this.identity.hasBirthDate();
    }

    public boolean hasBirthCity() {
        return this.identity.hasBirthCity();
    }

    public boolean hasFirstName() {
        return this.identity.hasFirstName();
    }

    public boolean hasLastName() {
        return this.identity.hasLastName();
    }

    @Override
    public IndividualIdentity getIdentity() {
        return this.identity;
    }

}
