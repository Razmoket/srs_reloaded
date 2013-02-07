package fr.afnic.commons.services.sql.converter.mapping;

public enum SqlColumnUserMapping {

    idUser("ID_USER"),
    idProfile("ID_PROFILE"),
    idBackupUser("ID_BACKUP_USER"),
    idUserStatus("ID_USER_STATUS"),
    iDNicPers("ID_NICPERS"),
    firstname("FIRST_NAME"),
    lastName("LAST_NAME"),
    email("EMAIL"),
    password("PASSWORD"),
    objectVersion("OBJECT_VERSION"),
    nicpersLogin("NICPERS_LOGIN");

    private final String sqlColumnName;

    private SqlColumnUserMapping(String desc) {
        this.sqlColumnName = desc;
    }

    @Override
    public String toString() {
        return this.sqlColumnName;
    }
}
