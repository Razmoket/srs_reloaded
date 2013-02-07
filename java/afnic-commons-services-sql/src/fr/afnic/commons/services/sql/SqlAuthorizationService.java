/*
 * $Id: SqlAuthorizationService.java,v 1.23 2010/09/09 08:26:42 ginguene Exp $
 * $Revision: 1.23 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.afnic.commons.beans.Authorization;
import fr.afnic.commons.beans.AuthorizationOperation;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IAuthorizationService;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.proxy.ProxyAuthorizationService;
import fr.afnic.commons.services.sql.utils.QueryStatementManagement;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

/**
 * Data Access Object permettant d'accéder aux informations et d'effectuer<br/>
 * des opération sur les code d'autorisation et les requete de code d'autorisation.<br/>
 * Cette implémentation est basée sur un accès direct à la base de données    
 * 
 * @author ginguene
 * 
 */
public class SqlAuthorizationService extends ProxyAuthorizationService {

    private static final String AUTHORIZATION_SELECTION = " nicope.autorisation.id"
                                                          + " ,nicope.autorisation.NDD"
                                                          + " ,nicope.autorisation.ref_holder"
                                                          + " ,nicope.autorisation.ref_registrar"
                                                          + " ,nicope.autorisation.operation"
                                                          + " ,nicope.autorisation.date_creation"
                                                          + " ,nicope.autorisation.date_validite"
                                                          + " ,nicope.autorisation.reference";

    private static final String AUTHORIZATION_INSERTION = "insert into nicope.autorisation("
                                                          + " nicope.autorisation.id"
                                                          + " ,nicope.autorisation.REF_HOLDER"
                                                          + " ,nicope.autorisation.REF_REGISTRAR"
                                                          + " ,nicope.autorisation.OPERATION"
                                                          + " ,nicope.autorisation.NDD"
                                                          + " ,nicope.autorisation.DATE_VALIDITE"
                                                          + " ,nicope.autorisation.DATE_CREATION"
                                                          + " ,nicope.autorisation.REFERENCE"
                                                          + " ,nicope.autorisation.ACTIF"
                                                          + " ,nicope.autorisation.REF_USERMAJ"
                                                          + ")"
                                                          + " values (?,?,?,?,?,?,?,?,?,?)";

    protected static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(SqlAuthorizationService.class);

    private final ISqlConnectionFactory sqlConnectionFactory;

