/*
 * $Id: SqlTicketDao.java,v 1.13 2010/07/16 12:59:22 ginguene Exp $ $Revision: 1.13 $ $Author: ginguene $
 */

package fr.afnic.commons.services.sql;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.beans.TicketStatus;
import fr.afnic.commons.beans.billing.BillableTicketInfo;
import fr.afnic.commons.beans.billing.CommandId;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.User;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.search.ticket.TicketSearchCriteria;
import fr.afnic.commons.beans.validatable.OperationFormId;
import fr.afnic.commons.services.ITicketService;
import fr.afnic.commons.services.exception.IllegalArgumentException;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.RequestFailedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.proxy.ProxyTicketService;
import fr.afnic.commons.services.sql.utils.QueryStatementManagement;
import fr.afnic.commons.services.sql.utils.SQLSelectQueryBuilder;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.XmlFormatter;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class SqlTicketService extends ProxyTicketService {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(SqlTicketService.class);
    private final ISqlConnectionFactory sqlConnectionFactory;

    private static final String TICKET_SELECTION = new StringBuffer().append("nicope.ticket.ID,")
                                                                     .append(" nicope.ticketdescoper.gw_operation_name,")
                                                                     .append(" nicope.ticketdescetat.gw_status,")
                                                                     .append(" nicope.nomdedomaine.NOM,")
                                                                     .append(" nicope.adherent.code, ")
                                                                     .append(" nicope.ticket.NOMORG, ")
                                                                     .append(" nicope.ticketinfo.NUMFO ").toString();

    /**
     * Constructeur avec un sqlConnectionFactory permettant d'obtenir une connection vers la base de donnees
     * 
     * @param sqlConnectionFactory
     */
    public SqlTicketService(final ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    public SqlTicketService(SqlDatabaseEnum database, TldServiceFacade tld, ITicketService delegationService) throws ServiceException {
        super(delegationService);
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    @Override
    public void updateStatus(final String ticketId, final TicketStatus newStatus, final String comment, final String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {

        final int maxRemHistoSize = 2000;
        if (comment.length() > maxRemHistoSize) {
            throw new IllegalArgumentException("Comment cannot have more than " + maxRemHistoSize + " characters (actual: " + comment.length() + ")");
        }

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {

            final String sql = "select " + " nicope.NOMDEDOMAINE.NOM," + " nicope.TICKET.SEED" + " from " + " nicope.TICKET, "
                               + " nicope.NOMDEDOMAINE " + " where " + " nicope.TICKET.IDNOMDOM = nicope.NOMDEDOMAINE.ID" + " and nicope.TICKET.Id = ?";

            String domainName = null;
            int ticketSeed;
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setString(1, ticketId);
            ResultSet resultSet = preparedStatement.executeQuery();

            User user = AppServiceFacade.getUserService().getUser(userLogin, userId, tld);

            if (resultSet.next()) {
                domainName = resultSet.getString(1);
                ticketSeed = resultSet.getInt(2);
            } else {
                throw new NotFoundException("no ticket found with id " + ticketId);
            }
            resultSet.close();
            preparedStatement.close();
            queryStatementManagement.closeConnection();

            final CallableStatement callableStatement = queryStatementManagement.getConnection().prepareCall("begin ? := ChangeEtatTicket(?, ?, ?, ?, ?, ?, ?) ; end;");
            callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
            callableStatement.setString(2, ticketId);
            callableStatement.setString(3, domainName);
            callableStatement.setInt(4, ticketSeed);
            callableStatement.setString(5, newStatus.getDescription(userId, tld));
            callableStatement.setString(6, user.getNicpersLogin());
            callableStatement.setString(7, comment);
            callableStatement.setString(8, null);
            callableStatement.execute();

            final String result = (String) callableStatement.getObject(1);
            if (!"OK".equals(result)) {
                throw new ServiceException("Stored procedure ChangeEtatTicket() failed:" + result);
            }
            callableStatement.close();
            resultSet.close();
            preparedStatement.close();

        } catch (final SQLException e) {
            throw new ServiceException("changeStatus(" + ticketId + ", " + newStatus + "," + comment + "," + userLogin + ") failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
    }

    @Override
    public List<String> getEmail(final String ticketId, UserId userId, TldServiceFacade tld) throws ServiceException {
        final List<String> mails = new ArrayList<String>();

        final String sql = "select nicope.ticketmel.mel, nicope.ticketmel.doctype from nicope.ticketmel where nicope.ticketmel.idtick=? order by nicope.ticketmel.numhisto desc";

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            final PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setString(1, ticketId);
            final ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                String mailText = resultSet.getString(1);
                final String mailType = resultSet.getString(2);

                // Dans les mails on a aussi mis des documents xml provenant d'euridile, l'insee, ...
                // Dans ce cas la on reformatte le xml pour que les balises soient correctement affichée
                if (this.hasNotMailType(mailType)) {
                    mailText = XmlFormatter.formatIgnoringHeader(mailText);
                }

                mails.add(mailText);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (final SQLException e) {
            SqlTicketService.LOGGER.error(e.getMessage(), e);
        } finally {
            queryStatementManagement.closeConnection();
        }
        return mails;
    }

    private boolean hasNotMailType(String mailType) {
        return !"MA".equals(mailType);
    }

    private SQLSelectQueryBuilder buildMainSearchDomainQuery(final TicketSearchCriteria criteria) {
        SQLSelectQueryBuilder builder = new SQLSelectQueryBuilder();
        String domainName = criteria.getDomainName();
        String ticketId = criteria.getTicketId();
        String registrar = criteria.getRegistrarCode();
        String holderHandle = criteria.getHolderHandle();
        Date dateDebut = criteria.getBeginningDate();
        Date dateFin = criteria.getEndingDate();
        String operation = null;
        if (criteria.getOperation() != null) {
            operation = criteria.getOperation().toString();
        }
        String ticketState = null;
        if (criteria.getTicketStatus() != null) {
            ticketState = criteria.getTicketStatus().toString();
        }
        boolean hasHint = false;

        builder.appendSelect("nicope.ticketdescetat.GW_STATUS as statusFromString"
                             + ", nicope.ticket.NOMORG as originalRequesterName"
                             + ", nicope.tickethisto.DATEMAJ as createDate" + ", nicope.adherent.CODE as registrarCode " + ", nicope.ticketinfo.NUMFO as formId "
                             + ", nicope.ticketdescoper.GW_OPERATION_NAME as operationFromString " + ", boa.nomdedomaine.NOM as domainName " + ", nicope.ticket.ID as id");

        builder.appendFrom("nicope.tickethisto,"
                           + " nicope.ticketinfo,"
                           + " nicope.ticketdescetat,"
                           + " nicope.adherent,"
                           + " nicope.ticket,"
                           + " nicope.ticketdescoper,"
                           + " boa.nomdedomaine");

        builder.appendWhere("boa.nomdedomaine.ID = nicope.ticket.IDNOMDOM " +
                            "and nicope.ticket.ID = nicope.ticketinfo.IDTICK " +
                            "and nicope.ticket.IDOPE = nicope.ticketdescoper.ID " +
                            "and nicope.ticket.IDETAT = nicope.ticketdescetat.ID " +
                            "and nicope.ticket.ID = nicope.tickethisto.IDTICK " +
                            "and nicope.adherent.ID = nicope.ticket.IDADHER " +
                            "and nicope.tickethisto.NUM = 0 ");

        if ((domainName != null) && (!"".equals(domainName))) {
            builder.appendWhere(" AND (nomdedomaine.nom = ? ");
            builder.addParameters(domainName);
            builder.appendWhere(" or nomdedomaine.nom_bundle = ? ");
            builder.addParameters(domainName);
            builder.appendWhere(" or nomdedomaine.nom_i18n = ?) ");
            builder.addParameters(domainName);
        }

        if ((registrar != null) && (!"".equals(registrar))) {
            if (!hasHint) {
                builder.appendFirstSelect(" /*+ parallel(adherent, 10) */ ");
                hasHint = true;
            }
            builder.appendWhere(" AND (upper(nicope.adherent.NOM) = upper(?) OR upper(nicope.adherent.CODE) = upper(?))");
            builder.addParameters(registrar);
            builder.addParameters(registrar);
        }

        if ((holderHandle != null) && (!"".equals(holderHandle))) {
            builder.appendFrom(", whois.domain, whois.object_contact_r, whois.nh");
            builder.appendWhere(" and boa.nomdedomaine.nom = whois.domain.name");
            builder.appendWhere(" and whois.object_contact_r.CONTACT_ID = whois.nh.object_id");
            builder.appendWhere(" and whois.object_contact_r.CONTACT_TYPE='HOLDER'");
            builder.appendWhere(" and whois.object_contact_r.object_id =  whois.domain.ID");
            builder.appendWhere(" and whois.nh.PREFIX||TO_CHAR(whois.nh.NUM)||'-'||whois.nh.SUFFIX = ?");
            builder.addParameters(holderHandle);
        }

        if ((ticketId != null) && (!"".equals(ticketId))) {
            builder.appendWhere(" AND nicope.ticket.ID = ?");
            builder.addParameters(ticketId);
        }

        if ((operation != null) && (!"".equals(operation))) {
            if (!hasHint) {
                builder.appendFirstSelect(" /*+ use_nl(ticketdescoper tickethisto ticket) LEADING( ticketdescoper )  parallel(ticket, 1) */ ");
                hasHint = true;
            }
            builder.appendWhere(" AND nicope.ticketdescoper.GW_OPERATION_NAME = ?");
            builder.addParameters(operation);
        }

        if ((ticketState != null) && (!"".equals(ticketState))) {
            if (!hasHint) {
                builder.appendFirstSelect(" /*+ use_nl(ticketdescetat tickethisto ticket) LEADING( ticketdescetat )  parallel(ticket, 1) */ ");
                hasHint = true;
            }
            builder.appendWhere(" AND nicope.ticketdescetat.GW_STATUS = ?");
            builder.addParameters(ticketState);
        }

        if (dateDebut != null) {
            java.sql.Date dateDebutSql = new java.sql.Date(dateDebut.getTime());
            builder.appendWhere(" AND nicope.tickethisto.DATEMAJ > ?");
            builder.addParameters(dateDebutSql);
        }

        if (dateFin != null) {
            java.sql.Date dateFinSql = new java.sql.Date(dateFin.getTime());
            builder.appendWhere(" AND nicope.tickethisto.DATEMAJ < ?");
            builder.addParameters(dateFinSql);
        }
        return builder;
    }

    @Override
    public List<Ticket> searchTicket(final TicketSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        SQLSelectQueryBuilder builder = this.buildMainSearchDomainQuery(criteria);

        System.err.println("searchTicket " + builder.getQuery());

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(builder.getQuery());
        try {
            QueryStatementManagement.initPreparedStatementParameters(preparedStatement, builder.getListParameters());
        } catch (SQLException e) {
            throw new ServiceException("searchTicket() failed query:" + builder.getQuery(), e);
        }
        List<Ticket> tickets = queryStatementManagement.executeStatementList(Ticket.class, preparedStatement, userId, tld);

        /*if (tickets.size() > criteria.getMaxResultCount()) {
            throw new TooManyResultException(criteria.getMaxResultCount());
        }*/
        return tickets;
    }

    /**
     * @throws DaoException
     * @throws RequestFailedDaoException
     *             Retourne un Ticket a partir d'un resulset qui a été obtenue un utilisant la methode getSelectionQuery pour écrire la partie select
     *             de la requete. La méthode next() du resultSet doit avoir été appelée avant de le passer comme parametre à cette méthode.
     * 
     * @param resultSet
     * @return
     * @throws SQLException
     * @throws
     */
    private Ticket getTicketFromResultSet(final ResultSet resultSet, UserId userId, TldServiceFacade tld) throws SQLException, RequestFailedException, ServiceException {

        final Ticket result = new Ticket(userId, tld);
        int ind = 1;
        result.setId(resultSet.getString(ind++));

        // on obtient le code de l'operation dans la BD mais on veut le code
        // gateway donc on passe
        // par le dictionnaire d'operation de ticket pour obtenir la conversion
        result.setOperationFromString(resultSet.getString(ind++));
        result.setStatusFromString(resultSet.getString(ind++));

        result.setDomainName(resultSet.getString(ind++));
        result.setRegistrarCode(resultSet.getString(ind++));
        result.setOriginalRequesterName(resultSet.getString(ind++));
        result.setOperationFormId(new OperationFormId(resultSet.getInt(ind++)));
        return result;
    }

    @Override
    public List<BillableTicketInfo> getBillableTickets(int month, int year, int resultCount, UserId userId, TldServiceFacade tld) throws ServiceException {
        return Collections.emptyList();
    }

    @Override
    public void updateTicketBillingReference(String ticketId, CommandId commandId, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();

    }

}
