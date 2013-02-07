/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/contactdetails/PostalAddress.java#33 $
 * $Revision: #33 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.contactdetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.afnic.commons.beans.BusinessObject;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.ToStringHelper;

/**
 * 
 * Classe permettant de stocker toutes les informations sur une adresse geographique.
 * 
 * @author ginguene
 * 
 */
public class PostalAddress extends BusinessObject<PostalAddressId> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String organization;
    protected String city;
    protected String postCode;
    protected Country country;
    protected String countryCode;
    protected String cityCedex;
    protected List<String> street;

    public PostalAddress(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
    }

    public String getPostCode() {
        return this.postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getOrganization() {
        return this.organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryAsString() {
        if (this.country != null) {
            return this.country.getValue();
        } else {
            return "";
        }
    }

    public String getCountryName() throws ServiceException {
        if (this.country != null) {
            return this.country.getDescription(this.userIdCaller, this.tldCaller);
        } else {
            return "";
        }
    }

    public void setCountryCode(String countryCode) {
        try {
            this.countryCode = countryCode;
            this.country = AppServiceFacade.getDictionaryService().getCountry(this.countryCode, this.userIdCaller, this.tldCaller);
        } catch (Exception e) {
            throw new RuntimeException("setCountryCode(" + this.countryCode + ") failed", e);
        }
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountry(Country country) {
        this.country = country;
        if (country != null) {
            this.countryCode = country.getDictionaryKey();
        } else {
            this.countryCode = null;
        }
    }

    public String getCityCedex() {
        return this.cityCedex;
    }

    public void setCityCedex(String cityCedex) {
        this.cityCedex = cityCedex;
    }

    public List<String> getStreet() {
        return this.street;
    }

    public String[] getStreetInArray(int arraySize) {
        if (arraySize <= 0) {
            return null;
        }
        String[] back = new String[arraySize];
        for (int i = 0; i < arraySize; i++) {
            if (this.street.size() >= i + 1) {
                back[i] = this.street.get(i);
            } else {
                back[i] = null;
            }
        }
        return back;
    }

    public void setStreet(List<String> street) {
        this.street = street;
    }

    public void setStreetStr(String streetStr) {
        if (streetStr != null) {
            this.setStreet(streetStr.split("\n"));
        }
    }

    public void setStreet(String... streetArray) {
        if (streetArray != null) {
            this.street = new ArrayList<String>();
            for (String streetLine : streetArray) {
                if (streetLine != null) {
                    this.street.add(streetLine);
                }
            }
        }
    }

    public String getStreetStr() {
        StringBuffer buffer = new StringBuffer();
        if (this.street != null) {
            for (int i = 0; i < this.street.size(); i++) {
                if (i > 0) {
                    buffer.append("\n");
                }
                buffer.append(this.street.get(i));
            }
        }
        return buffer.toString();
    }

    public String getStreetLine(int line) {
        if (this.street == null || this.street.size() <= line) {
            return null;
        } else {
            return this.street.get(line);
        }
    }

    public String getStreetStrWithoutNull() {
        StringBuffer buffer = new StringBuffer();
        boolean addNewLine = false;
        if (this.street != null) {
            for (int i = 0; i < this.street.size(); i++) {
                String tmp = this.street.get(i);
                if (tmp != null) {
                    if (addNewLine) {
                        buffer.append("\n");
                    }
                    addNewLine = true;
                    buffer.append(tmp);
                }
            }
        }
        return buffer.toString();
    }

    public boolean hasOrganization() {
        return this.organization != null && !this.organization.isEmpty();
    }

    public boolean hasCity() {
        return this.city != null;
    }

    public boolean hasPostCode() {
        return this.postCode != null;
    }

    public boolean hasCountry() {
        return this.country != null;
    }

    public boolean hasCityCedex() {
        return this.cityCedex != null;
    }

    public boolean hasStreet() {
        return this.street != null && !this.street.isEmpty();
    }

    public Country getCountry() {
        return this.country;
    }

    @Override
    public String toString() {
        return new ToStringHelper().add("id", this.id)
                                   .add("objectVersion", this.objectVersion)
                                   .add("organization", this.organization)
                                   .add("streets", this.getStreetStr())
                                   .add("postCode", this.postCode)
                                   .add("countryCode", this.countryCode)
                                   .add("city", this.city)
                                   .add("cityCedex", this.cityCedex)
                                   .toString();

    }

}
