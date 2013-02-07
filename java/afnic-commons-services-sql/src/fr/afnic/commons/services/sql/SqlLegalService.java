package fr.afnic.commons.services.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.domain.DomainStatus;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.legal.LegalData;
import fr.afnic.commons.legal.SyreliLegalData;
import fr.afnic.commons.services.ILegalService;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.RequestFailedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.sql.query.boa.LegalSqlQueries;
import fr.afnic.commons.services.sql.utils.QueryStatementManagement;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class SqlLegalService implements ILegalService {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(SqlLegalService.class);

    public static final String GEL_VALUE_BDD = "GEL";
    public static final String GEL_FOR_RECOVER_VALUE_BDD = "GEL_FOR_RECOVER";
    public static final String GEL_FOR_DELETE_VALUE_BDD = "GEL_FOR_DELETE";

    private final ISqlConnectionFactory sqlLegalConnectionFactory;
    private final ISqlConnectionFactory sqlAgtfConnectionFactory;

    public SqlLegalService(ISqlConnectionFactory sqlLegalConnectionFactory, ISqlConnectionFactory sqlAgtfConnectionFactory) {
        this.sqlLegalConnectionFactory = Preconditions.checkNotNull(sqlLegalConnectionFactory, "sqlLegalConnectionFactory");
        this.sqlAgtfConnectionFactory = Preconditions.checkNotNull(sqlAgtfConnectionFactory, "sqlAgtfConnectionFactory");
    }

    public SqlLegalService(SqlDatabaseEnum database, SqlDatabaseEnum database2, TldServiceFacade tld) throws ServiceException {
        this.sqlLegalConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
        this.sqlAgtfConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database2, tld);
    }

    @Override
    public SyreliLegalData getSyreliComplaintData(String pNdd, String pNumDossier, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(pNdd, "pNdd");
        Preconditions.checkNotNull(pNumDossier, "pNumDossier");
        Preconditions.checkNotEmpty(pNdd, "pNdd");
        Preconditions.checkNotEmpty(pNumDossier, "pNumDossier");

        List<SyreliLegalData> vListSyreliLegalData = AppServiceFacade.getLegalService().getListSyreliForDomainName(pNdd, userId, tld);
        for (SyreliLegalData s : vListSyreliLegalData) {
            if (pNdd.equals(s.getDomainName())) {
                return s;
            }
        }
        throw new NotFoundException("No complaint data found with Ndd[" + pNdd + "] and file number[" + pNumDossier + "].");
    }

    @Override
    public void updateComplaintState(String pNumDossier, int pState, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(pNumDossier, "pNumDossier");
        Preconditions.checkNotEmpty(pNumDossier, "pNumDossier");
        if (!AppServiceFacade.getLegalService().checkExistingLegalState(pState, userId, tld))
            throw new IllegalArgumentException("pState must be a defined value in legal database.");
        if (!AppServiceFacade.getLegalService().checkExistingNumDossier(pNumDossier, userId, tld))
            throw new IllegalArgumentException("pNumDossier must be a defined value in legal database.");

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlLegalConnectionFactory);
        try {
            queryStatementManagement.executeUpdate(LegalSqlQueries.UPDATE_LEGAL_STATE, pState, pNumDossier);
        } catch (SQLException e) {
            throw new RequestFailedException("updateComplaintState(" + pNumDossier + ", " + pState + ") failed", e);
        }
    }

    @Override
    public boolean checkExistingLegalState(int pState, UserId userId, TldServiceFacade tld) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlLegalConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(LegalSqlQueries.EXIST_CODE);
        try {
            preparedStatement.setInt(1, pState);
        } catch (SQLException e) {
            throw new ServiceException("checkExistingLegalState(" + pState + ") failed", e);
        }

        String vResult = queryStatementManagement.executeStatementAsString(preparedStatement);
        if ("".equals(vResult))
            return false;
        return true;
    }

    @Override
    public boolean checkExistingNumDossier(String pNumDossier, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(pNumDossier, "pNumDossier");
        Preconditions.checkNotEmpty(pNumDossier, "pNumDossier");
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlLegalConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(LegalSqlQueries.EXIST_DOSSIER);
        try {
            preparedStatement.setString(1, pNumDossier);
        } catch (SQLException e) {
            throw new ServiceException("checkExistingNumDossier(" + pNumDossier + ") failed", e);
        }

        String vResult = queryStatementManagement.executeStatementAsString(preparedStatement);
        if ("".equals(vResult))
            return false;
        return true;
    }

    @Override
    public LegalData getComplaintData(String pNdd, String pNumDossier, UserId userId, TldServiceFacade tld) throws ServiceException {
        LegalData vLegalData = new LegalData();
        vLegalData.setSyreliLegalData(AppServiceFacade.getLegalService().getSyreliComplaintData(pNdd, pNumDossier, userId, tld));
        vLegalData.setOkBOA(AppServiceFacade.getLegalService().fillBOAStatus(vLegalData, pNdd, userId, tld));
        vLegalData.setOkAGTF(AppServiceFacade.getLegalService().isOkAgtf(pNdd, userId, tld));
        return vLegalData;
    }

    public static String[] getGraphieAndExtensionForNdd(String pNdd) {
        if (pNdd.lastIndexOf(".") == -1)
            throw new IllegalArgumentException("pNdd must be a well formed domain name.");
        String vGraphTerme = pNdd.substring(0, pNdd.lastIndexOf("."));
        String vExtensionTerme = pNdd.substring(pNdd.lastIndexOf("."), pNdd.length());
        return new String[] { vGraphTerme, vExtensionTerme };
    }

    @Override
    public boolean isOkAgtf(String pNdd, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(pNdd, "pNdd");
        Preconditions.checkNotEmpty(pNdd, "pNdd");

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlAgtfConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(LegalSqlQueries.GET_STATUT_AGTF);

        String[] vNddSplit = getGraphieAndExtensionForNdd(pNdd);
        try {
            preparedStatement.setString(1, vNddSplit[0]);
            preparedStatement.setString(2, vNddSplit[1]);
        } catch (SQLException e) {
            throw new ServiceException("isOkAgtf(" + pNdd + ") failed", e);
        }
        String vCodeAgtf = queryStatementManagement.executeStatementAsString(preparedStatement);
        if ((vCodeAgtf == null) || ("".equals(vCodeAgtf))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean fillBOAStatus(LegalData pLegalData, String pNdd, UserId userId, TldServiceFacade tld) throws ServiceException {
        boolean isOk = true;
        Preconditions.checkNotNull(pLegalData, "pLegalData");
        Preconditions.checkNotNull(pNdd, "pNdd");
        Preconditions.checkNotEmpty(pNdd, "pNdd");

        Qualification vQualification = null;

        pLegalData.setDomain(AppServiceFacade.getDomainService().getDomainWithName(pNdd, userId, tld));
        if (!DomainStatus.isDomainStatusOkForW4(pLegalData.getDomainStatus())) {
            isOk = false;
        }

        List<Ticket> vListTicket = new ArrayList<Ticket>();
        try {
            vListTicket = pLegalData.getDomain().getTickets();
            if (!vListTicket.isEmpty()) {
                Ticket vTicket = vListTicket.get(vListTicket.size() - 1);
                if (vTicket.hasNotFinalStatus()) {
                    pLegalData.setTicket(vTicket);
                    isOk = false;
                }
            }
        } catch (NotFoundException e) {
            LOGGER.debug("No opened ticked for Ndd : [" + pNdd + "]");
        }
        String vNicHandle = pLegalData.getDomain().getHolderHandle();
        try {
            vQualification = AppServiceFacade.getQualificationService().getQualificationInProgress(vNicHandle, userId, tld);
        } catch (NotFoundException e) {
            LOGGER.debug("No qualification in progress for this nichandle : [" + vNicHandle + "]");
        }

        if (vQualification == null)
            return isOk;
        PortfolioStatus vPortfolioStatus = vQualification.getPortfolioStatus();
        pLegalData.setPortfolioStatus(vPortfolioStatus);
        if ((vPortfolioStatus != PortfolioStatus.Active) && (vPortfolioStatus != PortfolioStatus.PendingFreeze)) {
            isOk = false;
        }
        return isOk;
    }

    @Override
    public void createOrUpdateAgtfState(String pNdd, int pState, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(pNdd, "pNdd");
        Preconditions.checkNotEmpty(pNdd, "pNdd");

        String[] vNddSplit = getGraphieAndExtensionForNdd(pNdd);
        if ((pState < 0) || (pState > 3))
            throw new IllegalArgumentException("pState must be a defined value in legal database.");
        if (pState == 0) {
            this.logicalDeleteAgtf(vNddSplit[0], vNddSplit[1], userId, tld);
        } else {
            this.majAgtf(vNddSplit[0], vNddSplit[1], pState);
        }

    }

    private void logicalDeleteAgtf(String pGraphTerme, String pExtensionTerme, UserId userId, TldServiceFacade tld) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlAgtfConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(LegalSqlQueries.REMOVE_GEL_SYRELI);
        try {
            preparedStatement.setString(1, pGraphTerme);
            preparedStatement.setString(2, pExtensionTerme);
            queryStatementManagement.executeStatement(null, preparedStatement, userId, tld);
        } catch (SQLException e) {
            throw new RequestFailedException("majAgtf(" + pGraphTerme + ", " + pExtensionTerme + " ) failed", e);
        }
    }

    private void majAgtf(String pGraphTerme, String pExtensionTerme, int pState) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlAgtfConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(LegalSqlQueries.CREATE_GEL_SYRELI);
        try {
            preparedStatement.setString(1, pGraphTerme);
            preparedStatement.setString(2, pExtensionTerme);
            preparedStatement.setInt(3, pState);
            queryStatementManagement.executeStatement(preparedStatement);
        } catch (SQLException e) {
            throw new RequestFailedException("majAgtf(" + pGraphTerme + ", " + pExtensionTerme + ", " + pState + ") failed", e);
        }
    }

    @Override
    public List<SyreliLegalData> getListSyreliForDomainName(String pNdd, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(pNdd, "pNdd");
        Preconditions.checkNotEmpty(pNdd, "pNdd");

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlLegalConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(LegalSqlQueries.GET_LEGAL_DATA_LIST);
        try {
            preparedStatement.setString(1, pNdd);
        } catch (SQLException e) {
            throw new ServiceException("getListSyreliForDomainName(" + pNdd + ") failed", e);
        }
        return queryStatementManagement.executeStatementList(SyreliLegalData.class, preparedStatement, userId, tld);
    }

    @Override
    public boolean checkExistingSyreliForDomainName(String pNdd, String pNumDossier, UserId userId, TldServiceFacade tld) throws ServiceException {
        return (AppServiceFacade.getLegalService().getSyreliComplaintData(pNdd, pNumDossier, userId, tld) != null);
    }

    @Override
    public List<Domain> getDomainAgtfFrozenList(WhoisContact pWhoisContact, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(pWhoisContact, "pWhoisContact");
        if ((pWhoisContact.getHandle() == null) || ("".equals(pWhoisContact.getHandle())))
            throw new IllegalArgumentException("pWhoisContact must have a valid handle.");
        List<Domain> vListDomain = pWhoisContact.getDomains();
        List<Domain> vRetourListDomains = this.executeSuccessiveQueryForFilteringDomainInAgtf(LegalSqlQueries.IS_AGTF_FROZEN, vListDomain, userId, tld);
        return vRetourListDomains;
    }

    /**
    * exécute en chaine des requêtes afin de voir si les éléments de la liste en entrée sont présent en base. Les éléments présents sont remontés en sortie 
    */
    private List<Domain> executeSuccessiveQueryForFilteringDomainInAgtf(String pQuery, List<Domain> pParamList, UserId userId, TldServiceFacade tld) throws ServiceException {
        ArrayList<Domain> vRetour = new ArrayList<Domain>();
        try {

            for (Domain vDomain : pParamList) {
                QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlAgtfConnectionFactory);
                PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(pQuery);
                preparedStatement.setString(1, vDomain.getName());
                QueryStatementManagement.initPreparedStatementParameters(preparedStatement, vDomain);
                if (queryStatementManagement.executeStatement(String.class, preparedStatement, userId, tld) != null) {
                    vRetour.add(vDomain);
                }
            }
            return vRetour;
        } catch (SQLException e) {
            throw new ServiceException("executeStatementList() failed", e);
        }
    }

}
