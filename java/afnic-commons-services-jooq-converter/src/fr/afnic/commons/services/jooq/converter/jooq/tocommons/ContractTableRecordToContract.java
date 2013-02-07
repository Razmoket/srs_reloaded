package fr.afnic.commons.services.jooq.converter.jooq.tocommons;

import fr.afnic.commons.beans.contact.identity.ContactIdentity;
import fr.afnic.commons.beans.contract.Contract;
import fr.afnic.commons.beans.contract.ContractId;
import fr.afnic.commons.beans.contract.ContractStatus;
import fr.afnic.commons.beans.customer.CustomerId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.jooq.converter.JooqConverterFacade;
import fr.afnic.commons.services.jooq.stub.grc.tables.records.ContractTableRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class ContractTableRecordToContract extends AbstractConverter<ContractTableRecord, Contract> {

    public ContractTableRecordToContract() {
        super(ContractTableRecord.class, Contract.class);
    }

    @Override
    public Contract convert(ContractTableRecord toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        Contract ret = new Contract(userId, tld);
        ret.setCreateDate(toConvert.getCreateDate());
        if (toConvert.getDurability() != null) {
            ret.setDurability(toConvert.getDurability());
        }
        if (toConvert.getIdAccountManager() != null) {
            ret.setIdAccountManager(toConvert.getIdAccountManager());
        }
        ret.setContractId(new ContractId(toConvert.getIdContract()));
        ret.setContractStatus(JooqConverterFacade.convert(toConvert.getIdContractstatus(), ContractStatus.class, userId, tld));
        ret.setContractTypeTldId(toConvert.getIdContracttypetld());
        ret.setContractTypeOnTld(AppServiceFacade.getContractService().getContractType(toConvert.getIdContracttypetld(), userId, tld));
        if (toConvert.getIdCreateUser() != null) {
            ret.setCreateUserId(toConvert.getIdCreateUser());
        }
        ret.setCustomerId(new CustomerId(toConvert.getIdCustomer()));
        if (toConvert.getIdPaymentmethodtype() != null) {
            ret.setPayementMethodTypeId(toConvert.getIdPaymentmethodtype());
        }
        if (toConvert.getIdUpdateUser() != null) {
            ret.setUpdateUserId(toConvert.getIdUpdateUser());
        }
        if (toConvert.getPaymentDate() != null) {
            ret.setPayementDate(toConvert.getPaymentDate());
        }
        ret.setRemark(toConvert.getRemark());
        ret.setSigningDate(toConvert.getSigningDate());
        ret.setUpdateDate(toConvert.getCreateDate());

        ContactIdentity identity = null;
        if (toConvert.getIdIndividualentity() != null) {
            identity = AppServiceFacade.getIdentityService().getIndividualIdentity(toConvert.getIdIndividualentity(), userId, tld);
        }

        if (toConvert.getIdCorporateentity() != null) {
            identity = AppServiceFacade.getIdentityService().getCorporateEntityIdentity(toConvert.getIdCorporateentity(), userId, tld);
        }

        ret.setIdentity(identity);

        return ret;
    }
}
