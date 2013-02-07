package fr.afnic.commons.beans.contact.identity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.afnic.commons.beans.corporateentity.CorporateEntityIdentifier;
import fr.afnic.commons.beans.corporateentity.IntracommunityVat;
import fr.afnic.commons.beans.corporateentity.OrganizationType;
import fr.afnic.commons.beans.corporateentity.Siren;
import fr.afnic.commons.beans.corporateentity.Siret;
import fr.afnic.commons.beans.corporateentity.TradeMark;
import fr.afnic.commons.beans.corporateentity.Waldec;
import fr.afnic.commons.beans.validatable.IValidatable;
import fr.afnic.commons.beans.validatable.InvalidDataDescriptionEnum;
import fr.afnic.commons.beans.validatable.InvalidDataEnumDescription;
import fr.afnic.commons.beans.validatable.InvalidDataException;
import fr.afnic.utils.DateUtils;

public class CorporateEntityIdentity extends ContactIdentity implements IValidatable, Serializable {
    private static final long serialVersionUID = 1L;

    private String activity;
    /** Status juridique */
    private String legalStatus;
    private boolean isActive;
    private Date establishedDate;
    private Date closedDate;

    private List<String> thirdPartyLegalAnnounceHistoryUrl;
    private List<String> thirdPartyStructureInfoUrl;
    private List<String> thirdPartyStructureSearchUrl;

    private Waldec waldec = null;
    private Siren siren = null;
    private Siret siret = null;
    private TradeMark tradeMark = null;
    private IntracommunityVat intracommunityVat = null;

    private OrganizationType organizationType;

    private String description;

    private int id;

