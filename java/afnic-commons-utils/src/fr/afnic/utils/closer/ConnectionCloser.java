/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.utils.closer;

import java.sql.Connection;

/**
 * 
 * Classe chargée de fermer une connection.
 * 
 * @author ginguene
 * 
 */
public class ConnectionCloser implements ICloser {

    private final Connection connection;

    ConnectionCloser(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public void close() throws Exception {
        this.connection.close();
    }

}
