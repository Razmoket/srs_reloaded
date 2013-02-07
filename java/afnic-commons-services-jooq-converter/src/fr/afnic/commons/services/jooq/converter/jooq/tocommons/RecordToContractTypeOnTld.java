package fr.afnic.commons.services.jooq.converter.jooq.tocommons;

import org.jooq.Record;

import fr.afnic.commons.beans.Tld;
import fr.afnic.commons.beans.contract.ContractOffre;
import fr.afnic.commons.beans.contract.ContractTypeEnum;
import fr.afnic.commons.beans.contract.ContractTypeOnTld;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.converter.JooqConverterFacade;
import fr.afnic.commons.services.jooq.stub.grc.tables.ContractTypeTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.ContracttypeTldRTable;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class RecordToContractTypeOnTld extends AbstractConverter<Record, ContractTypeOnTld> {

    public RecordToContractTypeOnTld() {
        super(Record.class, ContractTypeOnTld.class);
    }

    private static ContracttypeTldRTable CONTRACTTYPE_TLD_R = ContracttypeTldRTable.CONTRACTTYPE_TLD_R;

    private static ContractTypeTable CONTRACT_TYPE = ContractTypeTable.CONTRACT_TYPE;

    @Override
    public ContractTypeOnTld convert(Record toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        ContractTypeOnTld ret = new ContractTypeOnTld(userId, tld);
        ret.setIdContractType(toConvert.getValue(CONTRACTTYPE_TLD_R.ID_CONTRACTTYPE));
        //ret.setIdDictionnary(toConvert.getValue(CONTRACT_TYPE.ID_DICTIONARY));
        if (toConvert.getValue(CONTRACTTYPE_TLD_R.ID_TEMPLATEEMAIL) != null) {
            ret.setIdTemplateMail(toConvert.getValue(CONTRACTTYPE_TLD_R.ID_TEMPLATEEMAIL));
        }
        ret.setTld(Tld.findTldById(toConvert.getValue(CONTRACTTYPE_TLD_R.ID_TLD)));
        ret.setTypeContract(ContractTypeEnum.findContractTypeEnumById(toConvert.getValue(CONTRACT_TYPE.PROFIL)));
        ret.setOffreContract(JooqConverterFacade.convert(toConvert.getValue(CONTRACT_TYPE.ID_CONTRACTTYPE), ContractOffre.class, userId, tld));

        ret.setIdContractTypeOnTld(toConvert.getValue(CONTRACTTYPE_TLD_R.ID_CONTRACTTYPETLD));
        ret.setAllowedCorporate(toConvert.getValue(CONTRACTTYPE_TLD_R.IS_ALLOWED_CORPORATE) == 1 ? true : false);
        ret.setAllowedIndividual(toConvert.getValue(CONTRACTTYPE_TLD_R.IS_ALLOWED_INDIVIDUAL) == 1 ? true : false);

        return ret;
    }
}
