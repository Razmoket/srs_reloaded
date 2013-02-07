/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.checkers;

import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.exception.invalidformat.InvalidOperationFormIdException;

/**
 * Vérifie que l'identifiant d'un formulaire est bien un nombre.
 * 
 * @author ginguene
 * 
 */
public class OperationFormIdChecker implements IInternalChecker {

    private static final long serialVersionUID = 1L;

    @Override
    public String check(String data) throws InvalidFormatException {
        int num = -1;
        try {
            num = Integer.parseInt(data);
        } catch (Exception e) {
            // on laisse le num à -1
        }

        if (num >= 0) {
            return data;
        } else {
            throw new InvalidOperationFormIdException(data);
        }

    }
}
