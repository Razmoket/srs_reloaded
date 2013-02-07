/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.search.domain;

import java.util.Date;

import fr.afnic.commons.beans.search.SearchCriteria;
import fr.afnic.utils.DateUtils;
import fr.afnic.utils.ToStringHelper;

public class DomainSearchCriteria extends SearchCriteria<DomainSearchCriterion> {

    private static final long serialVersionUID = 1L;

    public DomainSearchCriteria() {
        super();
        this.addCriterion(DomainSearchCriterion.DomainNameLike, true);
        this.addCriterion(DomainSearchCriterion.HolderNameLike, true);
        this.addCriterion(DomainSearchCriterion.RegistrarNameLike, true);
    }

    public void setSiret(String siret) {
        this.addCriterion(DomainSearchCriterion.Siret, siret);
    }

    public void setDomainName(String domainName) {
        //a priori pour du javascript, mais peut-etre inutile
        if ("undefined".equals(domainName)) {
            domainName = null;
        }
        this.addCriterion(DomainSearchCriterion.DomainName, domainName);
    }

    public void setRegistrarNameOrCode(String registrarNameOrCode) {
        this.addCriterion(DomainSearchCriterion.RegistrarNameOrCode, registrarNameOrCode);
    }

    public void setHolderHandle(String holderHandle) {
        if (holderHandle != null) {
            holderHandle = holderHandle.trim().toUpperCase();
        }
        this.addCriterion(DomainSearchCriterion.HolderHandle, holderHandle);
    }

    public void setHolderName(String holderName) {
        this.addCriterion(DomainSearchCriterion.HolderName, holderName);
    }

    public void setServerName(String serverName) {
        this.addCriterion(DomainSearchCriterion.ServerName, serverName);
    }

    public void setAnniversaryDateDebut(Date anniversaryDateDebut) {
        this.addCriterion(DomainSearchCriterion.AnniversaryDateDebut, DateUtils.clone(anniversaryDateDebut));
    }

    public void setAnniversaryDateFin(Date anniversaryDateFin) {
        this.addCriterion(DomainSearchCriterion.AnniversaryDateFin, DateUtils.clone(anniversaryDateFin));
    }

    public void setDomainNameLike(boolean like) {
        this.addCriterion(DomainSearchCriterion.DomainNameLike, like);
    }

    public void setHolderNameLike(boolean like) {
        this.addCriterion(DomainSearchCriterion.HolderNameLike, like);
    }

    public void setRegistrarNameLike(boolean like) {
        this.addCriterion(DomainSearchCriterion.RegistrarNameLike, like);
    }

    public String getSiret() {
        return (String) this.getCriterionValue(DomainSearchCriterion.Siret);
    }

    public String getDomainName() {
        return (String) this.getCriterionValue(DomainSearchCriterion.DomainName);
    }

    public String getRegistrarNameOrCode() {
        return (String) this.getCriterionValue(DomainSearchCriterion.RegistrarNameOrCode);
    }

    public String getHolderHandle() {
        return (String) this.getCriterionValue(DomainSearchCriterion.HolderHandle);
    }

    public String getHolderName() {
        return (String) this.getCriterionValue(DomainSearchCriterion.HolderName);
    }

    public String getServerName() {
        return (String) this.getCriterionValue(DomainSearchCriterion.ServerName);
    }

    public Date getAnniversaryDateDebut() {
        return (Date) this.getCriterionValue(DomainSearchCriterion.AnniversaryDateDebut);
    }

    public Date getAnniversaryDateFin() {
        return (Date) this.getCriterionValue(DomainSearchCriterion.AnniversaryDateFin);
    }

    public boolean getDomainNameLike() {
        return (Boolean) this.getCriterionValue(DomainSearchCriterion.DomainNameLike);
    }

    public boolean getHolderNameLike() {
        return (Boolean) this.getCriterionValue(DomainSearchCriterion.HolderNameLike);
    }

    public boolean getRegistrarNameLike() {
        return (Boolean) this.getCriterionValue(DomainSearchCriterion.RegistrarNameLike);
    }

    @Override
    public String toString() {
        return new ToStringHelper().addAllObjectAttributes(this).toString();
    }

}