    public CorporateEntityIdentity() {
        super();
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** Sigle */
    private String acronym;

    /** Enseigne */
    private String brand;

    /** Nom commercial */
    private String tradeName;

    private String organizationName;

    public List<String> getThirdPartyLegalAnnounceHistoryUrl() {
        return this.thirdPartyLegalAnnounceHistoryUrl;
    }

    public void setThirdPartyLegalAnnounceHistoryUrl(List<String> thirdPartyLegalAnnounceHistoryUrl) {
        this.thirdPartyLegalAnnounceHistoryUrl = thirdPartyLegalAnnounceHistoryUrl;
    }

    public List<String> getThirdPartyStructureInfoUrl() {
        return this.thirdPartyStructureInfoUrl;
    }

    public void setThirdPartyStructureInfoUrl(List<String> thirdPartyStructureInfoUrl) {
        this.thirdPartyStructureInfoUrl = thirdPartyStructureInfoUrl;
    }

    public List<String> getThirdPartyStructureSearchUrl() {
        return this.thirdPartyStructureSearchUrl;
    }

    public void setThirdPartyStructureSearchUrl(List<String> thirdPartyStructureSearchUrl) {
        this.thirdPartyStructureSearchUrl = thirdPartyStructureSearchUrl;
    }

    public boolean hasThirdPartyLegalAnnounceHistoryUrl() {
        return this.thirdPartyLegalAnnounceHistoryUrl != null && this.thirdPartyLegalAnnounceHistoryUrl.size() > 0;
    }

    public boolean hasThirdPartyStructureInfoUrl() {
        return this.thirdPartyStructureInfoUrl != null && this.thirdPartyStructureInfoUrl.size() > 0;
    }

    public boolean hasThirdPartyStructureSearchUrl() {
        return this.thirdPartyStructureSearchUrl != null && this.thirdPartyStructureSearchUrl.size() > 0;
    }

    /**
     * Retourne l'identifiant le plus pertinent de la structure legale
     * 
     * @return
     */
    public CorporateEntityIdentifier getIdentifier() {
        if (this.hasWaldec()) {
            return this.waldec;
        } else if (this.hasSiren()) {
            return this.siren;
        } else if (this.hasSiret()) {
            return this.siret;
        } else if (this.hasTradeMark()) {
            return this.tradeMark;
        } else if (this.hasIntracommunityVat()) {
            return this.intracommunityVat;
        }
        return null;
    }

    public boolean hasNotIntracommunityVat() {
        return !this.hasIntracommunityVat();
    }

    public boolean hasIntracommunityVat() {
        return this.intracommunityVat != null && this.intracommunityVat.hasNotEmptyValue();
    }

    public boolean hasNotSiren() {
        return !this.hasSiren();
    }

    public boolean hasSiren() {
        return this.siren != null && this.siren.hasNotEmptyValue();
    }

    public boolean hasNotSiret() {
        return !this.hasSiret();
    }

    public boolean hasSiret() {
        return this.siret != null && this.siret.hasNotEmptyValue();
    }

    public boolean hasNotWaldec() {
        return !this.hasWaldec();
    }

    public boolean hasWaldec() {
        return this.waldec != null && this.waldec.hasNotEmptyValue();
    }

    public boolean hasNotTradeMark() {
        return !this.hasTradeMark();
    }

    public boolean hasTradeMark() {
        return this.tradeMark != null && this.tradeMark.hasNotEmptyValue();
    }

    public boolean hasNotIdentifier() {
        return !this.hasIdentifier();
    }

    /**
     * Indique si un identifier est définit pour la structure
     * 
     * @return
     */
    public boolean hasIdentifier() {
        return this.getIdentifier() != null && this.getIdentifier().hasNotEmptyValue();
    }

    public String getActivity() {
        return this.activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Date getEstablishedDate() {
        return DateUtils.clone(this.establishedDate);
    }

    public void setEstablishedDate(Date establishedDate) {
        this.establishedDate = DateUtils.clone(this.establishedDate);
    }

    public Date getClosedDate() {
        return DateUtils.clone(this.closedDate);
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = DateUtils.clone(this.closedDate);
    }

    /** Statut juridique */
    public String getLegalStatus() {
        return this.legalStatus;
    }

    /** Statut juridique */
    public void setLegalStatus(String legalStatus) {
        this.legalStatus = legalStatus;
    }

    /** Statut juridique */
    public boolean hasLegalStatus() {
        return StringUtils.isNotBlank(this.legalStatus);
    }

    /** Raison social de la personne morale */
    @Override
    public String getName() {
        return this.organizationName;
    }

    /** Raison social de la personne morale */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationName() {
        return this.organizationName;
    }

    public String getAcronym() {
        return this.acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTradeName() {
        return this.tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public boolean hasAcronym() {
        return this.acronym != null;
    }

    public boolean hasBrand() {
        return this.brand != null;
    }

    public boolean hasTradeName() {
        return this.tradeName != null;
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
        return this.establishedDate != null;
    }

    public boolean hasClosedDate() {
        return this.closedDate != null;
    }

    public Waldec getWaldec() {
        return this.waldec;
    }

    public String getWaldecAsString() {
        return this.waldec != null ? this.waldec.getValue() : null;
    }

    public void setWaldec(Waldec waldec) {
        this.waldec = waldec;
    }

    public Siren getSiren() {
        return this.siren;
    }

    public String getSirenAsString() {
        return this.siren != null ? this.siren.getValue() : null;
    }

    public void setSiren(Siren siren) {
        this.siren = siren;
    }

    public Siret getSiret() {
        return this.siret;
    }

    public String getSiretAsString() {
        return this.siret != null ? this.siret.getValue() : null;
    }

    public void setSiret(Siret siret) {
        this.siret = siret;
    }

    public TradeMark getTradeMark() {
        return this.tradeMark;
    }

    public String getTradeMarkAsString() {
        return this.tradeMark != null ? this.tradeMark.getValue() : null;
    }

    public void setTradeMark(TradeMark tradeMark) {
        this.tradeMark = tradeMark;
    }

    public IntracommunityVat getIntracommunityVat() {
        return this.intracommunityVat;
    }

    public void setIntracommunityVat(IntracommunityVat intracommunityVat) {
        this.intracommunityVat = intracommunityVat;
    }

    public String getIntracommunityVatAsString() {
        return this.intracommunityVat != null ? this.intracommunityVat.getValue() : null;
    }

    public OrganizationType getOrganizationType() {
        return this.organizationType;
    }

    public void setOrganizationType(OrganizationType type) {
        this.organizationType = type;
    }

    @Override
    public void validate() throws InvalidDataException {
        if (this.hasNotIdentifier()) {
            throw new InvalidDataException(new InvalidDataEnumDescription(InvalidDataDescriptionEnum.ALL_EMPTY_IDENTIFIER));
        }
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

}
