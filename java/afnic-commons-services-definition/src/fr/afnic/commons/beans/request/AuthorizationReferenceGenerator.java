/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/request/AuthorizationReferenceGenerator.java#4 $
 * $Revision: #4 $
 * $Author: ginguene $
 */
package fr.afnic.commons.beans.request;

import fr.afnic.commons.beans.Authorization;
import fr.afnic.utils.StringUtils;

/**
 * Classe utilitaire permettant de générer des chaines de caractères.
 * 
 * @author alaphilippe
 * 
 */
public final class AuthorizationReferenceGenerator {

    private AuthorizationReferenceGenerator() {

    }

    /**
     * Génere la refence de l'autorisation, c'est à dire le code d'autorisation.<br/>
     * C'est ce code qui permettra de demander la creation ou le recover d'un domaine<br/>
     * le format est le suivant:<br/>
     * 8 premières lettre de l'opération concerné<br/>
     * + la date au format yyyyMMdd<br/>
     * + mot de 8 lettres aléatoire <br/>
     * + 8 derniers chiffres du timestamp. <br/>
     * L'utilisation de la date et du timestamp dans le code, garantie l'unicité<br/>
     * des code d'autorisation générée
     * 
     * @param authorization
     *            authorization à partir de laquelle on veut générer une référence
     * 
     * @return une reference unique pouvant servir de code pour une autorisation
     * 
     */
    public static String generateAuthorizationReference(final Authorization authorization) {

        if (authorization == null) {
            throw new IllegalArgumentException("authorization cannot be null");
        }

        if (!authorization.hasOperation()) {
            throw new IllegalArgumentException("authorization.operation must be initialized");
        }

        String reference = authorization.getOperation().toString().toUpperCase().substring(0, 8)
                           + StringUtils.REFERENCE_DATE_FORMAT.format(authorization.getCreateDate())
                           + StringUtils.generateWord(8)
                           + StringUtils.getTimestampEnd();

        return reference;

    }
}
