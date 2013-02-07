package fr.afnic.commons.beans.contact;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedInternalObject;
import fr.afnic.commons.services.exception.ServiceException;

public enum CustomerContactStatus implements IDescribedInternalObject {

    Active("Actif"),
    Deleted("Supprimé");

    private String description;

    private CustomerContactStatus(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() throws ServiceException {
        return this.description;
    }

    @Override
    public String getDescription(Locale locale) {
        return this.description;
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
    }

    @Override
    public String getDictionaryKey() {
        return this.toString();
    }

}
