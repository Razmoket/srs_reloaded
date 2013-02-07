/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.checkers;

import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;

/**
 * Checker pour qui toutes les données dont valides.<br/>
 * Utilisé pour des classes de Details qui n'ont pas encore de checker.<br/>
 * 
 * 
 * @author ginguene
 * 
 */
public class TrueChecker implements IInternalChecker {

    private static final long serialVersionUID = 1L;

    @Override
    public String check(String data) throws InvalidFormatException {
        return data;
    }

}
