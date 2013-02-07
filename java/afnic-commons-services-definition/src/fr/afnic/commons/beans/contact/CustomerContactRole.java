package fr.afnic.commons.beans.contact;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedInternalObject;
import fr.afnic.commons.services.exception.ServiceException;

public enum CustomerContactRole implements IDescribedInternalObject {

    Noc("Administrateur réseau"),
    Technical("Technique"),
    Administrative("Administratif"),
    Business("Commercial"),
    Billing("Facturation"),
    Product("Produit"),
    NotDefined("Indéfini"),
    Holder("Titulaire"),
    Admin("Administrateur"),
    ContactBilling("Facturation");

    private String description;

    private CustomerContactRole(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() throws ServiceException {
        return this.description;
    }

    @Override
    public String getDescription(Locale locale) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
        // TODO Auto-generated method stub

    }

    @Override
    public String getDictionaryKey() {
        // TODO Auto-generated method stub
        return null;
    }

}
