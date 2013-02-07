/*
 * $Id: ResultSetProcessor.java,v 1.3 2010/04/09 07:14:38 ginguene Exp $ $Revision: 1.3 $ $Author: ginguene $
 */

package fr.afnic.commons.services.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.afnic.commons.services.exception.ServiceException;

/**
 * Objet chargé de remplir un objet resultat a partir des données contenues dans un resultSet. La genericite est utilise pour pouvoir définir le type
 * d'object contenu dans le resultat
 * 
 * @author ginguene
 * 
 * @param <T>
 */
public abstract class ResultSetProcessor<T> {

    protected T result;

    public abstract void populateResultFromResultSet(ResultSet resultSet) throws SQLException, ServiceException;

    public T getResult() {
        return this.result;
    }

}
