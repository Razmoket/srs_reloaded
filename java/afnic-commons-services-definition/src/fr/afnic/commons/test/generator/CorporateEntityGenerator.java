/*
 * $Id: LegalStructureGenerator.java,v 1.2 2010/07/15 14:55:44 ginguene Exp $
 * $Revision: 1.2 $
 * $Author: ginguene $
 */

package fr.afnic.commons.test.generator;

import java.util.ArrayList;

import fr.afnic.commons.beans.corporateentity.CorporateEntity;
import fr.afnic.commons.beans.corporateentity.Siren;
import fr.afnic.commons.beans.corporateentity.TradeMark;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.exception.GeneratorException;
import fr.afnic.utils.StringUtils;

/**
 * Permet de générer des structures de test.
 * 
 * @author ginguene
 * 
 */
public final class CorporateEntityGenerator {

    /** Siren de l'AFNIC */
    public static final String VALID_SIREN = "414757567";

    /**
     * Empeche l'instanciation de cette classe utilitaire.
     * 
     */
    private CorporateEntityGenerator() {
    }

    /**
     * Genere une LegalStructure de type OtherLegalStructure.
     * 
     * @return Une legalstructure de Type Other avec une description aléatoire.
     * 
     * @throws GeneratorException
     *             Si la génération du jeu de test échoue.
     */
    public static CorporateEntity generateOtherLegalStructureWithRandomDescription() throws GeneratorException {

        CorporateEntity other;
        try {
            other = CorporateEntity.createOther(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            other = CorporateEntityGenerator.generateCommonsContent(other);
            other.setDescription("other " + System.currentTimeMillis());
            return other;
        } catch (ServiceException e) {
            throw new GeneratorException("generateOtherLegalStructureWithRandomDescription() failed", e);
        }
    }

    /**
     * Retourne une entreprise associée à une marque générée aléatoirement
     * 
     * 
     * @return un contact lié à une structure de type personne morale.
     * 
     * @throws GeneratorException
     *             Si la génération du jeu de test échoue.
     */
    public static CorporateEntity generateCompanyWithRandomTradeMark() throws GeneratorException {
        try {
            CorporateEntity company = CorporateEntity.createCompany(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            company = CorporateEntityGenerator.generateCommonsContent(company);
            company.setTradeMark(new TradeMark("UNEMARQUE" + System.currentTimeMillis()));
            return company;
        } catch (Exception e) {
            throw new GeneratorException("generateCompanyWithRandomTradeMark() failed", e);
        }
    }

    public static CorporateEntity generateCompanyWithValidSiren() throws GeneratorException {
        try {
            CorporateEntity legalStruture = CorporateEntity.createCompany(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            legalStruture = CorporateEntityGenerator.generateCommonsContent(legalStruture);
            legalStruture.setSiren(new Siren(CorporateEntityGenerator.VALID_SIREN));
            return legalStruture;
        } catch (Exception e) {
            throw new GeneratorException("generateCompanyWithValidSiren() failed", e);
        }

    }

    public static CorporateEntity generateNonProfitOrganizationWithValidSiren() throws GeneratorException {
        try {
            CorporateEntity nonProfitOrganization = CorporateEntity.createNonProfit(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            nonProfitOrganization = CorporateEntityGenerator.generateCommonsContent(nonProfitOrganization);
            nonProfitOrganization.setSiren(new Siren(CorporateEntityGenerator.VALID_SIREN));
            return nonProfitOrganization;
        } catch (Exception e) {
            throw new GeneratorException("generateNonProfitOrganizationWithValidSiren() failed", e);
        }

    }

    protected static CorporateEntity generateCommonsContent(CorporateEntity toGenerate) throws GeneratorException {
        toGenerate.setAcronym(StringUtils.generateWord(5));
        toGenerate.setBrand(StringUtils.generateWord(5));
        toGenerate.setOrgnaizationName(StringUtils.generateWord(15));
        toGenerate.setActive(true);

        toGenerate.setTradeName(StringUtils.generateWord(15));

        toGenerate.addEmailAddress(EmailAddressGenerator.getEmailAddress());
        toGenerate.addPhoneNumber(PhoneNumberGenerator.getPhoneNumber());
        toGenerate.addFaxNumber(PhoneNumberGenerator.getFaxNumber());

        ArrayList<String> urlList = new ArrayList<String>();
        urlList.add("www." + StringUtils.generateWord(10) + ".fr");

        toGenerate.setUrlsFromStrList(urlList);
        toGenerate.setThirdPartyLegalAnnounceHistoryUrl(urlList);
        toGenerate.setThirdPartyStructureInfoUrl(urlList);
        toGenerate.setThirdPartyStructureSearchUrl(urlList);
        toGenerate.setPostalAddress(PostalAddressGenerator.generateRandomPostalAddressInParis());

        return toGenerate;
    }
}
