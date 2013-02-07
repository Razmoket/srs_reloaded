package fr.afnic.commons.services.jooq;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.impl.Factory;

import fr.afnic.commons.beans.NicHandle;
import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.beans.TicketOperation;
import fr.afnic.commons.beans.TicketStatus;
import fr.afnic.commons.beans.billing.BillableTicketInfo;
import fr.afnic.commons.beans.billing.CommandId;
import fr.afnic.commons.beans.profiling.users.User;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.search.ticket.TicketSearchCriteria;
import fr.afnic.commons.services.ITicketService;
import fr.afnic.commons.services.exception.ConnectionException;
import fr.afnic.commons.services.exception.IllegalArgumentException;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.jooq.converter.JooqConverterFacade;
import fr.afnic.commons.services.jooq.stub.boa.tables.NomdedomaineTable;
import fr.afnic.commons.services.jooq.stub.boa.tables.VBillableTicketView;
import fr.afnic.commons.services.jooq.stub.boa.tables.VDomainView;
import fr.afnic.commons.services.jooq.stub.boa.tables.VTicketView;
import fr.afnic.commons.services.jooq.stub.boa.tables.records.VBillableTicketViewRecord;
import fr.afnic.commons.services.jooq.stub.boa.tables.records.VTicketViewRecord;
import fr.afnic.commons.services.jooq.stub.nicope.routines.Changeetatticket;
import fr.afnic.commons.services.jooq.stub.nicope.tables.TicketTable;
import fr.afnic.commons.services.jooq.stub.nicope.tables.TickethistoTable;
import fr.afnic.commons.services.jooq.stub.nicope.tables.TicketmelTable;
import fr.afnic.commons.services.jooq.stub.whois.tables.ContactTable;
import fr.afnic.commons.services.jooq.stub.whois.tables.NhTable;
import fr.afnic.commons.services.jooq.stub.whois.tables.ObjectContactRTable;
import fr.afnic.commons.services.proxy.ProxyTicketService;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.XmlFormatter;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class JooqTicketService extends ProxyTicketService {

    private ISqlConnectionFactory sqlConnectionFactory = null;

    public JooqTicketService(final ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    public JooqTicketService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    public JooqTicketService(final ISqlConnectionFactory sqlConnectionFactory, ITicketService delegationService) {
        super(delegationService);
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    @Override
    public void updateStatus(final String ticketId, final TicketStatus newStatus, final String comment, final String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {

        final int maxRemHistoSize = 2000;
        if (comment.length() > maxRemHistoSize) {
            throw new IllegalArgumentException("Comment cannot have more than " + maxRemHistoSize + " characters (actual: " + comment.length() + ")");
        }

        Factory factory = this.createFactory();
        try {
            Result<Record> result = factory.select(TicketTable.TICKET.SEED, NomdedomaineTable.NOMDEDOMAINE.NOM)
                                           .from(TicketTable.TICKET)
                                           .join(NomdedomaineTable.NOMDEDOMAINE)
                                           .on(TicketTable.TICKET.IDNOMDOM.equal(NomdedomaineTable.NOMDEDOMAINE.ID))
                                           .where(TicketTable.TICKET.ID.equal(ticketId))
                                           .fetch();

            if (result.isEmpty()) {
                throw new NotFoundException("no ticket found with id " + ticketId);
            }

            int seed = result.getValue(0, TicketTable.TICKET.SEED).intValue();
            String domainName = result.getValueAsString(0, NomdedomaineTable.NOMDEDOMAINE.NOM);

            User user = AppServiceFacade.getUserService().getUser(userLogin, userId, tld);

            Changeetatticket proc = new Changeetatticket();
            proc.setPIdtick(ticketId);
            proc.setPEtat(newStatus.getDescription(userId, tld));
            proc.setPLogin(user.getNicpersLogin());
            proc.setPSeed(seed);
            proc.setPAddhisto(comment);
            proc.setPNomdom(domainName);
            proc.execute(factory);

            if (!"OK".equals(proc.getReturnValue())) {
                throw new ServiceException("Stored procedure ChangeEtatTicket() failed: " + proc.getReturnValue());
            }

        } catch (Exception e) {
            throw new ServiceException("getNicHandlesToSurvey() failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public List<String> getEmail(final String ticketId, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            Result<Record> result = factory.select(TicketmelTable.TICKETMEL.DOCTYPE,
                                                   TicketmelTable.TICKETMEL.MEL)
                                           .from(TicketmelTable.TICKETMEL)
                                           .where(TicketmelTable.TICKETMEL.IDTICK.equal(ticketId))
                                           .orderBy(TicketmelTable.TICKETMEL.NUMHISTO.desc())
                                           .fetch();

            final List<String> emails = new ArrayList<String>();
            for (Record record : result) {
                String text = this.format(record.getValueAsString(TicketmelTable.TICKETMEL.MEL),
                                          record.getValueAsString(TicketmelTable.TICKETMEL.DOCTYPE));
                emails.add(text);
            }
            return emails;
        } catch (Exception e) {
            throw new ServiceException("getNicHandlesToSurvey() failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    private String format(String emailText, String emailType) {
        // Dans les mails on a aussi mis des documents xml provenant d'euridile, l'insee, ...
        // Dans ce cas la on reformatte le xml pour que les balises soient correctement affichée
        if (this.hasNotEmailType(emailType)) {
            return XmlFormatter.formatIgnoringHeader(emailText);
        } else {
            return emailText;
        }
    }

    private boolean hasNotEmailType(String mailType) {
        return !"MA".equals(mailType);
    }

    @Override
    public List<Ticket> searchTicket(final TicketSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(criteria, "criteria");
        Factory factory = this.createFactory();
        try {

            String domainName = criteria.getDomainName();
            String ticketId = criteria.getTicketId();
            String registrar = criteria.getRegistrarCode();
            String holderHandle = criteria.getHolderHandle();
            Date beginningDate = criteria.getBeginningDate();
            Date endingDate = criteria.getEndingDate();
            TicketOperation operation = criteria.getOperation();
            TicketStatus status = criteria.getTicketStatus();

            SelectJoinStep step = factory.select(VTicketView.V_TICKET.getFields())
                                         .from(VTicketView.V_TICKET);

            List<Condition> conditions = new ArrayList<Condition>();

            if (StringUtils.isNotEmpty(domainName)) {

                SelectConditionStep idDomainSelection = factory.select(NomdedomaineTable.NOMDEDOMAINE.ID)
                                                               .from(NomdedomaineTable.NOMDEDOMAINE)
                                                               .where(NomdedomaineTable.NOMDEDOMAINE.NOM.equal(domainName))
                                                               .or(NomdedomaineTable.NOMDEDOMAINE.NOM_BUNDLE.equal(domainName))
                                                               .or(NomdedomaineTable.NOMDEDOMAINE.NOM_I18N.equal(domainName));

                Condition condition = VTicketView.V_TICKET.ID_DOMAIN_NICOPE.in(idDomainSelection);
                conditions.add(condition);

            }

            if (StringUtils.isNotEmpty(registrar)) {
                /* SelectConditionStep idRegistrarSelection = factory.select(CustomerTable.CUSTOMER.ID_CUSTOMER_NICOPE)
                                                                   .from(CustomerTable.CUSTOMER)
                                                                   .where(CustomerTable.CUSTOMER.ORGANIZATION_NAME.equalIgnoreCase(registrar))
                                                                   .or(CustomerTable.CUSTOMER.CUSTOMER_NUMBER.equalIgnoreCase(registrar))
                                                                   .or(CustomerTable.CUSTOMER.CODE.equalIgnoreCase(registrar));

                 Condition condition = VTicketView.V_TICKET.ID_CUSTOMER_NICOPE.in(idRegistrarSelection);
                 conditions.add(condition);*/
            }

            if (StringUtils.isNotEmpty(holderHandle)) {
                NicHandle handle = new NicHandle(holderHandle);
                SelectConditionStep idHolderSelection = factory.select(VDomainView.V_DOMAIN.ID_DOMAIN_NICOPE)
                                                               .from(VDomainView.V_DOMAIN)
                                                               .join(ObjectContactRTable.OBJECT_CONTACT_R).on(ObjectContactRTable.OBJECT_CONTACT_R.OBJECT_ID.equal(VDomainView.V_DOMAIN.ID_DOMAIN_WHOIS))
                                                               .join(ContactTable.CONTACT).on(ObjectContactRTable.OBJECT_CONTACT_R.CONTACT_ID.equal(ContactTable.CONTACT.ID))
                                                               .join(NhTable.NH).on(NhTable.NH.OBJECT_ID.equal(ContactTable.CONTACT.ID))
                                                               .where(NhTable.NH.PREFIX.equal(handle.getPrefix()))
                                                               .and(NhTable.NH.NUM.equal(BigDecimal.valueOf(handle.getNumAsInt())))
                                                               .and(NhTable.NH.SUFFIX.equal(handle.getSuffix()))
                                                               .and(ObjectContactRTable.OBJECT_CONTACT_R.CONTACT_TYPE.equal("HOLDER"));

                Condition condition = VTicketView.V_TICKET.ID_DOMAIN_NICOPE.in(idHolderSelection);
                conditions.add(condition);

            }

            if (StringUtils.isNotEmpty(ticketId)) {
                conditions.add(VTicketView.V_TICKET.ID_TICKET.equalIgnoreCase(ticketId));
            }

            if (operation != null) {
                String operationStr = JooqConverterFacade.convert(operation, String.class, userId, tld);
                conditions.add(VTicketView.V_TICKET.OPERATION.equal(operationStr));
            }

            if (status != null) {
                String statusStr = JooqConverterFacade.convert(status, String.class, userId, tld);
                conditions.add(VTicketView.V_TICKET.STATUS.equal(statusStr));
            }

            if (beginningDate != null) {
                java.sql.Date sqlBeginningDate = new java.sql.Date(beginningDate.getTime());
                SelectConditionStep idBeginningDateSelection = factory.select(TickethistoTable.TICKETHISTO.IDTICK)
                                                                      .from(TickethistoTable.TICKETHISTO)
                                                                      .where(TickethistoTable.TICKETHISTO.DATEMAJ.greaterOrEqual(sqlBeginningDate))
                                                                      .and(TickethistoTable.TICKETHISTO.NUM.equal(Short.valueOf((short) 0)));

                Condition condition = VTicketView.V_TICKET.ID_TICKET.in(idBeginningDateSelection);
                conditions.add(condition);
            }

            if (endingDate != null) {
                java.sql.Date sqlEndingDate = new java.sql.Date(endingDate.getTime());

                SelectConditionStep idEndingDateDateSelection = factory.select(TickethistoTable.TICKETHISTO.IDTICK)
                                                                       .from(TickethistoTable.TICKETHISTO)
                                                                       .where(TickethistoTable.TICKETHISTO.DATEMAJ.lessOrEqual(sqlEndingDate))
                                                                       .and(TickethistoTable.TICKETHISTO.NUM.equal(Short.valueOf((short) 0)));

                Condition condition = VTicketView.V_TICKET.ID_TICKET.in(idEndingDateDateSelection);
                conditions.add(condition);
            }

            Result<VTicketViewRecord> results = step.where(conditions)
                                                    .fetchInto(VTicketView.V_TICKET);

            return JooqConverterFacade.convertIterator(results.iterator(), Ticket.class, userId, tld);

        } catch (Exception e) {
            throw new ServiceException("searchTicket(" + criteria.toString() + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public List<BillableTicketInfo> getBillableTickets(int month, int year, int resultCount, UserId userId, TldServiceFacade tld) throws ServiceException {

        Preconditions.checkGreaterThan(year, 0, "year");
        Preconditions.checkBetween(month, 1, 12, "month");

        Factory factory = this.createFactory();
        try {
            DateTime startDate = new DateTime(year, month, 1, 0, 0, 0, 0);
            DateTime endDate = startDate.plusMonths(1);

            Result<VBillableTicketViewRecord> results = factory.selectFrom(VBillableTicketView.V_BILLABLE_TICKET)
                                                               .where(VBillableTicketView.V_BILLABLE_TICKET.COMMAND_DATE.greaterOrEqual(new java.sql.Date(startDate.getMillis())))
                                                               .and(VBillableTicketView.V_BILLABLE_TICKET.COMMAND_DATE.lessThan(new java.sql.Date(endDate.getMillis())))
                                                               .limit(resultCount)
                                                               .fetch();

            return JooqConverterFacade.convertIterator(results.iterator(), BillableTicketInfo.class, userId, tld);

        } catch (Exception e) {
            throw new ServiceException("getBillableTickets(" + month + ", " + year + ", " + resultCount + ", " + userId + ", " + tld + ") failed", e);
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
    public void updateTicketBillingReference(String ticketId, CommandId commandId, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();

    }

}
