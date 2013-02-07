package fr.afnic.commons.beans;

import fr.afnic.commons.beans.search.SearchCriteria;

public class AuthorizationSearchCriteria extends SearchCriteria<AuthorizationSearchCriterion> {

    private static final long serialVersionUID = 1L;

    public AuthorizationSearchCriteria() {
        super();
    }

    public void setHolderHandle(String holderHandle) {
        this.addCriterion(AuthorizationSearchCriterion.HolderHandle, holderHandle);
    }

    public String getHolderHandle() {
        return (String) this.getCriterionValue(AuthorizationSearchCriterion.HolderHandle);
    }

    public void setRegistrarCode(String registrarCode) {
        this.addCriterion(AuthorizationSearchCriterion.RegistrarCode, registrarCode);
    }

    public String getRegistrarCode() {
        return (String) this.getCriterionValue(AuthorizationSearchCriterion.RegistrarCode);
    }

    public void setDomainName(String domainName) {
        this.addCriterion(AuthorizationSearchCriterion.DomainName, domainName);
    }

    public String getDomainName() {
        return (String) this.getCriterionValue(AuthorizationSearchCriterion.DomainName);
    }

    public void setUsableOnly(boolean usableOnly) {
        this.addCriterion(AuthorizationSearchCriterion.UsableOnly, Boolean.valueOf(usableOnly));
    }

    public boolean getUsableOnly() {
        return (Boolean) this.getCriterionValue(AuthorizationSearchCriterion.UsableOnly);
    }

}
