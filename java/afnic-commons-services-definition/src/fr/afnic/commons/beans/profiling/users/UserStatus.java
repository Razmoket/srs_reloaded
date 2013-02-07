package fr.afnic.commons.beans.profiling.users;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedInternalObject;
import fr.afnic.commons.services.exception.ServiceException;

public enum UserStatus implements IDescribedInternalObject {

    Active("Actif"),
    Inactive("Inactif"),
    Deleted("Supprimé");

    private String description;

    private UserStatus(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() throws ServiceException {
        return this.description;
    }

    @Override
    public String getDescription(Locale locale) throws ServiceException {
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
