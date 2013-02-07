/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.dbutils;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class CustomBeanListHandler<E> extends BeanListHandler<E> {

    private static final BasicRowProcessor ROW_PROCESSOR = new BasicRowProcessor(new CustomBeanProcessor());

    public static final <E> CustomBeanListHandler<E> create(final Class<E> type) {
        return new CustomBeanListHandler<E>(type);
    }

    public CustomBeanListHandler(final Class<E> type) {
        super(type, CustomBeanListHandler.ROW_PROCESSOR);
    }

}
