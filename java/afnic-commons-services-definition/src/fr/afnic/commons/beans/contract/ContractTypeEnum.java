/**
 * 
 */
package fr.afnic.commons.beans.contract;

import org.apache.commons.lang.StringUtils;

/**
 * Type d'un contrat.
 * 
 * 
 */
public enum ContractTypeEnum {

    Registrar("REGISTRAR"),
    Member("MEMBER"),
    Partner("PARTNER"),
    Registry("REGISTRY");

    private String text;

    private ContractTypeEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static ContractTypeEnum findContractTypeEnumById(String toFind) {
        for (ContractTypeEnum contractTypeTemp : ContractTypeEnum.values()) {
            if (StringUtils.equals(contractTypeTemp.getText(), toFind)) {
                return contractTypeTemp;
            }
        }
        return null;
    }
}
