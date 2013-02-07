/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.validatable;

/**
 * Interface décrivant un object dont le contenu peut etre validé. <br/>
 * Lors de l'appel à validate, si tout est ok, rien ne se passe sinon une InvalidFormatException est levée
 * 
 * @author ginguene
 * 
 */
public interface IValidatable {

    /**
     * Valide le contenu de l'objet.<br/>
     * Si ce dernier n'est pas correctement rempli, une InvalidDataException est retournée.<br/>
     * Cette exception contient la description de tous les problèmes rencontrés.
     * */
    public void validate() throws InvalidDataException;

}
