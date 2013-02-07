package fr.afnic.commons.checkers;

import java.io.Serializable;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Toutes les implémentations de cette classe ont pour but de <br/>
 * valider un format de donnée.
 * 
 * 
 * 
 */
public interface IExternalChecker extends Serializable {

    /**
     * Si la donnée n'est pas valide une exeption de type InvalidFormatException est levée. Si la donnée est valide, elle est retournée tel quel.
     * 
     * @param data
     * @throws InvalidFormatException
     */
    public String check(String data, UserId userId, TldServiceFacade tld) throws InvalidFormatException;

}
