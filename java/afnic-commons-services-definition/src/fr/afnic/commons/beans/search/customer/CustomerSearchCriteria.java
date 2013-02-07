/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.search.customer;

import java.util.List;

import fr.afnic.commons.beans.contract.ContractTypeOnTld;
import fr.afnic.commons.beans.customer.benefit.ServiceType;
import fr.afnic.commons.beans.search.SearchCriteria;
import fr.afnic.utils.ToStringHelper;

public class CustomerSearchCriteria extends SearchCriteria<CustomerSearchCriterion> {

    private static final long serialVersionUID = 1L;

    public void setCustomerNumber(String customerNumber) {
        this.addCriterion(CustomerSearchCriterion.CustomerNumber, customerNumber);
    }

    public void setBusinessContactRegionCode(String regionCode) {
        this.addCriterion(CustomerSearchCriterion.BusinessContactRegionCode, regionCode);
    }

    public void setBusinessContactCountryCode(String countryCode) {
        this.addCriterion(CustomerSearchCriterion.BusinessContactCountryCode, countryCode);
    }

    public void setName(String name) {
        this.addCriterion(CustomerSearchCriterion.Name, name);
    }

    public void setCode(String code) {
        this.addCriterion(CustomerSearchCriterion.Code, code);
    }

    public String getCode() {
        return (String) this.getCriterionValue(CustomerSearchCriterion.Code);
    }

    public String getName() {
        return (String) this.getCriterionValue(CustomerSearchCriterion.Name);
    }

    public String getCustomerNumber() {
        return (String) this.getCriterionValue(CustomerSearchCriterion.CustomerNumber);
    }

    public String getBusinessContactRegionCode() {
        return (String) this.getCriterionValue(CustomerSearchCriterion.BusinessContactRegionCode);
    }

    public String getBusinessContactCountryCode() {
        return (String) this.getCriterionValue(CustomerSearchCriterion.BusinessContactCountryCode);
    }

    public ContractTypeOnTld getCustomerContractType() {
        return (ContractTypeOnTld) this.getCriterionValue(CustomerSearchCriterion.ContractType);
    }

    public void setCustomerContractType(ContractTypeOnTld type) {
        this.addCriterion(CustomerSearchCriterion.ContractType, type);
    }

    @SuppressWarnings("unchecked")
    public List<ServiceType> getBenefitTypes() {
        return (List<ServiceType>) this.getCriterionValue(CustomerSearchCriterion.BenefitTypes);
    }

    public void setBenefits(List<ServiceType> types) {
        this.addCriterion(CustomerSearchCriterion.BenefitTypes, types);
    }

    @Override
    public String toString() {
        return new ToStringHelper().addAllObjectAttributes(this).toString();
    }

}
