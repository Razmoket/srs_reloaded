/*
 * $Id: CustomerGenerator.java,v 1.1 2010/07/08 09:39:28 alaphil Exp $
 * $Revision: 1.1 $
 * $Author: alaphil $
 */

package fr.afnic.commons.test.generator;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import fr.afnic.commons.beans.contact.CustomerContact;
import fr.afnic.commons.beans.contract.Contract;
import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.customer.CustomerId;
import fr.afnic.commons.beans.customer.PaymentMethod;
import fr.afnic.commons.beans.customer.benefit.ServiceType;
import fr.afnic.commons.beans.customer.status.CustomerStatus;
import fr.afnic.commons.beans.profiling.UserAccount;
import fr.afnic.commons.test.generator.exception.GeneratorException;
import fr.afnic.utils.StringUtils;

public final class CustomerGenerator {

    public static final String CODE = "TEST";

    private CustomerGenerator() {
    }

    protected static Customer generateRandomCommonsFieldsContent(Customer toCustomerCreate) throws GeneratorException {
        toCustomerCreate.setComment(StringUtils.generateWord(50));

        toCustomerCreate.setCustomerNumber("TEST" + System.currentTimeMillis());

        CustomerGenerator.populateAccount(toCustomerCreate);

        toCustomerCreate.setPaymentMethod(PaymentMethod.CreditCard);
        Random random = new Random(System.currentTimeMillis());
        toCustomerCreate.setCustomerId(new CustomerId(random.nextInt(20000)));

        // necessite des converters
        ArrayList<ServiceType> services = new ArrayList<ServiceType>();
        toCustomerCreate.setServices(services); //

        ArrayList<CustomerContact> customerContacts = new ArrayList<CustomerContact>();
        customerContacts.add(CustomerContactGenerator.createRandomCustomerContact());
        toCustomerCreate.setCustomerContacts(customerContacts); //

        ArrayList<Contract> customerContracts = new ArrayList<Contract>();
        customerContracts.add(CustomerContractGenerator.createRandomCustomerContract());
        toCustomerCreate.setContracts(customerContracts); //

        toCustomerCreate.setStatus(CustomerStatus.ACTIVE);

        toCustomerCreate.setCreateDate(new Date());
        return toCustomerCreate;
    }

    private static void populateAccount(Customer customer) throws GeneratorException {
        UserAccount account = new UserAccount();
        account.setLogin("" + System.currentTimeMillis());
        account.setPassword("password");
        customer.setAccount(account);
    }
}
