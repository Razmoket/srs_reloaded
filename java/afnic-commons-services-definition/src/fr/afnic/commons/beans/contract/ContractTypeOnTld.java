/**
 * 
 */
package fr.afnic.commons.beans.contract;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import fr.afnic.commons.beans.Tld;
import fr.afnic.commons.beans.description.IDescribedExternallyObject;
import fr.afnic.commons.beans.mail.template.EmailTemplate;
import fr.afnic.commons.beans.mail.template.EmailTemplateType;
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
public class ContractTypeOnTld implements IDescribedExternallyObject {

    private static final long serialVersionUID = -1960099585362322669L;

    private static HashMap<Integer, EmailTemplate> mapTemplate = new HashMap<Integer, EmailTemplate>();

    private int idContractType;

    private int idContractTypeOnTld;

    private String idDictionnary;

    private int idTemplateMail;

    private ContractTypeEnum typeContract;

    private ContractOffre offreContract;

    private boolean isAllowedCorporate;

    private boolean isAllowedIndividual;

    private Tld tld;

    @Override
    public String getDescription(UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDescription(Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return this.getClass().getSimpleName().toLowerCase();
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
        // TODO Auto-generated method stub

    }

    @Override
    public String getDictionaryKey() {
        // TODO Auto-generated method stub
        return this.getClass().getSimpleName().toLowerCase();
    }

    /** le user appelant l'objet */
    private final UserId userIdCaller;
    protected final TldServiceFacade tldCaller;

    public ContractTypeOnTld(UserId userIdCaller, TldServiceFacade tld) {
        super();
        this.userIdCaller = userIdCaller;
        this.tldCaller = tld;
    }

    /**
     * Retourne le template du mail de confirmation d'inscription
     * 
     * @throws ServiceException
     */
    public EmailTemplate getConfirmationEMailTemplate() throws ServiceException {
        populateMapTemplateIfEmpty(this.userIdCaller, this.tldCaller);
        return mapTemplate.get(this.idTemplateMail);
    }

    private static void populateMapTemplateIfEmpty(UserId userId, TldServiceFacade tld) throws ServiceException {
        if (mapTemplate.isEmpty()) {
            List<EmailTemplate> templates = AppServiceFacade.getEmailService().getTemplates(EmailTemplateType.Gerico, userId, tld);
            for (EmailTemplate template : templates) {
                mapTemplate.put(template.getIdTemplate(), template);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        ContractTypeOnTld contratType = (ContractTypeOnTld) obj;
        if (contratType.getTld().equals(this.tld) && (contratType.getIdContractType() == this.idContractType)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

    public int getIdContractType() {
        return this.idContractType;
    }

    public void setIdContractType(int idContractType) {
        this.idContractType = idContractType;
    }

    public String getIdDictionnary() {
        return this.idDictionnary;
    }

    public void setIdDictionnary(String idDictionnary) {
        this.idDictionnary = idDictionnary;
    }

    public int getIdTemplateMail() {
        return this.idTemplateMail;
    }

    public void setIdTemplateMail(int idTemplateMail) {
        this.idTemplateMail = idTemplateMail;
    }

    public Tld getTld() {
        return this.tld;
    }

    public void setTld(Tld tld) {
        this.tld = tld;
    }

    public ContractTypeEnum getTypeContract() {
        return this.typeContract;
    }

    public void setTypeContract(ContractTypeEnum typeContract) {
        this.typeContract = typeContract;
    }

    public ContractOffre getOffreContract() {
        return offreContract;
    }

    public void setOffreContract(ContractOffre offreContract) {
        this.offreContract = offreContract;
    }

    public int getIdContractTypeOnTld() {
        return this.idContractTypeOnTld;
    }

    public void setIdContractTypeOnTld(int idContractTypeOnTld) {
        this.idContractTypeOnTld = idContractTypeOnTld;
    }

    public boolean isAllowedCorporate() {
        return this.isAllowedCorporate;
    }

    public void setAllowedCorporate(boolean isAllowedCorporate) {
        this.isAllowedCorporate = isAllowedCorporate;
    }

    public boolean isAllowedIndividual() {
        return this.isAllowedIndividual;
    }

    public void setAllowedIndividual(boolean isAllowedIndividual) {
        this.isAllowedIndividual = isAllowedIndividual;
    }
}
