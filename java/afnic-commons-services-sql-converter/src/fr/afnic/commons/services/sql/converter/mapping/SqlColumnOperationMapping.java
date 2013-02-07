package fr.afnic.commons.services.sql.converter.mapping;

public enum SqlColumnOperationMapping {

    idOperation("ID_OPERATION"),
    idOperationStatus("ID_OPERATION_STATUS"),
    idCreateUser("ID_CREATE_USER"),
    idUpdateUser("ID_UPDATE_USER"),
    idLockingUser("ID_LOCKING_USER"),
    idNextOperation("ID_NEXT_OPERATION"),
    idPreviousOperation("ID_PREVIOUS_OPERATION"),
    idParent("ID_PARENT"),
    idOperationType("ID_OPERATION_TYPE"),
    createDate("CREATE_DATE"),
    updateDate("UPDATE_DATE"),
    lockingDate("LOCKING_DATE"),
    comments("COMMENTS"),
    operationDetails("OPERATION_DETAILS"),
    isBlocking("IS_BLOCKING"),
    objectVersion("OBJECT_VERSION"), ;

    private final String sqlColumnName;

    private SqlColumnOperationMapping(String desc) {
        this.sqlColumnName = desc;
    }

    @Override
    public String toString() {
        return this.sqlColumnName;
    }

}
