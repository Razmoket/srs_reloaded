package fr.afnic.commons.beans.contact.identity;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import fr.afnic.utils.DateUtils;
import fr.afnic.utils.ToStringHelper;

public class IndividualIdentity extends ContactIdentity {

    private String firstName;
    private String lastName;

    private Date birthDate;
    private String birthCity;

    private String birthCountryCode;
    private String birthPostCode;

    private int id;

    public IndividualIdentity() {
        super();
    }

    public String getBirthPostCode() {
        return this.birthPostCode;
    }

    public void setBirthPostCode(String birthPostCode) {
        this.birthPostCode = birthPostCode;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return DateUtils.clone(this.birthDate);
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = DateUtils.clone(birthDate);
    }

    public String getBirthCity() {
        return this.birthCity;
    }

    public void setBirthCity(String birthCity) {
        this.birthCity = birthCity;
    }

    @Override
    public String toString() {
        return new ToStringHelper("Individual").addAllObjectAttributes(this).toString();
    }

    public boolean hasFirstName() {
        return this.firstName != null;
    }

    public boolean hasLastName() {
        return this.lastName != null;
    }

    public boolean hasBirthDate() {
        return this.birthDate != null;
    }

    public boolean hasBirthCity() {
        return this.birthCity != null;
    }

    public String getBirthCountryCode() {
        return this.birthCountryCode;
    }

    public void setBirthCountryCode(String birthCountryCode) {
        this.birthCountryCode = birthCountryCode;
    }

    @Override
    public String getName() {
        String name = "";
        if (this.firstName != null) {
            name += StringUtils.capitalize(this.firstName);
        }

        if (this.firstName != null && this.lastName != null) {
            name += " ";
        }

        if (this.lastName != null) {
            name += StringUtils.capitalize(this.lastName);
        }

        return name;

    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
