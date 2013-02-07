/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.dbutils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.dbutils.BeanProcessor;

public class CustomBeanProcessor extends BeanProcessor {

    @Override
    protected Object processColumn(final ResultSet rs, final int index, final Class<?> propType) throws SQLException {

        if (!propType.isPrimitive() && rs.getObject(index) == null) {
            return null;
        }

        if (propType.equals(String.class)) {
            return rs.getString(index);

        } else if (propType.equals(Integer.TYPE) || propType.equals(Integer.class)) {
            return (rs.getInt(index));

        } else if (propType.equals(Boolean.TYPE) || propType.equals(Boolean.class)) {
            return (rs.getBoolean(index));

        } else if (propType.equals(Long.TYPE) || propType.equals(Long.class)) {
            return (rs.getLong(index));

        } else if (propType.equals(Double.TYPE) || propType.equals(Double.class)) {
            return (rs.getDouble(index));

        } else if (propType.equals(Float.TYPE) || propType.equals(Float.class)) {
            return (rs.getFloat(index));

        } else if (propType.equals(Short.TYPE) || propType.equals(Short.class)) {
            return (rs.getShort(index));

        } else if (propType.equals(Byte.TYPE) || propType.equals(Byte.class)) {
            return (rs.getByte(index));
        } else if (propType.equals(Timestamp.class)) {
            return rs.getTimestamp(index);
        } else if (propType.equals(Date.class)) {
            return new Date(rs.getTimestamp(index).getTime());
        } else {
            return rs.getObject(index);
        }

    }

}
