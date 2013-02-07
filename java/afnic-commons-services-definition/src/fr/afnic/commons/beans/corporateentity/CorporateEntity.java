/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/corporateentity/CorporateEntity.java#4 $
 * $Revision: #4 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.corporateentity;

import java.util.Date;
import java.util.List;

import fr.afnic.commons.beans.contact.GenericContact;
import fr.afnic.commons.beans.contact.identity.CorporateEntityIdentity;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Structure légale pouvant etre titulaire d'un nom de domaine. Cela peut etre une entreprise ou une association
 * 
 * 
 * TODO est-ce que cet objet doit vraiment etre un IDescribedObject? sa méthode getCode() ne semble pas avoir de sens...
 * 
 * @author ginguene
 * 
 */
public class CorporateEntity extends GenericContact {

    private static final long serialVersionUID = 1L;

    // protected CorporateEntityIdentity identity;

    public static CorporateEntity createCompany(UserId userId, TldServiceFacade tld) {
        return new CorporateEntity(OrganizationType.Company, userId, tld);
    }

    public static CorporateEntity createNonProfit(UserId userId, TldServiceFacade tld) {
        return new CorporateEntity(OrganizationType.NonProfit, userId, tld);
    }

    public static CorporateEntity createOther(UserId userId, TldServiceFacade tld) {
        return new CorporateEntity(OrganizationType.Other, userId, tld);
    }

    protected CorporateEntity(OrganizationType type, UserId userId, TldServiceFacade tld) {
        super(new CorporateEntityIdentity());
        this.getIdentity().setOrganizationType(type);
    }

    public final String getOrganizationTypeAsString() {
        return this.getIdentity().getOrganizationType().toString();
    }

    public final OrganizationType getOrganizationType() {
        return this.getIdentity().getOrganizationType();
    }

    public List<String> getThirdPartyLegalAnnounceHistoryUrl() {
        return this.getIdentity().getThirdPartyLegalAnnounceHistoryUrl();
    }

    public void setThirdPartyLegalAnnounceHistoryUrl(List<String> thirdPartyLegalAnnounceHistoryUrl) {
        this.getIdentity().setThirdPartyLegalAnnounceHistoryUrl(thirdPartyLegalAnnounceHistoryUrl);
    }

    public List<String> getThirdPartyStructureInfoUrl() {
        return this.getIdentity().getThirdPartyStructureInfoUrl();
    }

    public void setThirdPartyStructureInfoUrl(List<String> thirdPartyStructureInfoUrl) {
        this.getIdentity().setThirdPartyStructureInfoUrl(thirdPartyStructureInfoUrl);
    }

    public List<String> getThirdPartyStructureSearchUrl() {
        return this.getIdentity().getThirdPartyStructureSearchUrl();
    }

    public void setThirdPartyStructureSearchUrl(List<String> thirdPartyStructureSearchUrl) {
        this.getIdentity().setThirdPartyStructureSearchUrl(thirdPartyStructureSearchUrl);
    }

    public boolean hasThirdPartyLegalAnnounceHistoryUrl() {
        return this.getIdentity().hasThirdPartyLegalAnnounceHistoryUrl();
    }

    public boolean hasThirdPartyStructureInfoUrl() {
        return this.getIdentity().hasThirdPartyStructureInfoUrl();
    }

    public boolean hasThirdPartyStructureSearchUrl() {
        return this.getIdentity().hasThirdPartyStructureSearchUrl();
    }

    /**
     * Retourne l'identifiant le plus pertinent de la structure legale
     * 
     * @return
     */
    public CorporateEntityIdentifier getIdentifier() {
        return this.getIdentity().getIdentifier();
    }

    public boolean hasIntracommunityVat() {
        return this.getIdentity().hasIntracommunityVat();
    }

    public boolean hasSiren() {
        return this.getIdentity().hasSiren();
    }

    public boolean hasSiret() {
        return this.getIdentity().hasSiret();
    }

    public boolean hasWaldec() {
        return this.getIdentity().hasWaldec();
    }

    public boolean hasTradeMark() {
        return this.getIdentity().hasTradeMark();
    }

    /**
     * Indique si un identifier est définit pour la structure
     * 
     * @return
     */
    public boolean hasIdentifier() {
        return this.getIdentity().hasIdentifier();
    }

