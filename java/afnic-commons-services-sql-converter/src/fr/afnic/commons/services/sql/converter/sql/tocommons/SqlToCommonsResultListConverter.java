package fr.afnic.commons.services.sql.converter.sql.tocommons;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

import fr.afnic.commons.beans.list.Column;
import fr.afnic.commons.beans.list.IView;
import fr.afnic.commons.beans.list.Line;
import fr.afnic.commons.beans.list.QualificationStatResultList;
import fr.afnic.commons.beans.list.ResultList;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.sql.converter.mapping.SqlViewMapping;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.DateUtils;

@SuppressWarnings("rawtypes")
public class SqlToCommonsResultListConverter extends AbstractConverter<ResultSet, ResultList> {

    private static final SqlViewMapping VIEW_MAPPING = new SqlViewMapping();

    public SqlToCommonsResultListConverter() {
        super(ResultSet.class, ResultList.class);
    }

    @Override
    public ResultList<?> convert(ResultSet toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        ResultList<?> result = null;
        try {
            do {
                if (result == null) {
                    int pos = 0;
                    try {
                        pos = toConvert.findColumn("view_name");
                    } catch (Exception e) {
                        pos = -1;
                    }
                    if (pos > 0) {
                        result = this.createResultListWithViewNameColumn(toConvert, userId, tld);
                    } else {
                        result = this.createResultListWithoutViewNameColumn(toConvert);
                    }
                }
                this.manageLineContent(toConvert, result);
            } while (toConvert.next());
        } catch (Exception e) {
            throw new ServiceException("convert() failed", e);
        }
        return result;
    }

    private ResultList<?> createResultListWithViewNameColumn(ResultSet toConvert, UserId userId, TldServiceFacade tld) throws SQLException {
        IView view = VIEW_MAPPING.getCommonsValue(toConvert.getString(1), userId, tld);
        ResultList<?> result = null;

        if (view != null) {
            result = view.createResultList();
            this.populateResultListColumn(result, toConvert, view.getIdentifier(), null, 2);
        }
        return result;
    }

    /**
     * Pour les resultlists de type QualificationStatResultList
     */
    public QualificationStatResultList createResultListWithoutViewNameColumn(ResultSet toConvert) throws ServiceException, SQLException {
        QualificationStatResultList result = null;
        boolean isResultListInitialized = false;
        if (!isResultListInitialized) {
            result = new QualificationStatResultList();
            this.populateResultListColumn(result, toConvert, "CALCULATION_LIMIT", "CALCULATION_LIMIT_BE", 1);
        }
        return result;
    }

    protected void populateResultListColumn(ResultList resultList, ResultSet toConvert, String key, String key2, int beginIndex) throws SQLException {
        ResultSetMetaData rsMetaData = toConvert.getMetaData();
        int numberOfColumns = rsMetaData.getColumnCount();
        for (int i = beginIndex; i <= numberOfColumns; i++) { //la premiere colonne correspond au nom de la vue
            String columnLabel = rsMetaData.getColumnLabel(i);

            boolean isAnIdentifierColumn = columnLabel.equalsIgnoreCase(key);
            if ((!isAnIdentifierColumn) && (key2 != null)) {
                isAnIdentifierColumn = columnLabel.equalsIgnoreCase(key2);
            }
            Column column = new Column(columnLabel, columnLabel, isAnIdentifierColumn);
            resultList.addColumn(column);
        }
    }

    protected void manageLineContent(ResultSet resultSet, ResultList<?> resultList) throws SQLException {
        Line line = new Line();
        for (Column column : resultList.getColumns()) {
            line.addValue(column, this.getValue(resultSet, column));
        }
        resultList.addLine(line);
    }

    protected String getValue(ResultSet resultSet, Column column) throws SQLException {
        Object obj = resultSet.getObject(column.getReference());

        if (obj instanceof java.sql.Date || obj instanceof java.sql.Timestamp) {
            java.sql.Timestamp sqlTimestamp = resultSet.getTimestamp(column.getReference());
            return DateUtils.toSecondFormat(new Date(sqlTimestamp.getTime()));
        } else {
            return resultSet.getString(column.getReference());
        }
    }

}
