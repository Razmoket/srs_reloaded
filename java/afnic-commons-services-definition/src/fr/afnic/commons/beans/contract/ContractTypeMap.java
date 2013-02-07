/**
 * 
 */
package fr.afnic.commons.beans.contract;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.afnic.commons.beans.Tld;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Type d'un contrat.
 * 
 * @author alaphilippe
 * 
 */
public class ContractTypeMap implements Serializable {

    private static final long serialVersionUID = -1960099585362322669L;

    private static Map<Tld, Map<Integer, ContractTypeOnTld>> mapTemplate = new HashMap<Tld, Map<Integer, ContractTypeOnTld>>();

    private static Map<Integer, ContractTypeOnTld> mapTemplateByIdOnTld = new HashMap<Integer, ContractTypeOnTld>();

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(ContractTypeMap.class);

    private static void populateMap(UserId userIdCaller, TldServiceFacade tldCaller) {
        if (mapTemplate.isEmpty()) {
            try {
                List<ContractTypeOnTld> contractTypes;
                contractTypes = AppServiceFacade.getContractService().getContractTypes(userIdCaller, tldCaller);
                for (ContractTypeOnTld contractType : contractTypes) {
                    Map<Integer, ContractTypeOnTld> mapTld = mapTemplate.get(contractType.getTld());
                    if (mapTld == null) {
                        mapTld = new HashMap<Integer, ContractTypeOnTld>();
                        mapTemplate.put(contractType.getTld(), mapTld);
                    }
                    mapTld.put(contractType.getIdContractType(), contractType);
                    mapTemplateByIdOnTld.put(contractType.getIdContractTypeOnTld(), contractType);
                }
            } catch (ServiceException e) {
                LOGGER.error("erreur de chargement de la map des types de contract" + e, e);
            }
        }
    }

    public static ContractTypeOnTld getContractType(Tld tld, int contractId, UserId userIdCaller, TldServiceFacade tldCaller) {
        populateMap(userIdCaller, tldCaller);
        return mapTemplate.get(tld).get(contractId);
    }

    public static ContractTypeOnTld getContractTypeByIdOnTld(int contractTypeIdOnTld, UserId userIdCaller, TldServiceFacade tldCaller) {
        populateMap(userIdCaller, tldCaller);
        return mapTemplateByIdOnTld.get(contractTypeIdOnTld);
    }
}
