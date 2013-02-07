package fr.afnic.commons.beans.operations.qualification;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedInternalObject;
import fr.afnic.commons.services.exception.ServiceException;

public enum QualificationSource implements IDescribedInternalObject {

    Auto("Auto"),
    Plaint("Plainte"),
    Control("Controle"),
    Reporting("Signalement"),
    Survey("Surveillance");

    private final String description;

    private QualificationSource(String description) {
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
