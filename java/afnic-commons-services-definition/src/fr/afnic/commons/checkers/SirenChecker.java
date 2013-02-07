/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.checkers;

import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.exception.invalidformat.InvalidSirenException;

public class SirenChecker implements IInternalChecker {

    private static final long serialVersionUID = 1L;

    @Override
    public String check(String siren) throws InvalidFormatException {
        if (this.isSirenSyntaxValide(siren)) {
            return siren;
        } else {
            throw new InvalidSirenException(siren);
        }
    }

    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "IM_EXIT", justification = "i ne peut pas etre négatif lors de l'instruction ((i % 2) == 1) ")
    private boolean isSirenSyntaxValide(String siren) {
        int total = 0;
        int digit = 0;

        try {
            int intValue = Integer.parseInt(siren);

            // Pour les sirent 000000000
            if (intValue == 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        if (siren.length() != 9) {
            return false;
        }

        for (int i = 0; i < siren.length(); i++) {
            /**
             * Recherche les positions paires : 2ème, 4ème, 6ème et 8ème chiffre que l'on multiplie par 2 petite différence avec la définition
             * ci-dessus car ici on travail de gauche à droite
             */

            if (i % 2 != 0) {
                digit = Integer.parseInt(String.valueOf(siren.charAt(i))) * 2;
                /**
                 * si le résultat est >9 alors il est composé de deux digits tous les digits devant s'additionner et ne pouvant être >19 le calcule
                 * devient : 1 + (digit -10) ou : digit - 9
                 */
                if (digit > 9) digit -= 9;
            } else {
                digit = Integer.parseInt(String.valueOf(siren.charAt(i)));
            }
            total += digit;
        }

        /** Si la somme est un multiple de 10 alors le SIREN est valide */
        return ((total % 10) == 0);
    }
}
