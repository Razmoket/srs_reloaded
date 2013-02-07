/*
 * $Id: DomainGenerator.java,v 1.9 2010/09/28 12:25:17 ginguene Exp $ $Revision: 1.9 $ $Author: ginguene $
 */

package fr.afnic.commons.test.generator;

import java.util.ArrayList;
import java.util.List;

import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.corporateentity.CorporateEntity;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.exception.TimeOutException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.exception.GeneratorException;

/**
 * Permet de créer des domaines pour des tests.<br/>
 * 
 * 
 * @author ginguene
 * 
 */
public final class DomainGenerator {

    /** sequence permettant de ne pas crééer 2 fois le meme nom de domaine */
    private static int sequence = 1;

    /**
     * Empeche l'instanciation de cette classe utilitaire.
     * 
     */
    private DomainGenerator() {
    }

    /**
     * retourne un nom de domain qui n'existe pas dans les bases<br/>
     * et n'a jamais ete utilisé lors de tests précédents.
     * 
     * @return {@link String}
     */
    public static String getUnusedDomainName() {
        return DomainGenerator.createUnusedName("a-domain-for-test-", "fr");
    }

    /**
     * retourne un nom de domain qui possède un mauvais format<br/>
     * 
     * @return {@link String}
     */
    public static String getInvalidDomainName() {
        return "***";
    }

    /**
     * Creation d'un nouveau domaine, on cree un contact rattaché au BE test pour etre son titulaire. <br/>
     * On utilise le parametre pour la premiere partie du nom de domaine.<br/>
     * La 2eme partie est generée de manière a ce que chaque nom de domaine soit unique.<br/>
     * 
     * @param baseName
     *            Première partie du nom de domain à créer.
     * 
     * @param legalStructure
     *            Legastructure correspondant au contact
     * 
     * @return Le nom du domaine créé.
     * 
     ** @throws GeneratorException
     *             Si la génération du jeu de test échoue.
     */
    public static String createNewFrDomainWithHolderWithLegalStructure(String baseName, CorporateEntity legalStructure) throws GeneratorException {
        String domainName = DomainGenerator.createUnusedName(baseName, "fr");
        WhoisContact holder = ContactGenerator.createCorporateEntityContactForLegalStructure(legalStructure);

        // on s'assure que l'addresse postale du contact admin se trouve en France
        legalStructure.setPostalAddress(PostalAddressGenerator.generateRandomPostalAddressInParis());
        WhoisContact contact = ContactGenerator.createCorporateEntityContactForLegalStructure(legalStructure);
        DomainGenerator.createDomainWithNoTimeout(domainName, holder, contact);
        return domainName;
    }

    /**
     * Creation d'un nombre de domaines précisé en parametre nouveau domaine.<br/>
     * On crée un contact rattaché au BE test pour etre son titulaire. <br/>
     * On utilise le parametre pour la premiere partie du nom de domaine.<br/>
     * La 2eme partie est generée de manière a ce que chaque nom de domaine soit unique.<br/>
     * 
     * 
     * @param baseName
     *            Première partie du nom de domain à créer.
     * 
     * @param legalStructure
     *            Legastructure correspondant au contact
     * 
     * 
     * @param nbDomainsToCreate
     *            Nombre de domaines à créer.
     * 
     * @return La liste des noms de domaine créés.
     * 
     * 
     * @throws GeneratorException
     *             Si la génération du jeu de test échoue.
     */
    public static List<String> createNbNewFrDomainWithHolderWithLegalStructure(String baseName, CorporateEntity legalStructure, int nbDomainsToCreate)
                                                                                                                                                     throws GeneratorException {

        WhoisContact holder = ContactGenerator.createCorporateEntityContactForLegalStructure(legalStructure);

        // on s'assure que l'addresse postale du contact admin se trouve en France
        legalStructure.setPostalAddress(PostalAddressGenerator.generateRandomPostalAddressInParis());
        WhoisContact contact = ContactGenerator.createCorporateEntityContactForLegalStructure(legalStructure);

        List<String> createdDomainNames = new ArrayList<String>();
        for (int i = 0; i < nbDomainsToCreate; i++) {
            String domainName = DomainGenerator.createUnusedName(baseName, "fr");

            DomainGenerator.createDomainWithNoTimeout(domainName, holder, contact);
            createdDomainNames.add(domainName);
        }

        return createdDomainNames;
    }

