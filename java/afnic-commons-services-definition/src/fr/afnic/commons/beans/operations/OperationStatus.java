package fr.afnic.commons.beans.operations;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedInternalObject;
import fr.afnic.commons.services.exception.ServiceException;

public enum OperationStatus implements IDescribedInternalObject {

    Succed,
    Failed,
    Checked,
    Warn,
    Pending;

    @Override
    public String getDescription() throws ServiceException {
        return null;
    }

    @Override
    public String getDescription(Locale locale) {
        return null;
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
    }

    @Override
    public String getDictionaryKey() {
        return this.toString();
    }

}
