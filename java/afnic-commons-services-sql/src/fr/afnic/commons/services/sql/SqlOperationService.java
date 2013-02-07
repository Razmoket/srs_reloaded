package fr.afnic.commons.services.sql;

import java.io.IOException;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.List;

import oracle.sql.CLOB;

import org.apache.commons.lang.StringUtils;

import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.mail.EmailFormat;
import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.commons.SendEmail;
import fr.afnic.commons.beans.operations.qualification.operation.StatusUpdate;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.constraints.StringConstraint;
import fr.afnic.commons.services.IOperationService;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.sql.converter.SqlConverterFacade;
import fr.afnic.commons.services.sql.converter.mapping.SqlColumnEmailToSendMapping;
import fr.afnic.commons.services.sql.converter.mapping.SqlColumnStatusUpdateMapping;
import fr.afnic.commons.services.sql.query.boa.OperationSqlQueries;
import fr.afnic.commons.services.sql.utils.QueryStatementManagement;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class SqlOperationService implements IOperationService {

    private final ISqlConnectionFactory sqlConnectionFactory;

    public SqlOperationService(ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = Preconditions.checkNotNull(sqlConnectionFactory, "sqlConnectionFactory");
    }

    public SqlOperationService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    @Override
    public Operation getOperation(OperationId operationId, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(operationId, "operationId");

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);

        String sqlQuery = OperationSqlQueries.GET_OPERATION_CONTENT;
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        try {
            preparedStatement.setInt(1, operationId.getIntValue());
        } catch (SQLException e) {
            queryStatementManagement.closeConnection();
            throw new ServiceException("getOperation(" + operationId + ") failed", e);
        }
        Operation ope = queryStatementManagement.executeStatement(Operation.class, preparedStatement, userId, tld);
        if (ope == null) {
            queryStatementManagement.closeConnection();
            throw new NotFoundException("operationId [" + operationId.getIntValue() + "] not found.");
        }
        if (ope.getType() == null) {
            throw new ServiceException("Operation " + ope.getClass().getSimpleName() + " [" + operationId.getIntValue() + "].type hasn't been defined.");
        }

        ope.populate();

        return ope;
    }

    @Override
    public <STATUS extends Enum<?>> void populateStatusUpdate(StatusUpdate<STATUS> update, UserId userId, TldServiceFacade tld) throws ServiceException {
        String sqlQuery = OperationSqlQueries.GET_STATUS_UPDATE_CONTENT;
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);

        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        try {
            preparedStatement.setInt(1, update.getIdAsInt());
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int oldValue = resultSet.getInt(SqlColumnStatusUpdateMapping.oldValue.toString());
                int newValue = resultSet.getInt(SqlColumnStatusUpdateMapping.newValue.toString());
                update.setOldValue(SqlConverterFacade.convert(oldValue, update.getStatusClass(), userId, tld));
                update.setNewValue(SqlConverterFacade.convert(newValue, update.getStatusClass(), userId, tld));

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
    public void populateSendEmail(SendEmail sendEmail, UserId userId, TldServiceFacade tld) throws ServiceException {

        String sqlQuery = OperationSqlQueries.GET_EMAIL_TO_SEND_CONTENT;
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);

        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        try {
            preparedStatement.setInt(1, sendEmail.getIdAsInt());
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String subject = resultSet.getString(SqlColumnEmailToSendMapping.subject.toString());
                String body = resultSet.getString(SqlColumnEmailToSendMapping.body.toString());
                String cc = resultSet.getString(SqlColumnEmailToSendMapping.cc.toString());
                String cci = resultSet.getString(SqlColumnEmailToSendMapping.cci.toString());
                String to = resultSet.getString(SqlColumnEmailToSendMapping.to.toString());
                String from = resultSet.getString(SqlColumnEmailToSendMapping.from.toString());
                String emailFormat = resultSet.getString(SqlColumnEmailToSendMapping.emailFormat.toString());

                Email email = new Email();
                email.setSubject(subject);
                email.setContent(body);
                email.setFromEmailAddress(from);

                if (cc != null) {
                    email.setCcEmailAddresses(cc.split(";"));
                }

                if (cci != null) {
                    email.setBccEmailAddresses(cci.split(";"));
                }

                if (to != null) {
                    email.setToEmailAddresses(to.split(";"));
                }

                if ((emailFormat != null) && (!"".equals(emailFormat))) {
                    email.setFormat(EmailFormat.valueOf(emailFormat));
                }

                sendEmail.setEmailToSend(email);
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
    public <OPERATION extends Operation> OPERATION getOperation(OperationId operationId, Class<OPERATION> operationClass, UserId userId, TldServiceFacade tld) throws ServiceException {
        Operation operation = this.getOperation(operationId, userId, tld);
        try {
            return operationClass.cast(operation);
        } catch (ClassCastException e) {
            throw new ServiceException("OperationId[" + operationId.getIntValue() + "] is not " + operationClass.getSimpleName() + " but " + operation.getClass().getSimpleName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <OPERATION extends Operation> OPERATION createAndGet(OPERATION operation, UserId userId, TldServiceFacade tld) throws ServiceException {
        OperationId create = this.create(operation, userId, tld);
        return (OPERATION) this.getOperation(create, userId, tld);
    }

    @Override
    public List<Operation> getSubOperations(OperationId operationId, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (operationId == null) {
            return Collections.emptyList();
        }

        String sqlQuery = OperationSqlQueries.GET_SUB_OPERATION_CONTENT;
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);

        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        try {
            preparedStatement.setInt(1, operationId.getIntValue());
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return queryStatementManagement.executeStatementList(Operation.class, preparedStatement, userId, tld);
    }

    @Override
    public OperationId create(Operation operation, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(operation, "operation");
        Preconditions.checkIsExistingIdentifier(operation.getCreateUserId(), "operation.createUserId", userId, tld);
        Preconditions.checkNotLongerThan(operation.getComments(), StringConstraint.MAX_LENGTH, "operation.comments");
        Preconditions.checkNotLongerThan(operation.getDetails(), StringConstraint.MAX_LENGTH, "operation.details");
        Preconditions.checkNotNull(operation.getType(), "operation.type");

        //A refactorer si le nombre de cas s'accroit trop
        if (operation.getType().isStatusUpdate()) {
            return this.createStatusUpdate((StatusUpdate<?>) operation, userId, tld);
        }
        if (operation.getType().isSendEmail()) {
            return this.createSendEmail((SendEmail) operation, userId, tld);
        } else {
            return this.createDefault(operation, userId, tld);
        }
    }

    private OperationId createDefault(Operation operation, UserId userId, TldServiceFacade tld) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        String sqlQuery = OperationSqlQueries.CREATE_OPERATION;
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        try {
            // <li>id_createUser in INTEGER</li>
            preparedStatement.setInt(1, operation.getCreateUserId().getIntValue());
            // <li>id_nextOperation in integer</li>
            //preparedStatement.setInt(2, 1);
            preparedStatement.setNull(2, Types.INTEGER);
            // <li>id_previousOperation in integer</li>
            //preparedStatement.setInt(3, 1);
            preparedStatement.setNull(3, Types.INTEGER);
            // <li>id_parentField in integer</li>
            if (operation.hasParent()) {
                preparedStatement.setInt(4, operation.getParent().getIdAsInt());
            } else {
                preparedStatement.setNull(4, Types.INTEGER);
            }
            // <li>id_operationType in integer</li>

            preparedStatement.setInt(5, SqlConverterFacade.convert(operation.getType(), Integer.class, userId, tld));
            // <li>commentsField in varchar2</li>
            preparedStatement.setString(6, operation.getComments());
            // <li>operationDetails in varchar2</li>
            preparedStatement.setString(7, operation.getDetails());
            // <li>isBlocking in boolean</li>
            preparedStatement.setString(8, QueryStatementManagement.getCharValue(operation.isBlocking()));

            return queryStatementManagement.executeStatement(OperationId.class, preparedStatement, userId, tld);
        } catch (Exception e) {
            queryStatementManagement.closeConnection();
            throw new ServiceException("createDefault() failed with type " + operation.getType() + " and query " + sqlQuery, e);
        }

    }

    private OperationId createStatusUpdate(StatusUpdate<?> operation, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(operation.getOldValue(), "operation.oldValue");
        Preconditions.checkNotNull(operation.getNewValue(), "operation.newValue");

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        String sqlQuery = OperationSqlQueries.CREATE_STATUS_UPDATE;
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);

        try {
            //            id_operation_type in integer,
            preparedStatement.setInt(1, SqlConverterFacade.convert(operation.getType(), Integer.class, userId, tld).intValue());
            //            is_blocking in char,
            preparedStatement.setString(2, QueryStatementManagement.getCharValue(operation.isBlocking()));
            //            id_user in integer,
            preparedStatement.setInt(3, operation.getCreateUserId().getIntValue());
            //            idPrevious in integer,
            if (operation.hasPrevious()) {
                preparedStatement.setInt(4, operation.getPreviousOperationId().getIntValue());
            } else {
                preparedStatement.setNull(4, Types.INTEGER);
            }
            //            idParent in integer,
            if (operation.hasParent()) {
                preparedStatement.setInt(5, operation.getParent().getId().getIntValue());
            } else {
                preparedStatement.setNull(5, Types.INTEGER);
            }
            //            comments in varchar2,
            preparedStatement.setString(6, operation.getComments());
            //            oldValue in integer,
            preparedStatement.setInt(7, SqlConverterFacade.convert(operation.getOldValue(), Integer.class, userId, tld).intValue());
            //            newValue in integer
            preparedStatement.setInt(8, SqlConverterFacade.convert(operation.getNewValue(), Integer.class, userId, tld).intValue());
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return queryStatementManagement.executeStatement(OperationId.class, preparedStatement, userId, tld);
    }

    private OperationId createSendEmail(SendEmail sendEmail, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(sendEmail, "sendEmail");
        Preconditions.checkNotNull(sendEmail.getEmailToSend(), "sendEmail.emailToSend");

        Email emailToSend = sendEmail.getEmailToSend();

        String sqlQuery = OperationSqlQueries.CREATE_EMAIL_TO_SEND;
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);

        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        try {

            //            id_operation_type in integer,
            preparedStatement.setInt(1, SqlConverterFacade.convert(sendEmail.getType(), Integer.class, userId, tld).intValue());
            //            is_blocking in char,
            preparedStatement.setString(2, QueryStatementManagement.getCharValue(sendEmail.isBlocking()));
            //            id_user in integer,
            preparedStatement.setInt(3, sendEmail.getCreateUserId().getIntValue());
            //          idPrevious in integer,
            if (sendEmail.hasPrevious()) {
                preparedStatement.setInt(4, sendEmail.getPreviousOperationId().getIntValue());
            } else {
                preparedStatement.setNull(4, Types.INTEGER);
            }
            //            idParent in integer,
            if (sendEmail.hasParent()) {
                preparedStatement.setInt(5, sendEmail.getParent().getId().getIntValue());
            } else {
                preparedStatement.setNull(5, Types.INTEGER);
            }
            //      comments in varchar2,
            preparedStatement.setString(6, sendEmail.getComments());
            //            fromField in varchar2, 
            preparedStatement.setString(7, emailToSend.getFromEmailAddress().getValue());
            //            toField in varchar2, 
            preparedStatement.setString(8, emailToSend.getToEmailAddressesAsString());
            //            ccField in varchar2, 
            preparedStatement.setString(9, emailToSend.getCcEmailAddressesAsString());
            //            cciField in varchar2, 
            preparedStatement.setString(10, emailToSend.getBccEmailAddressesAsString());
            //            subjectField in varchar2,
            preparedStatement.setString(11, emailToSend.getSubject());
            //            bodyField in clob
            CLOB tempclob = null;
            tempclob = CLOB.createTemporary(preparedStatement.getConnection(), true, CLOB.DURATION_SESSION);
            tempclob.open(CLOB.MODE_READWRITE); //mode d'ouverture
            Writer tempClobWriter = tempclob.getCharacterOutputStream();
            tempClobWriter.write(emailToSend.getContent().toCharArray());
            tempClobWriter.flush();
            tempClobWriter.close();
            tempclob.close();
            preparedStatement.setClob(12, tempclob);
            //preparedStatement.setString(12, new Clob(emailToSend.getContent());
            preparedStatement.setString(13, emailToSend.getFormat().toString());
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return queryStatementManagement.executeStatement(OperationId.class, preparedStatement, userId, tld);
    }

    @Override
    public void attach(Operation operation, UserId updateUserId, Document document, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(operation, "operation");
        Preconditions.checkIsExistingIdentifier(operation.getId(), "operation.id", updateUserId, tld);
        Preconditions.checkIsExistingIdentifier(updateUserId, "updateUserId", updateUserId, tld);
        Preconditions.checkNotNull(document, "document");
        Preconditions.checkNotNull(document.getHandle(), "document.handle");
        String sqlQuery = OperationSqlQueries.ATTACH_OPERATION_DOCUMENT;

        for (Document alreadyAttacheddocument : operation.getDocuments()) {
            if (StringUtils.equals(alreadyAttacheddocument.getHandle(), document.getHandle())) {
                throw new ServiceException("Document " + document.getHandle() + " is already attached to " + operation.getDisplayName());
            }
        }

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);

        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        try {
            //            id_operation in integer,
            preparedStatement.setInt(1, operation.getId().getIntValue());
            //            id_user 
            preparedStatement.setInt(2, updateUserId.getIntValue());
            //            docushare handle, 
            preparedStatement.setString(3, document.getHandle());
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        queryStatementManagement.executeStatement(OperationId.class, preparedStatement, updateUserId, tld);
    }

    private void updateOperation(String query, Operation operation, UserId updateUserId, Integer newStatusValue, String newVarcharValue, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(query, "query");
        Preconditions.checkNotNull(operation, "operation");
        Preconditions.checkIsExistingIdentifier(operation.getId(), "operation.id", updateUserId, tld);

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);

        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(query);
        try {
            int i = 1;
            preparedStatement.setInt(i, operation.getId().getIntValue());
            if (updateUserId != null) {
                i += 1;
                preparedStatement.setInt(i, updateUserId.getIntValue());
            }
            if (newStatusValue != null) {
                i += 1;
                preparedStatement.setInt(i, newStatusValue);
            }
            if (newVarcharValue != null) {
                i += 1;
                if (newVarcharValue.length() > 4000) {
                    preparedStatement.setString(i, newVarcharValue.substring(0, 4000));
                } else {
                    preparedStatement.setString(i, newVarcharValue);
                }
            }
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        queryStatementManagement.executeStatement(null, preparedStatement, updateUserId, tld);
    }

    @Override
    public void updateComments(Operation operation, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkIsExistingIdentifier(updateUserId, "updateUserId", updateUserId, tld);
        Preconditions.checkNotNull(operation, "operation");
        Preconditions.checkIsExistingIdentifier(operation.getId(), "operation.id", updateUserId, tld);

        this.updateOperation(OperationSqlQueries.UPDATE_OPERATION_COMMENTS,
                             operation,
                             updateUserId,
                             null,
                             operation.getComments(), tld);
    }

    @Override
    public void updateDetails(Operation operation, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkIsExistingIdentifier(updateUserId, "updateUserId", updateUserId, tld);
        this.updateOperation(OperationSqlQueries.UPDATE_OPERATION_DETAILS,
                             operation,
                             updateUserId,
                             null,
                             operation.getDetails(), tld);
    }

    @Override
    public void updateStatus(Operation operation, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkIsExistingIdentifier(updateUserId, "updateUserId", updateUserId, tld);
        Preconditions.checkNotNull(operation, "operation");
        Preconditions.checkIsExistingIdentifier(operation.getId(), "operation.id", updateUserId, tld);

        this.updateOperation(OperationSqlQueries.UPDATE_OPERATION_STATUS,
                             operation,
                             updateUserId,
                             SqlConverterFacade.convert(operation.getStatus(), Integer.class, updateUserId, tld),
                             null, tld);
    }

    @Override
    public void lockOperation(Operation operation, UserId lockingUserId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkIsExistingIdentifier(lockingUserId, "lockingUserId", lockingUserId, tld);
        Preconditions.checkNotNull(operation, "operation");
        Preconditions.checkIsExistingIdentifier(operation.getId(), "operation.id", lockingUserId, tld);
        this.updateOperation(OperationSqlQueries.LOCK_OPERATION,
                             operation,
                             lockingUserId,
                             null,
                             null, tld);
    }

    @Override
    public void unlockOperation(Operation operation, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(operation, "operation");
        Preconditions.checkIsExistingIdentifier(operation.getId(), "operation.id", userId, tld);
        this.updateOperation(OperationSqlQueries.UNLOCK_OPERATION,
                             operation,
                             userId,
                             null,
                             null, tld);
    }

    @Override
    public void updateUser(Operation operation, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkIsExistingIdentifier(updateUserId, "updateUserId", updateUserId, tld);
        Preconditions.checkNotNull(operation, "operation");
        Preconditions.checkIsExistingIdentifier(operation.getId(), "operation.id", updateUserId, tld);
        this.updateOperation(OperationSqlQueries.UPDATE_OPERATION_USER,
                             operation,
                             updateUserId,
                             null,
                             null, tld);
    }

    @Override
    public boolean isExistingOperationId(OperationId operationId, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(operationId, "operationId");

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(OperationSqlQueries.IS_EXISTING_OPERATION);
        try {
            preparedStatement.setInt(1, operationId.getIntValue());
            return queryStatementManagement.executeStatement(preparedStatement) > 0;
        } catch (SQLException e) {
            throw new ServiceException("isExistingOperationId(" + operationId + ") failed" + e.getMessage());
        }
    }

    @Override
    public List<Document> getDocuments(OperationId operationId, UserId userId, TldServiceFacade tld) throws ServiceException {
        String sqlQuery = OperationSqlQueries.GET_OPERATION_DOCUMENTS;
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        try {
            //id_operation in integer,
            preparedStatement.setInt(1, operationId.getIntValue());
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return queryStatementManagement.executeStatementList(Document.class, preparedStatement, userId, tld);
    }

    @Override
    public OperationId getOperation(String documentHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        String sqlQuery = OperationSqlQueries.GET_OPERATION_ATTACHED_TO_DOCUMENT;
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        try {
            //            id_operation in integer,
            preparedStatement.setString(1, documentHandle);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        int nb = queryStatementManagement.executeStatement(preparedStatement);
        return new OperationId(nb);
    }

}