    public String getActivity() {
        return this.getIdentity().getActivity();
    }

    public void setActivity(String activity) {
        this.getIdentity().setActivity(activity);
    }

    public boolean isActive() {
        return this.getIdentity().isActive();
    }

    public void setActive(boolean isActive) {
        this.getIdentity().setActive(isActive);
    }

    public Date getEstablishedDate() {
        return this.getIdentity().getEstablishedDate();
    }

    public void setEstablishedDate(Date establishedDate) {
        this.getIdentity().setEstablishedDate(establishedDate);
    }

    public Date getClosedDate() {
        return this.getIdentity().getClosedDate();
    }

    public void setClosedDate(Date closedDate) {
        this.getIdentity().setClosedDate(closedDate);
    }

    /** Statut juridique */
    public String getLegalStatus() {
        return this.getIdentity().getLegalStatus();
    }

    /** Statut juridique */
    public void setLegalStatus(String legalStatus) {
        this.getIdentity().setLegalStatus(legalStatus);
    }

    /** Statut juridique */
    public boolean hasLegalStatus() {
        return this.getIdentity().hasLegalStatus();
    }

    /** Raison social de la personne morale */
    public void setOrgnaizationName(String name) {
        this.getIdentity().setOrganizationName(name);
    }

    public String getAcronym() {
        return this.getIdentity().getAcronym();
    }

    public void setAcronym(String acronym) {
        this.getIdentity().setAcronym(acronym);
    }

    public String getBrand() {
        return this.getIdentity().getBrand();
    }

    public void setBrand(String brand) {
        this.getIdentity().setBrand(brand);
    }

    public String getTradeName() {
        return this.getIdentity().getTradeName();
    }

    public void setTradeName(String tradeName) {
        this.getIdentity().setTradeName(tradeName);
    }

    public boolean hasAcronym() {
        return this.getIdentity().hasAcronym();
    }

    public boolean hasBrand() {
        return this.getIdentity().hasBrand();
    }

    public boolean hasTradeName() {
        return this.getIdentity().hasTradeName();
    }

    /**
     * Permet de connaitre le nom de l'implémentation de LegalStructure.
     * 
     * @return Nom court de la classe
     */
    public String getLegalStructureName() {
        return this.getClass().getSimpleName();
    }

    public boolean hasEstablishedDate() {
        return this.getIdentity().hasEstablishedDate();
    }

    public boolean hasClosedDate() {
        return this.getIdentity().hasClosedDate();
    }

    public Waldec getWaldec() {
        return this.getIdentity().getWaldec();
    }

    public String getWaldecAsString() {
        return this.getIdentity().getWaldecAsString();
    }

    public void setWaldec(Waldec waldec) {
        this.getIdentity().setWaldec(waldec);
    }

    public Siren getSiren() {
        return this.getIdentity().getSiren();
    }

    public String getSirenAsString() {
        return this.getIdentity().getSirenAsString();
    }

    public void setSiren(Siren siren) {
        this.getIdentity().setSiren(siren);
    }

    public Siret getSiret() {
        return this.getIdentity().getSiret();
    }

    public String getSiretAsString() {
        return this.getIdentity().getSiretAsString();
    }

    public void setSiret(Siret siret) {
        this.getIdentity().setSiret(siret);
    }

    public TradeMark getTradeMark() {
        return this.getIdentity().getTradeMark();
    }

    public String getTradeMarkAsString() {
        return this.getIdentity().getTradeMarkAsString();
    }

    public void setTradeMark(TradeMark tradeMark) {
        this.getIdentity().setTradeMark(tradeMark);
    }

    public IntracommunityVat getIntracommunityVat() {
        return this.getIdentity().getIntracommunityVat();
    }

    public String getIntracommunityVatAsString() {
        return this.getIdentity().getIntracommunityVatAsString();
    }

    public void setIntracommunityVat(IntracommunityVat intracommunityVat) {
        this.getIdentity().setIntracommunityVat(intracommunityVat);
    }

    public String getDescription() {
        return this.getIdentity().getDescription();
    }

    public void setDescription(String desc) {
        this.getIdentity().setDescription(desc);
    }

    @Override
    public CorporateEntityIdentity getIdentity() {
        return (CorporateEntityIdentity) this.identity;
    }

}
