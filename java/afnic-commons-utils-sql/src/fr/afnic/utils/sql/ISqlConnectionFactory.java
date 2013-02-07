package fr.afnic.utils.sql;

import java.sql.Connection;

import fr.afnic.commons.services.exception.ConnectionException;

/**
 * interface d'objet permettant de gérer des connection Sql
 * 
 * @author ginguene
 * 
 */
public interface ISqlConnectionFactory {

    /**
     * Génère une nouvelle connection Sql vers une base
     * 
     * @return
     * @throws ConnectionDaoException
     */
    public Connection createConnection() throws ConnectionException;

    /**
     * Demande la fermeture d'une connection
     * 
     * @param connection
     */
    public void closeConnection(Connection connection) throws ConnectionException;;

}
