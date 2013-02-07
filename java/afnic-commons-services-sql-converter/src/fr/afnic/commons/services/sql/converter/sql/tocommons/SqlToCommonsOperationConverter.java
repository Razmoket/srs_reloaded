package fr.afnic.commons.services.sql.converter.sql.tocommons;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import fr.afnic.commons.beans.operations.CompositeOperation;
import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.sql.converter.SqlConverterFacade;
import fr.afnic.commons.services.sql.converter.mapping.OperationFactory;
import fr.afnic.commons.services.sql.converter.mapping.SqlColumnOperationMapping;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class SqlToCommonsOperationConverter extends AbstractConverter<ResultSet, Operation> {

    public SqlToCommonsOperationConverter() {
        super(ResultSet.class, Operation.class);
    }

    @Override
    public Operation convert(ResultSet toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        Operation operation = null;
        try {
            int idOperationType = toConvert.getInt(SqlColumnOperationMapping.idOperationType.toString());
            operation = OperationFactory.create(SqlConverterFacade.convert(idOperationType, OperationType.class, userId, tld), userId, tld);

            int idOperation = toConvert.getInt(SqlColumnOperationMapping.idOperation.toString());

            int idCreateUser = toConvert.getInt(SqlColumnOperationMapping.idCreateUser.toString());
            int idUpdateUser = toConvert.getInt(SqlColumnOperationMapping.idUpdateUser.toString());
            int idOperationStatus = toConvert.getInt(SqlColumnOperationMapping.idOperationStatus.toString());
            int idParent = toConvert.getInt(SqlColumnOperationMapping.idParent.toString());
            int idLockingUser = toConvert.getInt(SqlColumnOperationMapping.idLockingUser.toString());

            Date createDate = toConvert.getTimestamp(SqlColumnOperationMapping.createDate.toString());
            Date updateDate = toConvert.getTimestamp(SqlColumnOperationMapping.updateDate.toString());
            Date lockingDate = toConvert.getTimestamp(SqlColumnOperationMapping.lockingDate.toString());

            // TODO champs a ajouter dans l'objet operation
            //int idNextOperation = toConvert.getInt(SqlColumnOperationMapping.idNextOperation.toString());
            //int idPreviousOperation = toConvert.getInt(SqlColumnOperationMapping.idPreviousOperation.toString());
            //fin des champsp a etudier

            String comments = toConvert.getString(SqlColumnOperationMapping.comments.toString());
            String operationDetails = toConvert.getString(SqlColumnOperationMapping.operationDetails.toString());
            String isBlocking = toConvert.getString(SqlColumnOperationMapping.isBlocking.toString());

            int objectVersion = toConvert.getInt(SqlColumnOperationMapping.objectVersion.toString());

            operation.setStatus(SqlConverterFacade.convert(idOperationStatus, OperationStatus.class, userId, tld));

            operation.setBlocking("1".equals(isBlocking));

            operation.setComments(comments);
            operation.setCreateDate(createDate);
            operation.setLockingDate(lockingDate);
            if (idLockingUser > 0) {
                operation.setLockingUserId(new UserId(idLockingUser));
            } else {
                operation.setLockingUserId(null);
            }
            if (idCreateUser > 0) {
                operation.setCreateUserId(new UserId(idCreateUser));
            }
            operation.setDetails(operationDetails);
            if (idOperation > 0) {
                operation.setId(new OperationId(idOperation));
            }
            operation.setObjectVersion(Long.valueOf(objectVersion));
            operation.setUpdateDate(updateDate);
            if (idUpdateUser > 0) {
                operation.setUpdateUserId(new UserId(idUpdateUser));
            }
            if (idParent > 0) {
                operation.setParentId(new OperationId(idParent));
            }
            if (operation instanceof CompositeOperation) {
                List<Operation> subOperations = AppServiceFacade.getOperationService().getSubOperations(new OperationId(idOperation), userId, tld);
                ((CompositeOperation) operation).setSubOperation(subOperations);
                for (Operation subOp : subOperations) {
                    subOp.setParent(operation);
                }
            }

            operation.populate();

        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return operation;
    }

}
