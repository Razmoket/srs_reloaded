package fr.afnic.commons.checkers;

import java.io.Serializable;

import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;

/**
 * Toutes les implémentations de cette classe ont pour but de <br/>
 * valider un format de donnée.
 * 
 * 
 * 
 */
public interface IInternalChecker extends Serializable {

    /**
     * Si la donnée n'est pas valide une exeption de type InvalidFormatException est levée. Si la donnée est valide, elle est retournée tel quel.
     * 
     * @param data
     * @throws InvalidFormatException
     */
    public String check(String data) throws InvalidFormatException;

}
