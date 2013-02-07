/**
 * 
 */
package fr.afnic.commons.beans.contract;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedInternalObject;
import fr.afnic.commons.services.exception.ServiceException;

/**
 * Statut d'un contrat.
 * 
 * 
 */
public enum ContractStatus implements IDescribedInternalObject {

    Active("Actif"),
    Deleted("Supprimé"),
    Blocked("Bloqué"), ;

    private String description;

    private ContractStatus(String description) {
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
