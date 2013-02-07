package fr.afnic.commons.services.jooq;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SelectConditionStep;
import org.jooq.SimpleSelectConditionStep;
import org.jooq.SimpleSelectWhereStep;
import org.jooq.impl.Factory;

import fr.afnic.commons.beans.contact.CustomerContactId;
import fr.afnic.commons.beans.contact.identity.IndividualIdentity;
import fr.afnic.commons.beans.contract.Contract;
import fr.afnic.commons.beans.contract.ContractId;
import fr.afnic.commons.beans.contract.ContractOffre;
import fr.afnic.commons.beans.contract.ContractTypeOnTld;
import fr.afnic.commons.beans.customer.CustomerId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IContractService;
import fr.afnic.commons.services.exception.ConnectionException;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.converter.JooqConverterFacade;
import fr.afnic.commons.services.jooq.stub.grc.packages.PkgContract;
import fr.afnic.commons.services.jooq.stub.grc.tables.ContractTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.ContractTypeTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.ContracttypeTldRTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.TldTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.records.ContractTableRecord;
import fr.afnic.commons.services.jooq.stub.grc.tables.records.ContractTypeTableRecord;
import fr.afnic.commons.services.jooq.stub.grc.tables.records.TldTableRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class JooqContractService implements IContractService {

    private final ISqlConnectionFactory sqlConnectionFactory;

    private static final ContractTable CONTRACT = ContractTable.CONTRACT;

    private static ContracttypeTldRTable CONTRACTTYPE_TLD_R = ContracttypeTldRTable.CONTRACTTYPE_TLD_R;

    private static ContractTypeTable CONTRACT_TYPE = ContractTypeTable.CONTRACT_TYPE;

    private static TldTable TLD = TldTable.TLD;

    public JooqContractService(ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    public JooqContractService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    @Override
    public ContractId createContract(Contract contract, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(contract, "contract");
        Preconditions.checkNotNull(contract.getCreateUserId(), "contract.createUserId");

        Factory factory = this.createFactory();
        try {
            int idIndividual = -1;
            int idCorporate = -1;
            if (contract.getIdentity() instanceof IndividualIdentity) {
                idIndividual = contract.getIdentity().getId();
            } else {
                idCorporate = contract.getIdentity().getId();
            }
            BigDecimal newId = PkgContract.createContract(factory, contract.getCustomerId().getIntValue(), (contract.getPayementDate() == null) ? null : new java.sql.Date(contract.getPayementDate()
                                                                                                                                                                                   .getTime()),
                                                          contract.getDurability(),
                                                          (contract.getSigningDate() == null) ? null : new java.sql.Date(contract.getSigningDate().getTime()), contract.getIdAccountManager(),
                                                          contract.getCreateUserId(), 1, contract.getRemark(), null,
                                                          contract.getContractTypeTldId(), (idIndividual == -1) ? null : idIndividual, (idCorporate == -1) ? null : idCorporate,
                                                          contract.getPostalAddress().getIdAsInt());

            return new ContractId(newId.intValue());

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("create(" + contract.toString() + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public Contract updateContract(Contract contract, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            Integer idIndividual = null;
            Integer idCorporate = null;
            if (contract.getIdentity() instanceof IndividualIdentity) {
                idIndividual = contract.getIdentity().getId();
            } else {
                idCorporate = contract.getIdentity().getId();
            }

            PkgContract.updateContract(factory, contract.getContractId().getIntValue(), (contract.getPayementDate() == null) ? null : new java.sql.Date(contract.getPayementDate()
                                                                                                                                                                .getTime()),
                                       contract.getDurability(), contract.getIdAccountManager(),
                                       userId.getIntValue(), JooqConverterFacade.convert(contract.getContractStatus(), Integer.class, userId, tld),
                                       contract.getRemark(), (contract.getPayementMethodTypeId() == 0) ? null : contract.getPayementMethodTypeId(),
                                       contract.getContractTypeOnTld().getIdContractTypeOnTld(), idIndividual, idCorporate);
            return contract;

        } catch (Exception e) {
            throw new ServiceException("create(" + contract.toString() + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public List<Contract> getContracts(UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getContracts("", userId, tld);
    }

    @Override
    public List<ContractTypeOnTld> getContractTypes(UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getContractTypes("", userId, tld);
    }

    @Override
    public Contract getContractWithId(ContractId customerId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getContract(CONTRACT.ID_CONTRACT.equal(customerId.getIntValue()), "id:" + customerId, userId, tld);
    }

    @Override
    public List<Contract> getContractWithCustomerId(CustomerId customerId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getContracts("", userId, tld, CONTRACT.ID_CUSTOMER.equal(customerId.getIntValue()));
    }

    private List<Contract> getContracts(String logComment, UserId userId, TldServiceFacade tld, Condition... conditions) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            SimpleSelectConditionStep<ContractTableRecord> select = factory.selectFrom(CONTRACT)
                                                                           .where(conditions);

            Result<ContractTableRecord> result = select.fetch();

            if (result.size() == 0) {
                throw new NotFoundException("No Customer found whith " + logComment);
            }

            return JooqConverterFacade.convertList(result, Contract.class, userId, tld);

        } catch (Exception e) {
            throw new ServiceException("getContracts(" + logComment + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    private List<ContractTypeOnTld> getContractTypes(String logComment, UserId userId, TldServiceFacade tld, Condition... conditions) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            SelectConditionStep select = factory.select().from(CONTRACTTYPE_TLD_R)
                                                .join(CONTRACT_TYPE).on(CONTRACTTYPE_TLD_R.ID_CONTRACTTYPE.equal(CONTRACT_TYPE.ID_CONTRACTTYPE))
                                                .where(conditions);

            Result<Record> result = select.fetch();

            if (result.size() == 0) {
                throw new NotFoundException("No Customer found whith " + logComment);
            }

            return JooqConverterFacade.convertList(result, ContractTypeOnTld.class, userId, tld);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("getContractTypes(" + logComment + ") failed", e);

        } finally {
            this.closeFactory(factory);
        }
    }

    private List<ContractOffre> getContractOffres(String logComment, UserId userId, TldServiceFacade tld, Condition... conditions) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            SimpleSelectConditionStep<ContractTypeTableRecord> select = factory.selectFrom(CONTRACT_TYPE)
                                                                               .where(conditions);

            Result<ContractTypeTableRecord> result = select.fetch();

            if (result.size() == 0) {
                throw new NotFoundException("No Offer found" + logComment);
            }

            return JooqConverterFacade.convertList(result.getValues(CONTRACT_TYPE.ID_CONTRACTTYPE, Integer.class), ContractOffre.class, userId, tld);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("getContractOffres(" + logComment + ") failed", e);

        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public List<ContractOffre> getContractOffres(String typeOffre, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getContractOffres("Offres membre", userId, tld, CONTRACT_TYPE.PROFIL.equal(typeOffre));
    }

    private Contract getContract(Condition condition, String logComment, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            SimpleSelectConditionStep<ContractTableRecord> select = factory.selectFrom(CONTRACT)
                                                                           .where(condition);

            Result<ContractTableRecord> result = select.fetch();

            if (result.size() == 0) {
                throw new NotFoundException("No Customer found whith " + logComment);
            }

            return JooqConverterFacade.convert(result.get(0), Contract.class, userId, tld);

        } catch (Exception e) {
            throw new ServiceException("getCustomer(" + logComment + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    protected Factory createFactory() throws ConnectionException {
        Connection connection = this.sqlConnectionFactory.createConnection();
        return new Factory(connection, SQLDialect.ORACLE);
    }

    protected void closeFactory(Factory factory) throws ConnectionException {
        this.sqlConnectionFactory.closeConnection(factory.getConnection());
    }

    @Override
    public ContractTypeOnTld getContractType(int idContracttypetld, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getContractTypes("", userId, tld, CONTRACTTYPE_TLD_R.ID_CONTRACTTYPE.equal(idContracttypetld)).get(0);
    }

    @Override
    public List<TldServiceFacade> getTlds(UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            SimpleSelectWhereStep<TldTableRecord> select = factory.selectFrom(TLD);

            Result<TldTableRecord> result = select.fetch();

            if (result.size() == 0) {
                throw new NotFoundException("No Tld found");
            }

            return JooqConverterFacade.convertList(result, TldServiceFacade.class, userId, tld);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("getTlds() failed", e);

        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public ContractTypeOnTld getContractType(int idTld, String idContractType, UserId userId, TldServiceFacade tld) throws ServiceException {
        List<ContractTypeOnTld> contractTypeOnTlds = this.getContractTypes("Search ContractTypeOnTld with id_tld:" + idTld + " id_contracttype:" + idContractType, userId, tld,
                                                                           CONTRACTTYPE_TLD_R.ID_TLD.equal(idTld).and(CONTRACT_TYPE.ID_DICTIONARY.equalIgnoreCase(idContractType)));
        if (contractTypeOnTlds.size() == 1) {
            return contractTypeOnTlds.get(0);
        } else {
            throw new ServiceException("getContractType() failed");
        }
    }

    @Override
    public boolean addContactRole(Contract contract, int id_role, CustomerContactId customerContactId, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return false;
    }

}
