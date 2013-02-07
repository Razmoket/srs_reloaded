package fr.afnic.commons.beans.operations.qualification;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedInternalObject;
import fr.afnic.commons.services.exception.ServiceException;

public enum IncoherentStatus implements IDescribedInternalObject {

    Incoherent("Informations marquées comme étant fantaisistes"),
    Coherent(""), ;

    private final String description;

    private IncoherentStatus(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() throws ServiceException {
        return this.description;

    }

    @Override
    public String getDescription(Locale locale) throws ServiceException {
        return this.getDescription();
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {

    }

    @Override
    public String getDictionaryKey() {
        return this.toString();
    }

}