    /**
     * Creation d'un nombre de domaines prÃ©cisÃ© en parametre nouveau domaine.<br/>
     * On crÃ©e un contact rattachÃ© au BE test pour etre son titulaire. <br/>
     * On utilise le parametre pour la premiere partie du nom de domaine.<br/>
     * La 2eme partie est generÃ©e de maniÃ¨re a ce que chaque nom de domaine soit unique.<br/>
     * 
     * 
     * @param baseName
     *            PremiÃ¨re partie du nom de domain Ã  crÃ©er.
     * 
     * @param legalStructure
     *            Legastructure correspondant au contact
     * 
     * 
     * @param nbDomainsToCreate
     *            Nombre de domaines Ã  crÃ©er.
     * 
     * @return La liste des noms de domaine crÃ©Ã©s.
     * 
     * 
     * @throws GeneratorException
     *             Si la gÃ©nÃ©ration du jeu de test Ã©choue.
     */
    public static List<String> createNbNewFrDomainWithHolderWithLegalStructure(String baseName, CorporateEntity legalStructure, String nicHandle, int nbDomainsToCreate)
                                                                                                                                                                       throws GeneratorException {

        WhoisContact holder = null;
        try {
            holder = AppServiceFacade.getWhoisContactService().getContactWithHandle(nicHandle, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        } catch (ServiceException e) {
            throw new GeneratorException("createNbNewFrDomainWithHolderWithLegalStructure() failed", e);
        }

        // on s'assure que l'addresse postale du contact admin se trouve en France
        legalStructure.setPostalAddress(PostalAddressGenerator.generateRandomPostalAddressInParis());
        WhoisContact contact = ContactGenerator.createCorporateEntityContactForLegalStructure(legalStructure);

        List<String> createdDomainNames = new ArrayList<String>();
        for (int i = 0; i < nbDomainsToCreate; i++) {
            String domainName = DomainGenerator.createUnusedName(baseName, "fr");

            DomainGenerator.createDomainWithNoTimeout(domainName, holder, contact);
            createdDomainNames.add(domainName);
        }

        return createdDomainNames;
    }

    /**
     * Creation d'un nouveau domaine, on cree un contact rattaché au BE test pour etre son titulaire. <br/>
     * On utilise le parametre pour la premiere partie du nom de domaine.<br/>
     * La 2eme partie est generée de manière a ce que chaque nom de domaine soit unique.<br/>
     * Le titulaire est domicilié sur l'ile de la réunion.
     * 
     * @param baseName
     *            Première partie du nom de domain à créer.
     * 
     * @param legalStructure
     *            Legastruture correspondant au contact
     * 
     * @return Le nom du domaine créé.
     * 
     * @throws GeneratorException
     *             Si la génération du jeu de test échoue.
     */
    public static String createNewReDomainWithHolderWithLegalStructure(String baseName, CorporateEntity legalStructure) throws GeneratorException {
        String domainName = DomainGenerator.createUnusedName(baseName, "re");

        // on s'assure que l'addresse postale du contact admin se trouve en France
        PostalAddress postalAddress = PostalAddressGenerator.generatePostalAddressInReunionIsland();
        legalStructure.setPostalAddress(postalAddress);

        WhoisContact holder = ContactGenerator.createCorporateEntityContactForLegalStructure(legalStructure);
        WhoisContact contact = ContactGenerator.createCorporateEntityContactForLegalStructure(legalStructure);
        DomainGenerator.createDomainWithNoTimeout(domainName, holder, contact);
        return domainName;
    }

    /**
     * Creation d'un nouveau domaine, on cree un contact rattaché au BE test pour etre son titulaire. <br/>
     * On utilise le parametre pour la premiere partie du nom de domaine.<br/>
     * La 2eme partie est generée de manière a ce que chaque nom de domaine soit unique. <br/>
     * 
     * @param baseName
     *            Première partie du nom de domain à créer.
     * 
     * @return Le nom du domaine créé.
     * 
     * @throws GeneratorException
     *             Si la génération du jeu de test échoue.
     * 
     */
    public static String createNewDomain(String baseName) throws GeneratorException {
        try {
            String domainName = DomainGenerator.createUnusedName(baseName, "fr");

            WhoisContact contact = ContactGenerator.createCorporateEntityContact();
            WhoisContact holder = ContactGenerator.createCorporateEntityContact();

            DomainGenerator.createDomainWithNoTimeout(domainName, holder, contact);

            try {
                AppServiceFacade.getDomainService().getDomainWithName(domainName, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            } catch (NotFoundException e) {
                throw new GeneratorException("Created domain was not found", e);
            }

            return domainName;
        } catch (Exception e) {
            throw new GeneratorException("createNewDomain() failed", e);
        }
    }

    public static String createNewDomain(String baseName, String extension) throws GeneratorException {
        if (!extension.startsWith(".")) throw new GeneratorException("extension must start with a dot");

        try {
            String domainName = DomainGenerator.createUnusedName(baseName, "fr");

            WhoisContact contact = ContactGenerator.createCorporateEntityContact();
            WhoisContact holder = ContactGenerator.createCorporateEntityContact();

            DomainGenerator.createDomainWithNoTimeout(domainName, holder, contact);
            return domainName;
        } catch (Exception e) {
            throw new GeneratorException("createNewDomain() failed", e);
        }
    }

    /**
     * Vérifie qu'un domaine existe si ce n'est pas le cas, il est créé.
     * 
     * @param domainName
     *            Nom de domain à vérifier ou à créer.
     * 
     * @return Le domaine
     * 
     * @throws GeneratorException
     *             Si la génération du jeu de test échoue.
     */
    public static Domain getOrCreateDomain(String domainName) throws GeneratorException {
        try {
            try {
                return AppServiceFacade.getDomainService().getDomainWithName(domainName, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            } catch (NotFoundException e) {
                WhoisContact contact = ContactGenerator.createCorporateEntityContact();
                WhoisContact holder = ContactGenerator.createCorporateEntityContact();
                return DomainGenerator.createDomainWithNoTimeout(domainName, holder, contact);
            }
        } catch (Exception e) {
            throw new GeneratorException("createNewDomain() failed", e);
        }
    }

    /**
     * Creation d'un nouveau domaine, on fournit un contact rattaché au BE test comme titulaire. <br/>
     * On utilise le parametre pour la premiere partie du nom de domaine.<br/>
     * La 2eme partie est generée de manière a ce que chaque nom de domaine soit unique. <br/>
     * 
     * @param baseName
     *            Première partie du nom de domain à créer.
     * @param holder
     *            titulaire du domaine
     * 
     * @return Le nom du domaine créé.
     * 
     * @throws GeneratorException
     *             Si la génération du jeu de test échoue.
     */
    public static String createNewDomainWithHolder(String baseName, WhoisContact holder) throws GeneratorException {
        try {
            String domainName = DomainGenerator.createUnusedName(baseName, "fr");
            WhoisContact contact = ContactGenerator.createCorporateEntityContactWithCustomerCode(holder.getRegistrarCode());
            DomainGenerator.createDomainWithNoTimeout(domainName, holder, contact);
            return domainName;
        } catch (Exception e) {
            throw new GeneratorException("createNewDomain() failed", e);
        }
    }

    /**
     * Creation d'un nouveau domaine, on fournit un contact rattaché au BE test comme admin. <br/>
     * On utilise le parametre pour la premiere partie du nom de domaine.<br/>
     * La 2eme partie est generée de manière a ce que chaque nom de domaine soit unique. <br/>
     * 
     * @param baseName
     *            Première partie du nom de domain à créer.
     * @param contact
     *            contact admin du domaine
     * 
     * @return Le nom du domaine créé.
     * 
     * @throws GeneratorException
     *             Si la génération du jeu de test échoue.
     */
    public static String createNewDomainWithAdmin(String baseName, WhoisContact contact) throws GeneratorException {
        try {
            String domainName = DomainGenerator.createUnusedName(baseName, "fr");
            WhoisContact holder = ContactGenerator.createCorporateEntityContactWithCustomerCode(contact.getRegistrarCode());
            DomainGenerator.createDomainWithNoTimeout(domainName, holder, contact);
            return domainName;
        } catch (Exception e) {
            throw new GeneratorException("createNewDomain() failed", e);
        }
    }

    /**
     * Crée un nom de domaine dont on est sur qu'il n'existe pas.<br/>
     * On part d'un nom de base, puis on y ajoute le timestamp et enfin une sequence.<br/>
     * 
     * 
     * @param baseName
     * @param extension
     * @return
     */
    private static String createUnusedName(String baseName, String extension) {
        return baseName + "-" + System.currentTimeMillis() + (DomainGenerator.sequence++) + "." + extension;

    }

    /**
     * Crée un domaine en ignorant le timeout.<br/>
     * Si un timeout se produit, on enter dans une boucle d'attente (10 essais)<br/>
     * qui teste régulièrement la création ou non du domaine.
     * 
     * 
     * 
     * 
     * @param domainName
     *            Nom du domaine à créer
     * 
     * @param holder
     *            titulaire du domaine
     * 
     * @param adminTechContact
     *            Contact technique et administratif du domaine
     * 
     * @return le Domaine créé.
     * 
     * @throws GeneratorException
     *             Si la génération du jeu de test échoue (timeout/ système de ticket bloqué)
     */
    private static Domain createDomainWithNoTimeout(String domainName, WhoisContact holder, WhoisContact adminTechContact) throws GeneratorException {

        try {
            try {

                return AppServiceFacade.getDomainService().createDomain(domainName, "authInfo", holder.getRegistrarCode(), holder, adminTechContact, adminTechContact, UserGenerator.getRootUserId(),
                                                                        TldServiceFacade.Fr);
            } catch (TimeOutException e) {
                // Le timeout signifie que le système de ticket n'a pas encore fini
                // la création mais il suffit d'attendre pour que le domaine arrive
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(10000);
                        return AppServiceFacade.getDomainService().getDomainWithName(domainName, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
                    } catch (NotFoundException e1) {
                        // Le domaine n'a toujours pas été créé
                    }
                }

                throw new GeneratorException("Nom de domaine non créé:\n" + "vérifier la liste des tickets sur "
                                             + "http://photon.nic.fr/kaplan/intranet/mitctl.pl/notify-queue-list");
            }
        } catch (Exception e) {
            throw new GeneratorException("createDomainWithNoTimeout(" + domainName + ") failed", e);
        }
    }

}
