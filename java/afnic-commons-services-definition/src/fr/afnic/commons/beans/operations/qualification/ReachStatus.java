package fr.afnic.commons.beans.operations.qualification;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedInternalObject;
import fr.afnic.commons.services.exception.ServiceException;

public enum ReachStatus implements IDescribedInternalObject {

    NotIdentified("Non identifié"),
    PendingEmail("En attente mail"),
    PendingEmailReminder("En attente rappel de mail"),
    PendingPhone("En attente télephone"),
    Email("Joignable par mail"),
    Phone("Joignable par téléphone"),
    NotReachable("Non joignable");

    private final String description;

    private ReachStatus(String description) {
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
