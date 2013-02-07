/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.search.form;

import fr.afnic.commons.beans.search.SearchCriteria;
import fr.afnic.utils.ToStringHelper;

public class FormSearchCriteria extends SearchCriteria<FormSearchCriterion> {

    private static final long serialVersionUID = 1L;

    public FormSearchCriteria() {
        super();
    }

    public void setOperationFormId(String operationFormId) {
        if (operationFormId != null) {
            operationFormId = operationFormId.trim().toUpperCase();
        }
        this.addCriterion(FormSearchCriterion.FormId, operationFormId);
    }

    public void setDomainName(String domainName) {
        if (domainName != null) {
            domainName = domainName.trim().toLowerCase();
        }
        this.addCriterion(FormSearchCriterion.DomainName, domainName);
    }

    public void setTicketId(String ticketId) {
        if (ticketId != null) {
            ticketId = ticketId.trim().toUpperCase();
        }
        this.addCriterion(FormSearchCriterion.TicketId, ticketId);
    }

    public void setHolderHandle(String holderHandle) {
        if (holderHandle != null) {
            holderHandle = holderHandle.trim().toUpperCase();
        }
        this.addCriterion(FormSearchCriterion.HolderHandle, holderHandle);
    }

    public void setAdminHandle(String adminHandle) {
        if (adminHandle != null) {
            adminHandle = adminHandle.trim().toUpperCase();
        }
        this.addCriterion(FormSearchCriterion.AdminHandle, adminHandle);
    }

    public void setRegistrar(String registrar) {
        if (registrar != null) {
            registrar = registrar.trim().toUpperCase();
        }
        this.addCriterion(FormSearchCriterion.Registrar, registrar);
    }

    public String getFormId() {
        return (String) this.getCriterionValue(FormSearchCriterion.FormId);
    }

    public String getDomainName() {
        return (String) this.getCriterionValue(FormSearchCriterion.DomainName);
    }

    public String getTicketId() {
        return (String) this.getCriterionValue(FormSearchCriterion.TicketId);
    }

    public String getHolderHandle() {
        return (String) this.getCriterionValue(FormSearchCriterion.HolderHandle);
    }

    public String getAdminHandle() {
        return (String) this.getCriterionValue(FormSearchCriterion.AdminHandle);
    }

    public String getRegistrar() {
        return (String) this.getCriterionValue(FormSearchCriterion.Registrar);
    }

    @Override
    public String toString() {
        return new ToStringHelper().addAllObjectAttributes(this).toString();
    }

}
