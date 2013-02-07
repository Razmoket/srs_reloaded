package fr.afnic.commons.test.generator;

import fr.afnic.commons.beans.contract.Contract;
import fr.afnic.commons.beans.contract.ContractTypeDefault;
import fr.afnic.commons.beans.contract.ContractTypeOnTld;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.exception.GeneratorException;

public final class CustomerContractGenerator {

    private CustomerContractGenerator() {
    }

    public static Contract createRandomCustomerContract() throws GeneratorException {
        Contract customerContract;
        try {
            customerContract = new Contract(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        } catch (ServiceException e) {
            throw new GeneratorException("populateAccount() failed", e);
        }
        customerContract.setType(CustomerContractGenerator.getRandomContractType());
        customerContract.validate();
        return customerContract;
    }

    private static ContractTypeOnTld getRandomContractType() {
        return ContractTypeDefault.REGISTRAR_TYPE_1_CONTRACT;
    }
}
