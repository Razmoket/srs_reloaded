/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.mail.template;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.profiling.users.User;

public class CustomerTemplateParameterBuilder extends AbstractTemplateParameterBuilder<CustomerTemplateParameters> {

    private final Customer customer;

    public CustomerTemplateParameterBuilder(Customer customer) {
        this.customer = customer;
    }

    @Override
    public List<EmailTemplateParameter> build() {
        this.parameters = new ArrayList<EmailTemplateParameter>();

        this.addParameter(CustomerTemplateParameters.Year, new DateTime().getYear());
        this.addParameter(CustomerTemplateParameters.CustomerCustomerNumber, this.customer.getCustomerNumber());
        this.addParameter(CustomerTemplateParameters.CustomerName, this.customer.getName());

        try {
            User accountManager = this.customer.getAccountManager();
            this.addParameter(CustomerTemplateParameters.CustomerAcountManagerLastName, StringUtils.capitalize(accountManager.getLastName()));
            this.addParameter(CustomerTemplateParameters.CustomerAcountManagerFirstName, accountManager.getFirstName());
        } catch (Exception e) {
            throw new RuntimeException("build() failed", e);
        }

        return this.parameters;
    }

}
