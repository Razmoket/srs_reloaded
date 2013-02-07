/*
 * $Id: $ $Revision: $
 */

package fr.afnic.commons.beans.request;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedInternalObject;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;

public enum AuthorizationPreliminaryExamStatus implements IDescribedInternalObject {

    Pending("En attente / Pending"),
    Running("En cours d'examen / In progress"),
    Aborted("Abandonné / Aborted"),
    Rejected("Rejeté / Rejected"),
    Accepted("Accepté / Accepted"),
    Used("Code utilisé / Used");

    private String description;

    private AuthorizationPreliminaryExamStatus(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() throws ServiceException {
        return this.description;
    }

    @Override
    public String getDictionaryKey() {
        return this.toString();
    }

    @Override
    public String getDescription(Locale locale) throws ServiceException {
        return this.description;
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
        throw new NotImplementedException();
    }

}
