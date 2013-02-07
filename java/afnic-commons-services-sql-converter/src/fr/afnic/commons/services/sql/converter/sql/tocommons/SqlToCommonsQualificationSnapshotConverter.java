package fr.afnic.commons.services.sql.converter.sql.tocommons;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.afnic.commons.beans.boarequest.TopLevelOperationStatus;
import fr.afnic.commons.beans.operations.qualification.EligibilityStatus;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.beans.operations.qualification.QualificationSnapshot;
import fr.afnic.commons.beans.operations.qualification.ReachStatus;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.sql.converter.SqlConverterFacade;
import fr.afnic.commons.services.sql.converter.mapping.SqlColumnQualificationSnapshotMapping;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class SqlToCommonsQualificationSnapshotConverter extends AbstractConverter<ResultSet, QualificationSnapshot> {

    public SqlToCommonsQualificationSnapshotConverter() {
        super(ResultSet.class, QualificationSnapshot.class);
    }

    @Override
    public QualificationSnapshot convert(ResultSet toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        QualificationSnapshot retour = new QualificationSnapshot();

        this.insertIntoMapIntValue(retour, toConvert, SqlColumnQualificationSnapshotMapping.idQualificationSnapshot);

        this.insertIntoMapEnumValue(retour, toConvert, SqlColumnQualificationSnapshotMapping.idEligibilityStatus, EligibilityStatus.class, userId, tld);
        this.insertIntoMapEnumValue(retour, toConvert, SqlColumnQualificationSnapshotMapping.idPortfolioStatus, PortfolioStatus.class, userId, tld);
        this.insertIntoMapEnumValue(retour, toConvert, SqlColumnQualificationSnapshotMapping.idQualificationStatus, TopLevelOperationStatus.class, userId, tld);
        this.insertIntoMapEnumValue(retour, toConvert, SqlColumnQualificationSnapshotMapping.idReachStatus, ReachStatus.class, userId, tld);

        this.insertIntoMapStringValue(retour, toConvert, SqlColumnQualificationSnapshotMapping.name);
        this.insertIntoMapStringValue(retour, toConvert, SqlColumnQualificationSnapshotMapping.orgType);
        this.insertIntoMapStringValue(retour, toConvert, SqlColumnQualificationSnapshotMapping.siren);
        this.insertIntoMapStringValue(retour, toConvert, SqlColumnQualificationSnapshotMapping.siret);
        this.insertIntoMapStringValue(retour, toConvert, SqlColumnQualificationSnapshotMapping.trademark);
        this.insertIntoMapStringValue(retour, toConvert, SqlColumnQualificationSnapshotMapping.waldec);
        return retour;
    }

    private void insertIntoMapIntValue(QualificationSnapshot retour, ResultSet toConvert, SqlColumnQualificationSnapshotMapping column) throws ServiceException {
        try {
            retour.put(column.toString(), toConvert.getInt(column.toString()));
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private void insertIntoMapStringValue(QualificationSnapshot retour, ResultSet toConvert, SqlColumnQualificationSnapshotMapping column) throws ServiceException {
        try {
            retour.put(column.toString(), toConvert.getString(column.toString()));
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private void insertIntoMapEnumValue(QualificationSnapshot retour, ResultSet toConvert, SqlColumnQualificationSnapshotMapping column, Class<?> enumClass, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                                 throws ServiceException {
        try {
            int value = toConvert.getInt(column.toString());
            if (value == 0) {
                value = 1; //valeur par defaut...
            }
            retour.put(column.toString(), SqlConverterFacade.convert(value, enumClass, userId, tld));

        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
