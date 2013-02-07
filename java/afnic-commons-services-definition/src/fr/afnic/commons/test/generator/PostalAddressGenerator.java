/*
 * $Id: PostalAddressGenerator.java,v 1.3 2010/07/22 08:19:32 ginguene Exp $
 * $Revision: 1.3 $
 * $Author: ginguene $
 */

package fr.afnic.commons.test.generator;

import java.util.Random;

import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.exception.GeneratorException;
import fr.afnic.utils.StringUtils;

public final class PostalAddressGenerator {

    /**
     * Constructeur privé pour empecher l'implémentation de la classe
     */
    private PostalAddressGenerator() {
    }

    /**
     * Genere une adresse postale aléatoirement située à Paris.
     * 
     * @return Une adresse postale générée aléatoirement située à Paris.
     * @throws ServiceException 
     */
    public static PostalAddress generateRandomPostalAddressInParis() throws GeneratorException {
        PostalAddress postalAddress;
        try {
            postalAddress = new PostalAddress(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        } catch (ServiceException e) {
            throw new GeneratorException(e);
        }
        postalAddress.setCity("Paris");
        postalAddress.setStreet(PostalAddressGenerator.generateStreet());
        postalAddress.setPostCode(PostalAddressGenerator.generateParisPostCode());
        postalAddress.setCountryCode("FR");
        return postalAddress;
    }

    /**
     * Génère un code postale parisien aléatoirement<br/>
     * compris entre 75000 et 75020.
     * 
     * @return Une chaine de caractère correspondant à un code postal parisien
     */
    private static String generateParisPostCode() {
        int postcode = 75000;
        Random random = new Random(System.currentTimeMillis());
        postcode += random.nextInt(20);
        return Integer.toString(postcode);
    }

    /**
     * Genere une adresse postale aléatoirement située à Paris.
     * 
     * @return Une adresse postale générée aléatoirement située à la réunion.
     * @throws ServiceException 
     */
    public static PostalAddress generatePostalAddressInReunionIsland() throws GeneratorException {
        PostalAddress postalAddress;
        try {
            postalAddress = new PostalAddress(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        } catch (ServiceException e) {
            throw new GeneratorException(e);
        }
        postalAddress.setCity("Saint-Denis");
        postalAddress.setStreet(PostalAddressGenerator.generateStreet());
        postalAddress.setPostCode("97400");
        postalAddress.setCountryCode("FR");
        return postalAddress;
    }

    public static PostalAddress generatePostalAddressOutOfFrance() throws GeneratorException {
        PostalAddress postalAddress;
        try {
            postalAddress = new PostalAddress(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        } catch (ServiceException e) {
            throw new GeneratorException(e);
        }
        postalAddress.setCountryCode("IT");
        postalAddress.setStreet("Via S.Rocco, 245");
        postalAddress.setPostCode("31040");
        postalAddress.setCity("Giavera Del Montello");
        return postalAddress;
    }

    /**
     * Génère un nom de rue unique avec le format: <br/>
     * <numéro> rue du général <nom aléatoire>
     * 
     * @return Un nom de rue généré aléatoirement
     */
    private static String generateStreet() {
        Random random = new Random(System.currentTimeMillis());
        int numStreet = random.nextInt(100);
        String street = numStreet
                        + ", rue du général nœl "
                        // + ", rue du général Bob "
                        + StringUtils.generateWord(10).toLowerCase();

        return street;
    }

}