    public SqlAuthorizationService(ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    public SqlAuthorizationService(SqlDatabaseEnum database, TldServiceFacade tld, IAuthorizationService delegationService) throws ServiceException {
        super(delegationService);
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    @Override
    public List<Authorization> getAuthorizations(UserId userId, TldServiceFacade tld) throws ServiceException {
        List<Authorization> result = null;

        String sql = " SELECT "
                     + SqlAuthorizationService.AUTHORIZATION_SELECTION
                     + " FROM "
                     + " nicope.autorisation";

        if (SqlAuthorizationService.LOGGER.isTraceEnabled())
            SqlAuthorizationService.LOGGER.trace("Execute " + sql);

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {

            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            result = new ArrayList<Authorization>();

            while (resultSet.next()) {
                result.add(this.getAuthorizationFromResultSet(resultSet, userId, tld));
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            SqlAuthorizationService.LOGGER.error(e.getMessage(), e);
            result = null;
        } finally {
            queryStatementManagement.closeConnection();
        }
        return result;

    }

    @Override
    public List<Authorization> getAuthorizations(String domain, String registrarCode, String holderHandle, UserId userId, TldServiceFacade tld) throws ServiceException {

        String sql = " SELECT "
                     + SqlAuthorizationService.AUTHORIZATION_SELECTION
                     + " FROM "
                     + "  nicope.autorisation,"
                     + "  nicope.adherent"
                     + " WHERE "
                     + "  nicope.autorisation.REF_REGISTRAR = nicope.adherent.id"
                     + "  AND nicope.autorisation.NDD = ?"
                     + "  AND nicope.adherent.code = ?"
                     + "  AND nicope.autorisation.REF_HOLDER = ?";

        if (SqlAuthorizationService.LOGGER.isTraceEnabled()) {
            SqlAuthorizationService.LOGGER.trace("Execute " + sql + " with holderHandle=" + holderHandle + ";domain=" + domain + ";registrarCode=" + registrarCode);
        }

        // Le champs REF_HOLDER est un char(15), on complete donc la fin du holderHandle par des espaces
        // while ( holderHandle.length() < 15 ) holderHandle += " ";

        try {
            return this.getAuthorizationsFromSql(sql, userId, tld, domain, registrarCode, holderHandle);
        } catch (SQLException e) {
            throw new ServiceException("getAuthorizations("
                                       + domain + " ,"
                                       + registrarCode + " ,"
                                       + holderHandle + ") failed", e);
        }
    }

    @Override
    public Authorization getUsableAuthorization(String domain, String registrarCode, String holderHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        List<Authorization> authorizations = this.getAuthorizations(domain, registrarCode, holderHandle, userId, tld);

        if (authorizations != null) {
            for (Authorization authorization : authorizations) {
                if (authorization.isAlwaysValid()
                    && authorization.hasNotBeenUsed()) {
                    return authorization;
                }
            }
        }

        throw new NotFoundException("No usable authorization for domain " + domain
                                    + ", registrarCode " + registrarCode
                                    + ", holderHandle " + holderHandle);
    }

    private List<Authorization> getAuthorizationsFromSql(String sql, final UserId userId, final TldServiceFacade tld, Object... params) throws SQLException, ServiceException {

        ResultSetProcessor<List<Authorization>> processor = new ResultSetProcessor<List<Authorization>>() {

            @Override
            public void populateResultFromResultSet(ResultSet resultSet) throws SQLException, ServiceException {
                this.result = new ArrayList<Authorization>();
                while (resultSet.next()) {
                    this.result.add(SqlAuthorizationService.this.getAuthorizationFromResultSet(resultSet, userId, tld));
                }
            }
        };

        new QueryStatementManagement(this.sqlConnectionFactory).executeQuery(processor, sql, params);
        return processor.getResult();

    }

    private Date getDateOfUse(int autorisationId) throws ServiceException {
        String sql = "SELECT th.datemaj "
                     + " FROM "
                     + " autorisation oto,"
                     + " foprinc fo, "
                     + " foautre fa, "
                     + " nomdedomaine ndd,"
                     + " ticket tk,"
                     + " tickethisto th,"
                     + " ticketinfo ti"
                     + " WHERE "
                     + " ndd.nom=oto.ndd "
                     + " AND fo.idnomdom=ndd.id "
                     + " AND oto.ref_registrar=fo.idadher "
                     + " AND fo.num=fa.num AND oto.ref_holder=fa.titunh "
                     + " AND oto.reference=fa.authinfo AND fo.oper=oto.operation "
                     + " AND ti.numfo=fo.num AND ti.idtick=tk.id AND th.idtick=tk.id "
                     + " AND tk.lasthisto=th.num and tk.idetat='FI' "
                     + " AND oto.id=?";

        if (SqlAuthorizationService.LOGGER.isTraceEnabled())
            SqlAuthorizationService.LOGGER.trace("Execute " + sql + " with autorisationId=" + autorisationId);

        ResultSetProcessor<Date> processor = new ResultSetProcessor<Date>() {
            @Override
            public void populateResultFromResultSet(ResultSet resultSet) throws SQLException, ServiceException {
                if (resultSet.next()) {
                    long time = resultSet.getDate(1).getTime();
                    this.result = new Date(time);
                }
            }
        };

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            queryStatementManagement.executeQuery(processor, sql, autorisationId);
        } catch (SQLException e) {
            throw new ServiceException("Error while getDateOfUse(" + autorisationId + ")", e);
        }
        return processor.getResult();

    }

    @Override
    public List<Authorization> getAuthorizations(String domain, final UserId userId, final TldServiceFacade tld) throws ServiceException {

        final String sql = " SELECT "
                           + SqlAuthorizationService.AUTHORIZATION_SELECTION
                           + " FROM "
                           + " nicope.autorisation"
                           + " WHERE "
                           + " nicope.autorisation.NDD = ?";

        if (SqlAuthorizationService.LOGGER.isTraceEnabled())
            SqlAuthorizationService.LOGGER.trace("Execute " + sql + " with domain=" + domain);

        ResultSetProcessor<ArrayList<Authorization>> processor = new ResultSetProcessor<ArrayList<Authorization>>() {
            @Override
            public void populateResultFromResultSet(ResultSet resultSet) throws SQLException, ServiceException {
                this.result = new ArrayList<Authorization>();
                while (resultSet.next()) {
                    this.result.add(SqlAuthorizationService.this.getAuthorizationFromResultSet(resultSet, userId, tld));
                }
            }

        };

        return processor.getResult();
    }

    @Override
    public Authorization getAuthorizationWithId(int id, UserId userId, TldServiceFacade tld) throws ServiceException {
        Authorization result = null;

        String sql = " SELECT "
                     + SqlAuthorizationService.AUTHORIZATION_SELECTION
                     + " FROM "
                     + " nicope.autorisation"
                     + " WHERE "
                     + " nicope.autorisation.ID = ? ";

        // if (LOGGER.isTraceEnabled())
        SqlAuthorizationService.LOGGER.debug("Execute " + sql + " with id=" + id);
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = this.getAuthorizationFromResultSet(resultSet, userId, tld);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("getAuthorizationWithId(" + id + ") failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
        return result;
    }

    /*
        @Override
        public int createAuthorization(Authorization authorization) throws ServiceException {

            if (authorization == null)
                throw new IllegalArgumentException("authorization cannot be null");
            if (authorization.getUserId() == null)
                throw new IllegalArgumentException("authorization.userId cannot be null");

            QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
            try {
                int nextId = this.getNextAuthorizationId();

                String sql = SqlAuthorizationService.AUTHORIZATION_INSERTION;

                if (SqlAuthorizationService.LOGGER.isTraceEnabled())
                    SqlAuthorizationService.LOGGER.trace("Execute " + sql + " with reference=" + authorization.getCode());

                java.sql.Date sqlCreateDate = null;
                if (authorization.getCreateDate() != null) {
                    sqlCreateDate = new java.sql.Date(authorization.getCreateDate().getTime());
                }

                java.sql.Date sqlValidityDate = null;
                if (authorization.getExpirationDate() != null) {
                    sqlValidityDate = new java.sql.Date(authorization.getExpirationDate().getTime());
                }

                String operationId = authorization.getOperation().getOperationId();

                String actifString = "O";
                if (!authorization.isActif()) {
                    actifString = "N";
                }

                PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
                preparedStatement.setInt(1, nextId);
                preparedStatement.setString(2, authorization.getHolderHandle());
                preparedStatement.setString(3, this.getRegistrarIdFromCode(authorization.getRegistrarCode()));
                preparedStatement.setString(4, operationId);
                preparedStatement.setString(5, authorization.getDomainName());
                preparedStatement.setDate(6, sqlValidityDate);
                preparedStatement.setDate(7, sqlCreateDate);
                preparedStatement.setString(8, authorization.getCode());
                preparedStatement.setString(9, actifString);
                preparedStatement.setString(10, authorization.getUserId().getObjectOwner().getNicpersId());

                preparedStatement.executeUpdate();
                preparedStatement.close();

                authorization.setActif(true);
                authorization.setId(nextId);

            } catch (SQLException e) {
                throw new ServiceException("createAuthorization() failed:\n" + ObjectSerializer.toXml(authorization), e);
            } finally {
                queryStatementManagement.closeConnection();
            }
            return authorization.getId();
        }*/

    private int getNextAuthorizationId() throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            String sql = "SELECT SEQ_AUTORISATION.nextval from dual";
            if (SqlAuthorizationService.LOGGER.isTraceEnabled())
                SqlAuthorizationService.LOGGER.trace("Execute " + sql);
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            int nextId = -1;
            if (resultSet.next()) {
                nextId = resultSet.getInt(1);
            }
            resultSet.close();
            preparedStatement.close();
            return nextId;
        } catch (SQLException e) {
            throw new ServiceException("getNextAuthorizationId() failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }

    }

    /**
     * La table autorisation est lié au champs id de la table adherent. Mais l'id n'est pas l'identifiant officiel qui est le code. Cette methode
     * permet de récupere l'id du BEà partir de son code.
     * 
     * @param registrarCode
     * @return
     * @throws ServiceException
     */
    private String getRegistrarIdFromCode(String registrarCode) throws ServiceException {

        String id = null;

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {

            String sql = "select nicope.adherent.id "
                         + " from nicope.adherent "
                         + " where "
                         + " nicope.adherent.code=?";

            if (SqlAuthorizationService.LOGGER.isTraceEnabled())
                SqlAuthorizationService.LOGGER.trace("Execute " + sql + " with registrarCode " + registrarCode);

            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setString(1, registrarCode);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getString(1);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("getRegistrarIdFromCode(" + registrarCode + ") failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }

        return id;
    }

    /* @Override
     public void updateAuthorization(Authorization authorization) throws ServiceException {
         Preconditions.checkNotNull(authorization, "authorization");
         Preconditions.checkNotNull(authorization.getOperation(), "authorization.operation");

         QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);

         try {

             String operationId = authorization.getOperation().getOperationId();

             java.sql.Date sqlValidityDate = null;
             if (authorization.getExpirationDate() != null) {
                 sqlValidityDate = new java.sql.Date(authorization.getExpirationDate().getTime());
             }

             String sql = "UPDATE nicope.autorisation "
                          + " SET "
                          + " nicope.autorisation.REF_HOLDER=?"
                          + " ,nicope.autorisation.REF_REGISTRAR=?"
                          + " ,nicope.autorisation.OPERATION=?"
                          + " ,nicope.autorisation.NDD=?"
                          + " ,nicope.autorisation.REFERENCE=?"
                          + " ,nicope.autorisation.ACTIF='O'"
                          + " ,nicope.autorisation.DATE_VALIDITE=?"
                          + " WHERE "
                          + " nicope.autorisation.id=?";

             if (SqlAuthorizationService.LOGGER.isTraceEnabled())
                 SqlAuthorizationService.LOGGER.trace("Execute " + sql + " with id " + authorization.getId());

             PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
             preparedStatement.setString(1, authorization.getHolderHandle());
             preparedStatement.setString(2, this.getRegistrarIdFromCode(authorization.getRegistrarCode()));
             preparedStatement.setString(3, operationId);
             preparedStatement.setString(4, authorization.getDomainName());
             preparedStatement.setString(5, authorization.getCode());
             preparedStatement.setDate(6, sqlValidityDate);
             preparedStatement.setInt(7, authorization.getId());

             preparedStatement.executeUpdate();
             preparedStatement.close();

         } catch (SQLException e) {
             throw new ServiceException("updateAuthorization(" + authorization.getId() + ") failed", e);
         } finally {
             queryStatementManagement.closeConnection();
         }
     }*/

    private String getRegistrarCodeFromId(String id) throws ServiceException {

        String sql = "SELECT nicope.adherent.code "
                     + "from "
                     + " nicope.adherent"
                     + " where "
                     + " nicope.adherent.id=?";

        if (SqlAuthorizationService.LOGGER.isTraceEnabled())
            SqlAuthorizationService.LOGGER.trace("Execute " + sql + " with id=" + id);

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("getRegistrarCodeFromId(" + id + ") failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
        return null;
    }

    /**
     * Retourne un Authorization a partir d'un resulset qui a été obtenue un utilisant la methode getAuthorizationSelectionQuery() pour écrire la
     * partie select de la requete. La méthode next() du resultSet doit avoir été appelée avant de le passer comme parametre à cette méthode.
     * 
     * @param resultSet
     * @return
     * @throws SQLException
     * @throws ServiceException
     * @throws SQLException
     */
    private Authorization getAuthorizationFromResultSet(ResultSet resultSet, UserId userId, TldServiceFacade tld) throws ServiceException, SQLException {
        Authorization result = new Authorization(userId, tld);

        int ind = 1;
        result.setId(resultSet.getInt(ind++));

        String domain = resultSet.getString(ind++);
        if (domain != null)
            result.setDomainName(domain.trim());

        String holderHandle = resultSet.getString(ind++);
        if (holderHandle != null)
            result.setHolderHandle(holderHandle.trim());

        String registrarCode = resultSet.getString(ind++);
        if (registrarCode != null)
            result.setRegistrarCode(this.getRegistrarCodeFromId(registrarCode.trim()));

        String operationId = resultSet.getString(ind++);

        if (operationId != null) {
            operationId = operationId.trim();
            result.setOperation(AuthorizationOperation.getOperation(operationId));
        } else {
            SqlAuthorizationService.LOGGER.error(" operation id is null");
        }

        result.setCreateDate(resultSet.getDate(ind++));
        result.setExpirationDate(resultSet.getDate(ind++));
        result.setCode(resultSet.getString(ind++));

        result.setUseDate(this.getDateOfUse(result.getId()));

        return result;
    }

}
