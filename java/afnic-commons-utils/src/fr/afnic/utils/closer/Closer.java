/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.utils.closer;

import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Simplifie la fermeture des objets possédant une méthode close().
 * 
 * @author ginguene
 * 
 */
public final class Closer {

    private Closer() {
    }

    public static void close(final Object... closeableObjects) throws CloseException {
        CloseException exception = null;

        for (final Object closeableObject : closeableObjects) {
            try {
                Closer.close(closeableObject);
            } catch (final CloseException e) {
                exception = e;
            }
        }

        if (exception != null) {
            throw exception;
        }
    }

    public static void close(final Object closeableObject) throws CloseException {
        if (closeableObject != null) {
            try {
                Closer.getCloser(closeableObject).close();
            } catch (final Exception e) {
                throw new CloseException(e);
            }
        }
    }

    private static ICloser getCloser(final Object object) {
        if (object instanceof Statement) {
            return new StatementCloser((Statement) object);
        }

        if (object instanceof Statement) {
            return new StatementCloser((Statement) object);
        }

        if (object instanceof Connection) {
            return new ConnectionCloser((Connection) object);
        }

        if (object instanceof ResultSet) {
            return new ResultSetCloser((ResultSet) object);
        }

        if (object instanceof InputStream) {
            return new InputStreamCloser((InputStream) object);
        }

        if (object instanceof Writer) {
            return new WriterCloser((Writer) object);
        }

        if (object instanceof Reader) {
            return new ReaderCloser((Reader) object);
        }

        throw new IllegalArgumentException("No closer defined for class " + object.getClass().getName());
    }
}
