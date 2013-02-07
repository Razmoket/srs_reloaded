package fr.afnic.commons.services.sql.converter.mapping;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;

public class QueryUtils {

    public static <COLUMN_MAPPING extends Enum<COLUMN_MAPPING> & IColumnMapping> String buildQuery(String tableName, Class<COLUMN_MAPPING> columnMappingClass, COLUMN_MAPPING idColumn) {

        return buildQuery(tableName, columnMappingClass, idColumn.getColumnName() + " = ?");
    }

    public static <COLUMN_MAPPING extends Enum<COLUMN_MAPPING> & IColumnMapping> String buildQuery(String tableName, Class<COLUMN_MAPPING> columnMappingClass, String where) {

        List<String> columns = new ArrayList<String>();

        for (COLUMN_MAPPING column : columnMappingClass.getEnumConstants()) {
            columns.add(column.getColumnName());
        }

        return "select " + Joiner.on(", ").join(columns) + " from " + tableName + " where " + where;
    }

}
