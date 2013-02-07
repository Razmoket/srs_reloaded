package fr.afnic.commons.services.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import fr.afnic.commons.beans.ContactSnapshot;
import fr.afnic.commons.beans.CorporateEntityWhoisContact;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.boarequest.TopLevelOperationStatus;
import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.corporateentity.CorporateEntity;
import fr.afnic.commons.beans.customer.CustomerId;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.OperationView;
import fr.afnic.commons.beans.operations.qualification.AutoMailReachability;
import fr.afnic.commons.beans.operations.qualification.EligibilityStatus;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.QualificationId;
import fr.afnic.commons.beans.operations.qualification.QualificationSnapshot;
import fr.afnic.commons.beans.operations.qualification.QualificationSource;
import fr.afnic.commons.beans.operations.qualification.ReachStatus;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.validatable.InvalidDataDescription;
import fr.afnic.commons.beans.validatable.InvalidDataException;
import fr.afnic.commons.services.IQualificationService;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.sql.converter.SqlConverterFacade;
import fr.afnic.commons.services.sql.converter.mapping.SqlColumnQualificationMapping;
import fr.afnic.commons.services.sql.converter.mapping.SqlViewMapping;
import fr.afnic.commons.services.sql.query.boa.QualificationSqlQueries;
import fr.afnic.commons.services.sql.utils.QueryStatementManagement;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.Delay;
import fr.afnic.utils.ObjectSerializer;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class SqlQualificationService implements IQualificationService {

    private static final SqlViewMapping VIEW_MAPPING = new SqlViewMapping();

    private final ISqlConnectionFactory sqlConnectionFactory;

    public SqlQualificationService(ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = Preconditions.checkNotNull(sqlConnectionFactory, "sqlConnectionFactory");
    }

    public SqlQualificationService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    @Override
    public Qualification getQualification(OperationId operationId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getOperationService().getOperation(operationId, Qualification.class, userId, tld);
    }

    @Override
    public OperationId create(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {

        Preconditions.checkNotNull(qualification, "qualification");
        Preconditions.checkNotNull(qualification.getSource(), "qualification.source");
        Preconditions.checkIsExistingIdentifier(qualification.getCreateUserId(), "qualification.createUserId", userId, tld);
        Preconditions.checkIsExistingNicHandle(qualification.getHolderNicHandle(), "qualification.holderNicHandle", userId, tld);

        if (qualification.getSource() == QualificationSource.Reporting
            || qualification.getSource() == QualificationSource.Plaint) {
            Preconditions.checkParameter(qualification.getInitiatorEmailAddress(), "qualification.initiatorEmail");
        }

        qualification.setEligibilityStatus(EligibilityStatus.NotIdentified);
        qualification.setReachStatus(ReachStatus.NotIdentified);

        String sqlQuery = QualificationSqlQueries.CREATE_QUALIFICATION_CONTENT;
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);

        try {
            // <li>id_source_type in integer: source de la qualification</li>
            preparedStatement.setInt(1, SqlConverterFacade.convert(qualification.getSource(), Integer.class, userId, tld));
            // <li>id_create_user in integer: identifiant de l'utilisateur créant la qualification</li>
            preparedStatement.setInt(2, qualification.getCreateUserId().getIntValue());
            // <li>nichandle in varchar2: nichandle du titulaire</li>
            preparedStatement.setString(3, qualification.getNicHandle());
            // <li>comments in varchar2: commentaire de création</li>
            preparedStatement.setString(4, qualification.getComments());
            preparedStatement.setString(5, qualification.getDetails());

            if (qualification.getDomainNameInitializedFrom() != null) {
                preparedStatement.setString(6, qualification.getDomainNameInitializedFrom());
            } else {
                preparedStatement.setString(6, null);
            }
            // <li>initiatorEmail in varchar2: email de l'emetteur du signalement ou de la plainte</li>
            if (qualification.getInitiatorEmailAddress() != null) {
                preparedStatement.setString(7, qualification.getInitiatorEmailAddress().getValue());
            } else {
                preparedStatement.setString(7, null);
            }

            OperationId id = queryStatementManagement.executeStatement(OperationId.class, preparedStatement, userId, tld);
            qualification.setId(id);

            this.createContactSnapshot(qualification, qualification.getCreateUserId(), userId, tld);
            return id;

        } catch (Exception e) {
            throw new ServiceException("create() failed  with qualification:\n" + ObjectSerializer.toXml(qualification), e);
        }

    }

    private void createContactSnapshot(Qualification qualification, UserId updateUserId, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(qualification, "qualification");
        Preconditions.checkIsExistingIdentifier(qualification.getId(), "qualification.id", userId, tld);

        ContactSnapshot snapshot = AppServiceFacade.getWhoisContactService().createSnapshot(qualification.getHolder(), userId, tld);

        String query = QualificationSqlQueries.UPDATE_QUALIFICATION_CONTACT_SNAPSHOT;
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(query);

        try {
            preparedStatement.setInt(1, qualification.getId().getIntValue());
            preparedStatement.setInt(2, updateUserId.getIntValue());
            preparedStatement.setString(3, snapshot.getId());
        } catch (Exception e) {
            throw new ServiceException("createContactSnapshot(" + qualification.getIdAsInt() + ", " + updateUserId.getIntValue() + ") failed");
        }
        queryStatementManagement.executeStatement(null, preparedStatement, userId, tld);
    }

    @Override
    public Qualification createAndGet(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {
        OperationId createdId = AppServiceFacade.getQualificationService().create(qualification, userId, tld);
        return AppServiceFacade.getQualificationService().getQualification(createdId, userId, tld);
    }

    private void updateOperation(String query, Qualification qualification, UserId updateUserId, Integer newStatusValue, Boolean newBooleanValue, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(query, "query");
        Preconditions.checkNotNull(qualification, "qualification");
        Preconditions.checkIsExistingIdentifier(qualification.getId(), "qualification.operationId", updateUserId, tld);
        Preconditions.checkIsExistingIdentifier(updateUserId, "updateUserId", updateUserId, tld);

        try {

            QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(query);
            int i = 1;
            preparedStatement.setInt(i, qualification.getId().getIntValue());
            if (updateUserId != null) {
                i += 1;
                preparedStatement.setInt(i, updateUserId.getIntValue());
            }
            if (newStatusValue != null) {
                i += 1;
                preparedStatement.setInt(i, newStatusValue);
            }
            if (newBooleanValue != null) {
                i += 1;
                preparedStatement.setBoolean(i, newBooleanValue);
            }
            queryStatementManagement.executeStatement(null, preparedStatement, updateUserId, tld);
        } catch (Exception e) {

            String message = "updateOperation() failed "
                             + " \n- qualification.operationId: " + qualification.getIdAsInt()
                             + " \n- updateUserId: " + updateUserId
                             + " \n- newStatusValue: " + newStatusValue
                             + " \n- newBooleanValue: " + newBooleanValue
                             + " \n- query: " + query;
            throw new ServiceException(message, e);

        }
    }

    @Override
    public void updateTopLevelStatus(Qualification qualification, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        this.updateOperation(QualificationSqlQueries.UPDATE_QUALIFICATION_TLO_STATUS,
                             qualification,
                             updateUserId,
                             SqlConverterFacade.convert(qualification.getTopLevelStatus(), Integer.class, updateUserId, tld),
                             null, tld);

        if (qualification.isFinished()) {
            qualification.sortDocuments();
        }
    }

    @Override
    public void updateReachStatus(Qualification qualification, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        this.updateOperation(QualificationSqlQueries.UPDATE_QUALIFICATION_REACH_STATUS,
                             qualification,
                             updateUserId,
                             SqlConverterFacade.convert(qualification.getReachStatus(), Integer.class, updateUserId, tld),
                             null, tld);
    }

    @Override
    public void updateEligibilityStatus(Qualification qualification, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        this.updateOperation(QualificationSqlQueries.UPDATE_QUALIFICATION_ELIGIBILITY_STATUS,
                             qualification,
                             updateUserId,
                             SqlConverterFacade.convert(qualification.getEligibilityStatus(), Integer.class, updateUserId, tld),
                             null, tld);
    }

    @Override
    public void updatePortfolioStatus(Qualification qualification, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(qualification, "qualification");
        Preconditions.checkNotNull(qualification.getPortfolioStatus(), "qualification.portfolioStatus");

        this.updateOperation(QualificationSqlQueries.UPDATE_QUALIFICATION_PORTFOLIO_STATUS,
                             qualification,
                             updateUserId,
                             SqlConverterFacade.convert(qualification.getPortfolioStatus(), Integer.class, updateUserId, tld),
                             null, tld);
    }

    @Override
    public void setIncoherent(Qualification qualification, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        this.updateOperation(QualificationSqlQueries.SET_QUALIFICATION_INCOHERENT,
                             qualification,
                             updateUserId,
                             null,
                             true, tld);
    }

    @Override
    public int getQualificationInProgressCount(UserId userId, TldServiceFacade tld) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(QualificationSqlQueries.GET_QUALIFICATION_IN_PROGRESS_COUNT);
        return queryStatementManagement.executeStatement(preparedStatement);
    }

    @Override
    public int createSnapshot(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(qualification, "qualification");
        Preconditions.checkNotNull(qualification.getId(), "qualification.id");

        String sqlQuery = QualificationSqlQueries.CREATE_QUALIFICATION_SNAPSHOT;
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);

        WhoisContact holder = qualification.getHolder();

        String name = holder.getName();
        String orgType = "";
        String siren = "";
        String siret = "";
        String trademark = "";
        String duns = "";
        String waldec = "";

        if (holder instanceof CorporateEntityWhoisContact) {
            CorporateEntityWhoisContact corp = (CorporateEntityWhoisContact) holder;
            if (corp.hasLegalStructure()) {
                CorporateEntity legalStructure = corp.getLegalStructure();

                orgType = legalStructure.getOrganizationTypeAsString();
                waldec = legalStructure.getWaldecAsString();
                siren = legalStructure.getSirenAsString();
                siret = legalStructure.getSiretAsString();
                trademark = legalStructure.getTradeMarkAsString();
            }
        }

        try {
            //            idQualification in integer,
            preparedStatement.setInt(1, qualification.getId().getIntValue());
            //            contactName in varchar2, 
            preparedStatement.setString(2, name);
            //            organizationType in varchar2, 
            preparedStatement.setString(3, orgType);
            //            sirenId in varchar2, 
            preparedStatement.setString(4, siren);
            //            siretId in varchar2, 
            preparedStatement.setString(5, siret);
            //            trademarkId in varchar2, 
            preparedStatement.setString(6, trademark);
            //            waldecId in varchar2, 
            preparedStatement.setString(7, waldec);
            //            dunsId in varchar2
            preparedStatement.setString(8, duns);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return queryStatementManagement.executeStatement(preparedStatement);
    }

    @Override
    public QualificationSnapshot getQualificationSnapshot(int snapshotId, UserId userId, TldServiceFacade tld) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);

        String sqlQuery = QualificationSqlQueries.GET_QUALIFICATION_SNAPSHOT;
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        try {
            preparedStatement.setInt(1, snapshotId);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return queryStatementManagement.executeStatement(QualificationSnapshot.class, preparedStatement, userId, tld);
    }

    @Override
    public List<Qualification> getQualifications(String nicHanlde, UserId userId, TldServiceFacade tld) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        String sqlQuery = QualificationSqlQueries.GET_QUALIFICATION_CONTENT_WITH_NICHANDLE;
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        try {
            preparedStatement.setString(1, nicHanlde);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return queryStatementManagement.executeStatementList(Qualification.class, preparedStatement, userId, tld);
    }

    @Override
    public List<Qualification> getQualificationsToUpdate(OperationView view, Delay delay, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(view, "view");
        Preconditions.checkNotNull(delay, "delay");
        Preconditions.checkNotNull(userId, "userId");

        String sqlQuery = QualificationSqlQueries.GET_SELECT_VIEW + VIEW_MAPPING.getOtherModelValue(view)
                          + QualificationSqlQueries.GET_SELECT_VIEW_TIME_FILTER;

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        try {
            Timestamp date = new Timestamp(System.currentTimeMillis() - delay.toMillis());
            preparedStatement.setTimestamp(1, date);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return queryStatementManagement.executeStatementList(Qualification.class, preparedStatement, userId, tld);
    }

    @Override
    public List<Qualification> getQualifications(OperationView view, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(view, "view");
        Preconditions.checkNotNull(userId, "userId");

        String sqlQuery = QualificationSqlQueries.GET_SELECT_VIEW + VIEW_MAPPING.getOtherModelValue(view);
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        return queryStatementManagement.executeStatementList(Qualification.class, preparedStatement, userId, tld);
    }

    @Override
    public int getQualificationsCount(OperationView view, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(view, "view");
        Preconditions.checkNotNull(userId, "userId");
        String sqlQuery = QualificationSqlQueries.GET_SELECT_VIEW_COUNT + VIEW_MAPPING.getOtherModelValue(view);
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        return queryStatementManagement.executeStatement(preparedStatement);
    }

    @Override
    public void populateQualification(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(qualification, "qualification");
        Preconditions.checkNotNull(qualification.getId(), "qualification.id");

        String sqlQuery = QualificationSqlQueries.GET_QUALIFICATION_CONTENT_WITH_OPERATION_ID;
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        try {
            preparedStatement.setInt(1, qualification.getIdAsInt());
        } catch (SQLException e) {
            throw new ServiceException("populateQualification(" + qualification.getId() + ") failed", e);
        }
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {

                int idQualification = resultSet.getInt(SqlColumnQualificationMapping.idQualification.toString());
                int idSourceType = resultSet.getInt(SqlColumnQualificationMapping.idSourceType.toString());
                //int idOperation = resultSet.getInt(SqlColumnQualificationMapping.idOperation.toString());
                int idTopLevelOperationStatus = resultSet.getInt(SqlColumnQualificationMapping.idTopLevelOperationStatus.toString());
                int idReachStatus = resultSet.getInt(SqlColumnQualificationMapping.idReachStatus.toString());
                int idEligibilityStatus = resultSet.getInt(SqlColumnQualificationMapping.idEligibilityStatus.toString());
                int idPortfolioStatus = resultSet.getInt(SqlColumnQualificationMapping.idPortfolioStatus.toString());
                String idContactSnapshot = resultSet.getString(SqlColumnQualificationMapping.idContactSnapshot.toString());
                int idClient = resultSet.getInt(SqlColumnQualificationMapping.idClient.toString());
                int objectVersion = resultSet.getInt(SqlColumnQualificationMapping.objectVersion.toString());

                String contactHolderHandler = resultSet.getString(SqlColumnQualificationMapping.contactHolderHandle.toString());
                String initiatorEmail = resultSet.getString(SqlColumnQualificationMapping.initiatorEmail.toString());
                String comments = resultSet.getString(SqlColumnQualificationMapping.comments.toString());

                boolean isIncoherent = "1".equals(resultSet.getString(SqlColumnQualificationMapping.isIncoherent.toString()));

                String domainNameFrom = resultSet.getString(SqlColumnQualificationMapping.domainNameFrom.toString());

                qualification.setQualificationId(new QualificationId(idQualification));
                qualification.setHolderNicHandle(contactHolderHandler);
                qualification.setInitiatorEmailAddress(new EmailAddress(initiatorEmail));
                qualification.setIncoherent(isIncoherent);
                qualification.setHolderSnapshotId(idContactSnapshot);
                qualification.setEligibilityStatus(SqlConverterFacade.convert(idEligibilityStatus, EligibilityStatus.class, userId, tld));
                qualification.setSource(SqlConverterFacade.convert(idSourceType, QualificationSource.class, userId, tld));
                qualification.setTopLevelStatus(SqlConverterFacade.convert(idTopLevelOperationStatus, TopLevelOperationStatus.class, userId, tld));
                qualification.setReachStatus(SqlConverterFacade.convert(idReachStatus, ReachStatus.class, userId, tld));
                qualification.setPortfolioStatus(SqlConverterFacade.convert(idPortfolioStatus, PortfolioStatus.class, userId, tld));
                qualification.setQualificationObjectVersion(objectVersion);
                qualification.setCustomerId(new CustomerId(idClient));
                qualification.setQualificationComment(comments);
                qualification.setDomainNameInitializedFrom(domainNameFrom);
            }
            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        } finally {
            queryStatementManagement.closeConnection();
        }

    }

    @Override
    public Qualification getQualificationInProgress(String nicHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(nicHandle, "nicHandle");
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(QualificationSqlQueries.GET_QUALIFICATION_IN_PROGRESS_WITH_NICHANDLE);
        try {
            preparedStatement.setString(1, nicHandle);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        int idQualification = queryStatementManagement.executeStatement(preparedStatement);
        if (idQualification > 0) {
            return AppServiceFacade.getQualificationService().getQualification(new OperationId(idQualification), userId, tld);
        } else {
            throw new NotFoundException("No qualification in progress found with nichandle[" + nicHandle + "].");
        }
    }

    @Override
    public Qualification getQualification(QualificationId qualificationId, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(qualificationId, "qualificationId");

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);

        String sqlQuery = QualificationSqlQueries.GET_QUALIFICATION_CONTENT_WITH_QUALIFICATION_ID;
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        try {
            preparedStatement.setInt(1, qualificationId.getIntValue());
        } catch (SQLException e) {
            throw new ServiceException("getQualification(" + qualificationId + ") failed", e);
        }
        Qualification qualification = queryStatementManagement.executeStatement(Qualification.class, preparedStatement, userId, tld);
        if (qualification == null) {
            throw new NotFoundException("qualificationId [" + qualificationId.getIntValue() + "] not found.");
        }
        return qualification;
    }

    @Override
    public boolean isExistingQualificationId(QualificationId qualificationId, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(qualificationId, "qualificationId");

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(QualificationSqlQueries.IS_EXISTING_QUALIFICATION);
        try {
            preparedStatement.setInt(1, qualificationId.getIntValue());
            return queryStatementManagement.executeStatement(preparedStatement) > 0;
        } catch (SQLException e) {
            throw new ServiceException("isExistingQualificationId(" + qualificationId + ") failed" + e.getMessage());
        }
    }

    @Override
    public AutoMailReachability checkKey(String key, UserId userId, TldServiceFacade tld) throws ServiceException {
        List<AutoMailReachability> retour = null;
        AutoMailReachability autoRetour = null;
        Preconditions.checkNotNull(key, "key");

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(QualificationSqlQueries.GET_MAIL_REACHABILITY);
        try {
            preparedStatement.setString(1, key);
            retour = queryStatementManagement.executeStatementList(AutoMailReachability.class, preparedStatement, userId, tld);
        } catch (SQLException e) {
            throw new ServiceException("setReachability(" + key + ") failed", e);
        }
        if (retour.size() == 0) {
            throw new NotFoundException("invalid key");
        }
        for (AutoMailReachability autoMail : retour) {
            if (autoMail.isValid()) {
                throw new InvalidDataException(new InvalidDataDescription("can't click on the validate email link twice"));
            }
        }
        if (retour.size() == 2) {
            for (AutoMailReachability autoMail : retour) {
                if (autoMail.isRelance()) {
                    autoRetour = autoMail;
                }
            }
        } else {
            autoRetour = retour.get(0);
        }
        return autoRetour;
    }

    @Override
    public void updateAutoMail(QualificationId id, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(id, "id");

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(QualificationSqlQueries.SET_REACHABILITY);
        try {
            preparedStatement.setInt(1, id.getIntValue());
            queryStatementManagement.executeStatement(null, preparedStatement, userId, tld);
        } catch (SQLException e) {
            throw new ServiceException("updateAutoMail(" + id + ") failed" + e.getMessage());
        }
    }

    @Override
    public List<QualificationId> getListQualificationWaitingCheckAutoReachability(UserId userId, TldServiceFacade tld) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        String sqlQuery = QualificationSqlQueries.GET_LIST_QUALIFICATION_WAITING_AUTO_REACHABILITY;
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        return queryStatementManagement.executeStatementList(QualificationId.class, preparedStatement, userId, tld);
    }

    @Override
    public List<QualificationId> getListQualificationWaitingCheckAutoReminderReachability(UserId userId, TldServiceFacade tld) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        String sqlQuery = QualificationSqlQueries.GET_LIST_QUALIFICATION_WAITING_AUTO_REMINDER_REACHABILITY;
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        try {
            preparedStatement.setInt(1, 7);
        } catch (SQLException e) {
            throw new ServiceException("getListQualificationWaitingCheckAutoReminderReachability() failed" + e.getMessage());
        }
        return queryStatementManagement.executeStatementList(QualificationId.class, preparedStatement, userId, tld);
    }

    @Override
    public List<QualificationId> getListQualificationAutoReminderTimeout(UserId userId, TldServiceFacade tld) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        String sqlQuery = QualificationSqlQueries.GET_LIST_QUALIFICATION_AUTO_REMINDER_TIMEOUT_REACHABILITY;
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        try {
            preparedStatement.setInt(1, 7);
        } catch (SQLException e) {
            throw new ServiceException("getListQualificationAutoReminderTimeout() failed" + e.getMessage());
        }
        return queryStatementManagement.executeStatementList(QualificationId.class, preparedStatement, userId, tld);
    }

    /**
     * 
     * @param length
     *            Nombre de caractères du mot à générer
     * 
     * @return un mot d'une longueur composé de caractère aléatoire
     */
    private String generateBaseKeyForConfirmReachability(final int length) {
        Random RANDOM = new Random(System.currentTimeMillis());
        final String[] letters = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6",
                                  "7", "8", "9" };

        final StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            final int rand = RANDOM.nextInt(letters.length);
            buffer.append(letters[rand]);
        }
        return buffer.toString();
    }

    @Override
    public void createQualificationCheckAutoReachability(Qualification qualification, boolean reminder, UserId userId, TldServiceFacade tld) throws ServiceException {
        String baseKey = "";
        if (reminder) {
            baseKey = this.getQualificationCheckAutoReachability(qualification, userId, tld);
        } else {
            baseKey = this.generateBaseKeyForConfirmReachability(30);
            baseKey += Calendar.getInstance().getTimeInMillis();
        }
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(QualificationSqlQueries.CREATE_AUTOMAIL_ENTRY);
        try {
            preparedStatement.setInt(1, qualification.getQualificationId().getIntValue());
            preparedStatement.setInt(2, reminder ? 1 : 0);
            preparedStatement.setString(3, qualification.getHolderEmailAddress().getValue());
            preparedStatement.setString(4, baseKey);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        queryStatementManagement.executeStatement(null, preparedStatement, userId, tld);
    }

    @Override
    public String getQualificationCheckAutoReachability(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(QualificationSqlQueries.GET_AUTOMAIL_ENTRY);
        try {
            preparedStatement.setInt(1, qualification.getQualificationId().getIntValue());
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return queryStatementManagement.executeStatementAsString(preparedStatement);
    }

    @Override
    public void updateComment(QualificationId qualificationId, String comment, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(qualificationId, "qualificationId");
        Preconditions.checkNotNull(comment, "comment");

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(QualificationSqlQueries.SET_COMMENT);
        try {
            preparedStatement.setString(1, comment);
            preparedStatement.setInt(2, qualificationId.getIntValue());
            queryStatementManagement.executeStatement(null, preparedStatement, userId, tld);
        } catch (SQLException e) {
            throw new ServiceException("updateComment(" + qualificationId + "," + comment + ") failed" + e.getMessage());
        }
    }

    @Override
    public void updateEnQualif(UserId userId, TldServiceFacade tld) throws ServiceException {
        String sqlQuery = QualificationSqlQueries.UPDATE_EN_QUALIF;
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);

        try {
            preparedStatement.setDate(1, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));

            queryStatementManagement.executeStatement(null, preparedStatement, userId, tld);

        } catch (Exception e) {
            throw new ServiceException("getNbQualifLaunchedToday() failed");
        }
    }

    @Override
    public int getNbQualifLaunchedToday(UserId userId, TldServiceFacade tld) throws ServiceException {
        String sqlQuery = QualificationSqlQueries.GET_NB_QUALIF_LAUNCHED;
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);

        try {
            preparedStatement.setDate(1, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));

            int nb = queryStatementManagement.executeStatementAsInt(preparedStatement);
            return nb;

        } catch (Exception e) {
            throw new ServiceException("getNbQualifLaunchedToday() failed");
        }
    }
}
