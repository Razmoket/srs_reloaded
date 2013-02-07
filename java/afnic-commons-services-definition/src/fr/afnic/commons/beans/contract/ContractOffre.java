/**
 * 
 */
package fr.afnic.commons.beans.contract;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedInternalObject;
import fr.afnic.commons.services.exception.ServiceException;

/**
 * Type d'offre d'un contrat.
 * 
 * 
 */
public enum ContractOffre implements IDescribedInternalObject {

    Mbe("Bureau Enregistrement"),
    Upm("Utilisateur Personne Morale"),
    Upp("Utilisateur Personne Physique"),
    Ci("Correspondant-Collège International"),
    Hpp("Membre Honneur"),
    Ca("Membre Conseil Administration"),
    Opt1("Option 1"),
    Opt2("Option 2"),
    Squaw("Partenaire SQUAW"),
    Parl("PARL"),
    Institu("Partenaire Institutionnel"),
    Fond("Membre Fondateur"),
    Ampleur("Ampleur"),
    Acces("Accès"),
    Essor("Essor");

    private String description;

    private ContractOffre(String description) {
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
