/**
 * 
 */
package fr.afnic.commons.beans.contract;

import java.io.Serializable;

import fr.afnic.commons.beans.Tld;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Type d'un contrat.
 * 
 * @author alaphilippe
 * 
 */
public class ContractTypeDefault implements Serializable {
    private static UserId defaultUser = new UserId(22);
    private static Tld defaultTld = Tld.Fr;
    private static TldServiceFacade defaultTldFacade = TldServiceFacade.Fr;

    private static final int AFNIC_MEMBER_PERSON = 3;
    private static final int AFNIC_MEMBER_ORGANIZATION = 2;
    private static final int AFNIC_MEMBER = 1;
    private static final int AFNIC_INTERNATIONAL_COMMITTEE_MEMBER = 4;
    private static final int AFNIC_HONORARY_MEMBER = 5;
    private static final int AFNIC_ADMINISTRATIVE_MEMBER = 6;
    private static final int REGISTRAR_TYPE_1 = 7;
    private static final int REGISTRAR_TYPE_2 = 8;
    private static final int PARL_PARTNER = 10;
    private static final int SQUAW_PARTNER = 9;
    private static final int INSTITUTIONNAL_PARTNER = 11;
    private static final int FOUNDING_PARTNER = 12;

    public static final ContractTypeOnTld MEMBER_PERSON_CONTRACT = ContractTypeMap.getContractType(defaultTld, AFNIC_MEMBER_PERSON, defaultUser, defaultTldFacade);
    public static final ContractTypeOnTld MEMBER_ORGANIZATION_CONTRACT = ContractTypeMap.getContractType(defaultTld, AFNIC_MEMBER_ORGANIZATION, defaultUser, defaultTldFacade);
    public static final ContractTypeOnTld AFNIC_MEMBER_CONTRACT = ContractTypeMap.getContractType(defaultTld, AFNIC_MEMBER, defaultUser, defaultTldFacade);
    public static final ContractTypeOnTld AFNIC_INTERNATIONAL_COMMITTEE_CONTRACT = ContractTypeMap.getContractType(defaultTld, AFNIC_INTERNATIONAL_COMMITTEE_MEMBER, defaultUser, defaultTldFacade);
    public static final ContractTypeOnTld AFNIC_HONORARY_CONTRACT = ContractTypeMap.getContractType(defaultTld, AFNIC_HONORARY_MEMBER, defaultUser, defaultTldFacade);
    public static final ContractTypeOnTld AFNIC_ADMINISTRATIVE_CONTRACT = ContractTypeMap.getContractType(defaultTld, AFNIC_ADMINISTRATIVE_MEMBER, defaultUser, defaultTldFacade);
    public static final ContractTypeOnTld REGISTRAR_TYPE_1_CONTRACT = ContractTypeMap.getContractType(defaultTld, REGISTRAR_TYPE_1, defaultUser, defaultTldFacade);
    public static final ContractTypeOnTld REGISTRAR_TYPE_2_CONTRACT = ContractTypeMap.getContractType(defaultTld, REGISTRAR_TYPE_2, defaultUser, defaultTldFacade);
    public static final ContractTypeOnTld PARL_CONTRACT = ContractTypeMap.getContractType(defaultTld, PARL_PARTNER, defaultUser, defaultTldFacade);
    public static final ContractTypeOnTld SQUAW_CONTRACT = ContractTypeMap.getContractType(defaultTld, SQUAW_PARTNER, defaultUser, defaultTldFacade);
    public static final ContractTypeOnTld INSTITUTIONNAL_CONTRACT = ContractTypeMap.getContractType(defaultTld, INSTITUTIONNAL_PARTNER, defaultUser, defaultTldFacade);
    public static final ContractTypeOnTld FOUNDING_CONTRACT = ContractTypeMap.getContractType(defaultTld, FOUNDING_PARTNER, defaultUser, defaultTldFacade);
}
